package edu.rutmiit.demo.controllers.trip;

import edu.rutmiit.demo.controllers.base.BaseController;
import edu.rutmiit.demo.dto.bus.BusCreateForm;
import edu.rutmiit.demo.dto.bus.BusEditForm;
import edu.rutmiit.demo.dto.ticket.TicketShowForm;
import edu.rutmiit.demo.dto.tour.TourShowForm;
import edu.rutmiit.demo.dto.trip.TripCreateForm;
import edu.rutmiit.demo.dto.trip.TripCreateForm1;
import edu.rutmiit.demo.dto.trip.TripEditForm;
import edu.rutmiit.demo.dto.trip.TripShowForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/trip")
public interface TripController extends BaseController {

    @GetMapping
    String listTrips(Principal principal, @ModelAttribute("form") TripShowForm form,
                     Model model);


    @GetMapping("/{id}/passengers")
    String tripPassengers(Principal principal,@PathVariable String id,
            @ModelAttribute("form") TripShowForm form,
                      Model model);

    @GetMapping("/{id}")
    String details(Principal principal,@PathVariable String id,
                   Model model
    );


    @GetMapping("/create/{id}")
    String createForm(Principal principal,@PathVariable String id,
                      Model model);

    @PostMapping("/create/{id}")
    String create(Principal principal,
            @PathVariable String id,
            @Valid @ModelAttribute("form") TripCreateForm1 form,
            BindingResult bindingResult,
            Model model
    );


    @GetMapping("/{id}/edit")
    String editForm(Principal principal,
            @PathVariable String id,
            Model model
    );


    @PostMapping("/{id}/edit")
    String edit(Principal principal,
            @PathVariable String id,
            @Valid @ModelAttribute("form") TripEditForm form,
            BindingResult bindingResult,
            Model model
    );

    @GetMapping("/soon")
    String getSoonTours (Principal principal, TourShowForm form, Model model);
}
