package demo.services;

import demo.dto.DriverDTO;
import demo.models.Driver;
import org.springframework.data.domain.Page;


import java.time.LocalDate;
import java.util.List;

public interface DriverService {
    DriverDTO createDriver(
    String name,
    String surname,
    String patronymic,
    LocalDate birthday,
    String passport,
    String driverLicense,
    LocalDate dateStartDriverLicense,
    String category
    );
    void deleteDriver(String id);
    DriverDTO getDriverById(String id);
    List<DriverDTO> findAll();

    public List<DriverDTO> findFreeDriversByTrip(LocalDate start, LocalDate end, String tourId);

    void  updateDriver(String id,
                               String name,
                               String surname,
                               String patronymic,
                               LocalDate birthday,
                               String passport,
                               String driverLicense,
                               LocalDate dateStartDriverLicense,
                               String category);

    Page<DriverDTO> getDrivers(int page, int size);

}
