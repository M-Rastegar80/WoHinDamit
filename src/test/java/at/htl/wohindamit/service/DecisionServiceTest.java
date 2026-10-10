
package at.htl.wohindamit.service;

import at.htl.wohindamit.dto.DecisionRequest;
import at.htl.wohindamit.dto.DecisionResponse;
import at.htl.wohindamit.entity.AnswerOption;
import at.htl.wohindamit.entity.Category;
import at.htl.wohindamit.entity.Item;
import at.htl.wohindamit.entity.Question;
import at.htl.wohindamit.entity.Recommendation;
import at.htl.wohindamit.entity.Rule;
import at.htl.wohindamit.enums.Action;
import at.htl.wohindamit.repository.AnswerOptionRepository;
import at.htl.wohindamit.repository.QuestionRepository;
import at.htl.wohindamit.repository.RuleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DecisionServiceTest {

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerOptionRepository answerOptionRepository;

    @InjectMocks
    private DecisionService decisionService;

    @BeforeEach
    void setUpQuestions() {
        Question question = new Question();
        question.setId(1L);
        question.setCode("repairable");
        question.setQuestionText(
                "Ist der Gegenstand reparierbar?"
        );
        question.setAnswerType("BOOLEAN");
        question.setOrderIndex(1);
        question.setRequired(true);
        question.setActive(true);

        lenient().when(questionRepository
                        .findAllByCategory_IdAndActiveTrueOrderByOrderIndexAsc(1L))
                .thenReturn(List.of(question));
    }

    @Test
    void shouldRejectRequestWhenRequiredAnswerIsMissing() {
        DecisionRequest request = createRequest(Map.of());

        assertThrows(
                IllegalArgumentException.class,
                () -> decisionService.evaluate(request)
        );
    }

    @Test
    void shouldRejectUnknownAnswerCode() {
        DecisionRequest request = createRequest(
                Map.of("unknownQuestion", true)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> decisionService.evaluate(request)
        );
    }

    @Test
    void shouldRejectInvalidBooleanAnswer() {
        DecisionRequest request = createRequest(
                Map.of("repairable", "maybe")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> decisionService.evaluate(request)
        );
    }

    @Test
    void shouldAcceptStringBooleanAnswer() {
        Recommendation recommendation = createRecommendation(
                10L, Action.REPAIR, "Repair"
        );

        Rule rule = createRule(
                1,
                Map.of("repairable", true),
                recommendation
        );

        when(ruleRepository.findAllByActiveTrue())
                .thenReturn(List.of(rule));

        DecisionRequest request = createRequest(
                Map.of("repairable", "true")
        );

        Optional<DecisionResponse> result =
                decisionService.evaluate(request);

        assertTrue(result.isPresent());
        assertEquals(
                Long.valueOf(10L),
                result.get().getRecommendationId()
        );
    }

    @Test
    void shouldReturnRecommendationWhenRuleMatches() {
        Recommendation recommendation = createRecommendation(
                10L, Action.REPAIR, "Repair"
        );

        Rule rule = createRule(
                1,
                Map.of("repairable", true),
                recommendation
        );

        when(ruleRepository.findAllByActiveTrue())
                .thenReturn(List.of(rule));

        DecisionRequest request = createRequest(
                Map.of("repairable", true)
        );

        Optional<DecisionResponse> result =
                decisionService.evaluate(request);

        assertTrue(result.isPresent());
        assertEquals(
                Long.valueOf(10L),
                result.get().getRecommendationId()
        );
        assertEquals("REPAIR", result.get().getAction());
        assertEquals("Repair", result.get().getTitle());
    }

    @Test
    void shouldReturnEmptyWhenConditionsDoNotMatch() {
        Recommendation recommendation = createRecommendation(
                10L, Action.REPAIR, "Repair"
        );

        Rule rule = createRule(
                1,
                Map.of("repairable", true),
                recommendation
        );

        when(ruleRepository.findAllByActiveTrue())
                .thenReturn(List.of(rule));

        DecisionRequest request = createRequest(
                Map.of("repairable", false)
        );

        Optional<DecisionResponse> result =
                decisionService.evaluate(request);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldChooseRuleWithHighestPriority() {
        Recommendation repairRecommendation = createRecommendation(
                10L, Action.REPAIR, "Repair"
        );

        Recommendation recycleRecommendation = createRecommendation(
                20L, Action.RECYCLE, "Recycle"
        );

        Rule lowPriorityRule = createRule(
                1,
                Map.of("repairable", true),
                repairRecommendation
        );

        Rule highPriorityRule = createRule(
                10,
                Map.of("repairable", true),
                recycleRecommendation
        );

        when(ruleRepository.findAllByActiveTrue())
                .thenReturn(List.of(
                        lowPriorityRule,
                        highPriorityRule
                ));

        DecisionRequest request = createRequest(
                Map.of("repairable", true)
        );

        Optional<DecisionResponse> result =
                decisionService.evaluate(request);

        assertTrue(result.isPresent());
        assertEquals(
                Long.valueOf(20L),
                result.get().getRecommendationId()
        );
        assertEquals("RECYCLE", result.get().getAction());
    }

    @Test
    void shouldIgnoreRuleForDifferentCategory() {
        Recommendation recommendation = createRecommendation(
                10L, Action.REPAIR, "Repair"
        );

        Rule rule = createRule(
                1,
                Map.of("repairable", true),
                recommendation
        );

        Category differentCategory = new Category();
        differentCategory.setId(2L);
        rule.setCategory(differentCategory);

        when(ruleRepository.findAllByActiveTrue())
                .thenReturn(List.of(rule));

        DecisionRequest request = createRequest(
                Map.of("repairable", true)
        );

        Optional<DecisionResponse> result =
                decisionService.evaluate(request);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldIgnoreRuleForDifferentItem() {
        Recommendation recommendation = createRecommendation(
                10L, Action.REPAIR, "Repair"
        );

        Rule rule = createRule(
                1,
                Map.of("repairable", true),
                recommendation
        );

        Item specificItem = new Item();
        specificItem.setId(7L);
        rule.setItem(specificItem);

        when(ruleRepository.findAllByActiveTrue())
                .thenReturn(List.of(rule));

        DecisionRequest request = createRequest(
                Map.of("repairable", true)
        );
        request.setItemId(5L);

        Optional<DecisionResponse> result =
                decisionService.evaluate(request);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldRejectChoiceNotInConfiguredOptions() {
        Question choiceQuestion = new Question();
        choiceQuestion.setId(2L);
        choiceQuestion.setCode("disposalMethod");
        choiceQuestion.setQuestionText(
                "What should happen to the item?"
        );
        choiceQuestion.setAnswerType("SINGLE_CHOICE");
        choiceQuestion.setOrderIndex(1);
        choiceQuestion.setRequired(true);
        choiceQuestion.setActive(true);

        when(questionRepository
                .findAllByCategory_IdAndActiveTrueOrderByOrderIndexAsc(1L))
                .thenReturn(List.of(choiceQuestion));

        AnswerOption option = new AnswerOption();
        option.setId(1L);
        option.setQuestion(choiceQuestion);
        option.setValue("donate");
        option.setLabel("Donate");
        option.setOrderIndex(1);

        when(answerOptionRepository
                .findAllByQuestion_IdOrderByOrderIndexAsc(2L))
                .thenReturn(List.of(option));

        DecisionRequest request = createRequest(
                Map.of("disposalMethod", "repair")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> decisionService.evaluate(request)
        );
    }

    private DecisionRequest createRequest(
            Map<String, Object> answers
    ) {
        DecisionRequest request = new DecisionRequest();
        request.setCategoryId(1L);
        request.setAnswers(answers);
        return request;
    }

    private Recommendation createRecommendation(
            Long id,
            Action action,
            String title
    ) {
        Recommendation recommendation = new Recommendation();
        recommendation.setId(id);
        recommendation.setAction(action);
        recommendation.setTitle(title);
        recommendation.setDescription("Test description");
        recommendation.setPriority(1);
        recommendation.setActive(true);

        return recommendation;
    }

    private Rule createRule(
            int priority,
            Map<String, Object> conditions,
            Recommendation recommendation
    ) {
        Category category = new Category();
        category.setId(1L);

        Rule rule = new Rule();
        rule.setCategory(category);
        rule.setPriority(priority);
        rule.setConditions(conditions);
        rule.setRecommendation(recommendation);
        rule.setReason("Test reason");
        rule.setActive(true);

        return rule;
    }
}
