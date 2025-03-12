package edu.rutmiit.demo.dto.city.forms;

import jakarta.validation.constraints.NotBlank;

public record CityEditForm(
        @NotBlank(message = "Идентификатор обязателен")
        String id,
        @NotBlank(message = "Название города обязательно")
        String nameCity,
        @NotBlank(message = "Описание города обязательно")
        String description
) {
}
