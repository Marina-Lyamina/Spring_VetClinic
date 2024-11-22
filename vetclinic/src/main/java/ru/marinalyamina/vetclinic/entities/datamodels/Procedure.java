package ru.marinalyamina.vetclinic.entities.datamodels;

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
@Table(name = "procedures")
public class Procedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, nullable = false)
    @NotEmpty(message = "Введите Название")
    @Size(max = 128, message = "Название не должно превышать 128 символов")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Введите стоимость")
    private Integer price;


    @ManyToMany(mappedBy = "procedures")
    @JsonManagedReference
    private List<Appointment> appointments;
}
