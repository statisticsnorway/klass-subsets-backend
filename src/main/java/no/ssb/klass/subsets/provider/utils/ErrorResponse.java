package no.ssb.klass.subsets.provider.utils;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ErrorResponse {

    private String message;
    private List<String> errors;

    public ErrorResponse(String message, List<String> errors) {
        super();
        this.message = message;
        this.errors = errors;
    }

    public ErrorResponse(String message, String error) {
        super();
        this.message = message;
        errors = Arrays.asList(error);
    }
}