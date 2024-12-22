package demo;

import demo.models.Passenger;
import demo.repos.BusRepository;
import demo.repos.PassengerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {BusRepository.class})
public class BusToursApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BusToursApplication.class);
        app.setDefaultProperties(Collections.singletonMap("spring.profiles.default", "postgres"));
        app.run(args);
    }

}
