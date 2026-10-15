
package at.htl.wohindamit.controller;

import at.htl.wohindamit.dto.LocationResponse;
import at.htl.wohindamit.entity.Location;
import at.htl.wohindamit.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public List<LocationResponse> findAllActive() {
        return locationService.findAllActive()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public LocationResponse findById(@PathVariable Long id) {
        Location location = locationService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Location not found"
                ));

        return toResponse(location);
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
