package no.ssb.klass.subsets.repository;

import no.ssb.klass.subsets.domain.SubsetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubsetCodeRepository extends JpaRepository<SubsetCode, Long> {
}
