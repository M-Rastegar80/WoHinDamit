
package at.htl.wohindamit.dto;

public record AnswerOptionResponse(
        Long id,
        String value,
        String label,
        Integer orderIndex
) {
}
