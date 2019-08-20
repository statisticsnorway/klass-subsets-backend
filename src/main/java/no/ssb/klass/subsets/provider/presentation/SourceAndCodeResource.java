package no.ssb.klass.subsets.provider.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SourceAndCodeResource {

    @JsonProperty(index = 1)
    private List<SourceResource> sources;

    @JsonProperty(index = 2)
    private List<CodeResource> codes;
}
