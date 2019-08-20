package no.ssb.klass.subsets.repository;

import no.ssb.klass.subsets.domain.SubsetSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubsetSourceRepository extends JpaRepository<SubsetSource, Long> {
}
