package tests;

import org.junit.jupiter.api.*;
import resources.dto.Item;

import static resources.utils.HttpMethod.GET;
import static resources.utils.TestBase.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Получить объявления по его идентификатору")
public class GetItemByIdTest {
    private final String endpoint = "/api/1/item/";

    @Nested
    @DisplayName("Позитивные сценарии")
    class PositiveTest{
        static String itemId;

        //Выполняем предусловие о наличии объявлений
        @BeforeAll
        public static void setItem(){
            itemId = createItemAndGetId(new Item());
        }

        @Test
        @DisplayName("№01 | Получение существующего объявления")
        public void getItemByIdValidIdTest(){
          var response = sendRequestValidateResponse(endpoint + itemId, null, GET, 200, GET_ITEMS_OK_SCHEMA);
          Assertions.assertEquals(itemId, response.jsonPath().get("[0].id"),
                  "itemId в запросе и ответе не совпадают");
        }

        @Test
        @DisplayName("№02 | Идемпотентность метода GET при повторном получении объявления")
        public void getItemByIdIdempotencyTest(){
            String [] responses = new String[2];
            for (int i = 0; i <= 1; i++){
                responses[i] = sendRequestValidateResponse(endpoint + itemId, null, GET, 200, GET_ITEMS_OK_SCHEMA).asString();
            }
            Assertions.assertEquals(responses[0], responses[1],
                    "Ответ от сервера при повторном запросе не совпадает с первым ответом");
        }
    }

    @Nested
    @DisplayName("Негативные сценарии")
    class NegativeTest{
        @Test
        @DisplayName("№03 | Получение объявления без указания идентификатора")
        public void getItemByIdNoIdTest(){
            sendRequestValidateResponse(endpoint, null,GET,404, null);
        }

        @Test
        @DisplayName("№04 | Получение объявления по недопустимому значению идентификатора")
        public void getItemByIdInvalidIdTest(){
            sendRequestValidateResponse(endpoint + "false", null,GET,400, BAD_REQUEST_SCHEMA);
        }
    }
}