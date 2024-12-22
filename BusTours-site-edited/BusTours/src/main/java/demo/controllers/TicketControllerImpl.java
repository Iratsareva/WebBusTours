package demo.controllers;

import demo.dto.PassengerDTO;
import demo.dto.TicketDTO;
import demo.dto.TourDTO;
import demo.dto.TripDTO;
import demo.models.Role;
import demo.models.UserRoles;
import demo.services.*;
import edu.rutmiit.demo.controllers.ticket.TicketController;
import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.bus.BusTicketViewModel;
import edu.rutmiit.demo.dto.driver.DriverTicketViewModel;
import edu.rutmiit.demo.dto.passenger.PassengerTicketViewModel;
import edu.rutmiit.demo.dto.ticket.*;
import edu.rutmiit.demo.dto.tour.TourViewModel;
import edu.rutmiit.demo.dto.tour.TourViewModelShort;
import edu.rutmiit.demo.dto.trip.TripViewModel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/ticket")
public class TicketControllerImpl  implements TicketController {
    private static final Logger LOG = LogManager.getLogger(Controller.class);


    @Autowired
    private TicketService ticketService;

    @Autowired
    private PassengerService passengerService;
    @Autowired
    private TourService tourService;

    @Autowired
    private TripService tripService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private BusService busService;

    @Autowired
    private AuthService authService;



    public TicketControllerImpl(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }

    @GetMapping()
    public String listTickets(Principal principal,
                              TicketShowForm form,
                              Model model) {
        var page = form.page() != null ? form.page() : 1;
        var size = form.size() != null ? form.size() : 3;

        String username = principal.getName();
        PassengerDTO passenger = passengerService.getPassengerByLogin(username);

        Page<TicketDTO> ticketPage;
        if (passengerService.isAdminPassengerByLogin(username) || passengerService.isModerPassengerByLogin(username)) {
            ticketPage = ticketService.getTickets(page, size);
        } else {
            ticketPage = ticketService.getTicketsUser(page, size, passenger.id());
        }

        var ticketViewModels = ticketPage.stream()
                .map(d -> new TicketDetailListViewModel(d.id(), d.price(), d.ticketStatus(),
                        passengerService.getPassengerById(d.passengerId()).name(),
                        passengerService.getPassengerById(d.passengerId()).surname(),
                        passengerService.getPassengerById(d.passengerId()).patronymic(),
                        d.tripId(),
                        tripService.getTripById(d.tripId()).startDate(),
                        tripService.getTripById(d.tripId()).endDate(),
                        tourService.getTourById(tripService.getTripById(d.tripId()).tourId()).nameTour())
                ).toList();

        var viewModel = new TicketListViewModel(
                createBaseViewModel("Список билетов"),
                ticketViewModels,
                ticketPage.getTotalPages()
        );

        LOG.log(Level.INFO, "Show all tickets for "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);

        if (passengerService.isAdminPassengerByLogin(username)|| passengerService.isModerPassengerByLogin(username)) {
            return "ticket-list-admin";
        } else {
            return "ticket-list-user";
        }

    }

    @Override
    @GetMapping("/{id}")
    public String details(Principal principal,String id, Model model) {
        var ticket = ticketService.getTicketById(id);
        var passenger = passengerService.getPassengerById(ticket.passengerId());
        var trip = tripService.getTripById(ticket.tripId());
        var driver = driverService.getDriverById(trip.driverId());
        var bus = busService.getBus(trip.driverId());
        var tour = tourService.getTourById(trip.tourId());

        var viewModel = new TicketDetailsViewModel(
                createBaseViewModel("Детали билета"),
                new TicketViewModel(ticket.id(), ticket.passengerId(), ticket.tripId(), ticket.price(), ticket.ticketStatus()),
                new PassengerTicketViewModel(passenger.id(), passenger.name(), passenger.surname(), passenger.patronymic()),
                new TripViewModel(trip.id(), trip.tourId(), trip.busId(), trip.driverId(), trip.startDate(), trip.endDate(), trip.tripStatus()),
                new DriverTicketViewModel(driver.id(), driver.name(), driver.surname(), driver.patronymic()),
                new BusTicketViewModel(bus.id(), bus.mark(), bus.numberBus()),
                new TourViewModelShort(tour.id(), tour.nameTour(), tour.itinerary(), tour.lengthTour(), tour.price(), null)
        );
        LOG.log(Level.INFO, "Show ticket "+id+" for "+ principal.getName());
        model.addAttribute("model", viewModel);
        return "ticket-details";
    }



