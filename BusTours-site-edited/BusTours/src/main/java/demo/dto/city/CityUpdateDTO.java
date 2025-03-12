package demo.dto.city;

import java.io.Serializable;

public record CityUpdateDTO(
        String id,
        String nameCity,
        String description
) implements Serializable {
}
