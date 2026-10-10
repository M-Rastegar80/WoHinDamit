package at.htl.wohindamit.service;

import at.htl.wohindamit.entity.ItemLocation;
import at.htl.wohindamit.repository.ItemLocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemLocationService {

    private final ItemLocationRepository itemLocationRepository;

    public ItemLocationService(ItemLocationRepository itemLocationRepository) {
        this.itemLocationRepository = itemLocationRepository;
    }

    public List<ItemLocation> findAll() {
        return itemLocationRepository.findAll();
    }

    public Optional<ItemLocation> findById(Long id) {
        return itemLocationRepository.findById(id);
    }

    public ItemLocation save(ItemLocation itemLocation) {
        return itemLocationRepository.save(itemLocation);
    }

    public void deleteById(Long id) {
        itemLocationRepository.deleteById(id);
    }

    public List<ItemLocation> findSuitableLocationsForItem(Long itemId) {
        return itemLocationRepository
                .findAllByItem_IdAndSuitableTrueAndLocation_ActiveTrue(itemId);
    }
}