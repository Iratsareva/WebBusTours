package demo.controllers;

import demo.dto.*;

import demo.services.*;
import edu.rutmiit.demo.controllers.trip.TripController;
import edu.rutmiit.demo.dto.base.BaseViewModel;

import edu.rutmiit.demo.dto.bus.BusStatus;
import edu.rutmiit.demo.dto.bus.BusViewModel;
import edu.rutmiit.demo.dto.driver.DriverViewModel;
import edu.rutmiit.demo.dto.passenger.PassengerListViewModel;
import edu.rutmiit.demo.dto.passenger.PassengerViewModel;
import edu.rutmiit.demo.dto.tour.TourShowForm;
import edu.rutmiit.demo.dto.tour.TourViewModel;
import edu.rutmiit.demo.dto.tourTrip.TourTripListViewModel;
import edu.rutmiit.demo.dto.tourTrip.TourTripViewModel;
import edu.rutmiit.demo.dto.trip.*;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequestMapping("/trip")
public class TripControllerImpl implements TripController {
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    private TripService tripService;
    @Autowired
    private TourService tourService;
    @Autowired
    private DriverService driverService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private BusService busService;

    public TripControllerImpl(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }

    @GetMapping()
    public String listTrips(Principal principal, TripShowForm form, Model model) {
        var page = form.page() != null ? form.page() : 1;
        var size = form.size() != null ? form.size() : 3;


        var tripPage = tripService.getTrips(page, size);
        DriverDTO driver =  null;
        BusDto bus = null;
        TourDTO tour = null;



        var tripViewModels = tripPage.stream()
        .map(d -> new TripViewModel(d.id(), d.tourId(), d.busId(), d.driverId(), d.startDate(), d.endDate(), d.tripStatus())
        ).toList();


       for(int i =0; i< tripViewModels.size(); i++){
           driver = driverService.getDriverById(tripViewModels.get(i).driverId());
           bus = busService.getBus(tripViewModels.get(i).busId());
           tour = tourService.getTourById(tripViewModels.get(i).tourId());
       }



        var viewModel = new TripListViewModel(
                createBaseViewModel("Список рейсов"),
                tripViewModels,
                new DriverViewModel(driver.id(), driver.name(), driver.surname(), driver.patronymic(), driver.birthday(), driver.passport(), driver.driverLicense(), driver.dateStartDriverLicense(), driver.category(), driver.driverStatus()),
                new BusViewModel(bus.id(), bus.mark(), bus.numberBus(), bus.numberSeats(), bus.classBus(), bus.year(), bus.busStatus()),
                new TourViewModel(tour.id(), tour.nameTour(), tour.description(), tour.itinerary(), tour.lengthTour(), tour.price(), tour.destination()),
                tripPage.getTotalPages()
        );

        LOG.log(Level.INFO, "Show all trips for "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);
        return "trip-list";
    }

    @Override
    @GetMapping("/{id}/passengers")
    public String tripPassengers(Principal principal,String id, TripShowForm form, Model model) {
        List<PassengerDTO> passengers = passengerService.getPassengersByTrip(id);

        var passengerViewModels = passengers.stream()
                .map(d -> new PassengerViewModel(d.id(), d.name(), d.surname(), d.patronymic(), d.birthday(),d.identificationDocument(), d.telephone(), d.email(), d.login(), d.password())
                ).toList();

        var viewModel = new PassengerListViewModel(
                createBaseViewModel("Список пассажиров"),
                passengerViewModels,
                1
        );
        LOG.log(Level.INFO, "Show passengers trips for "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);
        return "passenger-list";
    }

    @GetMapping("/{id}")
    public String details(Principal principal,String id, Model model) {
        var trip = tripService.getTripById(id);

        var viewModel = new TripDetailsViewModel(
                createBaseViewModel("Детали рейса"),
                new TripViewModel(trip.id(), trip.tourId(), trip.busId(), trip.driverId(), trip.startDate(), trip.endDate(), trip.tripStatus())
        );

        LOG.log(Level.INFO, "Show trip "+id+" for "+ principal.getName());
        model.addAttribute("model", viewModel);
        return "trip-details";
    }




    @Override
    @GetMapping("/create/{id}")
    public String createForm(Principal principal,@PathVariable String id,Model model) {
        TourDTO tour = tourService.getTourById(id);
        List<DriverViewModel> drivers = driverService.findAll().stream()
                .map(d -> new DriverViewModel(d.id(), d.name(), d.surname(), d.patronymic(), d.birthday(), d.passport(), d.driverLicense(), d.dateStartDriverLicense(), d.category(), d.driverStatus())).toList();
        List<BusViewModel> buses = busService.findAll().stream()
                .map(b -> new BusViewModel(b.id(), b.mark(), b.numberBus(), b.numberSeats(), b.classBus(), b.year(), b.busStatus())).toList();



        var viewModel = new TripCreateViewModel(
                createBaseViewModel("Создание рейса"),
                new TourViewModel(tour.id(), tour.nameTour(), tour.description(), tour.itinerary(), tour.lengthTour(), tour.price(), tour.destination()),
                drivers,
                buses
        );


        LOG.log(Level.INFO, "Show create form trip for "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new TripCreateForm(tour.id(), "", "", null, null, TripStatus.AWAITING.toString()));
        return "trip-create";
    }


