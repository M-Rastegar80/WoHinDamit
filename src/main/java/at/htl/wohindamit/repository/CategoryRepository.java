package at.htl.wohindamit.repository;

import at.htl.wohindamit.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}