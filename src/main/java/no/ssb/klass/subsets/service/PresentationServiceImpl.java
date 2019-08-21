package no.ssb.klass.subsets.service;

import no.ssb.klass.subsets.common.DateRange;
import no.ssb.klass.subsets.common.Language;
import no.ssb.klass.subsets.consumer.klass.KlassConsumer;
import no.ssb.klass.subsets.consumer.klass.resources.SourceData;
import no.ssb.klass.subsets.domain.*;
import no.ssb.klass.subsets.domain.exceptions.UnsupportedSourceException;
import no.ssb.klass.subsets.provider.exceptions.ResourceNotFoundException;
import no.ssb.klass.subsets.provider.presentation.*;
import no.ssb.klass.subsets.provider.utils.HalUtils;
import no.ssb.klass.subsets.repository.SubsetRepository;
import no.ssb.klass.subsets.repository.SubsetVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PresentationServiceImpl implements PresentationService {

    @Autowired
    KlassConsumer klassConsumer;

    @Autowired
    SubsetRepository subsetRepository;

    @Autowired
    SubsetVersionRepository versionRepository;


    @Override
    public SubsetResource getSubset(long id, Language language) {
        var optional = subsetRepository.findById(id);

        Subset subset = optional.orElseThrow(() -> new ResourceNotFoundException("No subset with id " + id + " found"));
        SubsetResource subsetResource = new SubsetResource();
        UserResource userResource = createUserResource(subset.getOwner());

        subsetResource.setName(subset.getName(language.name()));
        subsetResource.setOwner(userResource);
        subsetResource.setDescription(subset.getDescription(language.name()));
        subsetResource.getVersions().addAll(createVersionSummaryResources(subset, language));

        subsetResource.add(HalUtils.createSubsetSelfLink(subset.getId()));
        subsetResource.add(HalUtils.createCodesAtRelation(subset.getId()));

        return subsetResource;
    }


    @Override
    public VersionResource getVersion(long id, Language language) {
        var optional = versionRepository.findById(id);
        SubsetVersion version = optional.orElseThrow(() -> new ResourceNotFoundException("No version with id " + id + " found"));

        VersionResource resource = new VersionResource();
        resource.setValidTo(version.getValidTo());
        resource.setValidFrom(version.getValidFrom());
        resource.setName(version.getName(language.name()));
        resource.setDescription(version.getDescription(language.name()));
        resource.setOwner(createUserResource(version.getParent().getOwner()));

        List<SourceResource> sources = generateSources(version);
        List<CodeResource> codes = generateCodes(sources, version);
        resource.setSources(sources);
        resource.setCodes(codes);

        resource.add(HalUtils.createVersionSelfLink(version.getId()));

        return resource;
    }


    @Override
    public SourceAndCodeResource getCodesAt(long id, Language language, LocalDate date) {
        var optional = subsetRepository.findById(id);
        Subset set = optional.orElseThrow(() -> new ResourceNotFoundException("No subset with id " + id + " found"));

        for (SubsetVersion version : set.getSubsetVersions()) {
            DateRange dateRange = DateRange.create(version.getValidFrom(), version.getValidTo());
            if (dateRange.contains(date)) {
                SourceAndCodeResource result = new SourceAndCodeResource();
                List<SourceResource> sources = generateSources(version);
                List<CodeResource> codes = generateCodes(sources, version);
                result.setSources(sources);
                result.setCodes(codes);
                return result;
            }
        }

        throw new ResourceNotFoundException("Subset does not contain any codes for date " + date);
    }

    @Override
    public SourceAndCodeResource getCodes(long id, Language language, LocalDate from, LocalDate to) {
        // TODO 
        throw new RuntimeException("Not Implemented");
    }


    private List<CodeResource> generateCodes(List<SourceResource> sources, SubsetVersion setVersion) {
        // a map for fast lookups, key is source primarykey from database
        Map<Long, SourceData> databaseIdToDataMap = fetchSources(sources);
        //a mapping between sources primary key and id in response 
        Map<Long, Integer> databaseIdMap = createDatabaseIdToOutputIdMap(sources);

        List<CodeResource> resourceList = new ArrayList<>();
        for (SubsetCode code : setVersion.getSubsetCodes()) {

            Long databaseId = code.getSource().getId();
            SourceData sourceData = databaseIdToDataMap.get(databaseId);
            String name = sourceData.findNameForCode(code.getCode());

            long outputId = databaseIdMap.get(databaseId);
            CodeResource codeResource = new CodeResource();
            codeResource.setSource(outputId);
            codeResource.setCode(code.getCode());
            codeResource.setName(name);
            resourceList.add(codeResource);
        }
        return resourceList;
    }

    private List<SourceResource> generateSources(SubsetVersion setVersion) {
        long id = 0;
        List<SourceResource> resourceList = new ArrayList<>();
        for (SubsetSource source : setVersion.getSubsetSources()) {
            SourceResource sourceResource = new SourceResource();
            sourceResource.setSourceId(source.getSourceRefId());
            sourceResource.setDatabaseId(source.getId());
            sourceResource.setSource(source.getSourceType());
            sourceResource.setId(id++);
            resourceList.add(sourceResource);
        }
        return resourceList;
    }

    private Map<Long, Integer> createDatabaseIdToOutputIdMap(List<SourceResource> sources) {
        Map<Long, Integer> databaseIdMap = new HashMap<>();
        for (int i = 0; i < sources.size(); i++) {
            databaseIdMap.put(sources.get(i).getDatabaseId(), i);
        }
        return databaseIdMap;
    }


    private List<VersionSummaryResource> createVersionSummaryResources(Subset set, Language language) {
        List<VersionSummaryResource> versionSummaries = new ArrayList<>();
        for (SubsetVersion version : set.getSubsetVersions()) {
            VersionSummaryResource resource = new VersionSummaryResource();
            resource.setName(version.getName(language.name()));
            resource.add(HalUtils.createVersionSelfLink(version.getId()));
            versionSummaries.add(resource);
        }
        return versionSummaries;
    }

    private UserResource createUserResource(SubsetUser owner) {
        UserResource userResource = new UserResource();
        userResource.setDivision(owner.getDivision());
        userResource.setName(owner.getFullname());
        return userResource;
    }

    private Map<Long, SourceData> fetchSources(List<SourceResource> sources) {
        Map<Long, SourceData> sourcesMap = new HashMap<>();
        for (SourceResource source : sources) {
            switch (source.getSource()) {
                case KLASS_VERSION:
                    //TODO mlo pass language ?
                    SourceData version = klassConsumer.getClassificationVersion(source.getSourceId());
                    sourcesMap.put(source.getDatabaseId(), version);
                    break;
                case KLASS_VARIANT:
                    //TODO variant
                    break;
                default:
                    throw new UnsupportedSourceException(source.getSource());
            }
        }
        return sourcesMap;
    }


}
