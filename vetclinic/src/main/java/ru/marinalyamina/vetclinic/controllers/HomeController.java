package ru.marinalyamina.vetclinic.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.marinalyamina.vetclinic.models.entities.Schedule;
import ru.marinalyamina.vetclinic.models.enums.Role;
import ru.marinalyamina.vetclinic.services.EmployeeService;
import ru.marinalyamina.vetclinic.services.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;
    private final EmployeeService employeeService;

    public HomeController(UserService userService, EmployeeService employeeService){
        this.userService = userService;
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String home(Model model) {
        var currentUser = userService.getCurrentUser();

        if(currentUser.isEmpty()){
            return "redirect:/logout";
        }

        var employee = employeeService.getAll().stream()
                .filter(e -> e.getUser().getId().equals(currentUser.get().getId()))
                .findFirst();

        if(employee.isEmpty()){
            return "redirect:/logout";
        }


        List<Schedule> futureSchedules = employee.get().getSchedules().stream()
                .filter(schedule -> schedule.getDate().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Schedule::getDate))
                .toList();

        employee.get().setSchedules(futureSchedules);

        model.addAttribute("employee", employee.get());
        model.addAttribute("isAdmin",  currentUser.get().getRole() == Role.ROLE_ADMIN);

        return "index";
    }

}