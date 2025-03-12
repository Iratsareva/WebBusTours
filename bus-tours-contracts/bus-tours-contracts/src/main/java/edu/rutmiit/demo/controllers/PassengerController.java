package edu.rutmiit.demo.controllers;

import edu.rutmiit.demo.controllers.BaseController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/passenger")
public interface PassengerController extends BaseController {
    @GetMapping
    String listPassengers(Principal principal,
                          Model model)throws Exception;

    @GetMapping("/{id}")
    String details(Principal principal,@PathVariable String id,
                   Model model
    )throws Exception;
}
