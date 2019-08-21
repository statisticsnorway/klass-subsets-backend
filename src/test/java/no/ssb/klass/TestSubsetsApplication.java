package no.ssb.klass;

import no.ssb.klass.subsets.applicationtests.utils.TestDataProvider;
import no.ssb.klass.subsets.domain.SubsetUser;
import no.ssb.klass.subsets.repository.SubsetUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * Spring application setup with some test data for quick testing and debugging
 */
@SpringBootApplication
public class TestSubsetsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestSubsetsApplication.class, args);
    }

    @Autowired
    SubsetUserRepository userRepository;


    @PostConstruct
    public void addTestData() {
        SubsetUser user = TestDataProvider.createUserWithSetVersion();
        user = userRepository.saveAndFlush(user);
    }

}
