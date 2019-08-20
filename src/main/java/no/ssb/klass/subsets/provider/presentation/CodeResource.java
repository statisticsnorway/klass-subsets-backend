package no.ssb.klass.subsets.provider.presentation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CodeResource {

    private long source;
    private String code;
    private String name;
    private String notes;

}
