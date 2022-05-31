package uj.jwzp.kpnk.GymApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp.kpnk.GymApp.controller.request.EventTemplateCreateRequest;
import uj.jwzp.kpnk.GymApp.model.EventTemplate;
import uj.jwzp.kpnk.GymApp.service.EventTemplateService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/event_templates")
public class EventTemplateController {

    private final EventTemplateService service;

    public EventTemplateController(EventTemplateService service) {
        this.service = service;
    }

    @GetMapping
    public List<EventTemplate> allEventTemplates(@RequestParam Optional<Integer> clubId, @RequestParam Optional<Integer> coachId) {
        if (clubId.isEmpty() && coachId.isEmpty()) return service.allEventTemplates().stream().toList();
        if (clubId.isPresent()) return service.eventTemplatesByClub(clubId.get()).stream().toList();
        return service.eventTemplatesByCoach(coachId.get()).stream().toList();
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<?> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        return ResponseEntity.ok(service.findPaginated(pageNumber, pageSize));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getEventTemplate(@PathVariable int id) {
        return ResponseEntity.ok(service.eventTemplate(id));
    }

    @PostMapping
    public ResponseEntity<?> addEventTemplate(@RequestBody EventTemplateCreateRequest request) {
        EventTemplate createdEventTemplate = service.addEventTemplate(
                request.title(),
                request.day(),
                request.time(),
                request.duration(),
                request.clubId(),
                request.coachId()
        );

        return ResponseEntity.created(URI.create("/api/event_templates/" + createdEventTemplate.getId())).body(createdEventTemplate);
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<?> modifyEventTemplate(@PathVariable int id, @RequestBody EventTemplateCreateRequest request) {
        return ResponseEntity.ok(
                service.modifyEventTemplate(
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
    public void removeEventTemplate(@PathVariable int id) {
        service.removeEventTemplate(id);
    }
}
