package edu.rutmiit.demo.controllers.passenger;

import edu.rutmiit.demo.controllers.base.BaseController;
import edu.rutmiit.demo.dto.bus.BusEditForm;
import edu.rutmiit.demo.dto.passenger.PassengerCreateForm;
import edu.rutmiit.demo.dto.passenger.PassengerEditForm;
import edu.rutmiit.demo.dto.passenger.PassengerShowForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/passenger")
public interface PassengerController extends BaseController {
    @GetMapping
    String listPassengers(Principal principal, @ModelAttribute("form") PassengerShowForm form,
                          Model model);



    @GetMapping("/{id}")
    String details(Principal principal,@PathVariable String id,
                   Model model
    );

}
