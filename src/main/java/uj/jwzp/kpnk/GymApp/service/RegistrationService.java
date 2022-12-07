package uj.jwzp.kpnk.GymApp.service;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp.kpnk.GymApp.controller.request.CreateRequest;
import uj.jwzp.kpnk.GymApp.exception.event.EventFullyBookedException;
import uj.jwzp.kpnk.GymApp.exception.event.EventNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.registration.RegistrationNotFound;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.model.Registration;
import uj.jwzp.kpnk.GymApp.repository.EventRepository;
import uj.jwzp.kpnk.GymApp.repository.RegistrationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService implements ServiceLayer<Registration> {

    private final RegistrationRepository repository;
    private final EventRepository eventRepository;

    @Autowired
    public RegistrationService(RegistrationRepository repository, EventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Registration get(int id) {
        return repository.findById(id).orElseThrow(() -> new RegistrationNotFound(id));
    }

    @Override
    public List<Registration> getAll() {
        return repository.findAll();
    }

    @Override
    public Registration add(CreateRequest<Registration> createRequest) {
        int eventId = createRequest.asObject().getEventId();
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) throw new EventNotFoundException(eventId);
        Event event = eventOptional.get();
        if (repository.findByEventId(eventId).size() >= event.getPeopleLimit())
            throw new EventFullyBookedException(eventId);
        Registration registration = createRequest.asObject();
        return repository.save(registration);
    }

    @Override
    public Registration modify(int id, CreateRequest<Registration> createRequest) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(int id) {
        if (eventRepository.findById(id).isEmpty()) throw new EventNotFoundException(id);
        repository.deleteById(id);
    }
}
