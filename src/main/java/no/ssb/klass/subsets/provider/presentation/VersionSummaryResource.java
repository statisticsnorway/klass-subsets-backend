package no.ssb.klass.subsets.provider.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VersionSummaryResource extends BaseHalResource {

    @JsonProperty(index = 1)
    private String name;
}
