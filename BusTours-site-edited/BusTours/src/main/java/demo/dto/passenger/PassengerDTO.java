package demo.dto.passenger;

import java.io.Serializable;
import java.time.LocalDate;

public record PassengerDTO(
        String id,
        String name,
        String surname,
        String  patronymic,
        LocalDate birthday,
        String identificationDocument,
        String telephone,
        String email,
        String login,
        String password
) implements Serializable {
}
