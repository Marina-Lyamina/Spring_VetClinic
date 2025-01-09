package ru.marinalyamina.vetclinic.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateAppointmentChooseEmployeesDTO {
    @NotNull(message = "Выберите питомца")
    private Long animalId;

    @NotEmpty(message = "Выберите сотрудников")
    private List<Long> employeeIds;
}
