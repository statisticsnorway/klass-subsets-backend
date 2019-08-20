package no.ssb.klass.subsets.provider.managment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SubsetDto {
    private Long subsetId;

    @Setter(AccessLevel.PRIVATE)
    private Map<String, String> names = new HashMap<>();

    @Setter(AccessLevel.PRIVATE)
    private Map<String, String> descriptions = new HashMap<>();

    @NotNull
    private Long ownerId;

    public void setName(String locale, String name) {
        names.put(locale, name);
    }

    public String getName(String locale) {
        return names.get(locale);
    }

    public void setDescription(String locale, String description) {
        descriptions.put(locale, description);
    }

    public String getDescription(String locale) {
        return descriptions.get(locale);
    }

}
