package ru.marinalyamina.vetclinic.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "procedures")
public class Procedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, unique = true, nullable = false)
    @NotEmpty(message = "Введите название")
    @Size(max = 128, message = "Название не должно превышать 128 символов")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Введите стоимость")
    private Integer price;

    @ManyToMany(mappedBy = "procedures")
    @JsonBackReference
    private List<Appointment> appointments;
}
