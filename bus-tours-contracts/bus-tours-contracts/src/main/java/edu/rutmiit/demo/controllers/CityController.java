package edu.rutmiit.demo.controllers;

import edu.rutmiit.demo.dto.city.forms.CityCreateForm;
import edu.rutmiit.demo.dto.city.forms.CityEditForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/cities")
public interface CityController extends BaseController {

    @GetMapping
    String listCities(Principal principal,
                      Model model)throws Exception;;

    @GetMapping("/{id}")
    String details(Principal principal,
                   @PathVariable String id,
            Model model
    )throws Exception;;


    @GetMapping("/create")
    String createForm(Principal principal,Model model)throws Exception;;


    @PostMapping("/create")
    String create(Principal principal,
            @Valid @ModelAttribute("form") CityCreateForm form,
            BindingResult bindingResult,
            Model model
    )throws Exception;

    @GetMapping("/{id}/edit")
    String editForm(Principal principal,
            @PathVariable String id,
            Model model
    )throws Exception;;

    @PostMapping("/{id}/edit")
    String edit(Principal principal,
            @PathVariable String id,
            @Valid @ModelAttribute("form") CityEditForm form,
            BindingResult bindingResult,
            Model model
    )throws Exception;
}
