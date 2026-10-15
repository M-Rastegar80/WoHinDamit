package at.htl.wohindamit.controller;

import at.htl.wohindamit.dto.LocationResponse;
import at.htl.wohindamit.entity.Item;
import at.htl.wohindamit.entity.ItemLocation;
import at.htl.wohindamit.entity.Location;
import at.htl.wohindamit.service.ItemLocationService;
import at.htl.wohindamit.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/items/{itemId}/locations")
public class ItemLocationController {

    private final ItemLocationService itemLocationService;
    private final ItemService itemService;

    public ItemLocationController(
            ItemLocationService itemLocationService,
            ItemService itemService
    ) {
        this.itemLocationService = itemLocationService;
        this.itemService = itemService;
    }

    @GetMapping
    public List<LocationResponse> findSuitableLocations(
            @PathVariable Long itemId
    ) {
        Item item = itemService.findById(itemId)
                .filter(foundItem ->
                        Boolean.TRUE.equals(foundItem.getActive()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Active item not found"
                ));

        return itemLocationService.findSuitableLocationsForItem(item.getId())
                .stream()
                .map(ItemLocation::getLocation)
                .map(this::toResponse)
                .toList();
    }

    private LocationResponse toResponse(Location location) {
        return new LocationResponse(
                location.getId(),
                location.getName(),
                location.getLocationType().getName(),
                location.getAddress(),
                location.getPostalCode(),
                location.getCity(),
                location.getLatitude(),
                location.getLongitude(),
                location.getPhone(),
                location.getEmail(),
                location.getWebsite(),
                location.getOpeningHours(),
                location.getLastUpdated()
        );
    }
}