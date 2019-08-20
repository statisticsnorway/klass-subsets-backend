package no.ssb.klass.subsets.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class SubsetUser extends BaseEntity {

    @Column(nullable = false)
    private String initials;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private String division;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true)
    private List<Subset> subsets = new ArrayList<>();
}
