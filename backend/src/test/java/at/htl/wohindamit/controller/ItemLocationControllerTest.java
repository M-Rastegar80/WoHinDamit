package at.htl.wohindamit.controller;

import at.htl.wohindamit.entity.Item;
import at.htl.wohindamit.entity.ItemLocation;
import at.htl.wohindamit.entity.Location;
import at.htl.wohindamit.entity.LocationType;
import at.htl.wohindamit.service.ItemLocationService;
import at.htl.wohindamit.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemLocationControllerTest {

    private ItemLocationService itemLocationService;
    private ItemService itemService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        itemLocationService = mock(ItemLocationService.class);
        itemService = mock(ItemService.class);

        ItemLocationController controller =
                new ItemLocationController(itemLocationService, itemService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnLocationsForExistingActiveItem() throws Exception {
        Item item = new Item();
        item.setId(2L);
        item.setActive(true);

        LocationType type = new LocationType();
        type.setName("Recycling Center");

        LocalDateTime lastUpdated =
                LocalDateTime.of(2026, 10, 10, 12, 30);

        Location location = new Location();
        location.setId(10L);
        location.setName("Wiener Mistplatz");
        location.setLocationType(type);
        location.setAddress("Teststraße 1");
        location.setPostalCode("1100");
        location.setCity("Wien");
        location.setSource("TEST");
        location.setLastUpdated(lastUpdated);
        location.setActive(true);
        location.setLatitude(new BigDecimal("48.1742"));
        location.setLongitude(new BigDecimal("16.3768"));
        location.setPhone("+43 1 123456");
        location.setEmail("test@example.com");
        location.setWebsite("https://example.com");
        location.setOpeningHours("Mo-Fr 08:00-18:00");

        ItemLocation itemLocation = new ItemLocation();
        itemLocation.setItem(item);
        itemLocation.setLocation(location);
        itemLocation.setSuitable(true);

        when(itemService.findById(2L)).thenReturn(Optional.of(item));
        when(itemLocationService.findSuitableLocationsForItem(2L))
                .thenReturn(List.of(itemLocation));

        mockMvc.perform(get("/api/items/2/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].name").value("Wiener Mistplatz"))
                .andExpect(jsonPath("$[0].locationType").value("Recycling Center"))
                .andExpect(jsonPath("$[0].address").value("Teststraße 1"))
                .andExpect(jsonPath("$[0].postalCode").value("1100"))
                .andExpect(jsonPath("$[0].city").value("Wien"))
                .andExpect(jsonPath("$[0].latitude").value(48.1742))
                .andExpect(jsonPath("$[0].longitude").value(16.3768))
                .andExpect(jsonPath("$[0].phone").value("+43 1 123456"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].website").value("https://example.com"))
                .andExpect(jsonPath("$[0].openingHours").value("Mo-Fr 08:00-18:00"))
                .andExpect(jsonPath("$[0].lastUpdated").exists());

        verify(itemService).findById(2L);
        verify(itemLocationService).findSuitableLocationsForItem(2L);
        verifyNoMoreInteractions(itemService, itemLocationService);
    }

    @Test
    void shouldReturnEmptyListWhenItemHasNoSuitableLocations()
            throws Exception {

        Item item = new Item();
        item.setId(2L);
        item.setActive(true);

        when(itemService.findById(2L)).thenReturn(Optional.of(item));
        when(itemLocationService.findSuitableLocationsForItem(2L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/items/2/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(itemService).findById(2L);
        verify(itemLocationService).findSuitableLocationsForItem(2L);
        verifyNoMoreInteractions(itemService, itemLocationService);
    }

    @Test
    void shouldReturnNotFoundWhenItemDoesNotExist() throws Exception {
        when(itemService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/items/999/locations"))
                .andExpect(status().isNotFound());

        verify(itemService).findById(999L);
        verifyNoInteractions(itemLocationService);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    void shouldReturnNotFoundWhenItemIsInactive() throws Exception {
        Item item = new Item();
        item.setId(2L);
        item.setActive(false);

        when(itemService.findById(2L)).thenReturn(Optional.of(item));

        mockMvc.perform(get("/api/items/2/locations"))
                .andExpect(status().isNotFound());

        verify(itemService).findById(2L);
        verifyNoInteractions(itemLocationService);
        verifyNoMoreInteractions(itemService);
    }
}
