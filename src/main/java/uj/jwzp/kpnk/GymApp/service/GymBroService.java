package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp.kpnk.GymApp.controller.request.CreateRequest;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachNotFoundException;
import uj.jwzp.kpnk.GymApp.model.GymBro;
import uj.jwzp.kpnk.GymApp.model.special_case.DeletedGymBro;
import uj.jwzp.kpnk.GymApp.repository.GymBroRepository;

import java.util.List;

@Service
public class GymBroService implements ServiceLayer<GymBro> {

    private final GymBroRepository repository;

    @Autowired
    public GymBroService(GymBroRepository gymBroRepository) {
        this.repository = gymBroRepository;
    }


    @Override
    public GymBro get(int id) {
        return repository.findById(id).orElse(new DeletedGymBro(id));
    }

    @Override
    public List<GymBro> getAll() {
        return repository.findAll();
    }

    @Override
    public GymBro add(CreateRequest<GymBro> createRequest) {
        var gymBro = createRequest.asObject();
        return repository.save(gymBro);
    }

    @Override
    public GymBro modify(int id, CreateRequest<GymBro> createRequest) {
        if (repository.findById(id).isEmpty()) throw new CoachNotFoundException(id);
        var modified = createRequest.asObject(id);
        return repository.save(modified);
    }

    @Override
    public void delete(int id) {
        if (repository.findById(id).isEmpty()) throw new CoachNotFoundException(id);
        var deleted = removePersonalData(id);
        repository.save(deleted);
    }

    public DeletedGymBro removePersonalData(int id) {
        return new DeletedGymBro(id);
    }
}
