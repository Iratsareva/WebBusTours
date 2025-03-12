package demo.dto.city;

import java.io.Serializable;

public record CityDTO(
        String id,
        String nameCity,
        String description
) implements Serializable {
}
