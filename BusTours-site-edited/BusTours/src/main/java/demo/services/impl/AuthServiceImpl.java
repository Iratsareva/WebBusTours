package demo.services.impl;

import demo.dto.passenger.PassengerRegistrationDTO;
import demo.exception.BadRequestException;
import demo.exception.ClientException;
import demo.models.Passenger;
import demo.models.UserRoles;
import demo.repositories.PassengerRepository;
import demo.repositories.UserRoleRepository;
import demo.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private PassengerRepository passengerRepository;
    private UserRoleRepository userRoleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(@Valid PassengerRegistrationDTO registrationDTO) throws ClientException {
        if (!registrationDTO.password().equals(registrationDTO.confirmPassword())) {
            throw new BadRequestException("Пароли не совпадают");
        }
        if(passengerRepository.findByEmail(registrationDTO.email())!=null){
            throw new ClientException("Пользователь с таким email уже существует");
        }

        var userRole = userRoleRepository.
                findRoleByName(UserRoles.USER).orElseThrow();

        Passenger passenger  = new Passenger(
                registrationDTO.name(),
                registrationDTO.surname(),
                registrationDTO.patronymic(),
                registrationDTO.email(),
                registrationDTO.login(),
                passwordEncoder.encode(registrationDTO.password()));
        passenger.setRoles(List.of(userRole));
        passengerRepository.create(passenger);
    }
    @Override
    public Passenger getPassenger(String login) {
        Passenger passenger = passengerRepository.findByLogin(login);
        if(passenger ==null){
            throw new UsernameNotFoundException(login + " не найден");
        }
        return passenger;
    }
    @Autowired
    public void setPassengerRepository(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }
    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
