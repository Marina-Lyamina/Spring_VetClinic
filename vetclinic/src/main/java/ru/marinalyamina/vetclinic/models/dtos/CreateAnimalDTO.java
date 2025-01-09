package ru.marinalyamina.vetclinic.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.marinalyamina.vetclinic.models.enums.AnimalGender;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateAnimalDTO {

    @NotEmpty(message = "Введите кличку")
    @Size(max = 64, message = "Слишком длинная кличка")
    private String name;

    private LocalDate birthday;

    private AnimalGender gender;

    @Size(max = 64, message = "Слишком длинное название породы")
    private String breed;

    private Long animalTypeId;
}
