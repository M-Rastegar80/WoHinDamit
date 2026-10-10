
package at.htl.wohindamit.controller;

import at.htl.wohindamit.dto.LocationTypeResponse;
import at.htl.wohindamit.entity.LocationType;
import at.htl.wohindamit.service.LocationTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/location-types")
public class LocationTypeController {

    private final LocationTypeService locationTypeService;

    public LocationTypeController(
            LocationTypeService locationTypeService) {
        this.locationTypeService = locationTypeService;
    }

    @GetMapping
    public List<LocationTypeResponse> findAllActive() {
        return locationTypeService.findAllActive()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public LocationTypeResponse findById(@PathVariable Long id) {
        LocationType locationType = locationTypeService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Location type not found"
                ));

        return toResponse(locationType);
    }

    private LocationTypeResponse toResponse(LocationType locationType) {
        return new LocationTypeResponse(
                locationType.getId(),
                locationType.getName(),
                locationType.getDescription()
        );
    }
}
