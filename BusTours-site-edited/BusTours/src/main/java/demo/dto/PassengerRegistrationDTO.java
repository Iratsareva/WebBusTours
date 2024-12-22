package demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PassengerRegistrationDTO {

    private String name;

    private String surname;

    private String patronymic;

    private String email;

    private String login;

    private String password;

    private String confirmPassword;


    public PassengerRegistrationDTO() {}


    @NotEmpty(message = "Name cannot be null or empty!")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = "Surname cannot be null or empty!")
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }


    public String getPatronymic() {
        return patronymic;
    }
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    @NotEmpty(message = "Email cannot be null or empty!")
    @Email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    @NotEmpty(message = "Login cannot be null or empty!")
    @Size(min = 5, max = 20)
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }


    @NotEmpty(message = "Password cannot be null or empty!")
    @Size(min = 5, max = 20)
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    @NotEmpty(message = "Confirm Password cannot be null or empty!")
    @Size(min = 5, max = 20)
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "PassengerRegistrationDTO{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
