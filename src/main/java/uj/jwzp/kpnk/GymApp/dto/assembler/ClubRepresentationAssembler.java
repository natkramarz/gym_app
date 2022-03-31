package uj.jwzp.kpnk.GymApp.dto.assembler;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import uj.jwzp.kpnk.GymApp.controller.ClubController;
import uj.jwzp.kpnk.GymApp.dto.ClubRepresentation;
import uj.jwzp.kpnk.GymApp.model.Club;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class ClubRepresentationAssembler implements RepresentationModelAssembler<Club, ClubRepresentation> {


    @Override
    public ClubRepresentation toModel(Club entity) {
        ClubRepresentation clubRepresentation = new ClubRepresentation();
        clubRepresentation.setId(entity.getId());
        clubRepresentation.setName(entity.getName());
        clubRepresentation.setAddress(entity.getAddress());
        clubRepresentation.setWhenOpen(entity.getWhenOpen());

        clubRepresentation.add(
                linkTo(
                        methodOn(ClubController.class)
                                .getClub(
                                        clubRepresentation.getId()
                                )
                ).withSelfRel()
        );

        return clubRepresentation;
    }

    @Override
    public CollectionModel<ClubRepresentation> toCollectionModel(Iterable<? extends Club> entities) {
        CollectionModel<ClubRepresentation> clubRepresentations = RepresentationModelAssembler.super.toCollectionModel(entities);

        clubRepresentations.add(linkTo(methodOn(ClubController.class).allClubs()).withSelfRel());

        return clubRepresentations;
    }
}
