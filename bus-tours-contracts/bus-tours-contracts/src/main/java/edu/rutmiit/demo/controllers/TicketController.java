package edu.rutmiit.demo.controllers;

import edu.rutmiit.demo.dto.ticket.forms.TicketCreateForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/ticket")
public interface TicketController extends BaseController {
    @GetMapping
    String listTickets(Principal principal,
                       Model model)throws Exception;

    @GetMapping("/{id}")
    String details(Principal principal,@PathVariable String id,
                   Model model
    )throws Exception;


    @GetMapping("/create/{id}")
    String createForm(Principal principal,
            @PathVariable String id,
                      Model model)throws Exception;

    @PostMapping("/create/{id}")
    String create(Principal principal,
            @PathVariable String id,
            @Valid @ModelAttribute("form") TicketCreateForm form,
            BindingResult bindingResult,
            Model model
    )throws Exception;

    @GetMapping("/{id}/delete")
    String delete(Principal principal,@PathVariable String id)throws Exception;

    @GetMapping("/{id}/confirm")
    String confirm(Principal principal,@PathVariable String id)throws Exception;
}