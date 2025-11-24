import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import tests.CreateItemTest;
import tests.GetItemByIdTest;
import tests.GetItemsBySellerIdTest;
import tests.GetStatisticByIdTest;

@Suite
@SuiteDisplayName("Общий тестовый набор")
@SelectClasses({CreateItemTest.class, GetItemByIdTest.class, GetStatisticByIdTest.class, GetItemsBySellerIdTest.class})
public class TestsRunner {
}
