package at.htl.wohindamit.controller;

import at.htl.wohindamit.entity.AnswerOption;
import at.htl.wohindamit.entity.Question;
import at.htl.wohindamit.service.AnswerOptionService;
import at.htl.wohindamit.service.QuestionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AnswerOptionControllerTest {

    private AnswerOptionService answerOptionService;
    private QuestionService questionService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        answerOptionService = mock(AnswerOptionService.class);
        questionService = mock(QuestionService.class);

        AnswerOptionController controller =
                new AnswerOptionController(
                        answerOptionService,
                        questionService
                );

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnOptionsForActiveQuestion() throws Exception {
        Question question = new Question();
        question.setId(1L);
        question.setActive(true);

        AnswerOption yes = new AnswerOption();
        yes.setId(1L);
        yes.setValue("true");
        yes.setLabel("Ja");
        yes.setOrderIndex(1);

        AnswerOption no = new AnswerOption();
        no.setId(2L);
        no.setValue("false");
        no.setLabel("Nein");
        no.setOrderIndex(2);

        when(questionService.findById(1L))
                .thenReturn(Optional.of(question));

        when(answerOptionService.findByQuestionId(1L))
                .thenReturn(List.of(yes, no));

        mockMvc.perform(
                        get("/api/questions/1/answer-options")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].value").value("true"))
                .andExpect(jsonPath("$[0].label").value("Ja"))
                .andExpect(jsonPath("$[0].orderIndex").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].value").value("false"))
                .andExpect(jsonPath("$[1].label").value("Nein"))
                .andExpect(jsonPath("$[1].orderIndex").value(2));

        verify(questionService).findById(1L);
        verify(answerOptionService).findByQuestionId(1L);
        verifyNoMoreInteractions(questionService, answerOptionService);
    }

    @Test
    void shouldReturnEmptyListWhenActiveQuestionHasNoOptions()
            throws Exception {
        Question question = new Question();
        question.setId(2L);
        question.setActive(true);

        when(questionService.findById(2L))
                .thenReturn(Optional.of(question));

        when(answerOptionService.findByQuestionId(2L))
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/api/questions/2/answer-options")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(questionService).findById(2L);
        verify(answerOptionService).findByQuestionId(2L);
        verifyNoMoreInteractions(questionService, answerOptionService);
    }

    @Test
    void shouldReturn404WhenQuestionDoesNotExist() throws Exception {
        when(questionService.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        get("/api/questions/99/answer-options")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        verify(questionService).findById(99L);
        verifyNoInteractions(answerOptionService);
        verifyNoMoreInteractions(questionService);
    }

    @Test
    void shouldReturn404WhenQuestionIsInactive() throws Exception {
        Question question = new Question();
        question.setId(3L);
        question.setActive(false);

        when(questionService.findById(3L))
                .thenReturn(Optional.of(question));

        mockMvc.perform(
                        get("/api/questions/3/answer-options")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        verify(questionService).findById(3L);
        verifyNoInteractions(answerOptionService);
        verifyNoMoreInteractions(questionService);
    }
}
