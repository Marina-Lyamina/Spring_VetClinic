package ru.marinalyamina.vetclinic.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.marinalyamina.vetclinic.models.entities.AnimalType;

public interface AnimalTypeRepository extends CrudRepository<AnimalType, Long> {
    boolean existsByName(String name);
}
