
package at.htl.wohindamit.controller;

import at.htl.wohindamit.dto.LocationTypeResponse;
import at.htl.wohindamit.entity.LocationType;
import at.htl.wohindamit.service.LocationTypeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LocationTypeControllerTest {

    private LocationTypeService locationTypeService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        locationTypeService = mock(LocationTypeService.class);

        LocationTypeController controller =
                new LocationTypeController(locationTypeService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnActiveLocationTypes() throws Exception {
        LocationType type = createLocationType(
                1L,
                "Recycling Center",
                "Places for recycling"
        );

        when(locationTypeService.findAllActive())
                .thenReturn(List.of(type));

        mockMvc.perform(get("/api/location-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Recycling Center"))
                .andExpect(jsonPath("$[0].description")
                        .value("Places for recycling"));
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveTypesExist()
            throws Exception {

        when(locationTypeService.findAllActive())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/location-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldReturnLocationTypeById() throws Exception {
        LocationType type = createLocationType(
                5L,
                "Donation Center",
                "Places to donate usable items"
        );

        when(locationTypeService.findById(5L))
                .thenReturn(Optional.of(type));

        mockMvc.perform(get("/api/location-types/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Donation Center"))
                .andExpect(jsonPath("$.description")
                        .value("Places to donate usable items"));
    }

    @Test
    void shouldReturn404WhenLocationTypeDoesNotExist()
            throws Exception {

        when(locationTypeService.findById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/location-types/999"))
                .andExpect(status().isNotFound());
    }

    private LocationType createLocationType(
            Long id,
            String name,
            String description
    ) {
        LocationType type = new LocationType();
        type.setId(id);
        type.setName(name);
        type.setDescription(description);
        type.setActive(true);
        return type;
    }
}
