package no.ssb.klass.subsets.provider.presentation;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserResource {

    @JsonProperty(index = 1)
    private String name;

    @JsonProperty(index = 2)
    private String division;

}
