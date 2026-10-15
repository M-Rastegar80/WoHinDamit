package at.htl.wohindamit.service;

import at.htl.wohindamit.entity.Recommendation;
import at.htl.wohindamit.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public RecommendationService(
            RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    public List<Recommendation> findAll() {
        return recommendationRepository.findAll();
    }

    public Optional<Recommendation> findById(Long id) {
        return recommendationRepository.findById(id);
    }

    public Recommendation save(Recommendation recommendation) {
        return recommendationRepository.save(recommendation);
    }

    public void deleteById(Long id) {
        recommendationRepository.deleteById(id);
    }
}