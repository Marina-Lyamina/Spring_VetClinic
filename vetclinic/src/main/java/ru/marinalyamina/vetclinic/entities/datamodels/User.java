package ru.marinalyamina.vetclinic.entities.datamodels;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, nullable = false)
    @NotEmpty(message = "Введите Фамилию")
    @Size(max = 32, message = "Фамилия не должна превышать 32 символа")
    private String surname;

    @Column(length = 32, nullable = false)
    @NotEmpty(message = "Введите Имя")
    @Size(max = 32, message = "Имя не должно превышать 32 символа")
    private String name;

    @Column(length = 32)
    @Size(max = 32, message = "Отчество не должно превышать 32 символа")
    private String patronymic;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = {"dd.MM.yyyy"})
    private LocalDate birthday;

    @Column(length = 128, unique = true)
    @Email(message = "Некорректный ввод для Email")
    @Size(max = 128, message = "Email не должен превышать 128 символов")
    private String email;

    @Column(length = 15, unique = true)
    @Pattern(regexp = "^[0-9]*$", message = "Номер телефона может включать только цифры")
    @Size(max = 15, message = "Номер телефона не должен превышать 15 цифр")
    private String phone;
}
