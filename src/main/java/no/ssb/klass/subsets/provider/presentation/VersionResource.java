package no.ssb.klass.subsets.provider.presentation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import no.ssb.klass.subsets.provider.utils.RestConstants;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class VersionResource extends VersionSummaryResource {

    @JsonProperty(index = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = RestConstants.DATE_FORMAT)
    private LocalDate validFrom;

    @JsonProperty(index = 3)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = RestConstants.DATE_FORMAT)
    private LocalDate validTo;

    @JsonProperty(index = 4)
    private String description;

    @JsonProperty(index = 5)
    private UserResource owner;

    @JsonProperty(index = 6)
    private List<SourceResource> sources;

    @JsonProperty(index = 7)
    private List<CodeResource> codes;

}
