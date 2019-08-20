package no.ssb.klass.subsets.consumer.klass;

import no.ssb.klass.subsets.consumer.klass.resources.ClassificationVersionData;

public interface KlassConsumer {

    ClassificationVersionData getClassificationVersion(String id);
}
