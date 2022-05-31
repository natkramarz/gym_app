package uj.jwzp.kpnk.GymApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.jwzp.kpnk.GymApp.controller.request.RegistrationCreateRequest;
import uj.jwzp.kpnk.GymApp.model.Registration;
import uj.jwzp.kpnk.GymApp.service.RegistrationService;

import java.net.URI;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService service;

    @Autowired
    public RegistrationController(RegistrationService service) {
        this.service = service;
    }

    public ResponseEntity<?> registerUserForEvent(@RequestBody RegistrationCreateRequest request) {
        Registration createdRegistration = service.addRegistration(
                request.eventId(),
                request.name(),
                request.surname()
        );
        return ResponseEntity.created(URI.create("/api/registrations/" + createdRegistration.getId())).body(createdRegistration);
    }


}
