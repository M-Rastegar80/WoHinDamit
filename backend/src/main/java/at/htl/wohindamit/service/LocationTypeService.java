
package at.htl.wohindamit.service;

import at.htl.wohindamit.entity.LocationType;
import at.htl.wohindamit.repository.LocationTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationTypeService {

    private final LocationTypeRepository locationTypeRepository;

    public LocationTypeService(
            LocationTypeRepository locationTypeRepository) {
        this.locationTypeRepository = locationTypeRepository;
    }

    public List<LocationType> findAllActive() {
        return locationTypeRepository.findAllByActiveTrue();
    }

    public Optional<LocationType> findById(Long id) {
        return locationTypeRepository.findById(id)
                .filter(type ->
                        Boolean.TRUE.equals(type.getActive()));
    }
}
