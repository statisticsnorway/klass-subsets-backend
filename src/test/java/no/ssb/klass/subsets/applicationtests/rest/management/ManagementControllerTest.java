package no.ssb.klass.subsets.applicationtests.rest.management;

import io.restassured.http.ContentType;
import no.ssb.klass.subsets.applicationtests.utils.TestDataProvider;
import no.ssb.klass.subsets.domain.Subset;
import no.ssb.klass.subsets.domain.SubsetUser;
import no.ssb.klass.subsets.domain.SubsetVersion;
import no.ssb.klass.subsets.provider.managment.SubsetVersionDto;
import no.ssb.klass.subsets.repository.SubsetUserRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static no.ssb.klass.subsets.provider.utils.RestConstants.REST_MANAGMENT_URI;

//@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert"})
public class ManagementControllerTest {

    @Autowired
    SubsetUserRepository userRepository;

    @LocalServerPort
    private int port;

    private ObjectMapper jsonMapper = new ObjectMapper();

    private long setId;
    private long versionId;


    @Before
    public void createTestDataAndMockDependencies() {
        SubsetUser user = TestDataProvider.createUserWithSetVersion();
        user = userRepository.saveAndFlush(user);

        Subset set = user.getSubsets().get(0);
        SubsetVersion version = set.getSubsetVersions().get(0);

        setId = set.getId();
        versionId = version.getId();

    }

    @Test
    @Ignore("test used in validation debugging, remove when validation implementation is done")
    public void versionUpdateExample() throws Exception {
        SubsetVersionDto version = new SubsetVersionDto();
//		version.setSubsetId(setId);
//		version.setVersionId(versionId);

        given().port(port)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(jsonMapper.writeValueAsString(version))
                .put(REST_MANAGMENT_URI + "version/" + versionId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    @Ignore("test used in validation debugging, remove when validation implementation is done")
    public void canGetSubset() {

        given().port(port).accept(ContentType.JSON).get(REST_MANAGMENT_URI + "sets/{id}", setId)
                .prettyPeek()
                .then()
                .statusCode(200);
//				.body("name", containsString("Demo Uttrekk"))
//				.body("description", containsString("Enkelt test  av uttrekk"))
//				.body("owner.name", containsString("Ola Forvaltersen"))
//				.body("owner.division", containsString("723"))
//				.body("versions[0].name", containsString("Dummy Uttrekk 2018-10"))
//				.body("versions[0]._links.self.href", matchesPattern("http://localhost:(\\d*)" + REST_PRESENTATION_URI + "versions/\\d+"));
    }


}
