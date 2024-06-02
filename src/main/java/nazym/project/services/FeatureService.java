package nazym.project.services;

import nazym.project.models.Feature;
import nazym.project.repositories.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeatureService
{
    @Autowired
    private FeatureRepository featureRepository;

    public List<Feature> allFeatures()
    {
        return featureRepository.findAll();
    }

    public Feature findFeature(Long id)
    {
        return featureRepository.findById(id).orElse(null);
    }

    public void addFeature(Feature feature)
    {
        featureRepository.save(feature);
    }

    public void deleteFeature(Long id)
    {
        featureRepository.deleteById(id);
    }

    public void updateFeature(Feature feature)
    {
        featureRepository.save(feature);
    }
}