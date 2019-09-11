package no.ssb.klass.subsets.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    @Column(nullable = false)
    private Date lastModified = new Date(System.currentTimeMillis());

    @PreUpdate
    protected void update() {
        lastModified = new Date(System.currentTimeMillis());
    }
}