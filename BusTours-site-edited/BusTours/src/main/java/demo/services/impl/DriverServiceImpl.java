package demo.services.impl;

import demo.exception.PasNotFoundException;
import demo.models.Tour;
import demo.repos.DriverRepository;
import demo.repos.TourRepository;
import demo.repos.TripRepository;
import demo.services.DriverService;
import edu.rutmiit.demo.dto.driver.DriverStatus;
import demo.dto.DriverDTO;
import demo.models.Driver;
import demo.models.Trip;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableCaching
public class DriverServiceImpl implements DriverService {
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private TripRepository tripRepository;


    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public DriverDTO createDriver(String name,
                                  String surname,
                                  String patronymic,
                                  LocalDate birthday,
                                  String passport,
                                  String driverLicense,
                                  LocalDate dateStartDriverLicense,
                                  String category) {

        Driver driver =  driverRepository.create(new Driver(name, surname, patronymic, birthday, passport, driverLicense, dateStartDriverLicense, category, DriverStatus.WORK));
        return new DriverDTO(driver.getId(), driver.getName(), driver.getSurname(), driver.getPatronymic(), driver.getBirthday(), driver.getPassport(), driver.getDriverLicense(), driver.getDateStartDriverLicense(), driver.getCategory(), driver.getDriverStatus());
    }


    public  void  updateDriver(String id,
                               String name,
                               String surname,
                               String patronymic,
                               LocalDate birthday,
                               String passport,
                               String driverLicense,
                               LocalDate dateStartDriverLicense,
                               String category){
        var driver = driverRepository.findById(Driver.class, id);

        driver.setName(name);
        driver.setSurname(surname);
        driver.setPatronymic(patronymic);
        driver.setBirthday(birthday);
        driver.setPassport(passport);
        driver.setDriverLicense(driverLicense);
        driver.setDateStartDriverLicense(dateStartDriverLicense);
        driver.setCategory(category);
        driverRepository.create(driver);
    }

    @Override
    public void deleteDriver(String id) {
        Driver driver = driverRepository.findById(Driver.class, id);
        driver.setDriverStatus(DriverStatus.DISMISSED);
        driverRepository.create(driver);

    }




    @Override
    public DriverDTO getDriverById(String id) {
        Driver d = driverRepository.findById(Driver.class, id);
        return new DriverDTO(d.getId(), d.getName(), d.getSurname(), d.getPatronymic(), d.getBirthday(), d.getPassport(), d.getDriverLicense(), d.getDateStartDriverLicense(), d.getCategory(), d.getDriverStatus());
    }

    @Override
    @Transactional
    public List<DriverDTO> findAll() {
        List<Driver> drivers = driverRepository.getAll(Driver.class).stream()
                .filter(driver -> driver.getDriverStatus().equals(DriverStatus.WORK)).toList();
        return drivers.stream().map(d ->
                new DriverDTO(d.getId(), d.getName(), d.getSurname(), d.getPatronymic(), d.getBirthday(), d.getPassport(), d.getDriverLicense(), d.getDateStartDriverLicense(), d.getCategory(), d.getDriverStatus())
                ).toList();
    }



    @Override
    @Transactional
    public List<DriverDTO> findFreeDriversByTrip(
            LocalDate start,
            LocalDate end,
            String tourId
    ) {
        List<Trip> tripList = tripRepository.getAll(Trip.class);

        Tour tour = tourRepository.findById(Tour.class, tourId);

        List<Driver> findDrivers = new ArrayList<>();

        List<Driver> drivers =  driverRepository.getAll(Driver.class);



        for (Trip t : tripList){
            if (t.getEndDate().isAfter(start) || t.getEndDate().isEqual(start)){
                if (t.getStartDate().isBefore(end) || t.getStartDate().isEqual(start) ){
                    if( t.getDriver() != null) {

                        drivers.remove(driverRepository.findDriverByTrip(t.getId()));
                    }
                }
            }
        }


        //если продолжительность маршрута больше 10 дней стаж видителя должен быть больше 10 лет
        if(tour.getLengthTour()>10) {
            for (Driver driver : drivers) {
                int c = LocalDate.now().getYear() - driver.getDateStartDriverLicense().getYear();
                if (c > 10) {
                    findDrivers.add(driver);
                }
            }

        }else {
            findDrivers = drivers;
        }


        return findDrivers.stream().map(d ->
                new DriverDTO(d.getId(), d.getName(), d.getSurname(), d.getPatronymic(), d.getBirthday(), d.getPassport(), d.getDriverLicense(), d.getDateStartDriverLicense(), d.getCategory(), d.getDriverStatus())
        ).toList();
    }

    @Override
    @Cacheable(value = "drivers")
    public Page<DriverDTO> getDrivers(int page, int size) {
        List<Driver> listQuery1 = driverRepository.getAll(Driver.class);
        List<Driver> listQuery = new ArrayList<>();
        for (Driver driver : listQuery1) {
            if (driver.getDriverStatus() != DriverStatus.DISMISSED) {
                listQuery.add(driver);
            }
        }

        Pageable pageable = PageRequest.of(page - 1, size);

        int totalElements = listQuery.size();
        int start = (int) Math.min(pageable.getOffset(), totalElements);
        int end = Math.min(start + pageable.getPageSize(), totalElements);
        List<Driver> driversOnPage = listQuery.subList(start, end);

        Page<Driver> page1 = new PageImpl<>(driversOnPage, pageable, totalElements);

        return page1.map(d -> new DriverDTO(d.getId(), d.getName(), d.getSurname(), d.getPatronymic(), d.getBirthday(), d.getPassport(), d.getDriverLicense(), d.getDateStartDriverLicense(), d.getCategory(), d.getDriverStatus()));
    }


}
