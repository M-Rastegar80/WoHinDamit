package at.htl.wohindamit.repository;

import at.htl.wohindamit.entity.ItemLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemLocationRepository
        extends JpaRepository<ItemLocation, Long> {

    List<ItemLocation> findAllByItem_IdAndSuitableTrueAndLocation_ActiveTrue(
            Long itemId
    );
}