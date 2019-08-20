package no.ssb.klass.subsets.service;

import no.ssb.klass.subsets.provider.managment.SubsetDto;
import no.ssb.klass.subsets.provider.managment.SubsetVersionDto;

public interface ManagementService {

    Long createVersion(SubsetVersionDto version);

    void updateVersion(SubsetVersionDto version);

    Object getVersion(Long id);

    void deleteVersion(Long id);

    Long createSubset(SubsetDto set);

    void updateSubset(SubsetDto set);

    SubsetDto getSubset(Long id);

    void deleteSubset(Long id);


    boolean subsetExists(Long id);

    boolean versionExists(Long id);
}
