package ru.marinalyamina.vetclinic.services;

import org.springframework.stereotype.Service;

import ru.marinalyamina.vetclinic.models.entities.Animal;
import ru.marinalyamina.vetclinic.models.entities.Appointment;
import ru.marinalyamina.vetclinic.models.entities.Schedule;
import ru.marinalyamina.vetclinic.repositories.AnimalRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnimalService {
    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public List<Animal> getAll() { return animalRepository.findAll();}

    public Optional<Animal> getById(Long id) {
        Optional<Animal> animalOptional = animalRepository.findById(id);

        if (animalOptional.isPresent()) {
            Animal animal = animalOptional.get();

            // Записи на приемы
            List<Schedule> upcomingSchedules = animal.getSchedules().stream()
                    .filter(schedule -> schedule.getDate().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
            animal.setSchedules(upcomingSchedules);

            return Optional.of(animal);
        }
        return Optional.empty();
    }


    public boolean existsById(Long id) {
        return animalRepository.existsById(id);
    }

    public Animal create(Animal animal) {
        return animalRepository.save(animal);
    }

    public void delete(Long id) {
        animalRepository.deleteById(id);
    }

    public List<Animal> getAnimalsWithUpcomingSchedulesByUserId(Long userId) {
        List<Animal> animals = animalRepository.findByClient_User_Id(userId);

        // записи на приемы
        for (Animal animal : animals) {
            List<Schedule> upcomingSchedules = animal.getSchedules().stream()
                    .filter(schedule -> schedule.getDate().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
            animal.setSchedules(upcomingSchedules);
        }

        return animals;
    }

    public Animal update(Animal animal) {
        return animalRepository.save(animal);
    }

}
