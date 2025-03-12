package demo.dto.passenger;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record PassengerRegistrationDTO(
        @NotEmpty(message = "Name cannot be null or empty!")
        String name,

        @NotEmpty(message = "Surname cannot be null or empty!")
        String surname,

        String patronymic,

        @NotEmpty(message = "Email cannot be null or empty!")
        @Email
        String email,

        @NotEmpty(message = "Login cannot be null or empty!")
        @Size(min = 5, max = 20)
        String login,

        @NotEmpty(message = "Password cannot be null or empty!")
        @Size(min = 5, max = 20)
        String password,

        @NotEmpty(message = "Confirm Password cannot be null or empty!")
        @Size(min = 5, max = 20)
        String confirmPassword

) {
}
