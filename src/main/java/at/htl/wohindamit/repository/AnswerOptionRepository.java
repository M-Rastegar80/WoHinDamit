
package at.htl.wohindamit.repository;

import at.htl.wohindamit.entity.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerOptionRepository
        extends JpaRepository<AnswerOption, Long> {

    List<AnswerOption> findAllByQuestion_IdOrderByOrderIndexAsc(
            Long questionId
    );
}
