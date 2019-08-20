package no.ssb.klass.subsets.repository;

import no.ssb.klass.subsets.domain.SubsetUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubsetUserRepository extends JpaRepository<SubsetUser, Long> {
}
