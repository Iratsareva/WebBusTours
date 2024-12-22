package edu.rutmiit.demo.controllers.ticket;

import edu.rutmiit.demo.controllers.base.BaseController;
import edu.rutmiit.demo.dto.passenger.PassengerCreateForm;
import edu.rutmiit.demo.dto.passenger.PassengerShowForm;
import edu.rutmiit.demo.dto.ticket.TicketCreateForm;
import edu.rutmiit.demo.dto.ticket.TicketCreateForm1;
import edu.rutmiit.demo.dto.ticket.TicketShowForm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/ticket")
public interface TicketController extends BaseController {


    @GetMapping
    String listTickets(Principal principal,
                       @ModelAttribute("form") TicketShowForm form,
                       Model model);

    @GetMapping("/{id}")
    String details(Principal principal,@PathVariable String id,
                   Model model
    );


    @GetMapping("/create/{id}")
    String createForm(Principal principal,
            @PathVariable String id,
                      Model model);


    @PostMapping("/create/{id}")
    String create(Principal principal,
            @PathVariable String id,
            @Valid @ModelAttribute("form") TicketCreateForm form,
            BindingResult bindingResult,
            Model model
    );


    @GetMapping("/{id}/delete")
    String delete(Principal principal,@PathVariable String id);

    @GetMapping("/{id}/confirm")
    String confirm(Principal principal,@PathVariable String id);
}