    @Override
    @PostMapping("/create/{id}")
    public String create(Principal principal,
            @PathVariable String id,
            TripCreateForm1 form, BindingResult bindingResult, Model model) {
        TourDTO tour = tourService.getTourById(id);
        List<DriverViewModel> drivers = driverService.findAll().stream()
                .map(d -> new DriverViewModel(d.id(), d.name(), d.surname(), d.patronymic(), d.birthday(), d.passport(), d.driverLicense(), d.dateStartDriverLicense(), d.category(), d.driverStatus())).toList();
        List<BusViewModel> buses = busService.findAll().stream()
                .map(b -> new BusViewModel(b.id(), b.mark(), b.numberBus(), b.numberSeats(), b.classBus(), b.year(), b.busStatus())).toList();




        if (bindingResult.hasErrors()) {
            var viewModel = new TripCreateViewModel(
                    createBaseViewModel("Создание рейса"),
                    new TourViewModel(tour.id(), tour.nameTour(), tour.description(), tour.itinerary(), tour.lengthTour(), tour.price(), tour.destination()),
                    drivers,
                    buses
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "trip-create";
        }

        TripDTO trip = tripService.createTrip(form.tourId(), form.busId(), form.driverId(), form.startDate(), form.endDate(), form.tripStatus());
        LOG.log(Level.INFO, "Create trip for "+ principal.getName());
        return "redirect:/trip/"+trip.id();
    }

    @Override
    @GetMapping("/{id}/edit")
    public String editForm(Principal principal,String id, Model model) {
        var trip = tripService.getTripById(id);
        List<DriverViewModel> drivers = driverService.findFreeDriversByTrip(trip.startDate(), trip.endDate(), trip.tourId()).stream()
                .map(d -> new DriverViewModel(d.id(), d.name(), d.surname(), d.patronymic(), d.birthday(), d.passport(), d.driverLicense(), d.dateStartDriverLicense(), d.category(), d.driverStatus())).toList();
        List<BusViewModel> buses = busService.findAll().stream()
                .map(b -> new BusViewModel(b.id(), b.mark(), b.numberBus(), b.numberSeats(), b.classBus(), b.year(), b.busStatus())).toList();


        var viewModel = new TripEditViewModel(
                createBaseViewModel("Редактирование рейса"),
                drivers,
                buses);
        LOG.log(Level.INFO, "Show edit form trip for "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form",new TripEditForm(trip.id(), trip.tourId(), trip.busId(), trip.driverId(), trip.startDate().toString(), trip.endDate().toString(), trip.tripStatus()));
        return "trip-edit";
    }

    @Override
    @PostMapping("/{id}/edit")
    public String edit(Principal principal,@PathVariable String id,
                       @Valid @ModelAttribute("form") TripEditForm form,
                       BindingResult bindingResult, Model model) {

        List<DriverViewModel> drivers = driverService.findFreeDriversByTrip(LocalDate.parse(form.startDate()), LocalDate.parse(form.endDate()),id).stream()
                .map(d -> new DriverViewModel(d.id(), d.name(), d.surname(), d.patronymic(), d.birthday(), d.passport(), d.driverLicense(), d.dateStartDriverLicense(), d.category(), d.driverStatus())).toList();
        List<BusViewModel> buses = busService.findAll().stream()
                .map(b -> new BusViewModel(b.id(), b.mark(), b.numberBus(), b.numberSeats(), b.classBus(), b.year(), b.busStatus())).toList();

        if (bindingResult.hasErrors()) {
            var viewModel = new TripEditViewModel(
                    createBaseViewModel("Редактирование рейса"),
                    drivers,
                    buses
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "trip-edit";
        }

        tripService.updateTrip(id, form.tourId(), form.busId(), form.driverId(), LocalDate.parse(form.startDate()), LocalDate.parse(form.endDate()), form.tripStatus());
        LOG.log(Level.INFO, "Edit trip for "+ principal.getName());
        return "redirect:/trip";
    }




    @GetMapping("/soon")
    public String getSoonTours (Principal principal,TourShowForm form, Model model){
        var toursPage = tripService.findTourByDateTrip();

        var tourTripViewModels = toursPage.stream()
                .map(t -> new TourTripViewModel(t.id(), t.nameTour(),t.destination(), t.itinerary(), t.price(), t.startDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), t.endDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                ).toList();


        var viewModel = new TourTripListViewModel(
                createBaseViewModel("Список туров"),
                tourTripViewModels,
                toursPage.getTotalPages()
        );
        LOG.log(Level.INFO, "Show soon trip for "+ (principal != null ? principal.getName() : "anonymousUser"));
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);

        return "tour-soon-list";
    }
}
