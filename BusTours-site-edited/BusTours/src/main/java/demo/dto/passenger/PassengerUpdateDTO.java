package demo.dto.passenger;

import java.io.Serializable;
import java.time.LocalDate;

public record PassengerUpdateDTO(
        String name,
        String surname,
        String  patronymic,
        LocalDate birthday,
        String identificationDocument,
        String telephone,
        String login
) implements Serializable {
}
