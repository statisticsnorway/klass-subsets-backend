package no.ssb.klass.subsets.provider.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

class BaseHalResource extends ResourceSupport {

    //Override JSON output position
    @Override
    @JsonProperty(value = "_links", index = Integer.MAX_VALUE)
    public List<Link> getLinks() {
        return super.getLinks();
    }
}
