package ru.marinalyamina.vetclinic.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.marinalyamina.vetclinic.models.entities.Appointment;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
}
