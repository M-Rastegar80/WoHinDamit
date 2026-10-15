package at.htl.wohindamit.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemLocation extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private Boolean suitable = true;

    @Column(columnDefinition = "TEXT")
    private String note;
}