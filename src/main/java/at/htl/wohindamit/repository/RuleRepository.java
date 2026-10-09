package at.htl.wohindamit.repository;

import at.htl.wohindamit.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
}