package at.htl.wohindamit.repository;

import at.htl.wohindamit.entity.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationTypeRepository
        extends JpaRepository<LocationType, Long> {
}