package ru.marinalyamina.vetclinic.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, fallbackPatterns = {"dd.MM.yyyy HH:mm:ss"})
    @NotNull(message = "Введите дату приёма")
    private LocalDateTime date;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String medicalPrescription;


    @ManyToOne
    @JsonBackReference
    private Animal animal;

    @ManyToMany
    @JsonManagedReference
    private List<Employee> employees;

    @ManyToMany
    @JsonManagedReference
    private List<Procedure> procedures;
}
