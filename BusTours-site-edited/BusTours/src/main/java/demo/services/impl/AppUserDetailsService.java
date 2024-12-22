package demo.services.impl;

import demo.models.Passenger;
import demo.repos.PassengerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class AppUserDetailsService implements UserDetailsService {

    private PassengerRepository passengerRepository;

    public AppUserDetailsService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<Passenger> passengers = passengerRepository.getAll(Passenger.class);
        Passenger passenger = null;
        for( int i =0; i<passengers.size(); i++){
            if(passengers.get(i).getLogin().equals(username)){
                passenger = passengers.get(i);
            }
        }


        if(passenger == null){
            throw new UsernameNotFoundException(username + " was not found!");
        }


        return new User(
                passenger.getLogin(),
                passenger.getPassword(),
                passenger.getRoles().stream()
                                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName().name()))
                                .collect(Collectors.toList())
                );
    }
}
