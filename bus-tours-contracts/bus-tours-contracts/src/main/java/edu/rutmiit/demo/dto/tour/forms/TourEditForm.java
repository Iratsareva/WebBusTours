package edu.rutmiit.demo.dto.tour.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TourEditForm(
        @NotBlank(message = "Идентификатор обязателен")
        String id,
        @NotBlank(message = "Название тура обязательно")
        String nameTour,

        @NotBlank(message = "Описание тура обязательно")
        String description,

        @NotBlank(message = "Маршрут тура обязательно")
        String itinerary,

        @NotNull(message = "Продолжительность тура обязательно")
        Integer lengthTour,

        @NotNull(message = "Цена тура обязательно")
        Integer price,

        @NotBlank(message = "Место назначения обязательно")
        String destination
) {
}
