package uj.jwzp.kpnk.GymApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp.kpnk.GymApp.controller.request.ClubCreateRequest;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.service.ClubService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    private final ClubService service;

    @Autowired
    public ClubController(ClubService service) {
        this.service = service;
    }

    @GetMapping
    public List<Club> allClubs() {
        return service.allClubs().stream().toList();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> getClub(@PathVariable int id) {
        return ResponseEntity.ok(service.club(id));
    }

    @PostMapping
    public ResponseEntity<?> addClub(@RequestBody ClubCreateRequest request) {
        var createdClub = service.addClub(request.name(), request.address(), request.whenOpen());
        return ResponseEntity.created(URI.create("/api/clubs" + createdClub.id())).body(createdClub);
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<?> modifyClub(@PathVariable int id, @RequestBody ClubCreateRequest request) {
        return ResponseEntity.ok(service.modifyClub(id, request.name(), request.address(), request.whenOpen()));
    }

    @DeleteMapping(path = "{id}")
    public void removeClub(@PathVariable int id) {
        service.removeClub(id);
    }
}
