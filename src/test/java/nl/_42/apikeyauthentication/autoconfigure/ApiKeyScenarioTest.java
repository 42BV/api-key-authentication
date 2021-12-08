package nl._42.apikeyauthentication.autoconfigure;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class ApiKeyScenarioTest {

    @ExtendWith(SpringExtension.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @ActiveProfiles({ "unit-test", "api-key-all-endpoints" })
    public static class ApiKeyOnAllEndpointsTest extends AbstractSpringTest {

    }

    @ExtendWith(SpringExtension.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @ActiveProfiles({ "unit-test", "api-key-custom-header-name" })
    public static class ApiKeyCustomHeaderNameTest extends AbstractSpringTest {

    }

    @ExtendWith(SpringExtension.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @ActiveProfiles({ "unit-test", "api-key-ant-pattern" })
    public static class ApiKeyAntPatternTest extends AbstractSpringTest {

    }

    @ExtendWith(SpringExtension.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @ActiveProfiles({ "unit-test", "api-key-custom-request-matcher" })
    public static class ApiKeyCustomRequestMatcherTest extends AbstractSpringTest {

    }

    @ExtendWith(SpringExtension.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @ActiveProfiles({ "unit-test", "api-key-filter-positioning-after" })
    public static class ApiKeyFilterPositioningAfterTest extends AbstractSpringTest {

    }

    @ExtendWith(SpringExtension.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @ActiveProfiles({ "unit-test", "api-key-filter-positioning-before" })
    public static class ApiKeyFilterPositioningBeforeTest extends AbstractSpringTest {

    }

    @ExtendWith(SpringExtension.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @ActiveProfiles({ "unit-test", "api-key-filter-positioning-missing" })
    public static class ApiKeyFilterPositioningMissingTest extends AbstractSpringTest {

    }

}
