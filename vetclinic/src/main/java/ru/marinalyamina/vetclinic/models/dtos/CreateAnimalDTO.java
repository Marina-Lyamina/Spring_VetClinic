package ru.marinalyamina.vetclinic.models.dtos;

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
    private String name;
    private LocalDate birthday;
    private AnimalGender gender;
    private String breed;
    private Long animalTypeId;
}
