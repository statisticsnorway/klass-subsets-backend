package no.ssb.klass.subsets.provider.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubsetResource extends BaseHalResource {


    @JsonProperty(index = 1)
    private String name;

    @JsonProperty(index = 2)
    private String description;

    @JsonProperty(index = 3)
    private UserResource owner;

    @JsonProperty(index = 4)
    private List<VersionSummaryResource> versions = new ArrayList<>();

}
