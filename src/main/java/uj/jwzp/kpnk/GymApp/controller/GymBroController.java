package uj.jwzp.kpnk.GymApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp.kpnk.GymApp.controller.request.GymBroCreateRequest;
import uj.jwzp.kpnk.GymApp.model.GymBro;
import uj.jwzp.kpnk.GymApp.service.GymBroService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gym_bros")
public class GymBroController {
    private final GymBroService service;
    Logger logger = LoggerFactory.getLogger("jsonLogger");

    @Autowired
    public GymBroController(GymBroService service) {
        this.service = service;
    }

    @GetMapping
    public List<GymBro> allGymBros() {
        return service.getAll().stream().toList();
    }

    @GetMapping(path = "{id}")
    public GymBro getGymBro(@PathVariable int id) {
        return service.get(id);
    }

    @PostMapping()
    public ResponseEntity<?> addGymBro(@RequestBody GymBroCreateRequest request) {
        var createdCoach = service.add(request);
        logger.info("Created gym bro: {}", createdCoach);
        return ResponseEntity.created(URI.create("/api/v1/coaches/" + createdCoach.getId())).body(createdCoach);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> modifyGymBro(@PathVariable int id, @RequestBody GymBroCreateRequest request) {
        var modifiedCoach = service.modify(id, request);
        logger.info("Modified gym bro: {}", modifiedCoach);
        return ResponseEntity.ok(modifiedCoach);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteGymBro(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
