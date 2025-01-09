package ru.marinalyamina.vetclinic.controllers;

import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.marinalyamina.vetclinic.models.dtos.UpdateEmployeeDTO;
import ru.marinalyamina.vetclinic.models.dtos.UpdateUserDTO;
import ru.marinalyamina.vetclinic.models.entities.Employee;
import ru.marinalyamina.vetclinic.models.entities.Position;
import ru.marinalyamina.vetclinic.models.entities.User;
import ru.marinalyamina.vetclinic.models.enums.Role;
import ru.marinalyamina.vetclinic.services.EmployeeService;
import ru.marinalyamina.vetclinic.services.PositionService;
import ru.marinalyamina.vetclinic.services.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final PositionService positionService;

    public EmployeeController(EmployeeService employeeService, UserService userService, PasswordEncoder passwordEncoder, PositionService positionService){
        this.employeeService = employeeService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.positionService = positionService;
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("employees", employeeService.getAll());
        return "employees/all";
    }

    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable("id") Long id) {
        Optional<Employee> optionalEmployee = employeeService.getById(id);
        if (optionalEmployee.isEmpty()) {
            return "redirect:/employees/all";
        }

        Employee employee = optionalEmployee.get();

        String imagePath = "/files/" + (employee.getMainImage() != null ? employee.getMainImage().getName() : "default.jpg");
        model.addAttribute("imagePath", imagePath);

        model.addAttribute("employee", employee);
        return "employees/details";
    }

    @GetMapping("/create")
    public String createGet(Model model) {
        List<Position> positions = positionService.getAll();
        model.addAttribute("positions", positions);
        model.addAttribute("employee", new Employee());
        return "employees/create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Position> positions = positionService.getAll();
            model.addAttribute("positions", positions);
            return "employees/create";
        }

        try {
            var user = employee.getUser();
            var encodePassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodePassword);
            user.setRole(Role.ROLE_OPERATOR);
            userService.create(user);
            employeeService.create(employee);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("user.surname", "error.employee", e.getMessage());
            bindingResult.rejectValue("user.name", "error.employee", e.getMessage());
            bindingResult.rejectValue("user.patronymic", "error.employee", e.getMessage());
            bindingResult.rejectValue("user.birthday", "error.employee", e.getMessage());
            bindingResult.rejectValue("user.email", "error.employee", e.getMessage());
            bindingResult.rejectValue("user.phone", "error.employee", e.getMessage());
            bindingResult.rejectValue("user.username", "error.employee", e.getMessage());
            bindingResult.rejectValue("user.password", "error.employee", e.getMessage());
            List<Position> positions = positionService.getAll();
            model.addAttribute("positions", positions);
            return "employees/create";
        }

        return "redirect:/employees/all";
    }

    @GetMapping("/update/{id}")
    public String updateGet(Model model, @PathVariable("id") Long id) {
        Optional<Employee> optionalEmployee = employeeService.getById(id);
        if (optionalEmployee.isEmpty()) {
            return "redirect:/employees/all";
        }

        List<Position> positions = positionService.getAll();
        model.addAttribute("positions", positions);
        model.addAttribute("employee", optionalEmployee.get());
        return "employees/update";
    }


    @PostMapping("/update")
    public String updatePost(@ModelAttribute("employee") @Valid UpdateEmployeeDTO employeeDTO, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            return "employees/update";
        }

        try {
            Optional<Employee> existingEmployeeOpt = employeeService.getById(employeeDTO.getId());
            if (existingEmployeeOpt.isEmpty()) {
                bindingResult.rejectValue("id", "error.employee", "Сотрудник не найден");
                return "employees/update";
            }

            Employee existingEmployee = existingEmployeeOpt.get();
            User existingUser = existingEmployee.getUser();

            UpdateUserDTO updatedUserDTO = employeeDTO.getUser();

            existingUser.setSurname(updatedUserDTO.getSurname());
            existingUser.setName(updatedUserDTO.getName());
            existingUser.setPatronymic(updatedUserDTO.getPatronymic());
            existingUser.setBirthday(updatedUserDTO.getBirthday());
            existingUser.setEmail(updatedUserDTO.getEmail());
            existingUser.setPhone(updatedUserDTO.getPhone());
            existingUser.setUsername(updatedUserDTO.getUsername());

            existingEmployee.setDescription(employeeDTO.getDescription());
            existingEmployee.setPosition(employeeDTO.getPosition());

            userService.update(existingUser);
            employeeService.update(existingEmployee);

        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("user.surname", "error.employee", e.getMessage());
            bindingResult.rejectValue("user.name", "error.employee", e.getMessage());
            bindingResult.rejectValue("user.patronymic", "error.employee", e.getMessage());
            bindingResult.rejectValue("user.birthday", "error.employee", e.getMessage());
            bindingResult.rejectValue("user.email", "error.employee", e.getMessage());
            bindingResult.rejectValue("user.phone", "error.employee", e.getMessage());
            bindingResult.rejectValue("user.username", "error.employee", e.getMessage());
            return "employees/update";
        }

        return "redirect:/employees/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteGet(Model model, @PathVariable("id") Long id) {
        Optional<Employee> optionalEmployee = employeeService.getById(id);
        if (optionalEmployee.isEmpty()) {
            return "redirect:/employees/all";
        }
        Employee employee = optionalEmployee.get();

        boolean hasAppointments = employee.getAppointments() != null && !employee.getAppointments().isEmpty();
        boolean hasSchedules = employee.getSchedules() != null && !employee.getSchedules().isEmpty();

        model.addAttribute("employee", employee);
        model.addAttribute("hasAppointments", hasAppointments);
        model.addAttribute("hasSchedules", hasSchedules);

        return "employees/delete";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, Model model) {
        Optional<Employee> optionalEmployee = employeeService.getById(id);
        if (optionalEmployee.isEmpty()) {
            return "redirect:/employees/all";
        }

        Employee employeeToDelete = optionalEmployee.get();

        boolean hasAppointments = employeeToDelete.getAppointments() != null && !employeeToDelete.getAppointments().isEmpty();
        boolean hasSchedules = employeeToDelete.getSchedules() != null && !employeeToDelete.getSchedules().isEmpty();

        if (hasAppointments || hasSchedules) {
            model.addAttribute("employee", employeeToDelete);
            model.addAttribute("hasAppointments", hasAppointments);
            model.addAttribute("hasSchedules", hasSchedules);
            return "employees/delete";
        }

        employeeService.delete(employeeToDelete.getId());
        return "redirect:/employees/all";
    }

}
