package demo.dto.city;

import java.io.Serializable;

public record CityCreateDTO(
        String nameCity,
        String description
) implements Serializable {
}
