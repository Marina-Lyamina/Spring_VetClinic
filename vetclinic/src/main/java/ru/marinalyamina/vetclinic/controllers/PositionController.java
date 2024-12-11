package ru.marinalyamina.vetclinic.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.marinalyamina.vetclinic.models.entities.Position;
import ru.marinalyamina.vetclinic.services.PositionService;

import java.util.Optional;

@Controller
@RequestMapping("/positions")
public class PositionController {
    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping("all")
    public String getAll(Model model) {
        model.addAttribute("positions", positionService.getAll());
        return "positions/all";
    }

    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable("id") Long id) {
        Optional<Position> optionalPosition = positionService.getById(id);
        if(optionalPosition.isEmpty()){
            return "redirect:/positions/all";
        }
        model.addAttribute("position", optionalPosition.get());
        return "positions/details";
    }

    @GetMapping("/create")
    public String createGet(Model model) {
        model.addAttribute("position", new Position());
        return "positions/create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("position") @Valid Position position, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "positions/create";
        }

        try {
            positionService.create(position);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("name", "error.position", e.getMessage());
            return "positions/create";
        }

        return "redirect:/positions/all";
    }

    @GetMapping("/update/{id}")
    public String updateGet(Model model, @PathVariable("id") Long id) {
        Optional<Position> optionalPosition = positionService.getById(id);
        if (optionalPosition.isEmpty()) {
            return "redirect:/positions/all";
        }
        model.addAttribute("position", optionalPosition.get());
        return "positions/update";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute("position") @Valid Position position, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "positions/update";
        }
        try {
            positionService.create(position);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("name", "error.position", e.getMessage());
            return "positions/update";
        }

        return "redirect:/positions/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteGet(Model model, @PathVariable("id") Long id) {
        Optional<Position> optionalPosition = positionService.getById(id);
        if(optionalPosition.isEmpty()){
            return "redirect:/positions/all";
        }

        model.addAttribute("procedure", optionalPosition.get());
        return "positions/delete";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        if(positionService.existsById(id)){
            positionService.delete(id);
        }
        return "redirect:/positions/all";
    }
}
