package no.ssb.klass.subsets.applicationtests.configuration;

import no.ssb.klass.subsets.SubsetsApplication;
import no.ssb.klass.subsets.configuration.SubsetsConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = SubsetsApplication.class)
public class ApplicationTestConfiguration extends SubsetsConfiguration {


}
