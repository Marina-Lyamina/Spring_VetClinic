package ru.marinalyamina.vetclinic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.marinalyamina.vetclinic.models.dtos.CreateAnimalScheduleDTO;
import ru.marinalyamina.vetclinic.models.dtos.ScheduleDTO;
import ru.marinalyamina.vetclinic.models.entities.Employee;
import ru.marinalyamina.vetclinic.models.entities.Schedule;
import ru.marinalyamina.vetclinic.repositories.ScheduleRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<ScheduleDTO> getSchedulesForEmployee(Long employeeId) {
        List<Schedule> schedules = scheduleRepository.findByEmployeeId(employeeId);
        return schedules.stream()
                .filter(schedule -> schedule.getAnimal() == null)
                .sorted(Comparator.comparing(Schedule::getDate))
                .map(schedule -> {
            return new ScheduleDTO(
                    schedule.getId(),
                    schedule.getDate(),
                    schedule.getEmployee().getId()
            );
        }).collect(Collectors.toList());
    }

    public Optional<Schedule> getById(Long id) {
        return scheduleRepository.findById(id);
    }

    public void createAnimalSchedule(CreateAnimalScheduleDTO animalScheduleDTO) {
        try {
            scheduleRepository.createAnimalSchedule(animalScheduleDTO.getScheduleId(), animalScheduleDTO.getAnimalId());
        }
        catch (Exception exception){
            var temp = exception;
        }
    }
}
