package ru.marinalyamina.vetclinic.controllers;

import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.marinalyamina.vetclinic.models.dtos.UpdateClientDTO;
import ru.marinalyamina.vetclinic.models.dtos.UpdateUserDTO;
import ru.marinalyamina.vetclinic.models.entities.Client;
import ru.marinalyamina.vetclinic.models.entities.User;
import ru.marinalyamina.vetclinic.models.enums.Role;
import ru.marinalyamina.vetclinic.services.ClientService;
import ru.marinalyamina.vetclinic.services.UserService;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;
    private final UserService userService;
    private final PasswordEncoder  passwordEncoder;

    public ClientController(ClientService clientService, UserService userService, PasswordEncoder passwordEncoder){
        this.clientService = clientService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("clients", clientService.getAll());
        return "clients/all";
    }

    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable("id") Long id) {
        Optional<Client> optionalClient = clientService.getById(id);
        if(optionalClient.isEmpty()){
            return "redirect:/clients/all";
        }
        model.addAttribute("client", optionalClient.get());
        return "clients/details";
    }

    @GetMapping("/create")
    public String createGet(Model model) {
        model.addAttribute("client", new Client());
        return "clients/create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("client") @Valid Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "clients/create";
        }

        User user = client.getUser();
        if (userService.existsByPhone(user.getPhone())) {
            bindingResult.rejectValue("user.phone", "error.client", "Такой телефон уже использован");
        }
        if (userService.existsByEmail(user.getEmail())) {
            bindingResult.rejectValue("user.email", "error.client", "Такой email уже использован");
        }
        if (userService.existsByUsername(user.getUsername())) {
            bindingResult.rejectValue("user.username", "error.client", "Придумайте другой логин");
        }

        if (bindingResult.hasErrors()) {
            return "clients/create";
        }

        var encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user.setRole(Role.ROLE_USER);

        userService.create(user);
        clientService.create(client);

        return "redirect:/clients/all";
    }

    @GetMapping("/update/{id}")
    public String updateGet(Model model, @PathVariable("id") Long id) {
        Optional<Client> optionalClient = clientService.getById(id);
        if (optionalClient.isEmpty()) {
            return "redirect:/clients/all";
        }
        model.addAttribute("client", optionalClient.get());
        return "clients/update";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute("client") @Valid UpdateClientDTO clientDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "clients/update";
        }

        Optional<Client> existingClientOpt = clientService.getById(clientDTO.getId());
        if (existingClientOpt.isEmpty()) {
            return "redirect:/clients/all";
        }

        Client existingClient = existingClientOpt.get();
        User user = existingClient.getUser();

        if (!Objects.equals(user.getPhone(), clientDTO.getUser().getPhone()) && userService.existsByPhone(clientDTO.getUser().getPhone())) {
            bindingResult.rejectValue("user.phone", "error.client", "Такой телефон уже использован");
        }
        if (!Objects.equals(user.getEmail(), clientDTO.getUser().getEmail()) && userService.existsByEmail(clientDTO.getUser().getEmail())) {
            bindingResult.rejectValue("user.email", "error.client", "Такой email уже использован");
        }
        if (!Objects.equals(user.getUsername(), clientDTO.getUser().getUsername()) && userService.existsByUsername(clientDTO.getUser().getUsername())) {
            bindingResult.rejectValue("user.username", "error.client", "Придумайте другой логин");
        }

        if (bindingResult.hasErrors()) {
            return "clients/update";
        }

        UpdateUserDTO updatedUserDTO = clientDTO.getUser();

        user.setSurname(updatedUserDTO.getSurname());
        user.setName(updatedUserDTO.getName());
        user.setPatronymic(updatedUserDTO.getPatronymic());
        user.setBirthday(updatedUserDTO.getBirthday());
        user.setEmail(updatedUserDTO.getEmail());
        user.setPhone(updatedUserDTO.getPhone());
        user.setUsername(updatedUserDTO.getUsername());

        userService.update(user);
        clientService.update(existingClient);

        return "redirect:/clients/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteGet(Model model, @PathVariable("id") Long id) {
        Optional<Client> optionalClient = clientService.getById(id);
        if(optionalClient.isEmpty()){
            return "redirect:/clients/all";
        }
        Client client = optionalClient.get();

        model.addAttribute("client", client);
        model.addAttribute("hasPets", client.getAnimals() != null && !client.getAnimals().isEmpty());

        return "clients/delete";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, Model model) {
        Optional<Client> optionalClient = clientService.getById(id);
        if(optionalClient.isEmpty()) {
            return "redirect:/clients/all";
        }

        Client clientToDelete = optionalClient.get();

        if (clientToDelete.getAnimals() != null && !clientToDelete.getAnimals().isEmpty()) {
            model.addAttribute("client", clientToDelete);
            model.addAttribute("hasPets", true);
            return "clients/delete";
        }

        clientService.delete(clientToDelete.getId());
        return "redirect:/clients/all";
    }
}
