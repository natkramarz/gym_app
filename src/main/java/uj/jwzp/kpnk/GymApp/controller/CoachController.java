package uj.jwzp.kpnk.GymApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp.kpnk.GymApp.controller.request.CoachCreateRequest;
import uj.jwzp.kpnk.GymApp.model.Coach;
import uj.jwzp.kpnk.GymApp.service.CoachService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/coaches")
public class CoachController {

    Logger logger = LoggerFactory.getLogger("jsonLogger");

    private final CoachService service;

    @Autowired
    public CoachController(CoachService service) {
        this.service = service;
    }

    @GetMapping
    public List<Coach> allCoaches() {
        return service.allCoaches().stream().toList();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> getCoach(@PathVariable int id) {
        return ResponseEntity.ok(service.coach(id));
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<?> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        return ResponseEntity.ok(service.findPaginated(pageNumber, pageSize));
    }

    @PostMapping
    public ResponseEntity<?> addCoach(@RequestBody CoachCreateRequest request) {
        Coach createdCoach = service.addCoach(request.firstName(), request.lastName(), request.yearOfBirth());
        logger.info("Created coach: {}", createdCoach);
        return ResponseEntity.created(URI.create("/api/coaches/" + createdCoach.getId())).body(createdCoach);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> modifyCoach(@PathVariable int id, @RequestBody CoachCreateRequest request) {
        var modifiedCoach = service.modifyCoach(id, request.firstName(), request.lastName(), request.yearOfBirth());
        logger.info("Modified coach: {}", modifiedCoach);
        return ResponseEntity.ok(modifiedCoach);
    }

    @DeleteMapping(path = "{id}")
    public void removeCoach(@PathVariable int id) {
        service.removeCoach(id);
    }


}
