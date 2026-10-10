package at.htl.wohindamit.controller;

import at.htl.wohindamit.dto.LocationResponse;
import at.htl.wohindamit.entity.Location;
import at.htl.wohindamit.entity.LocationType;
import at.htl.wohindamit.service.LocationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LocationControllerTest {

    private LocationService locationService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        locationService = mock(LocationService.class);

        LocationController controller =
                new LocationController(locationService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnActiveLocations() throws Exception {
        LocationType locationType = new LocationType();
        locationType.setName("Recycling Center");

        Location location = createLocation(1L, locationType);

        when(locationService.findAllActive())
                .thenReturn(List.of(location));

        mockMvc.perform(get("/api/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Recycling Center"))
                .andExpect(jsonPath("$[0].locationType").value("Recycling Center"))
                .andExpect(jsonPath("$[0].city").value("Wien"));
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveLocationsExist()
            throws Exception {

        when(locationService.findAllActive())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldReturnLocationById() throws Exception {
        LocationType locationType = new LocationType();
        locationType.setName("Recycling Center");

        Location location = createLocation(5L, locationType);

        when(locationService.findById(5L))
                .thenReturn(Optional.of(location));

        mockMvc.perform(get("/api/locations/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Test Recycling Center"))
                .andExpect(jsonPath("$.address").value("Testgasse 1"))
                .andExpect(jsonPath("$.postalCode").value("1010"))
                .andExpect(jsonPath("$.city").value("Wien"))
                .andExpect(jsonPath("$.locationType").value("Recycling Center"));
    }

    @Test
    void shouldReturn404WhenLocationDoesNotExist() throws Exception {
        when(locationService.findById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/locations/999"))
                .andExpect(status().isNotFound());
    }

    private Location createLocation(
            Long id,
            LocationType locationType
    ) {
        Location location = new Location();
        location.setId(id);
        location.setLocationType(locationType);
        location.setName("Test Recycling Center");
        location.setAddress("Testgasse 1");
        location.setPostalCode("1010");
        location.setCity("Wien");
        location.setLatitude(new BigDecimal("48.208200"));
        location.setLongitude(new BigDecimal("16.373800"));
        location.setSource("TEST");
        location.setLastUpdated(LocalDateTime.of(2026, 10, 11, 12, 0));
        location.setActive(true);
        return location;
    }
}