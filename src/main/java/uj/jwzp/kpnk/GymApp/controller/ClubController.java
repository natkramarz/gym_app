package uj.jwzp.kpnk.GymApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp.kpnk.GymApp.controller.request.ClubCreateRequest;
import uj.jwzp.kpnk.GymApp.dto.ClubRepresentation;
import uj.jwzp.kpnk.GymApp.dto.assembler.ClubRepresentationAssembler;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.service.ClubService;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    private final ClubService service;
    private final ClubRepresentationAssembler clubRepresentationAssembler;

    @Autowired
    public ClubController(ClubService service, ClubRepresentationAssembler clubRepresentationAssembler) {
        this.service = service;
        this.clubRepresentationAssembler = clubRepresentationAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<ClubRepresentation>> allClubs() {
        return ResponseEntity.ok(clubRepresentationAssembler.toCollectionModel(service.allClubs().stream().toList()));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> getClub(@PathVariable int id) {
        Club club = service.club(id);
        ClubRepresentation clubRepresentation = clubRepresentationAssembler.toModel(club)
                .add(linkTo(methodOn(ClubController.class).allClubs()).withRel("clubs"));
        return ResponseEntity.ok(
                clubRepresentation
        );
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<?> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        return ResponseEntity.ok(service.findPaginated(pageNumber, pageSize));
    }

    @PostMapping
    public ResponseEntity<?> addClub(@RequestBody ClubCreateRequest request) {
        var createdClub = service.addClub(request.name(), request.address(), request.whenOpen());
        return ResponseEntity.created(URI.create("/api/clubs" + createdClub.getId())).body(createdClub);
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
