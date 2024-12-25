package ru.marinalyamina.vetclinic.models.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.IOException;
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
    @JsonManagedReference
    private User user;

    @OneToOne
    @JsonBackReference
    private DbFile mainImage;

    @ManyToOne
    @JsonManagedReference
    private Position position;

    @ManyToMany(mappedBy = "employees")
    @JsonBackReference
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "employee")
    @JsonBackReference
    private List<Schedule> schedules;


    public void initFiles() throws IOException {
        if (mainImage != null){
            mainImage.initContent();
        }
    }
}
