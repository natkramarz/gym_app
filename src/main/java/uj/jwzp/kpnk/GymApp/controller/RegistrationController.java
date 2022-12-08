package uj.jwzp.kpnk.GymApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp.kpnk.GymApp.controller.request.RegistrationCreateRequest;
import uj.jwzp.kpnk.GymApp.model.Registration;
import uj.jwzp.kpnk.GymApp.service.RegistrationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService service;

    @Autowired
    public RegistrationController(RegistrationService service) {
        this.service = service;
    }


    @GetMapping
    public List<Registration> getAllRegistrations() {
        return service.getAll();
    }

    @GetMapping(path = "{id}")
    public Registration getRegistration(@PathVariable int id) {
        return service.get(id);
    }


    @PostMapping
    public ResponseEntity<?> registerUserForEvent(@RequestBody RegistrationCreateRequest request) {
        Registration createdRegistration = service.add(request);
        return ResponseEntity.created(URI.create("/api/registrations/" + createdRegistration.getId())).body(createdRegistration);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRegistrationForEvent(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
