package uj.jwzp.kpnk.GymApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp.kpnk.GymApp.controller.request.EventTemplateCreateRequest;
import uj.jwzp.kpnk.GymApp.model.EventTemplate;
import uj.jwzp.kpnk.GymApp.service.EventTemplateService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/event_templates")
public class EventTemplateController {

    private final EventTemplateService service;
    Logger logger = LoggerFactory.getLogger("jsonLogger");

    @Autowired
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
    public Page<EventTemplate> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        return service.findPaginated(pageNumber, pageSize);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getEventTemplate(@PathVariable int id) {
        return ResponseEntity.ok(service.eventTemplate(id));
    }

    @PostMapping
    public ResponseEntity<?> addEventTemplate(@RequestBody EventTemplateCreateRequest request) {
        EventTemplate createdEventTemplate = service.createEventTemplate(
                request.title(),
                request.day(),
                request.time(),
                request.duration(),
                request.clubId(),
                request.coachId(),
                request.peopleLimit()
        );
        logger.info("Created event: {}", createdEventTemplate);
        return ResponseEntity.created(URI.create("/api/v1/event_templates/" + createdEventTemplate.getId())).body(createdEventTemplate);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> modifyEventTemplate(@PathVariable int id, @RequestBody EventTemplateCreateRequest request) {
        var modifiedEventTemplate = service.modifyEventTemplate(
                id,
                request.title(),
                request.day(),
                request.time(),
                request.duration(),
                request.clubId(),
                request.coachId(),
                request.peopleLimit()
        );
        logger.info("Modified event template: {}", modifiedEventTemplate);
        return ResponseEntity.ok(modifiedEventTemplate);
    }

    @DeleteMapping(path = "{id}")
    public void removeEventTemplate(@PathVariable int id) {
        service.deleteEventTemplate(id);
    }
}
