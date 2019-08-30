package no.ssb.klass.subsets.documentation;

import no.ssb.klass.subsets.applicationtests.utils.DatabaseUtil;
import no.ssb.klass.subsets.applicationtests.utils.TestDataProvider;
import no.ssb.klass.subsets.domain.Subset;
import no.ssb.klass.subsets.domain.SubsetUser;
import no.ssb.klass.subsets.domain.SubsetVersion;
import no.ssb.klass.subsets.repository.SubsetUserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.format.DateTimeFormatter;

import static no.ssb.klass.subsets.provider.utils.RestConstants.REST_PRESENTATION_URI;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert"})
public class PresentationApiDocumentationTest extends ApiDocumentationBase {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets/presentation");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SubsetUserRepository userRepository;

    @Autowired
    private DatabaseUtil databaseUtil;

    private RestDocumentationResultHandler documentationHandler;

    private MockMvc mockMvc;

    private long setId;

    private long versionId;

    private Subset set;
    private SubsetVersion version;

    @Before
    public void setUp() {
        databaseUtil.clearDatabase();
        createTestData();
        prepareTestHandlers();
    }

    private void prepareTestHandlers() {
        this.documentationHandler = document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.documentationHandler).build();
    }


    private void createTestData() {
        SubsetUser user = TestDataProvider.createUserWithSetVersion();
        user = userRepository.saveAndFlush(user);

        set = user.getSubsets().get(0);
        version = set.getSubsetVersions().get(0);

        setId = set.getId();
        versionId = version.getId();

    }


    @Test
    public void errorExample() throws Exception {
        this.mockMvc.perform(get(REST_PRESENTATION_URI + "sets/{id}", 9999999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void subsetExample() throws Exception {
        this.mockMvc.perform(get(REST_PRESENTATION_URI + "sets/{id}", setId))
                .andDo(this.documentationHandler.document(
                        links(
                                linkWithRel("self")
                                        .description("The current request"),
                                linkWithRel("codesAt")
                                        .description("Used for getting codes from the subset valid at a specific date, see <<CodesAt, codesAt>>")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("owner")
                                        .description("information about the creator of this Subset"),
                                fieldWithPath("versions")
                                        .description("List of versions for given set")
                        )))
                .andExpect(status().isOk());
    }

    @Test
    public void versionExample() throws Exception {
        this.mockMvc.perform(get(REST_PRESENTATION_URI + "versions/{id}", versionId))
                .andDo(this.documentationHandler.document(
                        links(
                                linkWithRel("self")
                                        .description("The current request")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("owner")
                                        .description("information about the creator of this Subset"),
                                fieldWithPath("sources")
                                        .description("A list of all the sources from where the codes originates"),
                                fieldWithPath("sources[].id")
                                        .description("Id used to link codes with their respective source"),
                                fieldWithPath("sources[].sourceId")
                                        .description("Id used in master system"),
                                fieldWithPath("sources[].sourceType")
                                        .description("used to distinguish different types of sources from a master system"),
                                fieldWithPath("codes")
                                        .description("List of codes for given version"),
                                fieldWithPath("codes[].source")
                                        .description("A reference to a source in the sources list"),
                                fieldWithPath("codes[].code")
                                        .description("Code value"),
                                fieldWithPath("codes[].name")
                                        .description("Code name")
                        )))
                .andExpect(status().isOk());
    }

    @Test
    public void codesAtExample() throws Exception {
        this.mockMvc.perform(get(REST_PRESENTATION_URI + "sets/{id}/codesAt?date={date}",
                setId, version.getValidFrom().format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .andDo(this.documentationHandler.document(
                        relaxedResponseFields(
                                fieldWithPath("sources")
                                        .description("A list of all the sources from where the codes originates"),
                                fieldWithPath("sources[].id")
                                        .description("Id used to link codes with their respective source"),
                                fieldWithPath("sources[].sourceId")
                                        .description("Id used in master system"),
                                fieldWithPath("sources[].sourceType")
                                        .description("used to distinguish different types of sources from a master system"),
                                fieldWithPath("codes")
                                        .description("List of codes for given version"),
                                fieldWithPath("codes[].source")
                                        .description("A reference to a source in the sources list"),
                                fieldWithPath("codes[].code")
                                        .description("Code value"),
                                fieldWithPath("codes[].name")
                                        .description("Code name")
                        )))
                .andExpect(status().isOk());
    }

    @Test
    public void codesAtOptionalParametersExample() throws Exception {

        this.mockMvc.perform(get(REST_PRESENTATION_URI + "sets/{id}/codesAt?date={date}&language=nb",
                setId, version.getValidFrom().format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .andDo(this.documentationHandler.document(
                        requestParameters(
                                dateParameterDescription(),
                                languageDescription()),
                        relaxedResponseFields(
                                fieldWithPath("sources")
                                        .description("A list of all the sources from where the codes originates"),
                                fieldWithPath("sources[].id")
                                        .description("Id used to link codes with their respective source"),
                                fieldWithPath("sources[].sourceId")
                                        .description("Id used in master system"),
                                fieldWithPath("sources[].sourceType")
                                        .description("used to distinguish different types of sources from a master system"),
                                fieldWithPath("codes")
                                        .description("List of codes for given version"),
                                fieldWithPath("codes[].source")
                                        .description("A reference to a source in the sources list"),
                                fieldWithPath("codes[].code")
                                        .description("Code value"),
                                fieldWithPath("codes[].name")
                                        .description("Code name")
                        )))
                .andExpect(status().isOk());

    }

    @Test
    public void languageExample() throws Exception {

        this.mockMvc.perform(get(REST_PRESENTATION_URI + "sets/{id}/codesAt?date={date}&language=en",
                setId, version.getValidFrom().format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .andExpect(status().isOk());
        // @formatter:on
    }

    private ParameterDescriptor dateParameterDescription() {
        return parameterWithName("date").description(
                "[Mandatory] specifies codes at a certain date with format `<yyyy-MM-dd>`.");
    }

    private ParameterDescriptor languageDescription() {
        return parameterWithName("language").description(
                "[Optional] specifies language of retrieved data. Default is nb (bokmål). For details see <<language, language>>");
    }


}
