package ru.marinalyamina.vetclinic.entities.datamodels;

import ru.marinalyamina.vetclinic.entities.enums.AnimalGender;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    @NotEmpty(message = "Введите Имя")
    @Size(max = 64, message = "Имя не должно превышать 64 символа")
    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = {"dd.MM.yyyy"})
    private LocalDate birthday;

    private AnimalGender gender;

    @Column(length = 64)
    @Size(max = 64, message = "Порода не должна превышать 64 символа")
    private String breed;


    @ManyToOne
    @JsonBackReference
    private AnimalType animalType;

    @ManyToOne
    @JsonBackReference
    private Client client;

    @OneToMany(mappedBy = "animal")
    @JsonManagedReference
    private List<Appointment> appointments;
}
