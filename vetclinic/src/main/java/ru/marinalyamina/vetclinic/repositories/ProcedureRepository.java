package ru.marinalyamina.vetclinic.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.marinalyamina.vetclinic.models.entities.Procedure;

public interface ProcedureRepository extends CrudRepository<Procedure, Long> {
}
