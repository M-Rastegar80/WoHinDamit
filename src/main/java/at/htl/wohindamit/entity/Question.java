package at.htl.wohindamit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(nullable = false)
    private String answerType;

    @Column(nullable = false)
    private Integer orderIndex;

    @Column(nullable = false)
    private Boolean required = true;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "question")
    @Builder.Default
    private List<AnswerOption> answerOptions = new ArrayList<>();
}