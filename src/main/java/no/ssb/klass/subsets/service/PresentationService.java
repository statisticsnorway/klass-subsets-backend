package no.ssb.klass.subsets.service;

import no.ssb.klass.subsets.common.Language;
import no.ssb.klass.subsets.provider.presentation.SourceAndCodeResource;
import no.ssb.klass.subsets.provider.presentation.SubsetResource;
import no.ssb.klass.subsets.provider.presentation.VersionResource;

import java.time.LocalDate;

public interface PresentationService {

    VersionResource getVersion(long id, Language language);

    SubsetResource getSubset(long id, Language language);

    SourceAndCodeResource getCodesAt(long id, Language language, LocalDate date);

    SourceAndCodeResource getCodes(long id, Language language, LocalDate from, LocalDate to);

}
