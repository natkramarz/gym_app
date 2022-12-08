package uj.jwzp.kpnk.GymApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp.kpnk.GymApp.controller.request.CoachCreateRequest;
import uj.jwzp.kpnk.GymApp.model.Coach;
import uj.jwzp.kpnk.GymApp.service.CoachService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/coaches")
public class CoachController {

    private final CoachService service;
    Logger logger = LoggerFactory.getLogger("jsonLogger");

    @Autowired
    public CoachController(CoachService service) {
        this.service = service;
    }

    @GetMapping
    public List<Coach> allCoaches() {
        return service.getAll().stream().toList();
    }

    @GetMapping(path = "{id}")
    public Coach getCoach(@PathVariable int id) {
        return service.get(id);
    }

    @GetMapping(params = {"page", "size"})
    public Page<Coach> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        return service.findPaginated(pageNumber, pageSize);
    }

    @PostMapping()
    public ResponseEntity<?> addCoach(@RequestBody CoachCreateRequest request) {
        var createdCoach = service.add(request);
        logger.info("Created coach: {}", createdCoach);
        return ResponseEntity.created(URI.create("/api/v1/coaches/" + createdCoach.getId())).body(createdCoach);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> modifyCoach(@PathVariable int id, @RequestBody CoachCreateRequest request) {
        var modifiedCoach = service.modify(id, request);
        logger.info("Modified coach: {}", modifiedCoach);
        return ResponseEntity.ok(modifiedCoach);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> removeCoach(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
