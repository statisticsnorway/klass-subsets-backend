package no.ssb.klass.subsets.provider.managment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubsetSourceDto {
    private int id;
    private String sourceId;
    private String sourceType;
    private String sourceOrigin;

}
