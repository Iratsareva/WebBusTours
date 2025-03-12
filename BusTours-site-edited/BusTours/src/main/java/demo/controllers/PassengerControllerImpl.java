package demo.controllers;

import demo.exception.NotFoundException;
import edu.rutmiit.demo.controllers.PassengerController;
import edu.rutmiit.demo.dto.passenger.*;
import demo.services.PassengerService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
@RequestMapping("/passenger")
public class PassengerControllerImpl extends BaseControllerIpl implements PassengerController {
    private static final Logger LOG = LogManager.getLogger(Controller.class);
    private PassengerService passengerService;



    @GetMapping()
    public String listPassengers(Principal principal, Model model)throws NotFoundException {
        var passengerPage = passengerService.getPassengers();
        var passengerViewModels = passengerPage.stream()
                .map(passengerDTO -> new PassengerViewModel(passengerDTO.id(), passengerDTO.name(), passengerDTO.surname(), passengerDTO.patronymic(), passengerDTO.birthday(), passengerDTO.identificationDocument(), passengerDTO.telephone(), passengerDTO.email(), passengerDTO.login(), passengerDTO.password())
                ).toList();

        var viewModel = new PassengerListViewModel(
                createBaseViewModel("Список пассажиров"),
                passengerViewModels
        );
        LOG.log(Level.INFO, "Show all passengers for "+ principal.getName());
        model.addAttribute("model", viewModel);
        return "passenger-list";
    }



    @Override
    @GetMapping("/{id}")
    public String details(Principal principal,String id, Model model)throws NotFoundException {
        var passenger = passengerService.getPassengerById(id);
        var viewModel = new PassengerShowViewModel(
                createBaseViewModel("Пассажир"),
                new PassengerViewModel(passenger.id(), passenger.surname(), passenger.name(), passenger.patronymic(), passenger.birthday(), passenger.identificationDocument(), passenger.telephone(), passenger.email(), passenger.login(), passenger.password())
        );
        LOG.log(Level.INFO, "Show passenger "+id+" for "+ principal.getName());
        model.addAttribute("model", viewModel);
        return "passenger-details";
    }


    @Autowired
    public void setPassengerService(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

}