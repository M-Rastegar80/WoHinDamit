package at.htl.wohindamit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "locations",
        indexes = {
                @Index(name = "idx_location_latitude", columnList = "latitude"),
                @Index(name = "idx_location_longitude", columnList = "longitude")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "location_type_id", nullable = false)
    private LocationType locationType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String city;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    private String phone;

    private String email;

    private String website;

    @Column(columnDefinition = "TEXT")
    private String openingHours;

    @Column(nullable = false)
    private String source;

    private String sourceId;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "location")
    @Builder.Default
    private List<ItemLocation> itemLocations = new ArrayList<>();
}