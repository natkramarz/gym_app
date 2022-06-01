package uj.jwzp.kpnk.GymApp.controller;

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
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/coaches")
public class CoachController {

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
    public Coach getCoach(@PathVariable int id) {
        return service.coach(id);
    }

    @GetMapping(params = {"page", "size"})
    public Page<Coach> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        return service.findPaginated(pageNumber, pageSize);
    }

    @PostMapping("/template")
    public ResponseEntity<?> addCoach(@RequestBody CoachCreateRequest request) {
        Coach createdCoach = service.addCoach(request.firstName(), request.lastName(), request.yearOfBirth());
        return ResponseEntity.created(URI.create("/api/v1/coaches/" + createdCoach.getId())).body(createdCoach);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> modifyCoach(@PathVariable int id, @RequestBody CoachCreateRequest request) {
        return ResponseEntity.ok(service.modifyCoach(id, request.firstName(), request.lastName(), request.yearOfBirth()));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> removeCoach(@PathVariable int id) {
        service.removeCoach(id);
        return ResponseEntity.noContent().build();
    }


}
