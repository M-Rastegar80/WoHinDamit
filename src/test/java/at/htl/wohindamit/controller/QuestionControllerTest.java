package at.htl.wohindamit.controller;

import at.htl.wohindamit.entity.Category;
import at.htl.wohindamit.entity.Question;
import at.htl.wohindamit.service.QuestionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class QuestionControllerTest {

    private QuestionService questionService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        questionService = mock(QuestionService.class);

        QuestionController controller =
                new QuestionController(questionService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnQuestionsForCategory() throws Exception {
        Category category = new Category();
        category.setId(2L);

        Question question = new Question();
        question.setId(1L);
        question.setCategory(category);
        question.setCode("repairable");
        question.setQuestionText(
                "Ist der Gegenstand reparierbar?"
        );
        question.setAnswerType("BOOLEAN");
        question.setOrderIndex(1);
        question.setRequired(true);
        question.setActive(true);

        when(questionService.findActiveByCategoryId(2L))
                .thenReturn(List.of(question));

        mockMvc.perform(get("/api/categories/2/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].code").value("repairable"))
                .andExpect(jsonPath("$[0].answerType").value("BOOLEAN"))
                .andExpect(jsonPath("$[0].required").value(true));
    }

    @Test
    void shouldReturnEmptyListWhenCategoryHasNoQuestions()
            throws Exception {

        when(questionService.findActiveByCategoryId(2L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/categories/2/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}