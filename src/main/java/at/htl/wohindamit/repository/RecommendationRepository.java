package at.htl.wohindamit.repository;

import at.htl.wohindamit.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository
        extends JpaRepository<Recommendation, Long> {
}