
package at.htl.wohindamit.service;

import at.htl.wohindamit.entity.Location;
import at.htl.wohindamit.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> findAllActive() {
        return locationRepository.findAllByActiveTrue();
    }

    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id)
                .filter(location ->
                        Boolean.TRUE.equals(location.getActive()));
    }
}
