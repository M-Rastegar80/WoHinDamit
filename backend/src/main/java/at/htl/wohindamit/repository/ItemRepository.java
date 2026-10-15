package at.htl.wohindamit.repository;

import at.htl.wohindamit.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}