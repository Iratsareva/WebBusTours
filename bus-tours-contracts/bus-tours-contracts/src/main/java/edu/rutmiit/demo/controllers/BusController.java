package edu.rutmiit.demo.controllers;

import edu.rutmiit.demo.dto.bus.forms.BusCreateForm;
import edu.rutmiit.demo.dto.bus.forms.BusEditForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/bus")
public interface BusController extends BaseController {
    @GetMapping
    String listBuses(
            Principal principal,
            Model model
    )throws Exception;

    @GetMapping("/{id}")
    String details(
            Principal principal,
            @PathVariable String id,
            Model model
    )throws Exception;

    @GetMapping("/create")
    String createForm(Principal principal,Model model)throws Exception;;

    @PostMapping("/create")
    String create(Principal principal,
            @Valid @ModelAttribute("form") BusCreateForm form,
            BindingResult bindingResult,
            Model model
    )throws Exception;;

    @GetMapping("/{id}/edit")
    String editForm(Principal principal,
            @PathVariable String id,
            Model model
    )throws Exception;;

    @PostMapping("/{id}/edit")
    String edit(Principal principal,
            @PathVariable String id,
            @Valid @ModelAttribute("form") BusEditForm form,
            BindingResult bindingResult,
            Model model
    )throws Exception;;


    @GetMapping("/{id}/delete")
    String delete(Principal principal,@PathVariable String id)throws Exception;;
}