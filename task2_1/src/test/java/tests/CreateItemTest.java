package tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import resources.dto.Item;
import resources.dto.Statistics;

import static resources.utils.HttpMethod.GET;
import static resources.utils.HttpMethod.POST;
import static resources.utils.TestBase.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Создать объявления")
public class CreateItemTest {
    private final String endpoint = "/api/1/item";
    Item item;
    Statistics statistics;

    @BeforeEach
    public void makeItemTemplate(){
        //Выполнение предусловия для тест-сьюта "Создать объявления"
        item = new Item();
        statistics = new Statistics();
    }

    @Nested
    @DisplayName("Позитивные сценарии")
    class PositiveTest{
        @ParameterizedTest
        @ValueSource(ints = {111111, 111112, 999998, 999999})
        @DisplayName("№14-17 | Допустимое значение параметра 'sellerID'")
        public void createItemValidSellerIdTest(int sellerID){
            item.sellerID = sellerID;
            sendRequestValidateResponse(endpoint,item, POST, 200, CREATE_ITEM_OK_SCHEMA);
        }

        @ParameterizedTest
        @ValueSource(strings = {"testname", "TESTNAME", "test Name", "testname12", "test-name"})
        @DisplayName("№18-22 | Допустимое значение параметра 'name'")
        public void createItemValidNameTest(String name){
            item.name = name;
            sendRequestValidateResponse(endpoint,item, POST, 200, CREATE_ITEM_OK_SCHEMA);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 100_000_000})
        @DisplayName("№23-25 | Допустимое значение параметра 'price'")
        public void createItemValidPriceTest(int price){
            item.price = price;
            sendRequestValidateResponse(endpoint,item, POST, 200, CREATE_ITEM_OK_SCHEMA);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 100_000})
        @DisplayName("№26-28 | Допустимое значение параметра 'statistics.likes'")
        public void createItemValidLikesTest(int likes){
            statistics.likes = likes;
            item.statistics = statistics;
            sendRequestValidateResponse(endpoint,item, POST, 200, CREATE_ITEM_OK_SCHEMA);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 10_000_000})
        @DisplayName("№29-31 | Допустимое значение параметра 'statistics.viewCount'")
        public void createItemValidViewCountTest(int viewCount){
            statistics.viewCount = viewCount;
            item.statistics = statistics;
            sendRequestValidateResponse(endpoint,item, POST, 200, CREATE_ITEM_OK_SCHEMA);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 1000})
        @DisplayName("№32-34 | Допустимое значение параметра 'statistics.contacts'")
        public void createItemValidContactsTest(int contacts){
            statistics.contacts = contacts;
            item.statistics = statistics;
            sendRequestValidateResponse(endpoint,item, POST, 200, CREATE_ITEM_OK_SCHEMA);
        }

        @Test
        @DisplayName("№35 | Двухэтапная проверка создания объявления через метод GET")
        public void createItemTwoStepTest(){
            String itemId = createItemAndGetId(item);
            var response = sendRequestValidateResponse(endpoint + "/" + itemId, null, GET, 200, GET_ITEMS_OK_SCHEMA);
            Assertions.assertEquals( itemId, response.path("id[0]"), "Не совпадает");
        }

        @Test
        @DisplayName("№36 | Альтернативный порядок параметров в теле запроса")
        public void createItemAlternativeParametersOrderTest(){
            Item.ItemReversed item = new Item.ItemReversed();
            sendRequestValidateResponse(endpoint,item, POST, 200, CREATE_ITEM_OK_SCHEMA);
        }
    }

    @Nested
    @DisplayName("Негативные сценарии")
    class NegativeTest{
        @Test
        @DisplayName("№37 | Отсутствие значения параметра 'sellerID' в теле запроса")
        public void createItemNoSellerIdTest(){
            item.sellerID = null;
            sendRequestValidateResponse(endpoint,item, POST, 400, BAD_REQUEST_SCHEMA);
        }

        @Test
        @DisplayName("№38 | Отсутствие значения параметра 'name' в теле запроса")
        public void createItemNoNameTest(){
            item.name = null;
            sendRequestValidateResponse(endpoint,item, POST, 400, BAD_REQUEST_SCHEMA);
        }

        @Test
        @DisplayName("№39 | Отсутствие значения параметра 'price' в теле запроса")
        public void createItemNoPriceTest(){
            item.price = null;
            sendRequestValidateResponse(endpoint,item, POST, 400, BAD_REQUEST_SCHEMA);
        }

        @Test
        @DisplayName("№40 | Отсутствие значения параметра 'statistics' в теле запроса")
        public void createItemNoStatisticsTest(){
            item.statistics = null;
            sendRequestValidateResponse(endpoint,item, POST, 400, BAD_REQUEST_SCHEMA);
        }

        @Test
        @DisplayName("№41 | Отсутствие значения параметра 'statistics.likes' в теле запроса")
        public void createItemNoLikesTest(){
            statistics.likes = null;
            item.statistics = statistics;
            sendRequestValidateResponse(endpoint,item, POST, 400, BAD_REQUEST_SCHEMA);
        }

        @Test
        @DisplayName("№42 | Отсутствие значения параметра 'statistics.viewCount' в теле запроса")
        public void createItemNoViewCountTest(){
            statistics.viewCount = null;
            item.statistics = statistics;
            sendRequestValidateResponse(endpoint,item, POST, 400, BAD_REQUEST_SCHEMA);
        }

        @Test
        @DisplayName("№43 | Отсутствие значения параметра 'statistics.contacts' в теле запроса")
        public void createItemNoContactsTest(){
            statistics.contacts = null;
            item.statistics = statistics;
            sendRequestValidateResponse(endpoint,item, POST, 400, BAD_REQUEST_SCHEMA);
        }

        @Test
        @DisplayName("№44 | Отсутствие тела запроса")
        public void createItemNoBodyTest(){
            sendRequestValidateResponse(endpoint,null, POST, 400, BAD_REQUEST_SCHEMA);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        @DisplayName("№45-46 | Недопустимое значение параметра 'sellerID'")
        public void createItemInvalidSellerIDTest(int sellerID){
            item.sellerID = sellerID;
            sendRequestValidateResponse(endpoint,item, POST, 400, null);
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "1"})
        @DisplayName("№47-48 | Недопустимое значение параметра 'name'")
        public void createItemInvalidNameTest(String name){
            item.name = name;
            sendRequestValidateResponse(endpoint,item, POST, 400, null);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        @DisplayName("№49-50 | Недопустимое значение параметра 'price'")
        public void createItemInvalidPriceTest(int price){
            item.price = price;
            sendRequestValidateResponse(endpoint,item, POST, 400, null);
        }


        @Test
        @DisplayName("№51 | Недопустимое значение параметра 'statistics'")
        public void createItemInvalidStatisticsTest(){
            item.statistics = "statistics";
            sendRequestValidateResponse(endpoint,item, POST, 400, null);
        }

        @Test
        @DisplayName("№52 | Недопустимое значение параметра 'statistics.likes'")
        public void createItemInvalidLikesTest(){
            statistics.likes = -1;
            item.statistics = statistics;
            sendRequestValidateResponse(endpoint,item, POST, 400, null);
        }

        @Test
        @DisplayName("№53 | Недопустимое значение параметра 'statistics.viewCount'")
        public void createItemInvalidViewCountTest(){
            statistics.viewCount = -1;
            item.statistics = statistics;
            sendRequestValidateResponse(endpoint,item, POST, 400, null);
        }

        @Test
        @DisplayName("№54 | Недопустимое значение параметра 'statistics.contacts'")
        public void createItemInvalidContactsTest(){
            statistics.contacts = -1;
            item.statistics = statistics;
            sendRequestValidateResponse(endpoint,item, POST, 400, null);
        }
    }
}