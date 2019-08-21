package no.ssb.klass.subsets.domain.exceptions;

import no.ssb.klass.subsets.domain.utils.SourceType;

public class UnsupportedSourceException extends RuntimeException {
    public UnsupportedSourceException(String origin, String type) {
        super("Unsupported data source (" + origin + ":" + type + ")");
    }

    public UnsupportedSourceException(SourceType sourceType) {
        super("Unsupported data source (" + sourceType.origin + ":" + sourceType.type + ")");
    }
}
