package no.ssb.klass.subsets.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import no.ssb.klass.subsets.domain.utils.DateUtil;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class SubsetVersion extends BaseEntity {

    @Column
    protected LocalDate validFrom;
    @Column
    protected LocalDate validTo;

    @ManyToOne
    private Subset parent;

    @MapKey(name = "localizedId.locale")
    @OneToMany(mappedBy = "subsetVersion",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            orphanRemoval = true)
    private Map<String, SubsetVersionLocalization> localizations = new HashMap<>();


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true)
    private final List<SubsetSource> subsetSources = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true)
    private final List<SubsetCode> subsetCodes = new ArrayList<>();

    public String getName(String locale) {
        return parent.getName(locale) + " " + DateUtil.createDatePostfix(validFrom, validTo);
    }

    public Set<String> getLocales() {
        return localizations.keySet();
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
            SubsetVersionLocalization localization = new SubsetVersionLocalization(locale);
            localization.setSubsetVersion(this);
            localizations.put(locale, localization);
        }
    }

    private void throwErrorIfLocaleIsMissing(String locale) {
        if (!localizations.containsKey(locale)) {
            //TODO custom exception ?
            throw new RuntimeException("No locale:" + locale);
        }
    }

}
