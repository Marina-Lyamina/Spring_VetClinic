package ru.marinalyamina.vetclinic.controllers;

import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.marinalyamina.vetclinic.models.dtos.UpdateClientDTO;
import ru.marinalyamina.vetclinic.models.dtos.UpdateUserDTO;
import ru.marinalyamina.vetclinic.models.entities.Client;
import ru.marinalyamina.vetclinic.models.entities.User;
import ru.marinalyamina.vetclinic.models.enums.Role;
import ru.marinalyamina.vetclinic.services.ClientService;
import ru.marinalyamina.vetclinic.services.UserService;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;
    private final UserService userService;
    private final PasswordEncoder  passwordEncoder;

    public ClientController(ClientService clientService, UserService userService, PasswordEncoder passwordEncoder){
        this.clientService = clientService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("clients", clientService.getAll());
        return "clients/all";
    }

    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable("id") Long id) {
        Optional<Client> optionalClient = clientService.getById(id);
        if(optionalClient.isEmpty()){
            return "redirect:/clients/all";
        }
        model.addAttribute("client", optionalClient.get());
        return "clients/details";
    }

    @GetMapping("/create")
    public String createGet(Model model) {
        model.addAttribute("client", new Client());
        return "clients/create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("client") @Valid Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "clients/create";
        }

        User user = client.getUser();
        if (userService.existsByPhone(user.getPhone())) {
            bindingResult.rejectValue("user.phone", "error.client", "Такой телефон уже использован");
        }
        if (userService.existsByEmail(user.getEmail())) {
            bindingResult.rejectValue("user.email", "error.client", "Такой email уже использован");
        }
        if (userService.existsByUsername(user.getUsername())) {
            bindingResult.rejectValue("user.username", "error.client", "Придумайте другой логин");
        }

        if (bindingResult.hasErrors()) {
            return "clients/create";
        }

        var encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user.setRole(Role.ROLE_USER);

        userService.create(user);
        clientService.create(client);

        return "redirect:/clients/all";
    }

    @GetMapping("/update/{id}")
    public String updateGet(Model model, @PathVariable("id") Long id) {
        Optional<Client> optionalClient = clientService.getById(id);
        if (optionalClient.isEmpty()) {
            return "redirect:/clients/all";
        }
        model.addAttribute("client", optionalClient.get());
        return "clients/update";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute("client") @Valid UpdateClientDTO clientDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "clients/update";
        }

        Optional<Client> existingClientOpt = clientService.getById(clientDTO.getId());
        if (existingClientOpt.isEmpty()) {
            return "redirect:/clients/all";
        }

        Client existingClient = existingClientOpt.get();
        User user = existingClient.getUser();

        if (!Objects.equals(user.getPhone(), clientDTO.getUser().getPhone()) && userService.existsByPhone(clientDTO.getUser().getPhone())) {
            bindingResult.rejectValue("user.phone", "error.client", "Такой телефон уже использован");
        }
        if (!Objects.equals(user.getEmail(), clientDTO.getUser().getEmail()) && userService.existsByEmail(clientDTO.getUser().getEmail())) {
            bindingResult.rejectValue("user.email", "error.client", "Такой email уже использован");
        }
        if (!Objects.equals(user.getUsername(), clientDTO.getUser().getUsername()) && userService.existsByUsername(clientDTO.getUser().getUsername())) {
            bindingResult.rejectValue("user.username", "error.client", "Придумайте другой логин");
        }

        if (bindingResult.hasErrors()) {
            return "clients/update";
        }

        UpdateUserDTO updatedUserDTO = clientDTO.getUser();

        user.setSurname(updatedUserDTO.getSurname());
        user.setName(updatedUserDTO.getName());
        user.setPatronymic(updatedUserDTO.getPatronymic());
        user.setBirthday(updatedUserDTO.getBirthday());
        user.setEmail(updatedUserDTO.getEmail());
        user.setPhone(updatedUserDTO.getPhone());
        user.setUsername(updatedUserDTO.getUsername());

        userService.update(user);
        clientService.update(existingClient);

        return "redirect:/clients/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteGet(Model model, @PathVariable("id") Long id) {
        Optional<Client> optionalClient = clientService.getById(id);
        if(optionalClient.isEmpty()){
            return "redirect:/clients/all";
        }
        Client client = optionalClient.get();

        model.addAttribute("client", client);
        model.addAttribute("hasPets", client.getAnimals() != null && !client.getAnimals().isEmpty());

        return "clients/delete";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, Model model) {
        Optional<Client> optionalClient = clientService.getById(id);
        if(optionalClient.isEmpty()) {
            return "redirect:/clients/all";
        }

        Client clientToDelete = optionalClient.get();

        if (clientToDelete.getAnimals() != null && !clientToDelete.getAnimals().isEmpty()) {
            model.addAttribute("client", clientToDelete);
            model.addAttribute("hasPets", true);
            return "clients/delete";
        }

        clientService.delete(clientToDelete.getId());
        return "redirect:/clients/all";
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> generateClientReportSeparatedFIO() {
        List<Client> clients = clientService.getAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Клиенты");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"#", "Фамилия", "Имя", "Отчество", "Дата рождения", "Номер телефона", "Email"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            int rowIndex = 1;
            for (Client client : clients) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rowIndex - 1); // Номер
                if (client.getUser() != null) {
                    row.createCell(1).setCellValue(client.getUser().getSurname() != null ? client.getUser().getSurname() : "");
                    row.createCell(2).setCellValue(client.getUser().getName() != null ? client.getUser().getName() : "");
                    row.createCell(3).setCellValue(client.getUser().getPatronymic() != null ? client.getUser().getPatronymic() : "");
                    row.createCell(4).setCellValue(client.getUser().getBirthday() != null ?
                            client.getUser().getBirthday().toString() : "");
                    row.createCell(5).setCellValue(client.getUser().getPhone() != null ? client.getUser().getPhone() : "");
                    row.createCell(6).setCellValue(client.getUser().getEmail() != null ? client.getUser().getEmail() : "");
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clients_report.xlsx")
                    .body(outputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }


}
