
package at.htl.wohindamit.service;

import at.htl.wohindamit.dto.DecisionRequest;
import at.htl.wohindamit.dto.DecisionResponse;
import at.htl.wohindamit.entity.AnswerOption;
import at.htl.wohindamit.entity.Question;
import at.htl.wohindamit.entity.Recommendation;
import at.htl.wohindamit.entity.Rule;
import at.htl.wohindamit.repository.AnswerOptionRepository;
import at.htl.wohindamit.repository.QuestionRepository;
import at.htl.wohindamit.repository.RuleRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class DecisionService {

    private final RuleRepository ruleRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;

    public DecisionService(
            RuleRepository ruleRepository,
            QuestionRepository questionRepository,
            AnswerOptionRepository answerOptionRepository) {
        this.ruleRepository = ruleRepository;
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
    }

    public Optional<DecisionResponse> evaluate(DecisionRequest request) {
        if (request == null || request.getCategoryId() == null) {
            throw new IllegalArgumentException(
                    "A category ID is required."
            );
        }

        Map<String, Object> providedAnswers =
                request.getAnswers() != null
                        ? request.getAnswers()
                        : Map.of();

        Map<String, Object> answers = validateAnswers(
                request.getCategoryId(),
                providedAnswers
        );

        List<Rule> matchingRules = ruleRepository.findAllByActiveTrue()
                .stream()
                .filter(rule -> rule.getRecommendation() != null)
                .filter(rule -> Boolean.TRUE.equals(
                        rule.getRecommendation().getActive()
                ))
                .filter(rule -> matchesScope(rule, request))
                .filter(rule -> matchesConditions(
                        rule.getConditions(), answers
                ))
                .sorted(
                        Comparator.comparing(
                                Rule::getPriority
                        ).reversed()
                )
                .toList();

        return matchingRules.stream()
                .findFirst()
                .map(this::toResponse);
    }

    private Map<String, Object> validateAnswers(
            Long categoryId,
            Map<String, Object> providedAnswers) {

        List<Question> questions = questionRepository
                .findAllByCategory_IdAndActiveTrueOrderByOrderIndexAsc(
                        categoryId
                );

        Map<String, Question> questionsByCode = new LinkedHashMap<>();

        for (Question question : questions) {
            questionsByCode.put(question.getCode(), question);
        }

        for (String answerCode : providedAnswers.keySet()) {
            if (!questionsByCode.containsKey(answerCode)) {
                throw new IllegalArgumentException(
                        "Unknown answer code: " + answerCode
                );
            }
        }

        Map<String, Object> normalizedAnswers = new LinkedHashMap<>();

        for (Question question : questions) {
            String code = question.getCode();
            boolean answerProvided = providedAnswers.containsKey(code);
            Object answer = providedAnswers.get(code);

            if (Boolean.TRUE.equals(question.getRequired())
                    && (!answerProvided || answer == null)) {
                throw new IllegalArgumentException(
                        "Answer required for question: " + code
                );
            }

            if (!answerProvided || answer == null) {
                continue;
            }

            normalizedAnswers.put(
                    code,
                    normalizeAnswer(question, answer)
            );
        }

        return normalizedAnswers;
    }

    private Object normalizeAnswer(Question question, Object answer) {
        String answerType = question.getAnswerType() == null
                ? ""
                : question.getAnswerType()
                .trim()
                .toUpperCase(Locale.ROOT);

        switch (answerType) {
            case "BOOLEAN":
            case "YES_NO":
            case "TRUE_FALSE":
                if (answer instanceof Boolean) {
                    return answer;
                }

                if (answer instanceof String value) {
                    if ("true".equalsIgnoreCase(value.trim())) {
                        return true;
                    }

                    if ("false".equalsIgnoreCase(value.trim())) {
                        return false;
                    }
                }

                throw new IllegalArgumentException(
                        "Answer for question '" + question.getCode()
                                + "' must be true or false."
                );

            case "INTEGER":
            case "NUMBER":
            case "DECIMAL":
                return normalizeNumber(question, answer);

            case "TEXT":
            case "STRING":
                if (answer instanceof String) {
                    return answer;
                }

                throw new IllegalArgumentException(
                        "Answer for question '" + question.getCode()
                                + "' must be a string."
                );

            case "SINGLE_CHOICE":
            case "SELECT":
                if (answer instanceof String value) {
                    return normalizeSingleChoice(question, value);
                }

                throw new IllegalArgumentException(
                        "Answer for question '" + question.getCode()
                                + "' must be a single option value."
                );

            case "MULTIPLE_CHOICE":
            case "MULTI_SELECT":
                if (answer instanceof Collection<?> values) {
                    return normalizeMultipleChoice(question, values);
                }

                throw new IllegalArgumentException(
                        "Answer for question '" + question.getCode()
                                + "' must be a list of option values."
                );

            default:
                throw new IllegalArgumentException(
                        "Unsupported answer type for question '"
                                + question.getCode() + "': " + answerType
                );
        }
    }

    private String normalizeSingleChoice(
            Question question,
            String selectedValue) {

        List<String> allowedValues = findAllowedOptionValues(question);

        if (!allowedValues.contains(selectedValue)) {
            throw new IllegalArgumentException(
                    "Invalid option '" + selectedValue
                            + "' for question: " + question.getCode()
            );
        }

        return selectedValue;
    }

    private List<String> normalizeMultipleChoice(
            Question question,
            Collection<?> selectedValues) {

        List<String> allowedValues = findAllowedOptionValues(question);
        List<String> normalizedValues = new ArrayList<>();

        for (Object selectedValue : selectedValues) {
            if (!(selectedValue instanceof String value)
                    || !allowedValues.contains(value)) {
                throw new IllegalArgumentException(
                        "Invalid option for question: "
                                + question.getCode()
                );
            }

            normalizedValues.add(value);
        }

        if (normalizedValues.isEmpty()
                && Boolean.TRUE.equals(question.getRequired())) {
            throw new IllegalArgumentException(
                    "At least one option is required for question: "
                            + question.getCode()
            );
        }

        return List.copyOf(normalizedValues);
    }

    private List<String> findAllowedOptionValues(Question question) {
        return answerOptionRepository
                .findAllByQuestion_IdOrderByOrderIndexAsc(
                        question.getId()
                )
                .stream()
                .map(AnswerOption::getValue)
                .toList();
    }

    private BigDecimal normalizeNumber(
            Question question,
            Object answer) {

        BigDecimal number;

        try {
            if (answer instanceof Number) {
                number = new BigDecimal(answer.toString());
            } else if (answer instanceof String value) {
                number = new BigDecimal(value.trim());
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Answer for question '" + question.getCode()
                            + "' must be a number."
            );
        }

        if ("INTEGER".equalsIgnoreCase(question.getAnswerType())
                && number.stripTrailingZeros().scale() > 0) {
            throw new IllegalArgumentException(
                    "Answer for question '" + question.getCode()
                            + "' must be an integer."
            );
        }

        return number;
    }

    private boolean matchesScope(
            Rule rule,
            DecisionRequest request) {

        if (rule.getCategory() != null
                && !Objects.equals(
                rule.getCategory().getId(),
                request.getCategoryId())) {
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
            Map<String, Object> answers) {

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
                    .compareTo(
                            new BigDecimal(actualNumber.toString())
                    ) == 0;
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
