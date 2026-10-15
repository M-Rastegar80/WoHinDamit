package at.htl.wohindamit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DecisionResponse {

    private Long recommendationId;

    private String action;

    private String title;

    private String description;

    private String reason;
}