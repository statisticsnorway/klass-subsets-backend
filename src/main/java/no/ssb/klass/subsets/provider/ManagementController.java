package no.ssb.klass.subsets.provider;

import no.ssb.klass.subsets.provider.managment.SubsetDto;
import no.ssb.klass.subsets.provider.managment.SubsetVersionDto;
import no.ssb.klass.subsets.provider.utils.ErrorResponse;
import no.ssb.klass.subsets.provider.utils.RestConstants;
import no.ssb.klass.subsets.service.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping(RestConstants.REST_MANAGMENT_URI)
public class ManagementController {

    @Autowired
    private ManagementService managementService;

    @ExceptionHandler
    public ErrorResponse argumentTypeMismatchExceptionHandler(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(field -> field.getField() + " : " + field.getDefaultMessage())
                .collect(Collectors.toList());
        return new ErrorResponse("input validation failed", errors);
    }

    @RequestMapping("/")
    public RedirectView localRedirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("api-guide.html");
        return redirectView;
    }

    @RequestMapping(value = "/sets/{id}", method = RequestMethod.GET)
    public Object loadSet(@PathVariable Long id) {

        return managementService.getSubset(id);
    }

    @RequestMapping(value = "/sets/", method = RequestMethod.POST)
    public ResponseEntity saveSet(@RequestBody SubsetDto subset) {
        if (subset.getSubsetId() != null) {
            return new ResponseEntity<>("new subset should not have id", HttpStatus.BAD_REQUEST);
        }
        //TODO evaluate if we should fetch latest version and return instead, just to be sure client is in sync with DB
        Long setId = managementService.createSubset(subset);
        subset.setSubsetId(setId);
        return new ResponseEntity<>(subset, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/sets/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteSet(@PathVariable Long id) {
        if (managementService.subsetExists(id)) {
            managementService.deleteSubset(id);
            return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/sets/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateSet(@PathVariable Long id, @RequestBody SubsetDto subset) {
        //TODO better error with info if object and url ids dont match
        if (managementService.subsetExists(id) && Objects.equals(subset.getSubsetId(), id)) {
            managementService.updateSubset(subset);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("unable to update, no set with id" + id, HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/version/{id}", method = RequestMethod.GET)
    public Object loadVersion(@PathVariable Long id) {
        return managementService.getVersion(id);
    }

    @PostMapping
    @RequestMapping(value = "/version/")
    public ResponseEntity saveVersion(@Valid @RequestBody SubsetVersionDto version) {
        if (version.getVersionId() != null) {
            return new ResponseEntity<>("new version should not have id", HttpStatus.BAD_REQUEST);
        }
        //TODO evaluate if we should fetch latest version and return instead, just to be sure client is in sync with DB
        Long versionId = managementService.createVersion(version);
        version.setVersionId(versionId);
        return new ResponseEntity<>(version, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/version/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteVersion(@PathVariable Long id) {
        if (managementService.versionExists(id)) {
            managementService.deleteVersion(id);
            return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/version/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateVersion(@PathVariable Long id, @Valid @RequestBody SubsetVersionDto version) {
        //TODO better error with info if object and url ids dont match
        if (managementService.versionExists(id) && Objects.equals(version.getVersionId(), id)) {
            managementService.updateVersion(version);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("unable to update, no version with id" + id, HttpStatus.BAD_REQUEST);
        }
    }


}
