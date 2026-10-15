
package at.htl.wohindamit.controller;

import at.htl.wohindamit.dto.AnswerOptionResponse;
import at.htl.wohindamit.entity.AnswerOption;
import at.htl.wohindamit.entity.Question;
import at.htl.wohindamit.service.AnswerOptionService;
import at.htl.wohindamit.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions/{questionId}/answer-options")
public class AnswerOptionController {

    private final AnswerOptionService answerOptionService;
    private final QuestionService questionService;

    public AnswerOptionController(
            AnswerOptionService answerOptionService,
            QuestionService questionService) {
        this.answerOptionService = answerOptionService;
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<List<AnswerOptionResponse>> getAnswerOptions(
            @PathVariable Long questionId) {

        Optional<Question> question =
                questionService.findById(questionId);

        if (question.isEmpty()
                || !Boolean.TRUE.equals(question.get().getActive())) {
            return ResponseEntity.notFound().build();
        }

        List<AnswerOptionResponse> options =
                answerOptionService.findByQuestionId(questionId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(options);
    }

    private AnswerOptionResponse toResponse(AnswerOption option) {
        return new AnswerOptionResponse(
                option.getId(),
                option.getValue(),
                option.getLabel(),
                option.getOrderIndex()
        );
    }
}
