package ru.marinalyamina.vetclinic.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.marinalyamina.vetclinic.models.dtos.CreateAnimalDTO;
import ru.marinalyamina.vetclinic.models.dtos.UpdateAnimalDTO;
import ru.marinalyamina.vetclinic.models.entities.Animal;
import ru.marinalyamina.vetclinic.models.entities.AnimalType;
import ru.marinalyamina.vetclinic.models.entities.Client;
import ru.marinalyamina.vetclinic.services.AnimalService;
import ru.marinalyamina.vetclinic.services.AnimalTypeService;
import ru.marinalyamina.vetclinic.services.ClientService;

import javax.management.openmbean.CompositeData;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalService animalService;
    private final ClientService clientService;
    private final AnimalTypeService animalTypeService;

    @Autowired
    public AnimalController(AnimalService animalService, ClientService clientService, AnimalTypeService animalTypeService) {
        this.animalService = animalService;
        this.clientService = clientService;
        this.animalTypeService = animalTypeService;
    }

    @GetMapping("/details/{id}")
    public String getAnimalDetails(@PathVariable Long id, Model model) {
        Optional<Animal> animalOptional = animalService.getById(id);
        if (animalOptional.isEmpty()) {
            return "redirect:/clients/all";
        }

        Animal animal = animalOptional.get();

        String imagePath = "/files/" + (animal.getMainImage() != null ? animal.getMainImage().getName() : "default.jpg");
        model.addAttribute("imagePath", imagePath);

        model.addAttribute("animal", animal);
        return "animals/details";
    }


    @GetMapping("/create")
    public String craeteGet(@RequestParam Long clientId, Model model) {
        Optional<Client> optionalClient = clientService.getById(clientId);
        if (optionalClient.isEmpty()) {
            return "redirect:/clients/all";
        }
        Client client = optionalClient.get();

        model.addAttribute("animal", new CreateAnimalDTO());
        model.addAttribute("client", client);
        model.addAttribute("animalTypes", animalTypeService.getAll());

        return "animals/create";
    }

    @PostMapping("/create")
    public String createPost(@RequestParam Long clientId, @ModelAttribute("animal") @Valid CreateAnimalDTO animalDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("animal", animalDTO);
            model.addAttribute("client", clientService.getById(clientId).get());
            model.addAttribute("animalTypes", animalTypeService.getAll());
            return "animals/create";
        }

        Optional<Client> clientOptional = clientService.getById(clientId);
        if (clientOptional.isEmpty()) {
            return "redirect:/clients/all";
        }

        Animal animal = new Animal();
        animal.setName(animalDTO.getName());
        animal.setBirthday(animalDTO.getBirthday());
        animal.setGender(animalDTO.getGender());
        animal.setBreed(animalDTO.getBreed());

        Optional<AnimalType> animalType = animalTypeService.getById(animalDTO.getAnimalTypeId());
        animalType.ifPresent(animal::setAnimalType);
        animal.setClient(clientOptional.get());

        try {
            animalService.create(animal);
        } catch (Exception e) {
            bindingResult.rejectValue("name", "error.animal.name", e.getMessage());
            bindingResult.rejectValue("breed", "error.animal.breed", e.getMessage());

            return "redirect:/animals/create?clientId=" + clientId;
        }

        return "redirect:/clients/details/" + clientId;
    }

    @GetMapping("/update/{id}")
    public String updateGet(@PathVariable("id") Long id, Model model) {
        Optional<Animal> optionalAnimal = animalService.getById(id);
        if (optionalAnimal.isEmpty()) {
            return "redirect:/clients/all";
        }

        Animal animal = optionalAnimal.get();
        model.addAttribute("animal", animal);
        model.addAttribute("animalTypes", animalTypeService.getAll());
        model.addAttribute("client", animal.getClient());

        return "animals/update";
    }


    @PostMapping("/update")
    public String updatePost(@ModelAttribute("animal") @Valid UpdateAnimalDTO animalDTO,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("animalTypes", animalTypeService.getAll());

            Optional<Animal> optionalAnimal = animalService.getById(animalDTO.getId());
            optionalAnimal.ifPresent(animal -> model.addAttribute("client", animal.getClient()));

            return "animals/update";
        }

        Optional<Animal> optionalAnimal = animalService.getById(animalDTO.getId());
        if (optionalAnimal.isEmpty()) {
            return "redirect:/clients/all";
        }

        Animal animal = optionalAnimal.get();
        animal.setName(animalDTO.getName());
        animal.setBirthday(animalDTO.getBirthday());
        animal.setGender(animalDTO.getGender());
        animal.setBreed(animalDTO.getBreed());
        animal.setAnimalType(animalDTO.getAnimalType());

        try {
            animalService.updateAnimal(animal);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("name", "error.animal.name", e.getMessage());
            bindingResult.rejectValue("breed", "error.animal.breed", e.getMessage());
            model.addAttribute("animalTypes", animalTypeService.getAll());
            return "animals/update";
        }

        return "redirect:/animals/details/" + animal.getId();
    }



    @GetMapping("/delete/{id}")
    public String deleteGet(@PathVariable("id") Long id, Model model) {
        Optional<Animal> optionalAnimal = animalService.getById(id);
        if (optionalAnimal.isEmpty()) {
            return "redirect:/clients/all";
        }

        Animal animal = optionalAnimal.get();

        boolean hasAppointmentsOrSchedules = (animal.getAppointments() != null && !animal.getAppointments().isEmpty()) ||
                (animal.getSchedules() != null && !animal.getSchedules().isEmpty());

        model.addAttribute("hasAppointmentsOrSchedules", hasAppointmentsOrSchedules);
        model.addAttribute("animal", animal);
        return "animals/delete";
    }


    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, Model model) {
        Optional<Animal> optionalAnimal = animalService.getById(id);
        if (optionalAnimal.isEmpty()) {
            return "redirect:/clients/all";
        }

        Animal animalToDelete = optionalAnimal.get();

        boolean hasAppointmentsOrSchedules = (animalToDelete.getAppointments() != null && !animalToDelete.getAppointments().isEmpty()) ||
                (animalToDelete.getSchedules() != null && !animalToDelete.getSchedules().isEmpty());

        if (hasAppointmentsOrSchedules) {
            model.addAttribute("animal", animalToDelete);
            model.addAttribute("hasAppointmentsOrSchedules", true);
            return "animals/delete";
        }

        animalService.delete(animalToDelete.getId());
        return "redirect:/clients/details/" + animalToDelete.getClient().getId();
    }



    /*
    // Обработка создания питомца
    @PostMapping("/create")
    public String createAnimal(@ModelAttribute CreateAnimalDTO animalDTO, @RequestParam Long clientId) {
        Optional<Client> clientOptional = clientService.getById(clientId);
        if (clientOptional.isEmpty()) {
            return "redirect:/clients/all"; // Если клиента нет, возвращаемся в список клиентов
        }

        Client client = clientOptional.get();
        Animal animal = new Animal();
        animal.setName(animalDTO.getName());
        animal.setBreed(animalDTO.getBreed());
        animal.setBirthday(animalDTO.getBirthday());
        animal.setGender(animalDTO.getGender());
        animal.setClient(client);

        animalService.create(animal);
        return "redirect:/animals/client/" + clientId; // Перенаправление на страницу списка питомцев клиента
    }



    // Отображение формы для редактирования питомца
    @GetMapping("/update/{id}")
    public String showUpdateAnimalForm(@PathVariable Long id, Model model) {
        Optional<Animal> animalOptional = animalService.getById(id);
        if (animalOptional.isEmpty()) {
            return "redirect:/animals/all"; // Если питомца нет, возвращаемся в список питомцев
        }

        model.addAttribute("animal", animalOptional.get());
        return "animals/update"; // Страница редактирования питомца
    }

    // Обработка обновления питомца
    @PostMapping("/update/{id}")
    public String updateAnimal(@PathVariable Long id, @ModelAttribute CreateAnimalDTO animalDTO) {
        Optional<Animal> animalOptional = animalService.getById(id);
        if (animalOptional.isEmpty()) {
            return "redirect:/animals/all"; // Если питомца нет, возвращаемся в список питомцев
        }

        Animal animal = animalOptional.get();
        animal.setName(animalDTO.getName());
        animal.setBreed(animalDTO.getBreed());
        animal.setBirthday(animalDTO.getBirthday());
        animal.setGender(animalDTO.getGender());

        animalService.update(animal);
        return "redirect:/animals/details/" + animal.getId(); // Перенаправление на страницу с деталями питомца
    }

    // Удаление питомца
    @GetMapping("/delete/{id}")
    public String deleteAnimal(@PathVariable Long id) {
        Optional<Animal> animalOptional = animalService.getById(id);
        if (animalOptional.isEmpty()) {
            return "redirect:/animals/all"; // Если питомца нет, возвращаемся в список питомцев
        }

        animalService.delete(id);
        return "redirect:/animals/all"; // Перенаправление на страницу списка питомцев
    }

    // Отображение списка всех питомцев
    @GetMapping("/all")
    public String getAllAnimals(Model model) {
        List<Animal> animals = animalService.getAllAnimals();
        model.addAttribute("animals", animals);
        return "animals/all"; // Страница со всеми питомцами
    }*/
}
