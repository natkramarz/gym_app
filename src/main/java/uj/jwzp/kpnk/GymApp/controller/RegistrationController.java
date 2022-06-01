package uj.jwzp.kpnk.GymApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp.kpnk.GymApp.controller.request.RegistrationCreateRequest;
import uj.jwzp.kpnk.GymApp.model.Registration;
import uj.jwzp.kpnk.GymApp.service.RegistrationService;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService service;

    @Autowired
    public RegistrationController(RegistrationService service) {
        this.service = service;
    }


    @GetMapping
    public Set<Registration> getAllRegistrations() {
        return service.allRegistrations();
    }

    @GetMapping(path = "{id}")
    public Registration getRegistration(@PathVariable int id) {
        return service.registration(id);
    }


    @PostMapping
    public ResponseEntity<?> registerUserForEvent(@RequestBody RegistrationCreateRequest request) {
        Registration createdRegistration = service.createRegistration(
                request.eventId(),
                request.name(),
                request.surname()
        );
        return ResponseEntity.created(URI.create("/api/registrations/" + createdRegistration.getId())).body(createdRegistration);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRegistrationForEvent(@PathVariable int id) {
        service.deleteRegistration(id);
        return ResponseEntity.noContent().build();
    }


}
