package uj.jwzp.kpnk.GymApp.service;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import uj.jwzp.kpnk.GymApp.controller.request.CreateRequest;
import uj.jwzp.kpnk.GymApp.exception.gym_bro.GymBroNotFoundException;
import uj.jwzp.kpnk.GymApp.model.GymBro;
import uj.jwzp.kpnk.GymApp.model.special_case.DeletedGymBro;
import uj.jwzp.kpnk.GymApp.repository.GymBroRepository;

import java.util.List;

@Service
public class GymBroService implements ServiceLayer<GymBro> {

    private final GymBroRepository repository;


    public GymBroService(ApplicationContext context) {
        repository = context.getBean(GymBroRepository.class);
    }

    @Override
    public GymBro get(int id) {
        return repository.findById(id).orElse(new DeletedGymBro(id));
    }

    @Override
    public List<GymBro> getAll() {
        return repository.findAll();
    }

    public List<GymBro> getAllByIds(List<Integer> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public GymBro add(CreateRequest<GymBro> createRequest) {
        var gymBro = createRequest.asObject();
        return repository.save(gymBro);
    }

    @Override
    public GymBro modify(int id, CreateRequest<GymBro> createRequest) {
        if (repository.findById(id).isEmpty()) throw new GymBroNotFoundException(id);
        var modified = createRequest.asObject(id);
        return repository.save(modified);
    }

    @Override
    public void delete(int id) {
        if (repository.findById(id).isEmpty()) throw new GymBroNotFoundException(id);
        var deleted = new DeletedGymBro(id);
        repository.save(new GymBro(id, deleted.getFirstName(), deleted.getLastName(), deleted.getAccountCreatedAt(), deleted.isDeleted()));
    }
}
