package ru.marinalyamina.vetclinic.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.marinalyamina.vetclinic.models.entities.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}