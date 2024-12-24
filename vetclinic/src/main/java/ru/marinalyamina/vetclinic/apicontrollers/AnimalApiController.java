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

    // TODO: фотка
    @GetMapping("/currentUser")
    public ResponseEntity<List<Animal>> getAnimalsForCurrentUser() {
        Long currentClientId = CurrentUser.clientId;

        List<Animal> animals = animalService.getAnimalsForClient(currentClientId);

        if (animals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(animals);
    }

    // TODO: фотка
    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable Long id) {
        Optional<Animal> animalOptional = animalService.getById(id);

        if (animalOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(animalOptional.get());
    }

    @PostMapping()
    public ResponseEntity<Animal> createAnimal(@RequestBody Animal animal) {
        Long currentClientId = CurrentUser.clientId;

        Optional<Client> clientOptional = clientService.getById(currentClientId);

        if (clientOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var client = clientOptional.get();
        animal.setClient(client);

        Animal createdAnimal = animalService.create(animal);

        return ResponseEntity.ok(createdAnimal);
    }

    @PutMapping("/{id}")
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

        Animal savedAnimal = animalService.update(existingAnimal);

        return ResponseEntity.ok(savedAnimal);
    }





    //delete - только у кого нет посещений и записей иначе ошибка
}
