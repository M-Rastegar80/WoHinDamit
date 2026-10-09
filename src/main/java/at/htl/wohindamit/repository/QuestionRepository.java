package at.htl.wohindamit.repository;

import at.htl.wohindamit.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}