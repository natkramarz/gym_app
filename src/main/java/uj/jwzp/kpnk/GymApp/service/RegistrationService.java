package uj.jwzp.kpnk.GymApp.service;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import uj.jwzp.kpnk.GymApp.controller.request.CreateRequest;
import uj.jwzp.kpnk.GymApp.exception.registration.RegistrationNotFound;
import uj.jwzp.kpnk.GymApp.model.GymBro;
import uj.jwzp.kpnk.GymApp.model.Registration;
import uj.jwzp.kpnk.GymApp.model.special_case.DeletedGymBro;
import uj.jwzp.kpnk.GymApp.repository.RegistrationRepository;
import java.util.List;

@Service
public class RegistrationService implements ServiceLayer<Registration> {

    private final RegistrationRepository repository;
    private final ServiceProxy.EventServiceProxyImp eventService;
    private final ServiceProxy.GymBroServiceProxyImp gymBroService;

    @Autowired
    public RegistrationService(ApplicationContext context) {
        this.repository = context.getBean(RegistrationRepository.class);
        this.eventService = context.getBean(ServiceProxy.EventServiceProxyImp.class);
        this.gymBroService = context.getBean(ServiceProxy.GymBroServiceProxyImp.class);
    }

    @Override
    public Registration get(int id) {
        return repository.findById(id).orElseThrow(() -> new RegistrationNotFound(id));
    }

    @Override
    public List<Registration> getAll() {
        return repository.findAll();
    }

    public List<Registration> getAllByEventId(int eventId) {
        return repository.findByEventId(eventId);
    }

    @Override
    public Registration add(CreateRequest<Registration> createRequest) {
        int eventId = createRequest.asObject().getEventId();
        var event = eventService.get(eventId);
        int gymBroId = createRequest.asObject().getGymBroId();
        var gymBro = gymBroService.get(gymBroId);
        var registration = createRequest.asObject();

        return repository.save(registration);
    }

    @Override
    public Registration modify(int id, CreateRequest<Registration> createRequest) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(int id) {
        eventService.get(id);
        repository.deleteById(id);
    }

    public List<GymBro> eventParticipants(int eventId) {
        var gymBroIds = repository.findByEventId(eventId).stream()
                .map(Registration::getGymBroId)
                .toList();
        return gymBroService.getService().getAllByIds(gymBroIds).stream().map(gymBro -> !gymBro.isDeleted() ? gymBro : new DeletedGymBro(gymBro.getId())).toList();
    }
}
