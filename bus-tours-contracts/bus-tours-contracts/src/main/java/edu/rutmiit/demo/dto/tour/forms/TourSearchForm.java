package edu.rutmiit.demo.dto.tour.forms;

import jakarta.validation.constraints.Min;
import java.time.LocalDate;

public record TourSearchForm(
        String searchDestination,
        LocalDate searchStartDate,

        Integer searchLengthTour,


        @Min(value = 0, message = "Страница должна быть больше 0")
        Integer page,
        @Min(value = 1, message = "Размер страницы должен быть больше 0")
        Integer size
) {
}
