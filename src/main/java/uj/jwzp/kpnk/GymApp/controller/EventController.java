package uj.jwzp.kpnk.GymApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp.kpnk.GymApp.controller.request.CreateEventWithTemplateRequest;
import uj.jwzp.kpnk.GymApp.controller.request.EventCreateRequest;
import uj.jwzp.kpnk.GymApp.controller.request.EventTemplateCreateRequest;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.service.EventService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    Logger logger = LoggerFactory.getLogger("jsonLogger");

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping
    public List<Event> allEvents(@RequestParam Optional<Integer> clubId, @RequestParam Optional<Integer> coachId) {
        if (clubId.isEmpty() && coachId.isEmpty()) return service.allEvents().stream().toList();
        if (clubId.isPresent()) return service.eventsByClub(clubId.get()).stream().toList();
        return service.eventsByCoach(coachId.get()).stream().toList();
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<?> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        return ResponseEntity.ok(service.findPaginated(pageNumber, pageSize));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getEvent(@PathVariable int id) {
        return ResponseEntity.ok(service.event(id));
    }

    @PostMapping
    public ResponseEntity<?> addEvent(@RequestBody EventCreateRequest request) {
        Event createdEvent = service.addEvent(
                request.title(),
                request.day(),
                request.time(),
                request.duration(),
                request.clubId(),
                request.coachId(),
                request.eventDate()
        );
        logger.info("Created event: {}", createdEvent);
        return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getId())).body(createdEvent);
    }

    // TODO: add creating event by template
    @PostMapping("")
    public ResponseEntity<?> createEventWithTemplate(@RequestBody CreateEventWithTemplateRequest request) {
        Event createdEvent = service.createEventWithTemplate(
                request.templateId(),
                request.eventDate()
        );
        logger.info("Created event: {}", createdEvent);
        return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getId())).body(createdEvent);
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<?> modifyEvent(@PathVariable int id, @RequestBody EventCreateRequest request) {
        var modifiedEvent = service.modifyEvent(
                id,
                request.title(),
                request.day(),
                request.time(),
                request.duration(),
                request.clubId(),
                request.coachId(),
                request.eventDate()
        );
        logger.info("Modified event: {}", modifiedEvent);
        return ResponseEntity.ok(modifiedEvent);
    }

    @GetMapping(params = {"date", "club_id"})
    public ResponseEntity<?> findByDateAndClubId(@RequestParam("date") LocalDate date, @RequestParam("club_id") int clubId) {
        return ResponseEntity.ok(service.eventsByDateAndClubId(date, clubId));
    }

}
