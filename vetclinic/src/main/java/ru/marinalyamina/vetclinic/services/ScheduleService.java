package ru.marinalyamina.vetclinic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.marinalyamina.vetclinic.models.dtos.CreateAnimalScheduleDTO;
import ru.marinalyamina.vetclinic.models.dtos.ScheduleDTO;
import ru.marinalyamina.vetclinic.models.entities.Schedule;
import ru.marinalyamina.vetclinic.repositories.ScheduleRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public Optional<Schedule> getById(Long id) {
        return scheduleRepository.findById(id);
    }

    public List<ScheduleDTO> getEmployeeFreeSchedules(Long employeeId) {
        List<Schedule> schedules = scheduleRepository.findByEmployeeId(employeeId);

        return schedules.stream()
                .filter(schedule -> schedule.getAnimal() == null)
                .sorted(Comparator.comparing(Schedule::getDate))
                .map(schedule -> new ScheduleDTO(
                        schedule.getId(),
                        schedule.getDate(),
                        schedule.getEmployee().getId()
                )).collect(Collectors.toList());
    }

    public void createAnimalSchedule(CreateAnimalScheduleDTO animalScheduleDTO) {
        scheduleRepository.createAnimalSchedule(animalScheduleDTO.getScheduleId(), animalScheduleDTO.getAnimalId());
    }

    public Schedule create(Schedule schedule){return scheduleRepository.save(schedule);}

    public boolean existsById(Long id) {
        return scheduleRepository.existsById(id);
    }

    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }

    public void update(Schedule schedule) {
        scheduleRepository.save(schedule);
    }
}