    @Override
    @GetMapping("/create/{id}")
    public String createForm(Principal principal,@PathVariable String id, Model model) {
        TourDTO tour = tourService.getTourById(id);
        PassengerDTO passenger = passengerService.getPassengerByLogin(principal.getName());
        List<TripViewModel> trips = tripService.findTripByTour(tour.id()).stream()
                .map(t -> new TripViewModel(t.id(), t.tourId(), t.busId(), t.driverId(), t.startDate(), t.endDate(), t.tripStatus())).toList();


        var viewModel = new TicketCreateViewModel(
                createBaseViewModel("Создание билета"),
                new TourViewModel(tour.id(), tour.nameTour(), tour.description(), tour.itinerary(), tour.lengthTour(), tour.price(), tour.destination()),
                trips
        );


        LOG.log(Level.INFO, "Show create form ticket for "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new TicketCreateForm(
                "", passenger.id(),"",tour.price(), passenger.surname(), passenger.name(), passenger.patronymic(),passenger.birthday() != null ?passenger.birthday().toString():"", passenger.identificationDocument(), passenger.telephone(), passenger.email()));
        return "ticket-create";
    }





    @Override
    @PostMapping("/create/{id}")
    public String create(Principal principal,
            @PathVariable String id,
            TicketCreateForm form, BindingResult bindingResult, Model model) {
        TourDTO tour = tourService.getTourById(id);
        PassengerDTO passenger = passengerService.getPassengerByLogin(principal.getName());
        List<TripViewModel> trips = tripService.findTripByTour(tour.id()).stream()
                .map(t -> new TripViewModel(t.id(), t.tourId(), t.busId(), t.driverId(), t.startDate(), t.endDate(), t.tripStatus())).toList();



        if (bindingResult.hasErrors()) {
            var viewModel = new TicketCreateViewModel(
                    createBaseViewModel("Создание билета"),
                    new TourViewModel(tour.id(), tour.nameTour(), tour.description(), tour.itinerary(), tour.lengthTour(), tour.price(), tour.destination()),
                    trips
            );

            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "ticket-create";
        }

        TicketDTO ticket = ticketService.createTicket(passenger.id(), form.tripId());
        LOG.log(Level.INFO, " Create ticket for "+ principal.getName());
        if (ticket == null) {
            model.addAttribute("errorMessage", "Вы уже оформили билет на данный рейс");
            var viewModel = new TicketCreateViewModel(
                    createBaseViewModel("Создание билета"),
                    new TourViewModel(tour.id(), tour.nameTour(), tour.description(), tour.itinerary(), tour.lengthTour(), tour.price(), tour.destination()),
                    trips
                    );

            LOG.log(Level.INFO, "post Create ticket for "+ principal.getName());
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "ticket-create";
        }else {
            passengerService.updatePassenger(form.name(), form.surname(), form.patronymic(), LocalDate.parse(form.birthday()), form.identificationDocument(), form.telephone(), principal.getName());

            return "redirect:/ticket/" + ticket.id();
        }
    }

    @Override
    @GetMapping("/{id}/delete")
    public String delete(Principal principal,String id) {
        ticketService.deleteTicket(id);
        LOG.log(Level.INFO, "Delete ticket for "+ principal.getName());
        return "redirect:/ticket";
    }

    @Override
    @GetMapping("/{id}/confirm")
    public String confirm(Principal principal,String id){
        ticketService.confirmTicket(id);
        LOG.log(Level.INFO, "Confirm ticket for "+ principal.getName());
        return "redirect:/ticket";
    }
}
