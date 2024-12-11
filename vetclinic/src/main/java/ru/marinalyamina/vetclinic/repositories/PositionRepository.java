package ru.marinalyamina.vetclinic.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.marinalyamina.vetclinic.models.entities.Position;

public interface PositionRepository extends CrudRepository<Position, Long> {
    boolean existsByName(String name);
}
