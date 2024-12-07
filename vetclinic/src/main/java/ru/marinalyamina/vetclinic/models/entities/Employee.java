package ru.marinalyamina.vetclinic.models.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JsonBackReference
    private User user;

    @ManyToOne
    @JsonBackReference
    private Position position;

    @ManyToMany(mappedBy = "employees")
    @JsonManagedReference
    private List<Appointment> appointments;
}
