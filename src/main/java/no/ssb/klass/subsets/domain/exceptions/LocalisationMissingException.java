package no.ssb.klass.subsets.domain.exceptions;

import no.ssb.klass.subsets.domain.Subset;
import no.ssb.klass.subsets.domain.SubsetVersion;

public class LocalisationMissingException extends RuntimeException {
    public LocalisationMissingException(SubsetVersion subsetVersion, String locale) {
        super("Subset version (id:" + subsetVersion.getId() + ") is missing localisation for " + locale);
    }

    public LocalisationMissingException(Subset subset, String locale) {
        super("Subset (id:" + subset.getId() + ") is missing localisation for " + locale);
    }
}
