package at.htl.wohindamit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "location_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationType extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "locationType")
    @Builder.Default
    private List<Location> locations = new ArrayList<>();
}