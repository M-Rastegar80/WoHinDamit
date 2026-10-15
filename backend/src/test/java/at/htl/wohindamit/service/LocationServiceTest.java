package at.htl.wohindamit.service;

import at.htl.wohindamit.entity.Location;
import at.htl.wohindamit.repository.LocationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationServiceTest {

    private LocationRepository locationRepository;
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        locationRepository = mock(LocationRepository.class);
        locationService = new LocationService(locationRepository);
    }

    @Test
    void shouldReturnAllActiveLocations() {
        Location location1 = new Location();
        location1.setId(1L);
        location1.setName("Recycling Center 1");
        location1.setActive(true);

        Location location2 = new Location();
        location2.setId(2L);
        location2.setName("Recycling Center 2");
        location2.setActive(true);

        when(locationRepository.findAllByActiveTrue())
                .thenReturn(List.of(location1, location2));

        List<Location> result = locationService.findAllActive();

        assertEquals(2, result.size());
        assertEquals("Recycling Center 1", result.get(0).getName());
        assertEquals("Recycling Center 2", result.get(1).getName());

        verify(locationRepository).findAllByActiveTrue();
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveLocationsExist() {
        when(locationRepository.findAllByActiveTrue())
                .thenReturn(List.of());

        List<Location> result = locationService.findAllActive();

        assertTrue(result.isEmpty());
        verify(locationRepository).findAllByActiveTrue();
    }

    @Test
    void shouldReturnLocationWhenItExistsAndIsActive() {
        Location location = new Location();
        location.setId(5L);
        location.setName("Test Recycling Center");
        location.setActive(true);

        when(locationRepository.findById(5L))
                .thenReturn(Optional.of(location));

        Optional<Location> result = locationService.findById(5L);

        assertTrue(result.isPresent());
        assertEquals("Test Recycling Center", result.get().getName());
        verify(locationRepository).findById(5L);
    }

    @Test
    void shouldReturnEmptyWhenLocationDoesNotExist() {
        when(locationRepository.findById(999L))
                .thenReturn(Optional.empty());

        Optional<Location> result = locationService.findById(999L);

        assertTrue(result.isEmpty());
        verify(locationRepository).findById(999L);
    }

    @Test
    void shouldReturnEmptyWhenLocationIsInactive() {
        Location location = new Location();
        location.setId(7L);
        location.setName("Inactive Recycling Center");
        location.setActive(false);

        when(locationRepository.findById(7L))
                .thenReturn(Optional.of(location));

        Optional<Location> result = locationService.findById(7L);

        assertTrue(result.isEmpty());
        verify(locationRepository).findById(7L);
    }
}