
package at.htl.wohindamit.controller;

import at.htl.wohindamit.entity.Category;
import at.htl.wohindamit.service.CategoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    private CategoryService categoryService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        categoryService = mock(CategoryService.class);

        CategoryController controller =
                new CategoryController(categoryService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnOnlyActiveCategories() throws Exception {
        Category activeCategory = new Category();
        activeCategory.setId(2L);
        activeCategory.setName("API Test Category");
        activeCategory.setDescription("Active test category");
        activeCategory.setActive(true);

        Category inactiveCategory = new Category();
        inactiveCategory.setId(3L);
        inactiveCategory.setName("Inactive Category");
        inactiveCategory.setDescription("Inactive test category");
        inactiveCategory.setActive(false);

        when(categoryService.findAll())
                .thenReturn(List.of(
                        activeCategory,
                        inactiveCategory
                ));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(
                        jsonPath("$[0].name")
                                .value("API Test Category")
                );
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveCategoriesExist()
            throws Exception {

        when(categoryService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
