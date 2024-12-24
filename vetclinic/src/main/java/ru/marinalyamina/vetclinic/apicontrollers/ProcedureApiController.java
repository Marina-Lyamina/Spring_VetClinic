package ru.marinalyamina.vetclinic.apicontrollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.marinalyamina.vetclinic.models.entities.Procedure;
import ru.marinalyamina.vetclinic.services.ProcedureService;

import java.util.List;

@RestController
@RequestMapping("/api/procedures")
public class ProcedureApiController {
    private final ProcedureService procedureService;

    public ProcedureApiController(ProcedureService procedureService) {
        this.procedureService = procedureService;
    }

    @GetMapping()
    public List<Procedure> getAllProcedures() {
        return procedureService.getAll();
    }
}
