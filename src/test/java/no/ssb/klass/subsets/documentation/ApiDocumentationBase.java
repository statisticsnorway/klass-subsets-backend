package no.ssb.klass.subsets.documentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.restdocs.generate.RestDocumentationGenerator;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class ApiDocumentationBase {

    @Value("${klass.env.server:localhost}")
    private String server;
    @Value("${klass.env.port:8080}")
    private int port;


    MockHttpServletRequestBuilder get(String urlTemplate, Object... urlVariables) {
        return MockMvcRequestBuilders.get(urlTemplate, urlVariables)
                .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
    }

    MockHttpServletRequestBuilder delete(String urlTemplate, Object... urlVariables) {
        return MockMvcRequestBuilders.delete(urlTemplate, urlVariables)
                .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
    }

    MockHttpServletRequestBuilder post(String urlTemplate, String payload) {
        return MockMvcRequestBuilders.post(urlTemplate)
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
    }

    MockHttpServletRequestBuilder put(String urlTemplate, String payload) {
        return MockMvcRequestBuilders.put(urlTemplate)
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
    }

    public MockHttpServletRequestBuilder getWithContextUri(String url) {
        return MockMvcRequestBuilders.get(toUri(url));
    }

    private URI toUri(String url) {
        return UriComponentsBuilder.fromUriString(url).build().encode().toUri();
    }

}
