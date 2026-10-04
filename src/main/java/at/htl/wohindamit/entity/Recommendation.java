package at.htl.wohindamit.entity;

import at.htl.wohindamit.enums.Action;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommendation extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer priority;

    @Column(nullable = false)
    private Boolean active = true;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Action action;

    @OneToMany(mappedBy = "recommendation")
    @Builder.Default
    private List<Rule> rules = new ArrayList<>();
}