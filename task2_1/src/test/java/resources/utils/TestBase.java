package resources.utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.dto.Item;

import java.io.File;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public final class TestBase {
    public static final String HOST = "https://qa-internship.avito.com/";
    public static final File BAD_REQUEST_SCHEMA = new File("src/test/java/resources/schemas/errorResponse/badRequestSchema.json");
    public static final File SERVER_ERROR_SCHEMA = new File("src/test/java/resources/schemas/errorResponse/serverErrorSchema.json");
    public static final File CREATE_ITEM_OK_SCHEMA = new File("src/test/java/resources/schemas/successResponse/createItemOkSchema.json");
    public static final File GET_ITEMS_OK_SCHEMA = new File("src/test/java/resources/schemas/successResponse/getItemsOkSchema.json");
    public static final File GET_STATISTIC_BY_ID_OK_SCHEMA = new File("src/test/java/resources/schemas/successResponse/getStatisticByIdOkSchema.json");

    //Базовая спецификация запроса
    public static RequestSpecification requestSpecification(String endpoint, Object body){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri(HOST). //общее предусловие
                setBasePath(endpoint).
                setContentType(ContentType.JSON). //общее предусловие
                setAccept(ContentType.JSON). //общее предусловие
                log(LogDetail.ALL);
        if (body!=null) return requestSpecBuilder.setBody(body).build();
        else return requestSpecBuilder.build();
    }

    //Базовая спецификация ответа
    public static ResponseSpecification responseSpecification(int statusCode, File schema){
        JsonSchemaValidator schemaMatcher = schema != null ? matchesJsonSchema(schema) : matchesJsonSchema("{}");
        return new ResponseSpecBuilder().
                expectStatusCode(statusCode).
                expectContentType(ContentType.JSON).
                expectBody(schemaMatcher).
                log(LogDetail.ALL).
                build();
    }

    //Отправка запроса и валидация ответа
    public static Response sendRequestValidateResponse(String endpoint, Object requestBody, HttpMethod httpMethod, int statusCode, File schema){
        RequestSpecification requestSpecification = given(requestSpecification(endpoint, requestBody));
        Response response = switch (httpMethod){
            case GET -> requestSpecification.get();
            case POST -> requestSpecification.post();
            case PUT -> requestSpecification.put();
            case DELETE -> requestSpecification.delete();
        };

        return response
                .then()
                .spec(responseSpecification(statusCode, schema))
                .extract()
                .response();
    }

    //Создание объявления и сохранение его id
    public static String createItemAndGetId(Item item) {
        //Тело ответа представляет собой строку вида "status": "Сохранили объявление - <itemId>"
        //Поэтому для получения id я извлекаю значение параметра и преобразую его в массив строк, разделенных пробелом
        String [] responseBody = given(requestSpecification("api/1/item", item))
                .post()
                .then()
                .extract()
                .response().getBody().jsonPath().getString("status").split(" ");
        //В таком случае, itemId будет последним элементом массива
        return responseBody[responseBody.length-1];
    }
}