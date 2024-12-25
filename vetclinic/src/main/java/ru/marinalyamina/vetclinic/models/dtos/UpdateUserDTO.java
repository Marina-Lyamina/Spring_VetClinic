package ru.marinalyamina.vetclinic.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserDTO {

    @NotEmpty(message = "Введите Фамилию")
    @Size(max = 32, message = "Фамилия не должна превышать 32 символа")
    private String surname;

    @NotEmpty(message = "Введите Имя")
    @Size(max = 32, message = "Имя не должно превышать 32 символа")
    private String name;

    @Size(max = 32, message = "Отчество не должно превышать 32 символа")
    private String patronymic;

    private LocalDate birthday;

    @Email(message = "Некорректный ввод для Email")
    @Size(max = 128, message = "Email не должен превышать 128 символов")
    private String email;

    @Pattern(regexp = "^[0-9]*$", message = "Номер телефона может включать только цифры")
    @Size(max = 15, message = "Номер телефона не должен превышать 15 цифр")
    private String phone;

    @NotEmpty(message = "Введите Логин")
    @Size(max = 32, message = "Логин не должен превышать 32 символа")
    private String username;
}
