package ru.marinalyamina.vetclinic.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.marinalyamina.vetclinic.models.entities.Procedure;
import ru.marinalyamina.vetclinic.services.ProcedureService;

import java.util.Optional;

@Controller
@RequestMapping("/procedures")
public class ProcedureController {
    private final ProcedureService procedureService;

    public ProcedureController(ProcedureService procedureService) {
        this.procedureService = procedureService;
    }

    @GetMapping("all")
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

        try {
            procedureService.create(procedure);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("name", "error.procedure", e.getMessage());
            return "procedures/create";
        }

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
        try {
            procedureService.create(procedure);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("name", "error.procedure", e.getMessage());
            return "procedures/update";
        }

        return "redirect:/procedures/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteGet(Model model, @PathVariable("id") Long id) {
        Optional<Procedure> optionalProcedure = procedureService.getById(id);
        if (optionalProcedure.isEmpty()) {
            return "redirect:/procedures/all";
        }
        model.addAttribute("procedure", optionalProcedure.get());
        return "procedures/delete";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        procedureService.delete(id);
        return "redirect:/procedures/all";
    }
}
