package no.ssb.klass.subsets.provider.managment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubsetCodeDto {

    @PositiveOrZero
    private int source;
    @NotBlank
    private String code;
    // do we need this ?
    private String name;
    @NotNull
    private Long sortId;

}
