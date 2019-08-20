package no.ssb.klass.subsets.repository;

import no.ssb.klass.subsets.domain.Subset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubsetRepository extends JpaRepository<Subset, Long> {
}
