package no.ssb.klass.subsets.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
class SubsetVersionLocalization {

    SubsetVersionLocalization(String locale) {
        localizedId.setLocale(locale);
    }

    @EmbeddedId
    private LocalizedId localizedId = new LocalizedId();

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "id")
    private SubsetVersion subsetVersion;

    @Column(nullable = false)
    private String description;
}
