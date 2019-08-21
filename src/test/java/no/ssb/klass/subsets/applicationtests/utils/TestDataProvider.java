package no.ssb.klass.subsets.applicationtests.utils;

import no.ssb.klass.subsets.common.Language;
import no.ssb.klass.subsets.domain.*;
import no.ssb.klass.subsets.domain.utils.SourceType;

import java.time.LocalDate;

public class TestDataProvider {

    public static SubsetUser createUserWithSetVersion() {
        SubsetUser user = createTestUser();
        Subset set = createTestSet(false);
        SubsetVersion version = createTestVersion();

        set.setOwner(user);
        version.setParent(set);

        user.getSubsets().add(set);
        set.getSubsetVersions().add(version);
        return user;
    }

    public static SubsetUser createTestUser() {
        SubsetUser user = new SubsetUser();
        user.setDivision("723");
        user.setInitials("zxy");
        user.setFullname("Ola Forvaltersen");
        return user;
    }


    public static Subset createTestSet(boolean createUser) {
        Subset set = new Subset();
        set.setName(Language.NB.name(), "Demo Uttrekk");
        set.setDescription(Language.NB.name(), "Enkelt test  av uttrekk");
        if (createUser) {
            SubsetUser testUser = createTestUser();
            set.setOwner(testUser);
        }
        return set;
    }

    public static SubsetVersion createTestVersion() {
        SubsetVersion version = new SubsetVersion();
        version.setDescription(Language.NB.name(), "Test uttrekksversjon");
        version.setValidFrom(LocalDate.of(2018, 10, 5));
        version.setValidTo(LocalDate.of(2019, 12, 30));

        SubsetSource versionA = createVersionSource(version, "14");
        version.getSubsetSources().add(versionA);
        version.getSubsetCodes().add(createCode(version, versionA, "1"));
        version.getSubsetCodes().add(createCode(version, versionA, "2"));

        SubsetSource versionB = createVersionSource(version, "50");
        version.getSubsetSources().add(versionB);
        version.getSubsetCodes().add(createCode(version, versionB, "3"));
        version.getSubsetCodes().add(createCode(version, versionB, "4"));
        version.getSubsetCodes().add(createCode(version, versionB, "5"));
        return version;
    }

    private static SubsetSource createVersionSource(SubsetVersion version, String id) {
        SubsetSource source = new SubsetSource();
        source.setParent(version);
        source.setSourceRefId(id);
        source.setSourceType(SourceType.KLASS_VERSION);
        return source;
    }

    private static SubsetCode createCode(SubsetVersion version, SubsetSource source, String codeRef) {
        SubsetCode code = new SubsetCode();
        code.setParent(version);
        code.setSource(source);
        code.setCode(codeRef);
        code.setSortId(1);
        return code;
    }
}
