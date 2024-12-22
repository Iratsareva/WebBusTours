package demo.controllers;

import edu.rutmiit.demo.controllers.passenger.PassengerController;
import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.bus.BusEditForm;
import edu.rutmiit.demo.dto.bus.BusEditViewModel;
import edu.rutmiit.demo.dto.passenger.*;
import jakarta.validation.Valid;
import demo.dto.PassengerDTO;
import demo.dto.TicketDTO;
import demo.services.PassengerService;
import demo.services.TicketService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/passenger")
public class PassengerControllerImpl implements PassengerController {
    private static final Logger LOG = LogManager.getLogger(Controller.class);


    @Autowired
    private PassengerService passengerService;
    @Autowired
    private TicketService ticketService;

    public PassengerControllerImpl(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }

    @GetMapping()
    public String listPassengers(Principal principal, PassengerShowForm form, Model model) {
        var page = form.page() != null ? form.page() : 1;
        var size = form.size() != null ? form.size() : 3;

        var passengerPage = passengerService.getPassengers(page, size);
        var passengerViewModels = passengerPage.stream()
                .map(d -> new PassengerViewModel(d.id(), d.name(), d.surname(), d.patronymic(), d.birthday(),d.identificationDocument(), d.telephone(), d.email(), d.login(), d.password())
                ).toList();


        var viewModel = new PassengerListViewModel(
                createBaseViewModel("Список пассажиров"),
                passengerViewModels,
                passengerPage.getTotalPages()
        );
        LOG.log(Level.INFO, "Show all passengers for "+ principal.getName());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);

        return "passenger-list";
    }



    @Override
    @GetMapping("/{id}")
    public String details(Principal principal,String id, Model model) {
        var passenger = passengerService.getPassengerById(id);

        var viewModel = new PassengerShowViewModel(
                createBaseViewModel("Пассажир"),
                new PassengerViewModel(passenger.id(), passenger.surname(), passenger.name(), passenger.patronymic(), passenger.birthday(), passenger.identificationDocument(), passenger.telephone(), passenger.email(), passenger.login(), passenger.password())
        );
        LOG.log(Level.INFO, "Show passenger "+id+" for "+ principal.getName());
        model.addAttribute("model", viewModel);
        return "passenger-details";
    }



}