
package at.htl.wohindamit.repository;

import at.htl.wohindamit.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByCategory_IdAndActiveTrueOrderByOrderIndexAsc(
            Long categoryId
    );
}
