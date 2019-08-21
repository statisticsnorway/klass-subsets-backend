package no.ssb.klass.subsets.applicationtests.rest.presentation;

import io.restassured.http.ContentType;
import no.ssb.klass.subsets.applicationtests.utils.TestDataProvider;
import no.ssb.klass.subsets.domain.Subset;
import no.ssb.klass.subsets.domain.SubsetUser;
import no.ssb.klass.subsets.domain.SubsetVersion;
import no.ssb.klass.subsets.repository.SubsetUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static no.ssb.klass.subsets.provider.utils.RestConstants.REST_PRESENTATION_URI;
import static org.cthul.matchers.CthulMatchers.matchesPattern;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert"})
public class PresentationControllerTest {

    @Autowired
    SubsetUserRepository userRepository;

    @LocalServerPort
    private int port;

    private long setId;
    private long versionId;


    @Before
    public void createTestDataAndMockDependencies() {
        SubsetUser user = TestDataProvider.CreateUserWithSetVersion();
        user = userRepository.saveAndFlush(user);

        Subset set = user.getSubsets().get(0);
        SubsetVersion version = set.getSubsetVersions().get(0);

        setId = set.getId();
        versionId = version.getId();

    }


    @Test
    public void canFetchSubset() {

        given().port(port).accept(ContentType.JSON).get(REST_PRESENTATION_URI + "sets/{id}", setId)
//                .prettyPeek()
                .then()
                .statusCode(200)
                .body("name", containsString("Demo Uttrekk"))
                .body("description", containsString("Enkelt test  av uttrekk"))
                .body("owner.name", containsString("Ola Forvaltersen"))
                .body("owner.division", containsString("723"))
                .body("versions[0].name", containsString("Demo Uttrekk 2018-10"))
                .body("versions[0]._links.self.href", matchesPattern("http://localhost:(\\d*)" + REST_PRESENTATION_URI + "versions/\\d+"));
    }

    @Test
    public void nonExistingSetWillReturnNotFound() {
        given().port(port).accept(ContentType.JSON).get(REST_PRESENTATION_URI + "sets/{id}", 99999)
//                .prettyPeek()
                .then()
                .statusCode(404)
                .body("error", containsString("Not Found"))
                .body("message", containsString("No subset with id 99999 found"));

    }

    @Test
    public void canFetchVersion() {
        given().port(port).accept(ContentType.JSON).get(REST_PRESENTATION_URI + "versions/{id}", versionId)
//                .prettyPeek()
                .then()
                .statusCode(200)
                .body("name", containsString("Demo Uttrekk 2018-10"))
                .body("validFrom", containsString("2018-10-05"))
                .body("validTo", containsString("2019-12-30"))
                .body("description", containsString("Test uttrekksversjon"))
                .body("owner.name", containsString("Ola Forvaltersen"))
                .body("owner.division", containsString("723"))
                .body("sources.size()", equalTo(2))
                .body("codes.size()", equalTo(5))
                //
                .body("sources[0].id", equalTo(0))
                .body("sources[0].sourceId", equalTo("14"))
                .body("sources[0].sourceName", equalTo("Klass"))
                .body("sources[0].sourceType", equalTo("ClassificationSeries"))
                //
                .body("codes[0].source", equalTo(0))
                .body("codes[0].code", equalTo("1"))
                .body("codes[0].name", equalTo("Mann"))
                //
                .body("codes[4].source", equalTo(1))
                .body("codes[4].code", equalTo("5"))
                .body("codes[4].name", equalTo("Separert"));

    }

    @Test
    public void nonExistingVersionWillReturnNotFound() {
        given().port(port).accept(ContentType.JSON).get(REST_PRESENTATION_URI + "versions/{id}", 99999)
//                .prettyPeek()
                .then()
                .statusCode(404)
                .body("error", containsString("Not Found"))
                .body("message", containsString("No version with id 99999 found"));
    }


    @Test
    public void canFetchCodesAt() {
        given().port(port).accept(ContentType.JSON).get(REST_PRESENTATION_URI + "sets/{id}/codesAt?date=2019-01-01", setId)
//                .prettyPeek()
                .then()
                .statusCode(200)
                .body("sources.size()", equalTo(2))
                .body("codes.size()", equalTo(5))
                //
                .body("sources[0].id", equalTo(0))
                .body("sources[0].sourceName", equalTo("Klass"))
                .body("sources[0].sourceId", equalTo("14"))
                .body("sources[0].sourceType", equalTo("ClassificationSeries"))
                //
                .body("codes[0].source", equalTo(0))
                .body("codes[0].code", equalTo("1"))
                .body("codes[0].name", equalTo("Mann"))
                //
                .body("codes[4].source", equalTo(1))
                .body("codes[4].code", equalTo("5"))
                .body("codes[4].name", equalTo("Separert"));
    }

    @Test
    public void codesAtWhenNoOverlappingVersionIsFound() {
        given().port(port).accept(ContentType.JSON).get(REST_PRESENTATION_URI + "sets/{id}/codesAt?date=1019-01-01", setId)
//                .prettyPeek()
                .then()
                .statusCode(404)
                .body("error", containsString("Not Found"))
                .body("message", containsString("Subset does not contain any codes for date 1019-01-01"));
    }
}
