
package at.htl.wohindamit.service;

import at.htl.wohindamit.entity.LocationType;
import at.htl.wohindamit.repository.LocationTypeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationTypeServiceTest {

    private LocationTypeRepository locationTypeRepository;
    private LocationTypeService locationTypeService;

    @BeforeEach
    void setUp() {
        locationTypeRepository = mock(LocationTypeRepository.class);
        locationTypeService =
                new LocationTypeService(locationTypeRepository);
    }

    @Test
    void shouldReturnAllActiveLocationTypes() {
        LocationType type1 = new LocationType();
        type1.setId(1L);
        type1.setName("Recycling Center");
        type1.setActive(true);

        LocationType type2 = new LocationType();
        type2.setId(2L);
        type2.setName("Donation Center");
        type2.setActive(true);

        when(locationTypeRepository.findAllByActiveTrue())
                .thenReturn(List.of(type1, type2));

        List<LocationType> result =
                locationTypeService.findAllActive();

        assertEquals(2, result.size());
        assertEquals("Recycling Center", result.get(0).getName());
        assertEquals("Donation Center", result.get(1).getName());

        verify(locationTypeRepository).findAllByActiveTrue();
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveTypesExist() {
        when(locationTypeRepository.findAllByActiveTrue())
                .thenReturn(List.of());

        List<LocationType> result =
                locationTypeService.findAllActive();

        assertTrue(result.isEmpty());

        verify(locationTypeRepository).findAllByActiveTrue();
    }

    @Test
    void shouldReturnLocationTypeWhenItExistsAndIsActive() {
        LocationType type = new LocationType();
        type.setId(5L);
        type.setName("Recycling Center");
        type.setActive(true);

        when(locationTypeRepository.findById(5L))
                .thenReturn(Optional.of(type));

        Optional<LocationType> result =
                locationTypeService.findById(5L);

        assertTrue(result.isPresent());
        assertEquals("Recycling Center", result.get().getName());

        verify(locationTypeRepository).findById(5L);
    }

    @Test
    void shouldReturnEmptyWhenLocationTypeDoesNotExist() {
        when(locationTypeRepository.findById(999L))
                .thenReturn(Optional.empty());

        Optional<LocationType> result =
                locationTypeService.findById(999L);

        assertTrue(result.isEmpty());

        verify(locationTypeRepository).findById(999L);
    }

    @Test
    void shouldReturnEmptyWhenLocationTypeIsInactive() {
        LocationType type = new LocationType();
        type.setId(7L);
        type.setName("Inactive Location Type");
        type.setActive(false);

        when(locationTypeRepository.findById(7L))
                .thenReturn(Optional.of(type));

        Optional<LocationType> result =
                locationTypeService.findById(7L);

        assertTrue(result.isEmpty());

        verify(locationTypeRepository).findById(7L);
    }
}
