package cl.duoc.AppFiesta.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.AppFiesta.dto.request.FiestaCreateRequest;
import cl.duoc.AppFiesta.dto.response.FiestaResponse;
import cl.duoc.AppFiesta.model.Fiesta;
import cl.duoc.AppFiesta.repository.FiestaRepository;

@Service
public class FiestaService {

    @Autowired
    private FiestaRepository fiestaRepository;

    // Crear una fiesta
    public FiestaResponse guardarFiesta(FiestaCreateRequest request) {
        Fiesta fiesta = new Fiesta();

        //fiesta.setIdFiesta(request.getIdFiesta());
        fiesta.setNombre(request.getNombre());
        fiesta.setTipoDeFiesta(request.getTipoDeFiesta());
        fiesta.setFechaRealizacion(request.getFechaRealizacion());
        fiesta.setUbicacion(request.getUbicacion());
        fiesta.setCapacidad(request.getCapacidad());


        Fiesta fiestaGuardada = fiestaRepository.save(fiesta);
        return new FiestaResponse(
                fiesta.getIdFiesta(),
                fiestaGuardada.getNombre(),
                fiestaGuardada.getTipoDeFiesta(),
                fiestaGuardada.getFechaRealizacion(),
                fiestaGuardada.getUbicacion(),
                fiestaGuardada.getCapacidad());
    }

    // Obtener todas las fiestas
    public List<FiestaResponse> obtenerTodasFiestas() {
        List<Fiesta> fiestas = fiestaRepository.findAll();
        List<FiestaResponse> respuesta = new ArrayList<>();

        for (Fiesta fiesta : fiestas) {
            FiestaResponse response = new FiestaResponse(
                    fiesta.getIdFiesta(),
                    fiesta.getNombre(),
                    fiesta.getTipoDeFiesta(),
                    fiesta.getFechaRealizacion(),
                    fiesta.getUbicacion(),
                    fiesta.getCapacidad());
            respuesta.add(response);
        }

        return respuesta;
    }

    // Obtener una fiesta por id
    public FiestaResponse obtenerUnaFiesta(Integer idFiesta) {
        Fiesta fiesta = fiestaRepository.findById(idFiesta).orElse(null);

        if (fiesta == null) {
            return null;
        }

        return new FiestaResponse(
                fiesta.getIdFiesta(),
                fiesta.getNombre(),
                fiesta.getTipoDeFiesta(),
                fiesta.getFechaRealizacion(),
                fiesta.getUbicacion(),
                fiesta.getCapacidad());
    }

    // Actualizar una fiesta
    public FiestaResponse actualizarFiesta(Integer idFiesta, FiestaCreateRequest request) {
        Fiesta fiesta = new Fiesta();

        fiesta.setIdFiesta(idFiesta);
        fiesta.setNombre(request.getNombre());
        fiesta.setTipoDeFiesta(request.getTipoDeFiesta());
        fiesta.setFechaRealizacion(request.getFechaRealizacion());
        fiesta.setUbicacion(request.getUbicacion());
        fiesta.setCapacidad(request.getCapacidad());

        Fiesta fiestaActualizada = fiestaRepository.save(fiesta);

        if (fiestaActualizada == null) {
            return null;
        }

        return new FiestaResponse(
                fiesta.getIdFiesta(),
                fiestaActualizada.getNombre(),
                fiestaActualizada.getTipoDeFiesta(),
                fiestaActualizada.getFechaRealizacion(),
                fiestaActualizada.getUbicacion(),
                fiestaActualizada.getCapacidad());
    }

    // Eliminar una fiesta
    public boolean eliminarFiesta(Integer idFiesta) {
        if(fiestaRepository.existsById(idFiesta)){
             fiestaRepository.deleteById(idFiesta);
             return true;
        }
        return false;
    }
}