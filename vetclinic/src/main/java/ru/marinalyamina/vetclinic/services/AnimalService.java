package ru.marinalyamina.vetclinic.services;

import org.springframework.stereotype.Service;

import ru.marinalyamina.vetclinic.models.entities.Animal;
import ru.marinalyamina.vetclinic.repositories.AnimalRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {
    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public List<Animal> getAll() { return (List<Animal>) animalRepository.findAll();}

    public Optional<Animal> getById(Long id) {
        return animalRepository.findById(id);
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
}
