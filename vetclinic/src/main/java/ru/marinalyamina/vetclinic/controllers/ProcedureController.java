package ru.marinalyamina.vetclinic.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.marinalyamina.vetclinic.models.entities.Procedure;
import ru.marinalyamina.vetclinic.services.ProcedureService;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/procedures")
public class ProcedureController {
    private final ProcedureService procedureService;

    public ProcedureController(ProcedureService procedureService) {
        this.procedureService = procedureService;
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("procedures", procedureService.getAll());
        return "procedures/all";
    }

    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable("id") Long id) {
        Optional<Procedure> optionalProcedure = procedureService.getById(id);
        if (optionalProcedure.isEmpty()) {
            return "redirect:/procedures/all";
        }
        model.addAttribute("procedure", optionalProcedure.get());
        return "procedures/details";
    }

    @GetMapping("/create")
    public String createGet(Model model) {
        model.addAttribute("procedure", new Procedure());
        return "procedures/create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("procedure") @Valid Procedure procedure, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "procedures/create";
        }

        if (procedureService.existsByName(procedure.getName())) {
            bindingResult.rejectValue("name", "error.procedure", "Услуга с таким названием уже существует");
            return "procedures/create";
        }

        procedureService.create(procedure);

        return "redirect:/procedures/all";
    }

    @GetMapping("/update/{id}")
    public String updateGet(Model model, @PathVariable("id") Long id) {
        Optional<Procedure> optionalProcedure = procedureService.getById(id);
        if (optionalProcedure.isEmpty()) {
            return "redirect:/procedures/all";
        }
        model.addAttribute("procedure", optionalProcedure.get());
        return "procedures/update";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute("procedure") @Valid Procedure procedure, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "procedures/update";
        }

        Optional<Procedure> existingProcedureOpt = procedureService.getById(procedure.getId());
        if (existingProcedureOpt.isEmpty()) {
            return "redirect:/procedures/all";
        }

        if (!Objects.equals(procedure.getName(), existingProcedureOpt.get().getName()) && procedureService.existsByName(procedure.getName())) {
            bindingResult.rejectValue("name", "error.procedure", "Услуга с таким названием уже существует");
            return "procedures/update";
        }

        procedureService.create(procedure);

        return "redirect:/procedures/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteGet(Model model, @PathVariable("id") Long id) {
        Optional<Procedure> optionalProcedure = procedureService.getById(id);
        if (optionalProcedure.isEmpty()) {
            return "redirect:/procedures/all";
        }

        Procedure procedure = optionalProcedure.get();
        boolean hasAppointments = procedure.getAppointments() != null && !procedure.getAppointments().isEmpty();

        model.addAttribute("procedure", procedure);
        model.addAttribute("hasAppointments", hasAppointments);

        return "procedures/delete";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, Model model) {
        Optional<Procedure> optionalProcedure = procedureService.getById(id);
        if (optionalProcedure.isEmpty()) {
            return "redirect:/procedures/all";
        }

        Procedure procedure = optionalProcedure.get();

        if (procedure.getAppointments() != null && !procedure.getAppointments().isEmpty()) {
            model.addAttribute("procedure", procedure);
            model.addAttribute("hasAppointments", true);
            return "procedures/delete";
        }

        if (procedureService.existsById(id)) {
            procedureService.delete(id);
        }

        return "redirect:/procedures/all";
    }

}
