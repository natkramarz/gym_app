package uj.jwzp.kpnk.GymApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp.kpnk.GymApp.controller.request.EventCreateRequest;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.service.ServiceProxy.EventServiceProxyImp;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventServiceProxyImp service;
    Logger logger = LoggerFactory.getLogger("jsonLogger");

    @Autowired
    public EventController(EventServiceProxyImp service) {
        this.service = service;
    }

    @GetMapping
    public List<Event> allEvents(@RequestParam Optional<Integer> clubId, @RequestParam Optional<Integer> coachId) {
        if (clubId.isEmpty() && coachId.isEmpty()) {
            return service.getAll().stream().toList();
        }
        if (clubId.isPresent()) {
            return service.getService().eventsByClub(clubId.get()).stream().toList();
        }
        return service.getService().eventsByCoach(coachId.get()).stream().toList();
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<?> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        return ResponseEntity.ok(service.getService().findPaginated(pageNumber, pageSize));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getEvent(@PathVariable int id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping
    public ResponseEntity<?> addEvent(@RequestBody EventCreateRequest request) {
        var createdEvent = service.add(request);
        logger.info("Created event: {}", createdEvent);
        return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getId())).body(createdEvent);
    }

    @PostMapping(params = {"template", "date"})
    public ResponseEntity<?> createEventWithTemplate(@RequestParam("template") int templateId, @RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate eventDate) {
        var createdEvent = service.getService().createEventWithTemplate(templateId, eventDate);
        logger.info("Created event: {}", createdEvent);
        return ResponseEntity.created(URI.create("/api/v1/events/" + createdEvent.getId())).body(createdEvent);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> modifyEvent(@PathVariable int id, @RequestBody EventCreateRequest request) {
        var modifiedEvent = service.modify(
                id,
                request
        );
        logger.info("Modified event: {}", modifiedEvent);
        return ResponseEntity.ok(modifiedEvent);
    }

    @GetMapping(params = {"date", "club_id"})
    public ResponseEntity<?> findByDateAndClubId(@RequestParam("date") LocalDate date, @RequestParam("club_id") int clubId) {
        return ResponseEntity.ok(service.getService().eventsByDateAndClubId(date, clubId));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> cancelEvent(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(params = {"id", "date", "startTime"})
    public ResponseEntity<?> changeEventDate(@RequestParam("id") int id, @RequestParam("date") LocalDate date, @RequestParam("startTime") LocalTime startTime) {
        return ResponseEntity.ok(service.getService().changeEventDate(id, date, startTime));
    }

}
