package no.ssb.klass.subsets.applicationtests;

import no.ssb.klass.subsets.applicationtests.utils.TestDataProvider;
import no.ssb.klass.subsets.domain.SubsetUser;
import no.ssb.klass.subsets.repository.SubsetUserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RepositoryTests {

    @Autowired
    SubsetUserRepository userRepository;

    @Test
    public void canPersistData() {
        SubsetUser subsetUser = TestDataProvider.CreateUserWithSetVersion();
        SubsetUser savedUser = userRepository.saveAndFlush(subsetUser);
        Assert.assertNotNull(savedUser.getId());

    }

}
