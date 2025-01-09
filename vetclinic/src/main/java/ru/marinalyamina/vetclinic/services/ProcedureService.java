package ru.marinalyamina.vetclinic.services;

import org.springframework.stereotype.Service;

import ru.marinalyamina.vetclinic.models.entities.Procedure;
import ru.marinalyamina.vetclinic.repositories.ProcedureRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProcedureService {
    private final ProcedureRepository procedureRepository;

    public ProcedureService(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    public List<Procedure> getAll() { return procedureRepository.findAll();}

    public Optional<Procedure> getById(Long id) {
        return procedureRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return procedureRepository.existsById(id);
    }

    public Procedure create(Procedure procedure) {
        return procedureRepository.save(procedure);
    }

    public void delete(Long id) {
        procedureRepository.deleteById(id);
    }
}
