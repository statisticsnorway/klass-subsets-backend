package no.ssb.klass.subsets.provider.utils;

import no.ssb.klass.subsets.common.Language;
import no.ssb.klass.subsets.provider.PresentationController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class HalUtils {

    private HalUtils() {
    }

    public static Link createSubsetSelfLink(Long setId) {
        return linkTo(methodOn(PresentationController.class).getSet(setId, null)).withSelfRel();
    }

    public static Link createVersionSelfLink(Long setId) {
        return linkTo(methodOn(PresentationController.class).getVersion(setId, null)).withSelfRel();
    }

    public static Link createSubsetSelfLink(Long setId, String rel) {
        return linkTo(methodOn(PresentationController.class).getSet(setId, null)).withRel(rel);
    }

    public static Link createVersionSelfLink(Long setId, String rel) {
        return linkTo(methodOn(PresentationController.class).getVersion(setId, null)).withRel(rel);
    }


    public static Link createCodesAtRelation(Long id) {
        ControllerLinkBuilder linkBuilder = linkTo(ControllerLinkBuilder.methodOn(PresentationController.class).getCodesAt(id, LocalDate.now(), Language.EN));
        return new Link(HalUtils.createUriTemplate(linkBuilder, date()), "codesAt");
    }


    public static UriTemplate createUriTemplate(ControllerLinkBuilder linkBuilder, String... parameters) {
        String baseUri = linkBuilder.toUriComponentsBuilder().replaceQuery(null).build().toUriString();

        return new UriTemplate(baseUri, createParameters(parameters));
    }

    private static TemplateVariables createParameters(String... parameters) {
        List<TemplateVariable> templateVariables = new ArrayList<>();
        for (String parameter : parameters) {
            templateVariables.add(new TemplateVariable(parameter, TemplateVariable.VariableType.REQUEST_PARAM));
        }
        return new TemplateVariables(templateVariables);
    }

    private static String date() {
        return "date=<" + RestConstants.DATE_FORMAT + ">";
    }

}
