package no.ssb.klass.subsets.provider.presentation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import no.ssb.klass.subsets.domain.utils.SourceType;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SourceResource {

    @JsonIgnore
    private long databaseId;
    @JsonIgnore
    private SourceType source;


    private long id;

    private String sourceId;

    public String getSourceType() {
        return source.type;
    }

    public String getSourceName() {
        return source.origin;
    }


}
