package edu.rutmiit.demo.dto.tour;

import edu.rutmiit.demo.dto.trip.TripViewModel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TourRegTicketForm(

            String id,
            @NotBlank(message = "Фамилия обязателен")
            String surname,

            @NotBlank(message = "Имя обязательно")
            String name,
            String patronymic,

            @NotBlank(message = "Дата рождения обязательно")
            String birthday,

            @NotBlank(message = "Паспорт обязательно")
            String identificationDocument,

            @NotBlank(message = "Телефон обязательно")
            String telephone,

            @NotBlank(message = "Телефон обязательно")
            String email,
            @NotBlank(message = "Дата отправления рейса")
            String tripId


){}
