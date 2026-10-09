package at.htl.wohindamit.repository;

import at.htl.wohindamit.entity.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerOptionRepository
        extends JpaRepository<AnswerOption, Long> {
}