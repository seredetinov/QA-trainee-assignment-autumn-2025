package tests;

import org.junit.jupiter.api.*;
import resources.dto.Item;
import resources.dto.Statistics;

import static resources.utils.HttpMethod.GET;
import static resources.utils.TestBase.*;

@DisplayName("Получить статистику по айтем id")
public class GetStatisticByIdTest {
    private final String endpoint = "/api/1/statistic/";

    @Nested
    @DisplayName("Позитивные сценарии")
    class PositiveTest{
        static Item item;
        static String itemId;

        //Выполняем предусловие о наличии объявлений
        @BeforeAll
        public static void setItem(){
            Statistics statistics = new Statistics(10, 20, 30);
            item = new Item();
            item.statistics = statistics;
            itemId = createItemAndGetId(item);
        }

        @Test
        @DisplayName("№5 | Получение статистики существующего объявления")
        public void getStatisticByIdValidIdTest(){
            var response = sendRequestValidateResponse(endpoint + itemId, null, GET, 200, GET_STATISTIC_BY_ID_OK_SCHEMA);
            Assertions.assertEquals(item.statistics.toString(), response.jsonPath().getObject("[0]", Statistics.class).toString(),
                    "Фактическая статистика объявления и статистика, полученная в ответе, не совпадают");
        }

        @Test
        @DisplayName("№6 | Идемпотентность метода GET при повторном получении статистики объявления")
        public void getStatisticByIdIdempotencyTest(){
            String [] responses = new String[2];
            for (int i = 0; i <= 1; i++){
                responses[i] = sendRequestValidateResponse(endpoint + itemId, null, GET, 200, GET_STATISTIC_BY_ID_OK_SCHEMA).asString();
            }
            Assertions.assertEquals(responses[0], responses[1],
                    "Ответ от сервера при повторном запросе не совпадает с первым ответом");
        }

    }

    @Nested
    @DisplayName("Негативные сценарии")
    class NegativeTest{
        @Test
        @DisplayName("№7 | Получение статистики объявления без указания идентификатора")
        public void getStatisticByIdNoIdTest(){
            sendRequestValidateResponse(endpoint, null,GET,404, null);
        }

        @Test
        @DisplayName("№8 | Получение статистики объявления по недопустимому значению идентификатора")
        public void getStatisticByIdInvalidIdTest(){
            sendRequestValidateResponse(endpoint + "false", null,GET,400, BAD_REQUEST_SCHEMA);
        }
    }
}
