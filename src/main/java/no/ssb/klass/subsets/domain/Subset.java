package no.ssb.klass.subsets.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Subset extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private SubsetUser owner;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubsetVersion> subsetVersions = new ArrayList<>();

    @MapKey(name = "localizedId.locale")
    @OneToMany(mappedBy = "subset", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = true)
    private Map<String, SubsetLocalization> localizations = new HashMap<>();

    public Set<String> getLocales() {
        return localizations.keySet();
    }

    public String getName(String locale) {
        throwErrorIfLocaleIsMissing(locale);
        return localizations.get(locale).getName();
    }

    public void setName(String locale, String name) {
        createLocaleIfMissing(locale);
        localizations.get(locale).setName(name);
    }

    public String getDescription(String locale) {
        throwErrorIfLocaleIsMissing(locale);
        return localizations.get(locale).getDescription();
    }

    public void setDescription(String locale, String description) {
        createLocaleIfMissing(locale);
        localizations.get(locale).setDescription(description);
    }

    private void createLocaleIfMissing(String locale) {
        if (!localizations.containsKey(locale)) {
            SubsetLocalization localization = new SubsetLocalization(locale);
            localizations.put(locale, localization);
            localization.setSubset(this);

        }
    }

    private void throwErrorIfLocaleIsMissing(String locale) {
        if (!localizations.containsKey(locale)) {
            //TODO custom exception ?
            throw new RuntimeException("No locale:" + locale);
        }
    }

}
