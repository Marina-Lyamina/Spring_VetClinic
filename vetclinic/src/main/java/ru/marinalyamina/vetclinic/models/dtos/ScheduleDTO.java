package ru.marinalyamina.vetclinic.models.dtos;

import java.time.LocalDateTime;

public class ScheduleDTO {

    private Long id;
    private LocalDateTime date;
    private Long employeeId;

    public ScheduleDTO(Long id, LocalDateTime date, Long employeeId) {
        this.id = id;
        this.date = date;
        this.employeeId = employeeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

}
