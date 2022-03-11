package uj.jwzp.kpnk.GymApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp.kpnk.GymApp.controller.request.EventCreateRequest;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.service.EventService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

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
                request.coachId()
        );

        return ResponseEntity.created(URI.create("/api/events/" + createdEvent.id())).body(createdEvent);
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<?> modifyEvent(@PathVariable int id, @RequestBody EventCreateRequest request) {
        return ResponseEntity.ok(
                service.modifyEvent(
                        id,
                        request.title(),
                        request.day(),
                        request.time(),
                        request.duration(),
                        request.clubId(),
                        request.coachId()
                )
        );
    }

    @DeleteMapping(path = "{id}")
    public void removeCoach(@PathVariable int id) {
        service.removeEvent(id);
    }
}
