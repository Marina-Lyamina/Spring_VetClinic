package ru.marinalyamina.vetclinic.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.marinalyamina.vetclinic.models.entities.AnimalType;
import ru.marinalyamina.vetclinic.services.AnimalTypeService;

import java.util.Optional;

@Controller
@RequestMapping("/animaltypes")
public class AnimalTypeController {
    private final AnimalTypeService animalTypeService;

    public AnimalTypeController(AnimalTypeService animalTypeService) {
        this.animalTypeService = animalTypeService;
    }

    @GetMapping("all")
    public String getAll(Model model) {
        model.addAttribute("animalTypes", animalTypeService.getAll());
        return "animaltypes/all";
    }

    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable("id") Long id) {
        Optional<AnimalType> optionalAnimalType = animalTypeService.getById(id);
        if(optionalAnimalType.isEmpty()){
            return "redirect:/animaltypes/all";
        }
        model.addAttribute("animalType", optionalAnimalType.get());
        return "animaltypes/details";
    }

    @GetMapping("/create")
    public String createGet(Model model) {
        model.addAttribute("animalType", new AnimalType());
        return "animaltypes/create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("animalType") @Valid AnimalType animalType, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "animaltypes/create";
        }

        try {
            animalTypeService.create(animalType);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("name", "error.animalType", e.getMessage());
            return "animaltypes/create";
        }

        return "redirect:/animaltypes/all";
    }

    @GetMapping("/update/{id}")
    public String updateGet(Model model, @PathVariable("id") Long id) {
        Optional<AnimalType> optionalAnimalType = animalTypeService.getById(id);
        if (optionalAnimalType.isEmpty()) {
            return "redirect:/animaltypes/all";
        }
        model.addAttribute("animalType", optionalAnimalType.get());
        return "animaltypes/update";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute("animalType") @Valid AnimalType animalType, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "animaltypes/update";
        }
        try {
            animalTypeService.create(animalType);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("name", "error.animalType", e.getMessage());
            return "animaltypes/update";
        }

        return "redirect:/animaltypes/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteGet(Model model, @PathVariable("id") Long id) {
        Optional<AnimalType> optionalAnimalType = animalTypeService.getById(id);
        if(optionalAnimalType.isEmpty()){
            return "redirect:/animaltypes/all";
        }

        model.addAttribute("animalType", optionalAnimalType.get());
        return "animaltypes/delete";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        if(animalTypeService.existsById(id)){
            animalTypeService.delete(id);
        }
        return "redirect:/animaltypes/all";
    }
}
