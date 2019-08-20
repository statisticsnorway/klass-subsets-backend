package no.ssb.klass.subsets.configuration;

import no.ssb.klass.subsets.provider.utils.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;


@Configuration
@EnableResourceServer
@EnableConfigurationProperties(SecurityProperties.class)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String MANAGMENT_ROOT_PATTERN = RestConstants.REST_MANAGMENT_URI + "**";

    private static final String HAS_WRITE = "#oauth2.hasScope('write')";
    private static final String HAS_READ = "#oauth2.hasScope('read')";

    @Value("${disableSecurity}")
    private boolean disableSecurity;

    @Autowired(required = false)
    private TokenStore tokenStore;

    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) {
        if (!disableSecurity) resources.tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //MLO: DISABLE SECURITY FOR DEV PURPOSES ONLY
        if (disableSecurity) {
            http.authorizeRequests().anyRequest().permitAll();
        } else {
            http.authorizeRequests()
                    .antMatchers(HttpMethod.GET, MANAGMENT_ROOT_PATTERN).access(HAS_READ)
                    .antMatchers(HttpMethod.POST, MANAGMENT_ROOT_PATTERN).access(HAS_WRITE)
                    .antMatchers(HttpMethod.PATCH, MANAGMENT_ROOT_PATTERN).access(HAS_WRITE)
                    .antMatchers(HttpMethod.PUT, MANAGMENT_ROOT_PATTERN).access(HAS_WRITE)
                    .antMatchers(HttpMethod.DELETE, MANAGMENT_ROOT_PATTERN).access(HAS_WRITE);
        }

    }


}
