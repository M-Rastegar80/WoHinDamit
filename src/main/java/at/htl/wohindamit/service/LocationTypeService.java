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

    public List<LocationType> findAll() {
        return locationTypeRepository.findAll();
    }

    public Optional<LocationType> findById(Long id) {
        return locationTypeRepository.findById(id);
    }

    public LocationType save(LocationType locationType) {
        return locationTypeRepository.save(locationType);
    }

    public void deleteById(Long id) {
        locationTypeRepository.deleteById(id);
    }
}