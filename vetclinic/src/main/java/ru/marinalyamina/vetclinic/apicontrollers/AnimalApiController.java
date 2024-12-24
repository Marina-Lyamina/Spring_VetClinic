package ru.marinalyamina.vetclinic.apicontrollers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.marinalyamina.vetclinic.models.entities.Animal;
import ru.marinalyamina.vetclinic.models.entities.Client;
import ru.marinalyamina.vetclinic.services.AnimalService;
import ru.marinalyamina.vetclinic.services.ClientService;
import ru.marinalyamina.vetclinic.utils.CurrentUser;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/animals")
public class AnimalApiController {
    private final AnimalService animalService;
    private final ClientService clientService;

    public AnimalApiController(AnimalService animalService, ClientService clientService) {
        this.animalService = animalService;
        this.clientService = clientService;
    }

    /*@GetMapping("/currentUser")
    public List<Animal> getAll() {
        Long currentUserId = CurrentUser.id;
        //getAllForUser - все жив для конк пользователя + у животных должны быть предстоящие посещения

        return animalService.getAll();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Animal>> getAllByUserId(@PathVariable Long userId) {
        List<Animal> animals = animalService.getAnimalsByUserId(userId);
        if (animals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(animals);
    }*/

    @GetMapping("/currentUser")
    public ResponseEntity<List<Animal>> getAllAnimalsWithFutureAppointmentsByUserId() {
        Long currentUserId = CurrentUser.id;
        List<Animal> animals = animalService.getAnimalsWithUpcomingSchedulesByUserId(currentUserId);
        if (animals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(animals);
    }

    //getById все визиты подключить

    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable Long id) {
        Optional<Animal> animal = animalService.getById(id);
        if (animal.isPresent()) {
            return ResponseEntity.ok(animal.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //create Long currentUserId = CurrentUser.id;
    @PostMapping("/currentUser")
    public ResponseEntity<Animal> createAnimal(@Valid @RequestBody Animal animal) {
        Long currentUserId = CurrentUser.id;

        Optional<Client> currentClientOpt = clientService.getById(currentUserId);

        if (currentClientOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Client currentClient = currentClientOpt.get();
        animal.setClient(currentClient);

        Animal createdAnimal = animalService.create(animal);

        return ResponseEntity.ok(createdAnimal);
    }

    //update
    @PostMapping("/{id}")
    public ResponseEntity<Animal> updateAnimal(@PathVariable Long id, @Valid @RequestBody Animal animal) {
        Optional<Animal> animalOptional = animalService.getById(id);

        if (animalOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Animal existingAnimal = animalOptional.get();

        existingAnimal.setName(animal.getName());
        existingAnimal.setBirthday(animal.getBirthday());
        existingAnimal.setGender(animal.getGender());
        existingAnimal.setBreed(animal.getBreed());
        existingAnimal.setAnimalType(animal.getAnimalType());
        //existingAnimal.setMainImage(animal.getMainImage());

        Animal savedAnimal = animalService.update(existingAnimal);

        return ResponseEntity.ok(savedAnimal);
    }





    //delete - только у кого нет посещений и записей иначе ошибка
}
