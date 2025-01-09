package ru.marinalyamina.vetclinic.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.marinalyamina.vetclinic.models.dtos.*;
import ru.marinalyamina.vetclinic.models.entities.*;
import ru.marinalyamina.vetclinic.services.*;
import ru.marinalyamina.vetclinic.utils.FileManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final ProcedureService procedureService;
    private final AnimalService animalService;
    private final EmployeeService employeeService;
    private final FileService fileService;

    public AppointmentController(AppointmentService appointmentService, ProcedureService procedureService, AnimalService animalService, EmployeeService employeeService, FileService fileService){
        this.appointmentService = appointmentService;
        this.procedureService = procedureService;
        this.animalService = animalService;
        this.employeeService = employeeService;
        this.fileService = fileService;
    }

    @GetMapping("/details/{id}")
    public String getAppointmentDetails(@PathVariable Long id, Model model) {
        Optional<Appointment> optionalAppointment = appointmentService.getById(id);
        if (optionalAppointment.isEmpty()) {
            return "redirect:/";
        }

        Appointment appointment = optionalAppointment.get();
        model.addAttribute("appointment", appointment);

        try{
            if(appointment.getFiles() != null){
                var filesPhoto = new ArrayList<>();
                for (DbFile file : appointment.getFiles()) {
                    filesPhoto.add(FileManager.getFile(file.getName()));
                }
                model.addAttribute("filesPhoto", filesPhoto);
            }
        }
        catch(Exception e){
            return "redirect:/animals/details/" + appointment.getAnimal().getId();
        }

        return "appointments/details";
    }

    @GetMapping("/deleteProcedure")
    public String deleteProcedure(@RequestParam Long appointmentId, @RequestParam Long procedureId){
        Optional<Appointment> optionalAppointment = appointmentService.getById(appointmentId);
        if (optionalAppointment.isEmpty()) {
            return "redirect:/";
        }

        Appointment appointment = optionalAppointment.get();

        if (!appointment.getProcedures().stream().anyMatch(procedure -> procedure.getId().equals(procedureId))) {
            return "redirect:/appointments/details/" + appointmentId;
        }

        appointment.getProcedures().removeIf(procedure -> procedure.getId().equals(procedureId));

        appointmentService.update(appointment);

        return "redirect:/appointments/details/" + appointmentId;
    }

    @GetMapping("/addProcedure/{id}")
    public String addProcedureGet(@PathVariable Long id, Model model) {
        Optional<Appointment> optionalAppointment = appointmentService.getById(id);
        if (optionalAppointment.isEmpty()) {
            return "redirect:/";
        }

        var appointmentProcedure = new AppointmentProcedureDTO();
        appointmentProcedure.setAppointmentId(id);
        model.addAttribute("appointmentProcedure", appointmentProcedure);
        model.addAttribute("procedures", procedureService.getAll());


        return "appointments/addProcedure";
    }

    @PostMapping("/addProcedure")
    public String addProcedurePost(@ModelAttribute("appointmentProcedure") @Valid AppointmentProcedureDTO appointmentProcedureDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("procedures", procedureService.getAll());
            return "appointments/addProcedure";
        }

        Optional<Appointment> optionalAppointment = appointmentService.getById(appointmentProcedureDTO.getAppointmentId());
        if (optionalAppointment.isEmpty()) {
            return "redirect:/";
        }
        Appointment appointment = optionalAppointment.get();

        Optional<Procedure> optionalProcedure = procedureService.getById(appointmentProcedureDTO.getProcedureId());
        if (optionalProcedure.isEmpty()) {
            return "redirect:/";
        }

        Procedure procedure = optionalProcedure.get();

        if (appointment.getProcedures().stream().anyMatch(pr -> pr.getId().equals(procedure.getId()))) {
            return "redirect:/appointments/details/" + appointment.getId();
        }

        appointment.getProcedures().add(procedure);
        appointmentService.update(appointment);

        return "redirect:/appointments/details/" + appointment.getId();
    }

    @GetMapping("/update/{id}")
    public String updateGet(@PathVariable("id") Long id, Model model) {
        Optional<Appointment> optionalAppointment = appointmentService.getById(id);
        if (optionalAppointment.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("appointment", optionalAppointment.get());
        return "appointments/update";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute("appointment") @Valid UpdateAppointmentDTO appointmentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "appointments/update";
        }

        Optional<Appointment> existingAppointmentOpt = appointmentService.getById(appointmentDTO.getId());
        if (existingAppointmentOpt.isEmpty()) {
            return "redirect:/";
        }

        Appointment appointment = existingAppointmentOpt.get();

        appointment.setReason(appointmentDTO.getReason());
        appointment.setDiagnosis(appointmentDTO.getDiagnosis());
        appointment.setMedicalPrescription(appointmentDTO.getMedicalPrescription());

        appointmentService.update(appointment);

        return "redirect:/appointments/details/" + appointment.getId();
    }

    @GetMapping("/create")
    public String createGet(@RequestParam Long animalId, Model model) {
        Optional<Animal> optionalAnimal = animalService.getById(animalId);
        if (optionalAnimal.isEmpty()) {
            return "redirect:/";
        }

        var chooseEmployees = new CreateAppointmentChooseEmployeesDTO();
        chooseEmployees.setAnimalId(animalId);

        model.addAttribute("chooseEmployees", chooseEmployees);
        model.addAttribute("employees", employeeService.getAll());

        return "appointments/createChooseEmployees";
    }

    @PostMapping("/createChooseEmployees")
    public String createChooseEmployees(@ModelAttribute("chooseEmployees") @Valid CreateAppointmentChooseEmployeesDTO chooseEmployees, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employees", employeeService.getAll());
            return "appointments/createChooseEmployees";
        }

        var employees = employeeService.getAll().stream()
                .filter(employee -> chooseEmployees.getEmployeeIds().contains(employee.getId()))
                .toList();

        Appointment appointment = new Appointment();

        appointment.setEmployees(employees);
        appointment.setAnimal(animalService.getById(chooseEmployees.getAnimalId()).get());
        appointment.setDate(LocalDateTime.now());

        model.addAttribute("appointment", appointment);

        return "appointments/create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("appointment") @Valid Appointment appointment, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "appointments/create";
        }

        var newAppointment = appointmentService.create(appointment);

        return "redirect:/appointments/details/" + newAppointment.getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteGet(Model model, @PathVariable("id") Long id) {
        Optional<Appointment> optionalAppointment = appointmentService.getById(id);
        if(optionalAppointment.isEmpty()){
            return "redirect:/";
        }
        Appointment appointment = optionalAppointment.get();

        model.addAttribute("appointment", appointment);

        return "appointments/delete";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, Model model) {
        Optional<Appointment> optionalAppointment = appointmentService.getById(id);
        if(optionalAppointment.isEmpty()){
            return "redirect:/";
        }

        Appointment appointment = optionalAppointment.get();

        appointmentService.delete(appointment.getId());

        return "redirect:/animals/details/" + appointment.getAnimal().getId();
    }

    @GetMapping("/addPhoto/{id}")
    public String addPhotoGet(@PathVariable("id") Long id, Model model) {
        Optional<Appointment> optionalAppointment = appointmentService.getById(id);
        if (optionalAppointment.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("appointmentId", id);

        return "appointments/addPhoto";
    }

    @PostMapping("/addPhoto/{id}")
    public String addPhotoPost(Model model, @PathVariable("id") Long id, @RequestParam("image") MultipartFile file) {
        Optional<Appointment> optionalAppointment = appointmentService.getById(id);
        if (optionalAppointment.isEmpty()) {
            return "redirect:/";
        }

        Appointment appointment = optionalAppointment.get();

        DbFile dbFile = new DbFile();


        String fileName = file.getOriginalFilename();
        String extension = ".jpg";
        int index = fileName.lastIndexOf('.');
        if (index > 0 && index < fileName.length() - 1) {
            extension = fileName.substring(index + 1);
        }

        dbFile.setName(FileManager.createFileName(extension));
        dbFile.setDate(LocalDateTime.now());

        try {
            FileManager.saveFile(dbFile.getName(), file.getBytes());
        } catch (IOException e) {
            return "redirect:/appointments/details/" + id;
        }

        fileService.update(dbFile);

        appointment.getFiles().add(dbFile);
        appointmentService.update(appointment);

        return "redirect:/appointments/details/" + id;
    }

}
