package at.htl.wohindamit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @Builder.Default
    private List<Category> children = new ArrayList<>();

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "category")
    @Builder.Default
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    @Builder.Default
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    @Builder.Default
    private List<Rule> rules = new ArrayList<>();
}