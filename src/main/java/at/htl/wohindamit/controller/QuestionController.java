
package at.htl.wohindamit.controller;

import at.htl.wohindamit.dto.QuestionResponse;
import at.htl.wohindamit.entity.Question;
import at.htl.wohindamit.service.QuestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories/{categoryId}/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public List<QuestionResponse> getQuestions(
            @PathVariable Long categoryId) {

        return questionService.findActiveByCategoryId(categoryId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private QuestionResponse toResponse(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getCode(),
                question.getQuestionText(),
                question.getAnswerType(),
                question.getOrderIndex(),
                question.getRequired()
        );
    }
}
