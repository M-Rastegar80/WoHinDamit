
package at.htl.wohindamit.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LocationResponse(
        Long id,
        String name,
        String locationType,
        String address,
        String postalCode,
        String city,
        BigDecimal latitude,
        BigDecimal longitude,
        String phone,
        String email,
        String website,
        String openingHours,
        LocalDateTime lastUpdated
) {
}
