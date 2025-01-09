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

        try {
            var user = client.getUser();
            var encodePassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodePassword);
            user.setRole(Role.ROLE_USER);
            userService.create(user);
            clientService.create(client);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("user.surname", "error.client", e.getMessage());
            bindingResult.rejectValue("user.name", "error.client", e.getMessage());
            bindingResult.rejectValue("user.patronymic", "error.client", e.getMessage());
            bindingResult.rejectValue("user.birthday", "error.client", e.getMessage());
            bindingResult.rejectValue("user.email", "error.client", e.getMessage());
            bindingResult.rejectValue("user.phone", "error.client", e.getMessage());
            bindingResult.rejectValue("user.username", "error.client", e.getMessage());
            bindingResult.rejectValue("user.password", "error.client", e.getMessage());
            return "clients/create";
        }

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

        try {
            Optional<Client> existingClientOpt = clientService.getById(clientDTO.getId());
            if (existingClientOpt.isEmpty()) {
                bindingResult.rejectValue("id", "error.client", "Клиент не найден");
                return "clients/update";
            }

            Client existingClient = existingClientOpt.get();
            User existingUser = existingClient.getUser();

            UpdateUserDTO updatedUserDTO = clientDTO.getUser();

            existingUser.setSurname(updatedUserDTO.getSurname());
            existingUser.setName(updatedUserDTO.getName());
            existingUser.setPatronymic(updatedUserDTO.getPatronymic());
            existingUser.setBirthday(updatedUserDTO.getBirthday());
            existingUser.setEmail(updatedUserDTO.getEmail());
            existingUser.setPhone(updatedUserDTO.getPhone());
            existingUser.setUsername(updatedUserDTO.getUsername());

            userService.update(existingUser);
            clientService.update(existingClient);

        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("user.surname", "error.client", e.getMessage());
            bindingResult.rejectValue("user.name", "error.client", e.getMessage());
            bindingResult.rejectValue("user.patronymic", "error.client", e.getMessage());
            bindingResult.rejectValue("user.birthday", "error.client", e.getMessage());
            bindingResult.rejectValue("user.email", "error.client", e.getMessage());
            bindingResult.rejectValue("user.phone", "error.client", e.getMessage());
            bindingResult.rejectValue("user.username", "error.client", e.getMessage());
            return "clients/update";
        }

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
