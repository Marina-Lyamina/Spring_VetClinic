package ru.marinalyamina.vetclinic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.marinalyamina.vetclinic.models.entities.Procedure;

@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, Long> {
}
