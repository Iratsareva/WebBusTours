package edu.rutmiit.demo.controllers;

import edu.rutmiit.demo.dto.trip.forms.TripCreateForm;
import edu.rutmiit.demo.dto.trip.forms.TripEditForm;
import edu.rutmiit.demo.dto.trip.forms.TripShowForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/trip")
public interface TripController extends BaseController {
    @GetMapping
    String listTrips(Principal principal,
                     Model model)throws Exception;;

    @GetMapping("/{id}/passengers")
    String tripPassengers(Principal principal,@PathVariable String id,
            @ModelAttribute("form") TripShowForm form,
                      Model model)throws Exception;;

    @GetMapping("/{id}")
    String details(Principal principal,@PathVariable String id,
                   Model model
    )throws Exception;


    @GetMapping("/create/{id}")
    String createForm(Principal principal,@PathVariable String id,
                      Model model)throws Exception;

    @PostMapping("/create/{id}")
    String create(Principal principal,
            @PathVariable String id,
            @Valid @ModelAttribute("form") TripCreateForm form,
            BindingResult bindingResult,
            Model model
    ) throws Exception;


    @GetMapping("/{id}/edit")
    String editForm(Principal principal,
            @PathVariable String id,
            Model model
    )throws Exception;


    @PostMapping("/{id}/edit")
    String edit(Principal principal,
            @PathVariable String id,
            @Valid @ModelAttribute("form") TripEditForm form,
            BindingResult bindingResult,
            Model model
    )throws Exception;

    @GetMapping("/soon")
    String getSoonTours (Principal principal,  Model model)throws Exception;
}
