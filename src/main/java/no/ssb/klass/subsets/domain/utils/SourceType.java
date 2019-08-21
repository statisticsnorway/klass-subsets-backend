package no.ssb.klass.subsets.domain.utils;

public enum SourceType {
    KLASS_VERSION("Klass", "ClassificationSeries"),
    KLASS_VARIANT("Klass", "ClassificationSeriesVariant");

    public final String origin;
    public final String type;

    SourceType(String origin, String type) {
        this.origin = origin;
        this.type = type;
    }

    public static SourceType find(String sourceOrigin, String sourceType) {
        for (SourceType type : SourceType.values()) {
            if (type.origin.equals(sourceOrigin) && type.type.equals(sourceType)) {
                return type;
            }
        }
        throw new UnknownSourceException(sourceOrigin, sourceType);
    }

    @Override
    public String toString() {
        return "SourceType{" +
                "origin='" + origin + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
