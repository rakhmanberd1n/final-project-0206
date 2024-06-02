package nazym.project.controllers;

import nazym.project.models.Feature;
import nazym.project.services.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/features")
public class FeatureController
{
    private final String redirectUrl = "/admin/feature";

    @Autowired
    private FeatureService featureService;

    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Feature getFeature(@PathVariable(name = "id") Long id)
    {
        return featureService.findFeature(id);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addFeature(@RequestBody Feature feature)
    {
        featureService.addFeature(feature);
        return ResponseEntity.ok(redirectUrl);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteFeature(@PathVariable(name = "id" ) Long id)
    {
        featureService.deleteFeature(id);
        return ResponseEntity.ok(redirectUrl);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateFeature(@RequestBody Feature feature)
    {
        featureService.updateFeature(feature);
        return ResponseEntity.ok(redirectUrl);
    }
}