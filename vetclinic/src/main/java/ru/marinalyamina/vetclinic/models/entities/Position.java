package ru.marinalyamina.vetclinic.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "positions")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, nullable = false)
    @NotEmpty(message = "Введите Название")
    @Size(max = 128, message = "Название не должно превышать 128 символов")
    private String name;


    @OneToMany(mappedBy = "position")
    @JsonManagedReference
    private List<Employee> employees;
}
