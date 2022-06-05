package uj.jwzp.kpnk.GymApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp.kpnk.GymApp.controller.request.ClubCreateRequest;
import uj.jwzp.kpnk.GymApp.dto.ClubRepresentation;
import uj.jwzp.kpnk.GymApp.dto.assembler.ClubRepresentationAssembler;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.service.ClubService;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/clubs")
public class ClubController {

    Logger logger = LoggerFactory.getLogger("jsonLogger");

    private final ClubService service;
    private final ClubRepresentationAssembler clubRepresentationAssembler;

    @Autowired
    public ClubController(ClubService service, ClubRepresentationAssembler clubRepresentationAssembler) {
        this.service = service;
        this.clubRepresentationAssembler = clubRepresentationAssembler;
    }

    @GetMapping
    public CollectionModel<ClubRepresentation> allClubs() {
        return clubRepresentationAssembler.toCollectionModel(service.allClubs().stream().toList());
    }

    @GetMapping(path = "{id}")
    public ClubRepresentation getClub(@PathVariable int id) {
        Club club = service.club(id);
        return clubRepresentationAssembler.toModel(club)
                .add(linkTo(methodOn(ClubController.class).allClubs()).withRel("clubs"));
    }

    @GetMapping(params = {"page", "size"})
    public Page<Club> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        return service.findPaginated(pageNumber, pageSize);
    }

    @PostMapping
    public ResponseEntity<?> addClub(@RequestBody ClubCreateRequest request) {
        var createdClub = service.addClub(request.name(), request.address(), request.whenOpen());
        logger.info("Created club: {}", createdClub);
        return ResponseEntity.created(URI.create("/api/v1/clubs" + createdClub.getId())).body(createdClub);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> modifyClub(@PathVariable int id, @RequestBody ClubCreateRequest request) {
        var modifiedClub = service.modifyClub(id, request.name(), request.address(), request.whenOpen());
        logger.info("Modified club: {}", modifiedClub);
        return ResponseEntity.ok(modifiedClub);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> removeClub(@PathVariable int id) {
        service.deleteClub(id);
        return ResponseEntity.noContent().build();
    }
}
