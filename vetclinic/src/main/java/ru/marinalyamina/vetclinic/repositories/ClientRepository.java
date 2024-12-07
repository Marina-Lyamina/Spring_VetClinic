package ru.marinalyamina.vetclinic.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.marinalyamina.vetclinic.models.entities.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
