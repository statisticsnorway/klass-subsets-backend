package no.ssb.klass.subsets.domain.utils;

public class UnknownSourceException extends RuntimeException {
    public UnknownSourceException(String origin, String type) {
        super("Unknown data source (" + origin + ":" + type + ")");
    }
}
