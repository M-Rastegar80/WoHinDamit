package at.htl.wohindamit.controller;

import at.htl.wohindamit.dto.DecisionResponse;
import at.htl.wohindamit.exception.GlobalExceptionHandler;
import at.htl.wohindamit.service.DecisionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DecisionControllerTest {

    private DecisionService decisionService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        decisionService = mock(DecisionService.class);

        DecisionController controller =
                new DecisionController(decisionService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturn200WhenRecommendationExists() throws Exception {
        DecisionResponse response = DecisionResponse.builder()
                .recommendationId(1L)
                .action("REPAIR")
                .title("Repair item")
                .description("Test recommendation")
                .reason("The item can be repaired")
                .build();

        when(decisionService.evaluate(any()))
                .thenReturn(Optional.of(response));

        mockMvc.perform(post("/api/decisions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"categoryId\":2,\"itemId\":2,"
                                        + "\"answers\":{\"repairable\":true}}"
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationId").value(1))
                .andExpect(jsonPath("$.action").value("REPAIR"))
                .andExpect(jsonPath("$.title").value("Repair item"))
                .andExpect(jsonPath("$.description")
                        .value("Test recommendation"))
                .andExpect(jsonPath("$.reason")
                        .value("The item can be repaired"));

        verify(decisionService).evaluate(any());
        verifyNoMoreInteractions(decisionService);
    }

    @Test
    void shouldReturn404WhenNoRuleMatches() throws Exception {
        when(decisionService.evaluate(any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/decisions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"categoryId\":2,\"itemId\":2,"
                                        + "\"answers\":{\"repairable\":false}}"
                        ))
                .andExpect(status().isNotFound());

        verify(decisionService).evaluate(any());
        verifyNoMoreInteractions(decisionService);
    }

    @Test
    void shouldReturn400WhenCategoryIdIsMissing() throws Exception {
        when(decisionService.evaluate(any()))
                .thenThrow(
                        new IllegalArgumentException(
                                "A category ID is required."
                        )
                );

        mockMvc.perform(post("/api/decisions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"itemId\":2,"
                                        + "\"answers\":{\"repairable\":true}}"
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.error")
                                .value("A category ID is required.")
                );

        verify(decisionService).evaluate(any());
        verifyNoMoreInteractions(decisionService);
    }
}
