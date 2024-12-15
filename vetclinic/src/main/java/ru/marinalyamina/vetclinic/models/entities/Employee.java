package ru.marinalyamina.vetclinic.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne
    //@JsonBackReference
    @JsonManagedReference
    private User user;

    @OneToOne
    @JsonBackReference
    private DbFile mainImage;

    @ManyToOne
    //@JsonBackReference
    @JsonIgnore
    private Position position;

    @ManyToMany(mappedBy = "employees")
    //@JsonManagedReference
    @JsonBackReference
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "employee")
    //@JsonManagedReference
    @JsonIgnore
    private List<Schedule> schedules;
}
