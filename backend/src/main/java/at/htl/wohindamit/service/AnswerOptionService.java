
package at.htl.wohindamit.service;

import at.htl.wohindamit.entity.AnswerOption;
import at.htl.wohindamit.repository.AnswerOptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnswerOptionService {

    private final AnswerOptionRepository answerOptionRepository;

    public AnswerOptionService(
            AnswerOptionRepository answerOptionRepository) {
        this.answerOptionRepository = answerOptionRepository;
    }

    public List<AnswerOption> findAll() {
        return answerOptionRepository.findAll();
    }

    public Optional<AnswerOption> findById(Long id) {
        return answerOptionRepository.findById(id);
    }

    public List<AnswerOption> findByQuestionId(Long questionId) {
        return answerOptionRepository
                .findAllByQuestion_IdOrderByOrderIndexAsc(questionId);
    }

    public AnswerOption save(AnswerOption answerOption) {
        return answerOptionRepository.save(answerOption);
    }

    public void deleteById(Long id) {
        answerOptionRepository.deleteById(id);
    }
}
