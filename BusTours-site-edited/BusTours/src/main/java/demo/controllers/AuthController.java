package demo.controllers;


import demo.dto.PassengerDTO;
import demo.dto.PassengerRegistrationDTO;
import demo.models.Passenger;
import demo.services.AuthService;
import demo.services.PassengerService;
import edu.rutmiit.demo.dto.base.BaseViewModel;
import edu.rutmiit.demo.dto.passenger.*;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;

@Controller
@RequestMapping("/users")
public class AuthController {
    private static final Logger LOG = LogManager.getLogger(Controller.class);
    private PassengerService passengerService;
    private AuthService authService;



    @Autowired
    public AuthController(PassengerService passengerService, AuthService authService) {
        this.passengerService = passengerService;
        this.authService = authService;
    }


    @ModelAttribute("passengerRegistrationDTO")
    public PassengerRegistrationDTO initForm() {
        return new PassengerRegistrationDTO();
    }



    @GetMapping("/register")
    public String register() {
        LOG.log(Level.INFO, "Show register");
        return "register";
    }


    @PostMapping("/register")
    public String doRegister(@Valid PassengerRegistrationDTO passengerRegistrationDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("passengerRegistrationDTO", passengerRegistrationDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passengerRegistrationDTO", bindingResult);

            return "redirect:/users/register";
        }


        String str = this.authService.register(passengerRegistrationDTO);
        LOG.log(Level.INFO, "Show register");

        if(str.equals("passwords.match")){
            redirectAttributes.addFlashAttribute("errorMessage", "Пароли не совпадают");
            redirectAttributes.addFlashAttribute("passengerRegistrationDTO", passengerRegistrationDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passengerRegistrationDTO", bindingResult);

            return "redirect:/users/register";


        } else if (str.equals("email.used")){
            redirectAttributes.addFlashAttribute("errorMessage", "Пользователь с такой почтой уже существует");
            redirectAttributes.addFlashAttribute("passengerRegistrationDTO", passengerRegistrationDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passengerRegistrationDTO", bindingResult);

            return "redirect:/users/register";

        }else {
            return "redirect:/users/login";
        }
    }



    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("badCredentials", true);

        return "redirect:/users/login";
    }



    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        String login = principal.getName();

        Passenger passenger = authService.getPassenger(login);
        PassengerViewModel passengerViewModel = new PassengerViewModel(
                passenger.getId(),
                passenger.getName(),
                passenger.getSurname(),
                passenger.getPatronymic(),
                passenger.getBirthday(),
                passenger.getIdentificationDocument(),
                passenger.getTelephone(),
                passenger.getEmail(),
                passenger.getLogin(),
                passenger.getPassword()
        );

        model.addAttribute("passengerViewModel", passengerViewModel);

        return "profile";
    }



    @GetMapping("/edit")
    public String editForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        PassengerDTO passenger = passengerService.getPassengerByLogin(username);


        var viewModel = new PassengerEditViewModel(
                createBaseViewModel("Редактирование пользователя")
        );

        model.addAttribute("model", viewModel);
        model.addAttribute("form", new PassengerEditForm(passenger.id(), passenger.name(), passenger.surname(), passenger.patronymic(), passenger.birthday() != null ? passenger.birthday().toString():"", passenger.identificationDocument(), passenger.telephone()));

        return "passenger-edit";
    }


    @PostMapping("/edit")
    public String edit( @Valid @ModelAttribute("form") PassengerEditForm form,
                        BindingResult bindingResult, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(bindingResult.hasErrors()){
            var viewModel = new PassengerEditViewModel(
                    createBaseViewModel("Редактирование пользователя")
            );

            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "passenger-edit";
        }

        passengerService.updatePassenger(form.name(), form.surname(), form.patronymic(),  LocalDate.parse(form.birthday()), form.identificationDocument(), form.telephone(),username );

        return "redirect:/users/profile";
    }

    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }


}
