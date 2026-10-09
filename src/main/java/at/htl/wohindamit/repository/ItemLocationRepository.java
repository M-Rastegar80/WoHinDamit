package at.htl.wohindamit.repository;

import at.htl.wohindamit.entity.ItemLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemLocationRepository
        extends JpaRepository<ItemLocation, Long> {
}