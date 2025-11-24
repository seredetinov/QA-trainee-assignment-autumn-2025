package tests;

import org.junit.jupiter.api.*;
import resources.dto.Item;
import resources.dto.Statistics;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import static resources.utils.HttpMethod.GET;
import static resources.utils.TestBase.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Получить все объявления по идентификатору продавца")
public class GetItemsBySellerIdTest {
    static Object sellerID;
    private String endpoint(){
        return "/api/1/" + sellerID + "/item";
    }

    @Nested
    @DisplayName("Позитивные сценарии")
    class PositiveTest{
        @Test
        @DisplayName("№09 | Получение объявлений по идентификатору существующего продавца")
        public void getItemsBySellerIdValidIdTest(){
            sellerID = 200225; //используется sellerID без объявлений на момент написания теста
                                //при повторном запуске необходимо задать нового продавца без объявлений
            Item item1 = new Item(sellerID, "item1", 100, new Statistics());
            Item item2 = new Item(sellerID, "item2", 1000, new Statistics());
            String firstItemId = createItemAndGetId(item1);
            String secondItemID = createItemAndGetId(item2);
            Set<String> actualItemsId = new HashSet<>(Set.of(firstItemId, secondItemID));

            var response = sendRequestValidateResponse(endpoint(), null, GET, 200, GET_ITEMS_OK_SCHEMA);
            var responseItems = response.jsonPath().getList("$");
            Set<String> responseItemsId = new HashSet<>();
            for (Object item: responseItems){
                var itemAsMap = (LinkedHashMap<String, Object>) item;
                responseItemsId.add(itemAsMap.get("id").toString());
            }
            Assertions.assertEquals(actualItemsId, responseItemsId);
        }

        @Test
        @DisplayName("№10 | Получение объявлений по идентификатору продавца без объявлений")
        public void getItemsBySellerIdNoItemsTest(){
            sellerID = 200221; //sellerID без объявлений на момент написания теста
            var response = sendRequestValidateResponse(endpoint(),null, GET, 200, GET_ITEMS_OK_SCHEMA);
            Assertions.assertEquals("[]\n", response.asString(),
                    "Тело ответа не является пустым массивом");
        }

        @Test
        @DisplayName("№11 | Идемпотентность метода GET при повторном получении объявлений продавца")
        public void getItemsBySellerIdIdempotencyTest(){
            sellerID = 123;
            String [] responses = new String[2];
            for (int i = 0; i <= 1; i++){
                responses[i] = sendRequestValidateResponse(endpoint(), null, GET, 200, GET_ITEMS_OK_SCHEMA).asString();
            }
            Assertions.assertEquals(responses[0], responses[1],
                    "Ответ от сервера при повторном запросе не совпадает с первым ответом");
        }
    }

    @Nested
    @DisplayName("Негативные сценарии")
    class NegativeTest{
        @Test
        @DisplayName("№12 | Получение объявлений продавца без указания его идентификатора")
        public void getItemsBySellerIdNoIdTest(){
            sellerID = "";
            sendRequestValidateResponse(endpoint(), null,GET,404, null);
        }

        @Test
        @DisplayName("№13 | Получение объявлений продавца с недопустимым идентификатором")
        public void getItemsBySellerIdInvalidIdTest(){
            sellerID = false;
            sendRequestValidateResponse(endpoint(), null,GET,400, BAD_REQUEST_SCHEMA);
        }
    }
}