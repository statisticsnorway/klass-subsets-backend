package no.ssb.klass.subsets.provider.managment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import no.ssb.klass.subsets.provider.utils.RestConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubsetVersionDto {

    @NotNull
    @PositiveOrZero
    private Long subsetId;
    private Long versionId;


    @Setter(AccessLevel.PRIVATE)
    private Map<String, String> descriptions = new HashMap<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = RestConstants.DATE_FORMAT)
    private LocalDate validFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = RestConstants.DATE_FORMAT)
    private LocalDate validTo;

    private List<SubsetSourceDto> sources = new ArrayList<>();
    private List<SubsetCodeDto> codes = new ArrayList<>();

    public void setDescription(String locale, String description) {
        descriptions.put(locale, description);
    }

    public String getDescription(String locale) {
        return descriptions.get(locale);
    }
}
