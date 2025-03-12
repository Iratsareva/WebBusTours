package demo.controllers;

import demo.dto.passenger.PassengerDTO;
import demo.dto.passenger.PassengerRegistrationDTO;
import demo.dto.passenger.PassengerUpdateDTO;
import demo.exception.ClientException;
import demo.exception.NotFoundException;
import demo.models.Passenger;
import demo.services.AuthService;
import demo.services.PassengerService;
import edu.rutmiit.demo.dto.passenger.*;
import edu.rutmiit.demo.dto.passenger.forms.PassengerEditForm;
import edu.rutmiit.demo.dto.register.*;
import edu.rutmiit.demo.dto.register.forms.LoginForm;
import edu.rutmiit.demo.dto.register.forms.RegisterForm;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/users")
public class AuthController extends BaseControllerIpl{
    private static final Logger LOG = LogManager.getLogger(Controller.class);
    private PassengerService passengerService;
    private AuthService authService;



    @GetMapping("/register")
    public String register1(Model model) {
        var viewModel =  new RegisterViewModel(
                createBaseViewModel("Регистрация")
        );
        RegisterForm form = new RegisterForm("", "", "", "", "", "", "");

        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegisterForm form,
                           BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "Show register");

        if(bindingResult.hasErrors()){
            var viewModel =  new RegisterViewModel(
                    createBaseViewModel("Регистрация")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "register";
        }

        try {
            authService.register(new PassengerRegistrationDTO(form.name(), form.surname(), form.patronymic(), form.email(), form.login(), form.password(), form.confirmPassword()));
        }catch (ClientException e){
            var viewModel =  new RegisterViewModel(
                    createBaseViewModel("Регистрация")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }

        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        var viewModel = new LoginViewModel(
                createBaseViewModel("Вход")
        );
        LoginForm form = new LoginForm("", "");
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);
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

        var viewModel = new ProfileViewModel(
                createBaseViewModel("Профиль")
        );

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
        model.addAttribute("model", viewModel);
        return "profile";
    }



    @GetMapping("/edit")
    public String editForm(Model model) throws NotFoundException {
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
                        BindingResult bindingResult, Model model)throws NotFoundException {
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

        passengerService.updatePassenger(new PassengerUpdateDTO(form.name(), form.surname(), form.patronymic(),  LocalDate.parse(form.birthday()), form.identificationDocument(), form.telephone(),username));

        return "redirect:/users/profile";
    }



    @Autowired
    public void setPassengerService(PassengerService passengerService) {
        this.passengerService = passengerService;
    }
    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }
}