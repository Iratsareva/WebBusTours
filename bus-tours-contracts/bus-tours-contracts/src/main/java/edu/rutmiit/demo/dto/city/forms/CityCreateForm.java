package edu.rutmiit.demo.dto.city.forms;

import jakarta.validation.constraints.NotBlank;

public record CityCreateForm(
        @NotBlank(message = "Название города обязательно")
        String nameCity,

        @NotBlank(message = "Описание города обязательно")
        String description
) {
}
