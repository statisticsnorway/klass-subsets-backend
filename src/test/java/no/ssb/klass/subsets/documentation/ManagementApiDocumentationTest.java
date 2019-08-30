package no.ssb.klass.subsets.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ssb.klass.subsets.applicationtests.utils.DatabaseUtil;
import no.ssb.klass.subsets.applicationtests.utils.TestDataProvider;
import no.ssb.klass.subsets.common.Language;
import no.ssb.klass.subsets.domain.Subset;
import no.ssb.klass.subsets.domain.SubsetUser;
import no.ssb.klass.subsets.domain.SubsetVersion;
import no.ssb.klass.subsets.domain.utils.SourceType;
import no.ssb.klass.subsets.provider.managment.SubsetCodeDto;
import no.ssb.klass.subsets.provider.managment.SubsetDto;
import no.ssb.klass.subsets.provider.managment.SubsetSourceDto;
import no.ssb.klass.subsets.provider.managment.SubsetVersionDto;
import no.ssb.klass.subsets.repository.SubsetUserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static no.ssb.klass.subsets.provider.utils.RestConstants.REST_MANAGMENT_URI;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert"})
public class ManagementApiDocumentationTest extends ApiDocumentationBase {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets/management");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SubsetUserRepository userRepository;

    @Autowired
    private DatabaseUtil databaseUtil;

    private MockMvc mockMvc;

    private long setId;

    private long versionId;

    @Autowired
    private ObjectMapper jsonMapper;

    @Before
    public void setUp() {
        databaseUtil.clearDatabase();
        prepareTestHandlers();
        createTestData();
    }

    private void prepareTestHandlers() {
        RestDocumentationResultHandler documentationHandler = document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(documentationHandler).build();
    }

    private void createTestData() {
        SubsetUser user = TestDataProvider.createUserWithSetVersion();
        user = userRepository.saveAndFlush(user);

        Subset set = user.getSubsets().get(0);
        SubsetVersion version = set.getSubsetVersions().get(0);

        setId = set.getId();
        versionId = version.getId();

    }


    @Test
    public void errorExample() throws Exception {
        this.mockMvc.perform(get(REST_MANAGMENT_URI + "sets/{id}", 9999999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void subsetsCreateExample() throws Exception {
        SubsetDto set = new SubsetDto();
        set.setOwnerId(1L);
        set.setName(Language.NB.name(), "Example Subset");
        set.setDescription(Language.NB.name(), "Example Subset descriptions");

        this.mockMvc.perform(post(REST_MANAGMENT_URI + "sets/", jsonMapper.writeValueAsString(set)))
                .andExpect(status().isCreated());
    }

    @Test
    public void subsetsUpdateExample() throws Exception {
        SubsetDto set = new SubsetDto();
        set.setSubsetId(setId);
        set.setOwnerId(1L);
        set.setName(Language.NB.name(), "New Name for Subset");
        set.setDescription(Language.NB.name(), "This will replace existing value");

        this.mockMvc.perform(put(REST_MANAGMENT_URI + "sets/" + setId, jsonMapper.writeValueAsString(set)))
                .andExpect(status().isOk());
    }

    @Test
    public void subsetsFetchExample() throws Exception {
        this.mockMvc.perform(get(REST_MANAGMENT_URI + "sets/{id}", setId))
                .andExpect(status().isOk());
    }

    @Test
    public void subsetsDeleteExample() throws Exception {
        this.mockMvc.perform(delete(REST_MANAGMENT_URI + "sets/{id}", setId))
                .andExpect(status().isAccepted());
    }


    @Test
    public void versionCreateExample() throws Exception {
        SubsetVersionDto version = new SubsetVersionDto();
        version.setSubsetId(setId);
        version.setValidFrom(LocalDate.of(2010, 12, 1));
        version.setValidTo(LocalDate.of(2019, 1, 1));
        version.setDescription(Language.NB.name(), "Denne versjonen Inneholder ingen koder");
        version.setDescription(Language.NN.name(), "Denne versjonen Inneholder ikkje koder");
        version.setDescription(Language.EN.name(), "This version does not contain any codes");

        this.mockMvc.perform(post(REST_MANAGMENT_URI + "version/", jsonMapper.writeValueAsString(version)))
                .andExpect(status().isCreated());
    }

    @Test
    public void versionUpdateExample() throws Exception {
        SubsetVersionDto version = new SubsetVersionDto();
        version.setSubsetId(setId);
        version.setVersionId(versionId);
        version.setValidFrom(LocalDate.now());
        version.setValidTo(LocalDate.now().plusYears(2));

        version.setDescription(Language.NB.name(), "Denne versjonen Inneholder nå koder");
        version.setDescription(Language.NN.name(), "Denne versjonen Inneholder nå koder");
        version.setDescription(Language.EN.name(), "This version now contains codes");

        SubsetSourceDto sourceDto = new SubsetSourceDto();
        sourceDto.setId(0);
        sourceDto.setSourceId("12");
        sourceDto.setSourceType(SourceType.KLASS_VERSION.type);
        sourceDto.setSourceOrigin(SourceType.KLASS_VERSION.origin);
        version.getSources().add(sourceDto);

        SubsetCodeDto codeDto = new SubsetCodeDto();
        codeDto.setSource(0);
        codeDto.setCode("codeXYZ");
        codeDto.setSortId(1L);
        version.getCodes().add(codeDto);

        this.mockMvc.perform(put(REST_MANAGMENT_URI + "version/" + versionId, jsonMapper.writeValueAsString(version)))
                .andExpect(status().isOk());
    }

    @Test
    public void versionFetchExample() throws Exception {
        this.mockMvc.perform(get(REST_MANAGMENT_URI + "version/{id}", versionId))
                .andExpect(status().isOk());
    }

    @Test
    public void versionDeleteExample() throws Exception {
        this.mockMvc.perform(delete(REST_MANAGMENT_URI + "version/{id}", versionId))
                .andExpect(status().isAccepted());
    }

}