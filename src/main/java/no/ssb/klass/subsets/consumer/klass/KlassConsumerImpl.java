package no.ssb.klass.subsets.consumer.klass;

import no.ssb.klass.subsets.consumer.klass.resources.ClassificationVersionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KlassConsumerImpl implements KlassConsumer {

    @Value("${env.klass.api.url}")
    private String klassAPiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ClassificationVersionData getClassificationVersion(String id) {
        return restTemplate.getForObject(klassAPiUrl + "/versions/" + id, ClassificationVersionData.class);
    }
}
