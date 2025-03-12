package demo.services.impl;

import demo.models.Passenger;
import demo.repositories.PassengerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.stream.Collectors;

public class AppUserDetailsService implements UserDetailsService {
    private PassengerRepository passengerRepository;
    public AppUserDetailsService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Passenger passenger = passengerRepository.findByLogin(username);
        if(passenger == null){
            throw new UsernameNotFoundException(username + " was not found!");
        }

        return new User(
                passenger.getLogin(),
                passenger.getPassword(),
                passenger.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                                .collect(Collectors.toList())
                );
    }
}
