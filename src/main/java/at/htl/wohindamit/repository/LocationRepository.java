package at.htl.wohindamit.repository;

import at.htl.wohindamit.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}