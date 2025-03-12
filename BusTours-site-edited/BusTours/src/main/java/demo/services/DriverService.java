package demo.services;

import demo.dto.driver.DriverCreateDTO;
import demo.dto.driver.DriverDTO;
import demo.dto.driver.DriverUpdateDTO;
import demo.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface DriverService {
    DriverDTO createDriver(DriverCreateDTO driverCreateDTO);
    void deleteDriver(String id) throws NotFoundException;
    DriverDTO getDriverById(String id) throws NotFoundException;
    List<DriverDTO> findAll() throws NotFoundException;
    List<DriverDTO> findFreeDriversByTrip(LocalDate start, LocalDate end, String tourId) throws NotFoundException;
    void  updateDriver(DriverUpdateDTO driverUpdateDTO) throws NotFoundException;
    List<DriverDTO> getDrivers() throws NotFoundException;
}
