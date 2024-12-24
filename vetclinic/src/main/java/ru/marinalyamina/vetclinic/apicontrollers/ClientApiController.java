package ru.marinalyamina.vetclinic.apicontrollers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.marinalyamina.vetclinic.models.dtos.LoginDTO;
import ru.marinalyamina.vetclinic.models.entities.Animal;
import ru.marinalyamina.vetclinic.models.entities.Client;
import ru.marinalyamina.vetclinic.services.AnimalService;
import ru.marinalyamina.vetclinic.services.ClientService;
import ru.marinalyamina.vetclinic.utils.CurrentUser;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientApiController {

    private final ClientService clientService;
    public ClientApiController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/login")
    public ResponseEntity<Long> login(@Valid @RequestBody LoginDTO loginDTO) {
        Optional<Client> clientOptional = clientService.getByLogin(loginDTO.getLogin());

        if(clientOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if(!Objects.equals(loginDTO.getPassword(), clientOptional.get().getUser().getPassword())){
            return ResponseEntity.badRequest().build();
        }

        CurrentUser.id = clientOptional.get().getId();

        return ResponseEntity.ok(CurrentUser.id);
    }

    @PostMapping("/registration")
    public void registration(@Valid @RequestBody Client client) {
        //create user create client
    }
}
