package at.htl.wohindamit.repository;

import at.htl.wohindamit.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuleRepository extends JpaRepository<Rule, Long> {

    List<Rule> findAllByActiveTrue();
}