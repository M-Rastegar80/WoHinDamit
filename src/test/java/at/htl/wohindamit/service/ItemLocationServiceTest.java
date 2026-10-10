package at.htl.wohindamit.service;

import at.htl.wohindamit.entity.ItemLocation;
import at.htl.wohindamit.repository.ItemLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
        verify(itemLocationRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoItemLocationsExist() {
        when(itemLocationRepository.findAll())
                .thenReturn(List.of());

        List<ItemLocation> result = itemLocationService.findAll();

        assertEquals(0, result.size());
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

        verify(itemLocationRepository)
                .findAllByItem_IdAndSuitableTrueAndLocation_ActiveTrue(2L);
    }
}