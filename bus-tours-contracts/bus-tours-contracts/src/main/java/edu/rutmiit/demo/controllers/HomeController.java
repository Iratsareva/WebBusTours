package edu.rutmiit.demo.controllers;

import edu.rutmiit.demo.controllers.BaseController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/")
public interface HomeController extends BaseController {
    @GetMapping()
    String index(Principal principal, Model model);
}
