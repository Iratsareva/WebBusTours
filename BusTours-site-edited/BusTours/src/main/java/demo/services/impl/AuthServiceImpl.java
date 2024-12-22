package demo.services.impl;

import demo.dto.PassengerRegistrationDTO;
import demo.models.Passenger;
import demo.models.UserRoles;
import demo.repos.PassengerRepository;
import demo.repos.UserRoleRepository;
import demo.services.AuthService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private PassengerRepository passengerRepository;

    private UserRoleRepository userRoleRepository;

    private PasswordEncoder passwordEncoder;


    public AuthServiceImpl(PassengerRepository passengerRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.passengerRepository = passengerRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }





    public String register(PassengerRegistrationDTO registrationDTO) {

        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            return "passwords.match";
        }

        if(passengerRepository.findByEmail(registrationDTO.getEmail())!=null){
            return "email.used";
        }


        var userRole = userRoleRepository.
                findRoleByName(UserRoles.USER).orElseThrow();

        Passenger passenger  = new Passenger(
                registrationDTO.getName(),
                registrationDTO.getSurname(),
                registrationDTO.getPatronymic(),
                registrationDTO.getEmail(),
                registrationDTO.getLogin(),
                passwordEncoder.encode(registrationDTO.getPassword()));
        passenger.setRoles(List.of(userRole));


        passengerRepository.create(passenger);
        return passenger.getId();
    }




    @Override
    public Passenger getPassenger(String login) {
        Passenger passenger = passengerRepository.findByLogin(login);
        if(passenger ==null){
            throw new UsernameNotFoundException(login + " was not found!");
        }
        return passenger;
    }
}
