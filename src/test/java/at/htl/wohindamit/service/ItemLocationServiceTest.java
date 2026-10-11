package at.htl.wohindamit.service;

import at.htl.wohindamit.entity.ItemLocation;
import at.htl.wohindamit.repository.ItemLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ItemLocationServiceTest {

    private ItemLocationRepository itemLocationRepository;
    private ItemLocationService itemLocationService;

    @BeforeEach
    void setUp() {
        itemLocationRepository = mock(ItemLocationRepository.class);
        itemLocationService =
                new ItemLocationService(itemLocationRepository);
    }

    @Test
    void shouldReturnAllItemLocations() {
        ItemLocation itemLocation = new ItemLocation();

        when(itemLocationRepository.findAll())
                .thenReturn(List.of(itemLocation));

        List<ItemLocation> result = itemLocationService.findAll();

        assertEquals(1, result.size());
        assertSame(itemLocation, result.get(0));
        verify(itemLocationRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoItemLocationsExist() {
        when(itemLocationRepository.findAll())
                .thenReturn(List.of());

        List<ItemLocation> result = itemLocationService.findAll();

        assertTrue(result.isEmpty());
        verify(itemLocationRepository).findAll();
    }

    @Test
    void shouldFindExistingItemLocationById() {
        ItemLocation itemLocation = new ItemLocation();

        when(itemLocationRepository.findById(1L))
                .thenReturn(Optional.of(itemLocation));

        Optional<ItemLocation> result =
                itemLocationService.findById(1L);

        assertTrue(result.isPresent());
        assertSame(itemLocation, result.get());
        verify(itemLocationRepository).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenItemLocationIdDoesNotExist() {
        when(itemLocationRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<ItemLocation> result =
                itemLocationService.findById(99L);

        assertTrue(result.isEmpty());
        verify(itemLocationRepository).findById(99L);
    }

    @Test
    void shouldSaveItemLocation() {
        ItemLocation itemLocation = new ItemLocation();
        ItemLocation savedItemLocation = new ItemLocation();

        when(itemLocationRepository.save(itemLocation))
                .thenReturn(savedItemLocation);

        ItemLocation result =
                itemLocationService.save(itemLocation);

        assertSame(savedItemLocation, result);
        verify(itemLocationRepository).save(itemLocation);
    }

    @Test
    void shouldDeleteItemLocationById() {
        itemLocationService.deleteById(1L);

        verify(itemLocationRepository).deleteById(1L);
        verifyNoMoreInteractions(itemLocationRepository);
    }

    @Test
    void shouldFindSuitableLocationsForItem() {
        ItemLocation itemLocation = new ItemLocation();

        when(itemLocationRepository
                .findAllByItem_IdAndSuitableTrueAndLocation_ActiveTrue(2L))
                .thenReturn(List.of(itemLocation));

        List<ItemLocation> result =
                itemLocationService.findSuitableLocationsForItem(2L);

        assertEquals(1, result.size());
        assertSame(itemLocation, result.get(0));

        verify(itemLocationRepository)
                .findAllByItem_IdAndSuitableTrueAndLocation_ActiveTrue(2L);
    }

    @Test
    void shouldReturnEmptyListWhenNoSuitableLocationsExist() {
        when(itemLocationRepository
                .findAllByItem_IdAndSuitableTrueAndLocation_ActiveTrue(2L))
                .thenReturn(List.of());

        List<ItemLocation> result =
                itemLocationService.findSuitableLocationsForItem(2L);

        assertTrue(result.isEmpty());

        verify(itemLocationRepository)
                .findAllByItem_IdAndSuitableTrueAndLocation_ActiveTrue(2L);
    }

    @Test
    void shouldReturnMultipleSuitableLocationsForItem() {
        ItemLocation firstLocation = new ItemLocation();
        ItemLocation secondLocation = new ItemLocation();

        when(itemLocationRepository
                .findAllByItem_IdAndSuitableTrueAndLocation_ActiveTrue(2L))
                .thenReturn(List.of(firstLocation, secondLocation));

        List<ItemLocation> result =
                itemLocationService.findSuitableLocationsForItem(2L);

        assertEquals(2, result.size());
        assertSame(firstLocation, result.get(0));
        assertSame(secondLocation, result.get(1));

        verify(itemLocationRepository)
                .findAllByItem_IdAndSuitableTrueAndLocation_ActiveTrue(2L);
    }
}