package no.ssb.klass.subsets.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class SubsetCode extends BaseEntity {

    @Column(nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    private SubsetSource source;

    @Column(nullable = false)
    private long sortId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private SubsetVersion parent;


}
