package no.ssb.klass.subsets.repository;

import no.ssb.klass.subsets.domain.SubsetVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubsetVersionRepository extends JpaRepository<SubsetVersion, Long> {
}
