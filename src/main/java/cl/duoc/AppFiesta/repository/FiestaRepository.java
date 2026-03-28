package cl.duoc.AppFiesta.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import cl.duoc.AppFiesta.model.Fiesta;

@Repository
public class FiestaRepository {

    // Arreglo para guardar datos
    private final List<Fiesta> listaFiestas = new ArrayList<>();

    // Guardar en la lista fiestas
    public Fiesta guardarFiesta(Fiesta fiesta) {
        listaFiestas.add(fiesta);
        return fiesta;
    }

    // Listar todas las fiestas
    public List<Fiesta> obtenerTodasFiestas() {
        return new ArrayList<>(listaFiestas);
    }

    // Listar una fiesta
    public Optional<Fiesta> obtenerUnaFiesta(Integer idfiesta) {
        for (Fiesta fiesta : listaFiestas) {
            if (idfiesta.equals(fiesta.getIdFiesta())) {
                return Optional.of(fiesta);
            }
        }
        return Optional.empty();
    }

    // Actualizar una fiesta
    public Fiesta actualizarFiesta(Fiesta actualizar) {
        for (Fiesta fiesta : listaFiestas) {
            if (actualizar.getIdFiesta().equals(fiesta.getIdFiesta())) {
                fiesta.setNombre(actualizar.getNombre());
                fiesta.setTipoDeFiesta(actualizar.getTipoDeFiesta());
                fiesta.setFechaRealizacion(actualizar.getFechaRealizacion());
                fiesta.setUbicacion(actualizar.getUbicacion());
                fiesta.setCapacidad(actualizar.getCapacidad());

                return fiesta;
            }
        }
        return null;
    }

    // Eliminar una fiesta
    public boolean eliminarFiesta(Integer idfiesta) {
        for (Fiesta fiesta : listaFiestas) {
            if (idfiesta.equals(fiesta.getIdFiesta())) {
                listaFiestas.remove(fiesta);
                return true;
            }
        }
        return false;
    }
}
