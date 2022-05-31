package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp.kpnk.GymApp.exception.event.EventNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.EventRegistrationLimitException;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.model.Registration;
import uj.jwzp.kpnk.GymApp.repository.EventRepository;
import uj.jwzp.kpnk.GymApp.repository.RegistrationRepository;

import java.util.Optional;

@Service
public class RegistrationService {

    private final RegistrationRepository repository;
    private final EventRepository eventRepository;

    @Autowired
    public RegistrationService(RegistrationRepository repository, EventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    public Registration addRegistration(int eventId, String name, String surname) {
        Optional<Event> eventQueryResult = eventRepository.findById(eventId);
        if (eventQueryResult.isEmpty()) throw new EventNotFoundException(eventId);
        Event event = eventQueryResult.get();
        if (repository.findByEventId(eventId).size() == event.getPeopleLimit()) throw new EventRegistrationLimitException(eventId);
        Registration registration = new Registration(eventId, name, surname);
        return repository.save(registration);
    }
}
