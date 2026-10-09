
package at.htl.wohindamit.service;

import at.htl.wohindamit.dto.DecisionRequest;
import at.htl.wohindamit.dto.DecisionResponse;
import at.htl.wohindamit.entity.Recommendation;
import at.htl.wohindamit.entity.Rule;
import at.htl.wohindamit.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class DecisionService {

    private final RuleRepository ruleRepository;

    public DecisionService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public Optional<DecisionResponse> evaluate(DecisionRequest request) {
        if (request == null || request.getCategoryId() == null) {
            throw new IllegalArgumentException(
                    "A category ID is required."
            );
        }

        Map<String, Object> answers = request.getAnswers() != null
                ? request.getAnswers()
                : Map.of();

        List<Rule> matchingRules = ruleRepository.findAllByActiveTrue()
                .stream()
                .filter(rule -> matchesScope(rule, request))
                .filter(rule -> matchesConditions(
                        rule.getConditions(), answers
                ))
                .sorted(
                        Comparator.comparing(Rule::getPriority).reversed()
                )
                .toList();

        return matchingRules.stream()
                .findFirst()
                .map(this::toResponse);
    }

    private boolean matchesScope(
            Rule rule,
            DecisionRequest request
    ) {
        if (rule.getCategory() != null
                && !Objects.equals(
                rule.getCategory().getId(),
                request.getCategoryId()
        )) {
            return false;
        }

        return rule.getItem() == null
                || Objects.equals(
                rule.getItem().getId(),
                request.getItemId()
        );
    }

    private boolean matchesConditions(
            Map<String, Object> conditions,
            Map<String, Object> answers
    ) {
        if (conditions == null || conditions.isEmpty()) {
            return true;
        }

        return conditions.entrySet().stream().allMatch(entry ->
                answers.containsKey(entry.getKey())
                        && valuesMatch(
                        entry.getValue(),
                        answers.get(entry.getKey())
                )
        );
    }

    private boolean valuesMatch(Object expected, Object actual) {
        if (expected instanceof Number expectedNumber
                && actual instanceof Number actualNumber) {
            return new BigDecimal(expectedNumber.toString())
                    .compareTo(new BigDecimal(actualNumber.toString())) == 0;
        }

        return Objects.equals(expected, actual);
    }

    private DecisionResponse toResponse(Rule rule) {
        Recommendation recommendation = rule.getRecommendation();

        return DecisionResponse.builder()
                .recommendationId(recommendation.getId())
                .action(recommendation.getAction().name())
                .title(recommendation.getTitle())
                .description(recommendation.getDescription())
                .reason(rule.getReason())
                .build();
    }
}