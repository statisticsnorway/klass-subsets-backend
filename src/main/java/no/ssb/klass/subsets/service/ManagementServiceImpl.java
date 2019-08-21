package no.ssb.klass.subsets.service;

import no.ssb.klass.subsets.consumer.klass.KlassConsumer;
import no.ssb.klass.subsets.domain.*;
import no.ssb.klass.subsets.domain.utils.SourceType;
import no.ssb.klass.subsets.provider.exceptions.ResourceNotFoundException;
import no.ssb.klass.subsets.provider.managment.SubsetCodeDto;
import no.ssb.klass.subsets.provider.managment.SubsetDto;
import no.ssb.klass.subsets.provider.managment.SubsetSourceDto;
import no.ssb.klass.subsets.provider.managment.SubsetVersionDto;
import no.ssb.klass.subsets.repository.SubsetRepository;
import no.ssb.klass.subsets.repository.SubsetUserRepository;
import no.ssb.klass.subsets.repository.SubsetVersionRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManagementServiceImpl implements ManagementService {

    @Autowired
    KlassConsumer klassConsumer;

    @Autowired
    SubsetUserRepository userRepository;

    @Autowired
    SubsetRepository subsetRepository;

    @Autowired
    SubsetVersionRepository versionRepository;


    @Override
    public Long createVersion(SubsetVersionDto dto) {
        Long versionId = dto.getVersionId();
        if (versionId != null) {
            throw new IllegalArgumentException("versionId must ID must be null");
        }
        SubsetVersion version = new SubsetVersion();
        mapAndSaveVersion(dto, version);
        return version.getId();
    }


    @Override
    public void updateVersion(SubsetVersionDto dto) {
        Long versionId = dto.getVersionId();
        if (versionId == null) {
            throw new IllegalArgumentException("Version must contain ID");
        }
        Optional<SubsetVersion> optionalVersion = versionRepository.findById(versionId);
        if (optionalVersion.isEmpty()) {
            throw new ResourceNotFoundException("No version with " + versionId + " Found");
        } else {
            SubsetVersion version = optionalVersion.get();
            mapAndSaveVersion(dto, version);
        }
    }

    @Override
    public SubsetVersionDto getVersion(Long id) {
        Optional<SubsetVersion> optionalVersion = versionRepository.findById(id);
        if (optionalVersion.isEmpty()) {
            throw new ResourceNotFoundException("No version with " + id + " Found");
        } else {
            SubsetVersion version = optionalVersion.get();
            SubsetVersionDto dto = new SubsetVersionDto();
            dto.setSubsetId(version.getParent().getId());
            dto.setVersionId(version.getId());
            for (String locale : version.getLocales()) {
                dto.setDescription(locale, version.getDescription(locale));
            }
            dto.setValidFrom(version.getValidFrom());
            dto.setValidTo(version.getValidTo());
            Map<String, SubsetSourceDto> sourceMap = new HashMap<>();
            dto.setSources(version.getSubsetSources().stream().map(s -> mapSources(s, sourceMap)).collect(Collectors.toList()));
            dto.setCodes(version.getSubsetCodes().stream().map(c -> mapCodes(c, sourceMap)).collect(Collectors.toList()));
            return dto;
        }
    }

    private SubsetSourceDto mapSources(SubsetSource source, Map<String, SubsetSourceDto> sourceMap) {
        SubsetSourceDto dto = new SubsetSourceDto();
        sourceMap.put(getSourceKey(source), dto);
        dto.setId(sourceMap.size());
        dto.setSourceId(source.getSourceRefId());
        dto.setSourceOrigin(source.getSourceType().origin);
        dto.setSourceType(source.getSourceType().type);

        return dto;
    }

    @NotNull
    private String getSourceKey(SubsetSource source) {
        return source.getSourceRefId() + ":" + source.getSourceType().origin + ":" + source.getSourceType().type;
    }

    private SubsetCodeDto mapCodes(SubsetCode code, Map<String, SubsetSourceDto> sourceMap) {
        SubsetCodeDto dto = new SubsetCodeDto();
        dto.setCode(code.getCode());
        dto.setSortId(code.getSortId());
        int id = sourceMap.get(getSourceKey(code.getSource())).getId();
        dto.setSource(id);
        return dto;
    }

    @Override
    public void deleteVersion(Long id) {
        versionRepository.deleteById(id);
    }

    @Override
    public Long createSubset(SubsetDto dto) {
        Long subsetId = dto.getSubsetId();
        if (subsetId != null) {
            throw new IllegalArgumentException("Set must ID must be null");
        }
        Subset set = new Subset();
        mapAndSaveSubset(dto, set);
        return set.getId();
    }


    @Override
    public void updateSubset(SubsetDto dto) {
        Long subsetId = dto.getSubsetId();
        if (subsetId == null) {
            throw new IllegalArgumentException("Set must contain ID");
        }
        Optional<Subset> optionalSubset = subsetRepository.findById(subsetId);
        if (optionalSubset.isEmpty()) {
            throw new ResourceNotFoundException("No set with " + subsetId + " Found");
        } else {
            Subset set = optionalSubset.get();
            mapAndSaveSubset(dto, set);
        }
    }

    @Override
    public SubsetDto getSubset(Long id) {
        Optional<Subset> optionalSubset = subsetRepository.findById(id);
        if (optionalSubset.isEmpty()) {
            throw new ResourceNotFoundException("No set with " + id + " Found");
        } else {
            Subset set = optionalSubset.get();
            SubsetDto dto = new SubsetDto();
            dto.setSubsetId(set.getId());
            for (String locale : set.getLocales()) {
                dto.getNames().put(locale, set.getName(locale));
            }
            for (String locale : set.getLocales()) {
                dto.getDescriptions().put(locale, set.getDescription(locale));
            }
            dto.setOwnerId(set.getOwner().getId());
            return dto;
        }
    }

    @Override
    public void deleteSubset(Long id) {
        subsetRepository.deleteById(id);
    }

    @Override
    public boolean subsetExists(Long id) {
        return subsetRepository.existsById(id);
    }

    @Override
    public boolean versionExists(Long id) {
        return versionRepository.existsById(id);
    }


    private void mapAndSaveVersion(SubsetVersionDto dto, SubsetVersion version) {
        Optional<Subset> optionalSubset = subsetRepository.findById(dto.getSubsetId());
        if (optionalSubset.isEmpty()) {
            throw new ResourceNotFoundException("Super set with id" + dto.getSubsetId() + " not found");
        }

        version.setParent(optionalSubset.get());
        dto.getDescriptions().forEach(version::setDescription);

        version.setValidFrom(dto.getValidFrom());
        version.setValidTo(dto.getValidTo());

        version.getSubsetSources().clear();
        version.getSubsetSources().addAll(dto.getSources().stream()
                .map(s -> mapDtoSources(s, version))
                .collect(Collectors.toList()));

        version.getSubsetCodes().clear();
        version.getSubsetCodes().addAll(dto.getCodes().stream()
                .map(c -> mapDtoCodes(c, version))
                .collect(Collectors.toList()));

        versionRepository.save(version);
    }


    private SubsetSource mapDtoSources(SubsetSourceDto dto, SubsetVersion version) {
        SubsetSource source = new SubsetSource();
        source.setParent(version);
        source.setSourceRefId(dto.getSourceId());
        source.setSourceType(SourceType.find(dto.getSourceOrigin(), dto.getSourceType()));
        return source;
    }

    private SubsetCode mapDtoCodes(SubsetCodeDto dto, SubsetVersion version) {
        SubsetCode code = new SubsetCode();
        code.setParent(version);
        code.setCode(dto.getCode());
        code.setSortId(dto.getSortId());
        code.setSource(version.getSubsetSources().get(dto.getSource()));
        return code;
    }

    private void mapAndSaveSubset(SubsetDto dto, Subset set) {
        dto.getNames().forEach(set::setName);
        dto.getDescriptions().forEach(set::setDescription);

        Optional<SubsetUser> optionalUser = userRepository.findById(dto.getOwnerId());
        if (optionalUser.isPresent()) {
            set.setOwner(optionalUser.get());
        } else {
            //TODO mlo - users should be created when logged in
            set.setOwner(createPlaceholderUser());
        }
        subsetRepository.save(set);
    }

    //TODO mlo - this should be replace when we get login supported
    private SubsetUser createPlaceholderUser() {
        SubsetUser user = new SubsetUser();
        user.setFullname("Ola Forvaltersen");
        user.setDivision("1337 - secret division");
        user.setInitials("off");
        userRepository.save(user);
        return user;
    }
}
