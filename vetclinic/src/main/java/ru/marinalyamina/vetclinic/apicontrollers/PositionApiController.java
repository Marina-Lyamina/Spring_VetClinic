package ru.marinalyamina.vetclinic.apicontrollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.marinalyamina.vetclinic.models.entities.Position;
import ru.marinalyamina.vetclinic.services.PositionService;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
public class PositionApiController {
    private final PositionService positionService;

    public PositionApiController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping()
    public List<Position> getAllPositions() {
        return positionService.getAll();
    }
}
