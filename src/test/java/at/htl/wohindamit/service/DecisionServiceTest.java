
package at.htl.wohindamit.service;

import at.htl.wohindamit.dto.DecisionRequest;
import at.htl.wohindamit.dto.DecisionResponse;
import at.htl.wohindamit.entity.Category;
import at.htl.wohindamit.entity.Recommendation;
import at.htl.wohindamit.entity.Rule;
import at.htl.wohindamit.enums.Action;
import at.htl.wohindamit.repository.RuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DecisionServiceTest {

    @Mock
    private RuleRepository ruleRepository;

    @InjectMocks
    private DecisionService decisionService;

    @Test
    void shouldReturnRecommendationWhenRuleMatches() {
        Recommendation recommendation = createRecommendation(
                10L, Action.REPAIR, "Repair"
        );

        Rule rule = createRule(
                1, Map.of("repairable", true), recommendation
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
                1, Map.of("repairable", true), recommendation
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


    @Test
    void shouldIgnoreRuleForDifferentCategory() {
        Recommendation recommendation = createRecommendation(
                10L, Action.REPAIR, "Repair"
        );

        Rule rule = createRule(
                1, Map.of("repairable", true), recommendation
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
                1, Map.of("repairable", true), recommendation
        );

        at.htl.wohindamit.entity.Item specificItem =
                new at.htl.wohindamit.entity.Item();
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