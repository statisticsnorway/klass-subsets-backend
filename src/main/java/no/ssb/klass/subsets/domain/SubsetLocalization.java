package no.ssb.klass.subsets.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
class SubsetLocalization {

    SubsetLocalization(String locale) {
        localizedId.setLocale(locale);
    }

    @EmbeddedId
    private LocalizedId localizedId = new LocalizedId();

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "id")
    private Subset subset;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;
}
