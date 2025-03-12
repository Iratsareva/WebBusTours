package demo.services.impl;

import demo.dto.driver.DriverCreateDTO;
import demo.dto.driver.DriverUpdateDTO;
import demo.exception.NotFoundException;
import demo.models.Tour;
import demo.repositories.DriverRepository;
import demo.repositories.TourRepository;
import demo.repositories.TripRepository;
import demo.services.DriverService;
import edu.rutmiit.demo.dto.driver.DriverStatus;
import demo.dto.driver.DriverDTO;
import demo.models.Driver;
import demo.models.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableCaching
public class DriverServiceImpl implements DriverService {
    private DriverRepository driverRepository;
    private TourRepository tourRepository;
    private TripRepository tripRepository;

    @Override
    public DriverDTO createDriver(DriverCreateDTO driverCreateDTO) {
        Driver driver =  driverRepository.create(new Driver(driverCreateDTO.name(), driverCreateDTO.surname(), driverCreateDTO.patronymic(), driverCreateDTO.birthday(), driverCreateDTO.passport(), driverCreateDTO.driverLicense(), driverCreateDTO.dateStartDriverLicense(), driverCreateDTO.category(), DriverStatus.WORK));
        return new DriverDTO(driver.getId(), driver.getName(), driver.getSurname(), driver.getPatronymic(), driver.getBirthday(), driver.getPassport(), driver.getDriverLicense(), driver.getDateStartDriverLicense(), driver.getCategory(), driver.getDriverStatus());
    }

    public  void  updateDriver(DriverUpdateDTO driverUpdateDTO)throws NotFoundException {
        var driver = driverRepository.findById(driverUpdateDTO.id());
        if (driver == null) {
            throw new NotFoundException("Водитель с ID " + driverUpdateDTO.id() + " не найден");
        }
        driver.setName(driverUpdateDTO.name());
        driver.setSurname(driverUpdateDTO.surname());
        driver.setPatronymic(driverUpdateDTO.patronymic());
        driver.setBirthday(driverUpdateDTO.birthday());
        driver.setPassport(driverUpdateDTO.passport());
        driver.setDriverLicense(driverUpdateDTO.driverLicense());
        driver.setDateStartDriverLicense(driverUpdateDTO.dateStartDriverLicense());
        driver.setCategory(driverUpdateDTO.category());
        driverRepository.create(driver);
    }

    @Override
    public void deleteDriver(String id) throws NotFoundException {
        Driver driver = driverRepository.findById(id);
        if (driver == null) {
            throw new NotFoundException("Водитель с ID " + id + " не найден");
        }
        driver.setDriverStatus(DriverStatus.DISMISSED);
        driverRepository.create(driver);
    }

    @Override
    public DriverDTO getDriverById(String id) throws NotFoundException {
        Driver driver = driverRepository.findById(id);
        if (driver == null) {
            throw new NotFoundException("Водитель с ID " + id + " не найден");
        }
        return new DriverDTO(driver.getId(), driver.getName(), driver.getSurname(), driver.getPatronymic(), driver.getBirthday(), driver.getPassport(), driver.getDriverLicense(), driver.getDateStartDriverLicense(), driver.getCategory(), driver.getDriverStatus());
    }

    @Override
    public List<DriverDTO> findAll()throws NotFoundException {
        List<Driver> drivers = driverRepository.findAll();
        if(drivers == null || drivers.isEmpty()){
            throw new NotFoundException("Водители не найдены");
        }
        List<Driver> workDrivers = drivers.stream()
                .filter(driver -> driver.getDriverStatus().equals(DriverStatus.WORK)).toList();
        if(workDrivers.isEmpty()){
            throw new NotFoundException("Работающие водители не найдены");
        }
        return workDrivers.stream().map(driver ->
                new DriverDTO(driver.getId(), driver.getName(), driver.getSurname(), driver.getPatronymic(), driver.getBirthday(), driver.getPassport(), driver.getDriverLicense(), driver.getDateStartDriverLicense(), driver.getCategory(), driver.getDriverStatus())
                ).toList();
    }

    @Override
    public List<DriverDTO> findFreeDriversByTrip(LocalDate start, LocalDate end, String tourId)throws NotFoundException {
        List<Trip> tripList = tripRepository.findAll();
        if(tripList == null || tripList.isEmpty()){
            throw new NotFoundException("Рейсы не найдены");
        }
        Tour tour = tourRepository.findById(tourId);
        if (tour == null) {
            throw new NotFoundException("Тур с ID " + tourId + " не найден");
        }

        List<Driver> findDrivers = new ArrayList<>();
        List<Driver> drivers =  driverRepository.findAll();
        if(drivers == null || drivers.isEmpty()){
            throw new NotFoundException("Водители не найдены");
        }

        for (Trip trip : tripList){
            if (trip.getEndDate().isAfter(start) || trip.getEndDate().isEqual(start)){
                if (trip.getStartDate().isBefore(end) || trip.getStartDate().isEqual(start) ){
                    if( trip.getDriver() != null) {
                        drivers.remove(driverRepository.findDriverByTrip(trip.getId()));
                    }
                }
            }
        }

        //если продолжительность маршрута больше 10 дней стаж водителя должен быть больше 10 лет
        if(tour.getLengthTour()>10) {
            for (Driver driver : drivers) {
                int experience = LocalDate.now().getYear() - driver.getDateStartDriverLicense().getYear();
                if (experience > 10) {
                    findDrivers.add(driver);
                }
            }
        }else {
            findDrivers = drivers;
        }

        return findDrivers.stream().map(driver ->
                new DriverDTO(driver.getId(), driver.getName(), driver.getSurname(), driver.getPatronymic(), driver.getBirthday(), driver.getPassport(), driver.getDriverLicense(), driver.getDateStartDriverLicense(), driver.getCategory(), driver.getDriverStatus())
        ).toList();
    }


    @Override
    @Cacheable(value = "drivers")
    public List<DriverDTO> getDrivers() throws NotFoundException {
        List<Driver> drivers =  driverRepository.findAll();
        if(drivers == null || drivers.isEmpty()){
            throw new NotFoundException("Водители не найдены");
        }
        List<Driver> driversNotDismissed =drivers.stream().filter(
                driver -> driver.getDriverStatus() != DriverStatus.DISMISSED).toList();
        if(drivers.isEmpty()){
            throw new NotFoundException("Не уволенные водители не найдены");
        }
        return driversNotDismissed.stream().map(driver -> new DriverDTO(driver.getId(), driver.getName(), driver.getSurname(), driver.getPatronymic(), driver.getBirthday(), driver.getPassport(), driver.getDriverLicense(), driver.getDateStartDriverLicense(), driver.getCategory(), driver.getDriverStatus())).toList();
    }


    @Autowired
    public void setDriverRepository(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }
    @Autowired
    public void setTourRepository(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }
    @Autowired
    public void setTripRepository(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

}
