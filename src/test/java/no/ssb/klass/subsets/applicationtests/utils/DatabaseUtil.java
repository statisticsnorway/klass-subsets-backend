package no.ssb.klass.subsets.applicationtests.utils;

import no.ssb.klass.subsets.repository.SubsetRepository;
import no.ssb.klass.subsets.repository.SubsetUserRepository;
import no.ssb.klass.subsets.repository.SubsetVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Component
public class DatabaseUtil {

    @Autowired
    private SubsetUserRepository userRepository;
    @Autowired
    private SubsetRepository subsetRepository;
    @Autowired
    private SubsetVersionRepository versionRepository;

    @Autowired
    private EntityManager em;

    @Transactional
    public void clearDatabase() {
        versionRepository.deleteAll();
        subsetRepository.deleteAll();
        userRepository.deleteAll();
        em.clear();
        em.flush();

    }
}
