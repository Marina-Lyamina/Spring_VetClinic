package ru.marinalyamina.vetclinic.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.marinalyamina.vetclinic.models.entities.Animal;

public interface AnimalRepository extends CrudRepository<Animal, Long> {
}
