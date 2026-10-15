
package at.htl.wohindamit.dto;

public record QuestionResponse(
        Long id,
        String code,
        String questionText,
        String answerType,
        Integer orderIndex,
        Boolean required
) {
}
