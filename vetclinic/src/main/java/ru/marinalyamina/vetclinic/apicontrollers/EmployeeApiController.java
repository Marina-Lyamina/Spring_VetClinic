package ru.marinalyamina.vetclinic.apicontrollers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.marinalyamina.vetclinic.models.dtos.ScheduleDTO;
import ru.marinalyamina.vetclinic.models.entities.Employee;
import ru.marinalyamina.vetclinic.services.EmployeeService;
import ru.marinalyamina.vetclinic.services.ScheduleService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeApiController {

    private final EmployeeService employeeService;
    private final ScheduleService scheduleService;

    public EmployeeApiController(EmployeeService employeeService, ScheduleService scheduleService){
        this.employeeService = employeeService;
        this.scheduleService = scheduleService;
    }

    // TODO: фотка
    @GetMapping()
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAll();

        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(employees);
    }

    // TODO: фотка
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.getById(id);

        if (employee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(employee.get());
    }

    @GetMapping("/{id}/free-schedules")
    public ResponseEntity<List<ScheduleDTO>> getEmployeeFreeSchedules(@PathVariable Long id) {
        List<ScheduleDTO> schedules = scheduleService.getEmployeeFreeSchedules(id);

        if (schedules.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(schedules);
    }
}
