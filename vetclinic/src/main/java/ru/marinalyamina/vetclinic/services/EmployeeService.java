package ru.marinalyamina.vetclinic.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ru.marinalyamina.vetclinic.models.entities.Employee;
import ru.marinalyamina.vetclinic.repositories.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAll() { return employeeRepository.findAll();}

    public Optional<Employee> getById(Long id) {
        return employeeRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return employeeRepository.existsById(id);
    }

    public Employee create(Employee employee) {
        if (employee.getDescription().isBlank()) {
            employee.setDescription(null);
        }
        return employeeRepository.save(employee);
    }

    public void update(Employee employee) {
        if (employeeRepository.existsById(employee.getId())) {
            if (employee.getDescription().isBlank()) {
                employee.setDescription(null);
            }
            employeeRepository.save(employee);
        } else {
            throw new IllegalArgumentException("Сотрудник не найден");
        }

    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

}
