package ru.marinalyamina.vetclinic.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateAppointmentDTO {

    private Long id;

    private String reason;

    private String diagnosis;

    private String medicalPrescription;
}
