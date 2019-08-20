package no.ssb.klass.subsets.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import no.ssb.klass.subsets.domain.utils.SourceType;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class SubsetSource extends BaseEntity {

    @Column(nullable = false)
    private String sourceRefId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SourceType sourceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private SubsetVersion parent;

}
