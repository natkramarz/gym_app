package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp.kpnk.GymApp.exception.event.EventNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.EventFullyBookedException;
import uj.jwzp.kpnk.GymApp.exception.registration.RegistrationNotFound;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.model.Registration;
import uj.jwzp.kpnk.GymApp.repository.EventRepository;
import uj.jwzp.kpnk.GymApp.repository.RegistrationRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RegistrationService {

    private final RegistrationRepository repository;
    private final EventRepository eventRepository;

    @Autowired
    public RegistrationService(RegistrationRepository repository, EventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    public Registration createRegistration(int eventId, String name, String surname) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) throw new EventNotFoundException(eventId);
        Event event = eventOptional.get();
        if (repository.findByEventId(eventId).size() >= event.getPeopleLimit()) throw new EventFullyBookedException(eventId);
        Registration registration = new Registration(eventId, name, surname);
        return repository.save(registration);
    }

    public void deleteRegistration(int id) {
        if (eventRepository.findById(id).isEmpty()) throw new EventNotFoundException(id);
        repository.deleteById(id);
    }

    public Registration registration(int id) {
        return repository.findById(id).orElseThrow(() -> new RegistrationNotFound(id));
    }

    public Set<Registration> allRegistrations() {
        return new HashSet<>(repository.findAll());
    }

}
