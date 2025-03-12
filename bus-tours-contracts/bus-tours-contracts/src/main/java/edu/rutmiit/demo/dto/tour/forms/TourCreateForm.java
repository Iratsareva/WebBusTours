package edu.rutmiit.demo.dto.tour.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record TourCreateForm(
        @NotBlank(message = "Название тура обязательно")
        String nameTour,

        @NotBlank(message = "Описание тура обязательно")
        String description,

        @NotBlank(message = "Маршрут тура обязательно")
        String itinerary,

        @Min(value = 1, message = "Продолжительность должна быть больше 1")
        Integer lengthTour,

        @Min(value = 1000,message = "Цена должна быть больше 1000")
        Integer price,

        @NotBlank(message = "Место назначения обязательно")
        String destination
) {
}
