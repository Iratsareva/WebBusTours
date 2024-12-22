package demo.services.impl;

import demo.dto.TripDTO;
import demo.exception.PasNotFoundException;
import demo.models.*;
import demo.repos.DriverRepository;
import demo.repos.TourRepository;
import demo.repos.TripRepository;
import demo.services.TripService;
import edu.rutmiit.demo.dto.trip.TripStatus;
import demo.dto.TourTripDTO;
import demo.repos.BusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service

public class TripServiceImpl implements TripService {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private BusRepository busRepository;

    @Autowired
    private DriverRepository driverRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public TripDTO createTrip(String tourId,
                           String busId,
                           String driverId,
                           LocalDate startDate,
                           LocalDate endDate,
                           TripStatus tripStatus) {



        if (tourId== null) {
            throw new PasNotFoundException("Такого тура не существует");
        }


        if (startDate.isAfter(LocalDate.now())) {
            tripStatus = (TripStatus.AWAITING);
        } else if (endDate.isBefore(LocalDate.now())) {
            tripStatus = (TripStatus.COMPLETED);
        }else tripStatus = (TripStatus.ON_WAY);

        Trip trip = tripRepository.create(new Trip(
                tourRepository.findTourByTrip(tourId),
                busRepository.findById(Bus.class,busId ),
                driverRepository.findById(Driver.class, driverId),
                startDate,
                endDate,
                tripStatus));
        return new TripDTO(trip.getId(), trip.getTour().getId(), trip.getBus().getId(), trip.getDriver().getId(),trip.getStartDate(), trip.getEndDate(), trip.getTripStatus());
    }

    @Override
    public void updateTrip(String id,
                           String tourId,
                           String busId,
                           String driverId,
                           LocalDate startDate,
                           LocalDate endDate,
                           TripStatus tripStatus){
        var trip = tripRepository.findById(Trip.class, id);

        trip.setTour(tourRepository.findById(Tour.class, tourId));
        trip.setBus(busRepository.findById(Bus.class, busId));
        trip.setDriver(driverRepository.findById(Driver.class, driverId));
        trip.setStartDate(startDate);
        trip.setEndDate(endDate);
        trip.setTripStatus(tripStatus);

        tripRepository.create(trip);
    }


    @Override
    public TripDTO getTripById(String id) {
        Trip t = tripRepository.findById(Trip.class, id);
        return new TripDTO(t.getId(), t.getTour().getId(), t.getBus().getId(), t.getDriver().getId(), t.getStartDate(), t.getEndDate(), t.getTripStatus());
    }



    @Override
    @Transactional
    public List<TripDTO> findAll() {

        for (Trip trip : tripRepository.getAll(Trip.class)) {
            if (trip.getStartDate().isAfter(LocalDate.now())) {
                trip.setTripStatus(TripStatus.AWAITING);
            } else if (trip.getEndDate().isBefore(LocalDate.now())) {
                trip.setTripStatus(TripStatus.COMPLETED);
            } else trip.setTripStatus(TripStatus.ON_WAY);
        }

        return tripRepository.getAll(Trip.class).stream().map(t ->
                new TripDTO(t.getId(), t.getTour().getId(), t.getBus().getId(), t.getDriver().getId(), t.getStartDate(), t.getEndDate(), t.getTripStatus())
        ).toList();
    }




    @Override
    @Transactional
    public Page<TourTripDTO> findTourByDateTrip (){

        List<Trip> tripList = tripRepository.getAll(Trip.class);
        List<TourTripDTO> result = new ArrayList<>();

        tripList.sort(Comparator.comparing(Trip::getStartDate));

        for(Trip trip :tripList){
            Tour t = (tourRepository.findTourByTrip(trip.getId()));

            TourTripDTO tourTripDTO = new TourTripDTO(t.getId(), t.getNameTour(), t.getDestination().getNameCity(), t.getItinerary(), t.getPrice(), trip.getStartDate(), trip.getEndDate());

            result.add(tourTripDTO);

        }
        List<TourTripDTO> listQuery = result.subList(0, 5);
        Pageable pageable = PageRequest.of(0, 5);
        int totalElements = listQuery.size();
        Page<TourTripDTO> page1 = new PageImpl<>(listQuery, pageable, totalElements);
        return page1;
    }

    @Override
    public Page<TripDTO> getTrips(int page, int size) {

        List<Trip> listQuery = tripRepository.getAll(Trip.class);


        for (Trip trip : tripRepository.getAll(Trip.class)) {
            if (trip.getStartDate().isAfter(LocalDate.now())) {
                trip.setTripStatus(TripStatus.AWAITING);
            } else if (trip.getEndDate().isBefore(LocalDate.now())) {
                trip.setTripStatus(TripStatus.COMPLETED);
            } else trip.setTripStatus(TripStatus.ON_WAY);
        }


        Collections.sort(listQuery, new Comparator<Trip>() {
            @Override
            public int compare(Trip t1, Trip t2) {
                return t2.getTripStatus().compareTo(t1.getTripStatus());
            }
        });

        Pageable pageable = PageRequest.of(page - 1, size);

        int totalElements = listQuery.size();
        int start = (int) Math.min(pageable.getOffset(), totalElements);
        int end = Math.min(start + pageable.getPageSize(), totalElements);
        List<Trip> tripOnPage = listQuery.subList(start, end);

        Page<Trip> page1 = new PageImpl<>(tripOnPage, pageable, totalElements);

        return page1.map(t ->
                new TripDTO(t.getId(), t.getTour().getId(), t.getBus().getId(), t.getDriver().getId(), t.getStartDate(), t.getEndDate(), t.getTripStatus())
        );

    }

    @Override
    public List<TripDTO> findTripByTour(String id) {
        List<Trip> trips = tripRepository.findTripByTour(id).stream()
                //проверка, что билет можно купить за день до отправления
                .filter(trip -> trip.getStartDate().minusDays(1).isAfter(LocalDate.now()))
                //проверка, что в автобусе еще есть места
                .filter(trip -> trip.getTickets().size()<trip.getBus().getNumberSeats()).toList();

        return trips.stream().map(t ->
                new TripDTO(t.getId(), t.getTour().getId(), t.getBus().getId(), t.getDriver().getId(), t.getStartDate(), t.getEndDate(), t.getTripStatus())
        ).toList();
    }

    @Override
    public String findTripByTourString(String id){
        List<TripDTO> trips = findTripByTour(id).stream()
                .filter(trip -> trip.startDate().isAfter(LocalDate.now())).toList();
        StringBuilder strBuilder = new StringBuilder();


     trips.forEach(trip -> strBuilder.append(trip.startDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
             .append(" - ").append(trip.endDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
             .append("; "));



     if (strBuilder.length() == 0){
         return "Скоро появятся ...";
     }else {
         return strBuilder.toString();
     }
    }


}
