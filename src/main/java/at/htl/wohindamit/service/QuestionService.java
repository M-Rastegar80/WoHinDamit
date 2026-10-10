package at.htl.wohindamit.service;

import at.htl.wohindamit.entity.Question;
import at.htl.wohindamit.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }

    public Question save(Question question) {
        return questionRepository.save(question);
    }

    public void deleteById(Long id) {
        questionRepository.deleteById(id);
    }

    public List<Question> findActiveByCategoryId(Long categoryId) {
        return questionRepository
                .findAllByCategory_IdAndActiveTrueOrderByOrderIndexAsc(
                        categoryId
                );
    }
}