package nazym.project.repositories;

import nazym.project.models.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long>
{
    @Override
    @Query("SELECT f FROM Feature f ORDER BY f.id")
    List<Feature> findAll();
}