package no.ssb.klass.subsets.applicationtests.mocks;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Slf4j
@Component
public class KlassApiMockServer {


    @Autowired
    public KlassApiMockServer(RestTemplate restTemplate, ResourcePatternResolver resourceResolver) {
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();

        try {
            Resource[] versionResources = resourceResolver.getResources("classpath:mockData/klass/versions/*.json");
            log.info("adding " + versionResources.length + " versions to KlassApiMock");
            for (Resource r : versionResources) {
                String fileName = r.getFile().getName();
                String versionId = fileName.substring(0, fileName.indexOf('.'));
                StringWriter writer = new StringWriter();
                IOUtils.copy(r.getInputStream(), writer, StandardCharsets.UTF_8);
                String versionContent = writer.toString();
                server.expect(manyTimes(), requestTo("/versions/" + versionId)).andExpect(method(HttpMethod.GET))
                        .andRespond(withSuccess(versionContent, MediaType.APPLICATION_JSON));
            }

        } catch (IOException e) {
            log.info("unable to find versions to mock");
        }

        try {
            Resource[] variantResources = resourceResolver.getResources("classpath:mockData/klass/variants/*.json");
            log.info("adding " + variantResources.length + " variants to KlassApiMock");
            for (Resource r : variantResources) {
                String fileName = r.getFile().getName();
                String versionId = fileName.substring(0, fileName.indexOf('.'));
                StringWriter writer = new StringWriter();
                IOUtils.copy(r.getInputStream(), writer, StandardCharsets.UTF_8);
                String versionContent = writer.toString();
                server.expect(manyTimes(), requestTo("/variants/" + versionId)).andExpect(method(HttpMethod.GET))
                        .andRespond(withSuccess(versionContent, MediaType.APPLICATION_JSON));
            }
        } catch (IOException e) {
            log.info("unable to find variants to mock");
        }

    }


}
