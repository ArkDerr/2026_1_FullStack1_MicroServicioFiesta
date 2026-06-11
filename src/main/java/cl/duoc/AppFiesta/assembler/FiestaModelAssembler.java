package cl.duoc.AppFiesta.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import cl.duoc.AppFiesta.controller.FIestaController;
import cl.duoc.AppFiesta.dto.response.FiestaResponse;
import java.util.List;

@Component
public class FiestaModelAssembler {

    public EntityModel<FiestaResponse> toModel(FiestaResponse fiesta) {
        return EntityModel.of(
                fiesta,
                linkTo(methodOn(FIestaController.class)
                        .obtenerUnaFiesta(fiesta.getIdFiesta()))
                        .withSelfRel(),

                linkTo(methodOn(FIestaController.class)
                        .listarTodasFiestas())
                        .withRel("fiestas"),

                linkTo(methodOn(FIestaController.class)
                        .eliminarFiesta(fiesta.getIdFiesta()))
                        .withRel("eliminar"));
    }

    public CollectionModel<EntityModel<FiestaResponse>> toCollectionModel(
            List<FiestaResponse> fiestas) {

        List<EntityModel<FiestaResponse>> fiestasConLinks = fiestas.stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(
                fiestasConLinks,
                linkTo(methodOn(FIestaController.class)
                        .listarTodasFiestas())
                        .withSelfRel());
    }
}