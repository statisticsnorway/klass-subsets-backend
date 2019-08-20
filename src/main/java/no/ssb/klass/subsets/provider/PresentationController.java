package no.ssb.klass.subsets.provider;

import no.ssb.klass.subsets.common.Language;
import no.ssb.klass.subsets.provider.presentation.SourceAndCodeResource;
import no.ssb.klass.subsets.provider.presentation.SubsetResource;
import no.ssb.klass.subsets.provider.presentation.VersionResource;
import no.ssb.klass.subsets.provider.utils.CaseInsensitiveConverter;
import no.ssb.klass.subsets.provider.utils.RestConstants;
import no.ssb.klass.subsets.service.PresentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;

@RestController
@RequestMapping(RestConstants.REST_PRESENTATION_URI)
public class PresentationController {

    @Autowired
    private PresentationService presentationService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Language.class, new CaseInsensitiveConverter<>(Language.class));
    }

    @RequestMapping("/")
    public RedirectView localRedirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("api-guide.html");
        return redirectView;
    }


    @RequestMapping(value = "/sets/{id}", method = RequestMethod.GET)
    public SubsetResource getSet(@PathVariable Long id, @RequestParam(value = "language", defaultValue = "nb") Language language) {

        return presentationService.getSubset(id, language);
    }

    @RequestMapping(value = "/versions/{id}", method = RequestMethod.GET)
    public VersionResource getVersion(@PathVariable Long id, @RequestParam(value = "language", defaultValue = "nb") Language language) {

        return presentationService.getVersion(id, language);
    }

    @RequestMapping(value = "/sets/{id}/codes", method = RequestMethod.GET)
    public SourceAndCodeResource getCodes(@PathVariable Long id,
                                          @RequestParam(value = "from") @DateTimeFormat(pattern = RestConstants.DATE_FORMAT) LocalDate from,
                                          @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = RestConstants.DATE_FORMAT) LocalDate to,
                                          @RequestParam(value = "language", defaultValue = "nb") Language language) {

        return presentationService.getCodes(id, language, from, to);
    }

    @RequestMapping(value = "/sets/{id}/codesAt", method = RequestMethod.GET)
    public SourceAndCodeResource getCodesAt(@PathVariable Long id,
                                            @RequestParam(value = "date")
                                            @DateTimeFormat(pattern = RestConstants.DATE_FORMAT) LocalDate date,
                                            @RequestParam(value = "language", defaultValue = "nb") Language language) {

        return presentationService.getCodesAt(id, language, date);
    }


}
