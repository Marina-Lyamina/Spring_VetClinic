package ru.marinalyamina.vetclinic.apicontrollers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.marinalyamina.vetclinic.models.dtos.CreateAnimalScheduleDTO;
import ru.marinalyamina.vetclinic.models.entities.Schedule;
import ru.marinalyamina.vetclinic.services.ScheduleService;

import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleApiController {

    final private ScheduleService scheduleService;

    public ScheduleApiController(ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id) {
        Optional<Schedule> scheduleOptional = scheduleService.getById(id);

        if (scheduleOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(scheduleOptional.get());
    }

    @PostMapping("/animals")
    public ResponseEntity<Long> createAnimalSchedule(@Valid @RequestBody CreateAnimalScheduleDTO animalScheduleDTO) {
        Optional<Schedule> scheduleOptional = scheduleService.getById(animalScheduleDTO.getScheduleId());

        if (scheduleOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        scheduleService.createAnimalSchedule(animalScheduleDTO);

        return ResponseEntity.ok(scheduleOptional.get().getId());
    }
}


