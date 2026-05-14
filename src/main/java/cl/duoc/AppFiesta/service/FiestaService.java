package cl.duoc.AppFiesta.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.beans.factory.annotation.Autowired; / Se quito al agregar @RequiredArgsConstructor
import org.springframework.stereotype.Service;
import cl.duoc.AppFiesta.dto.request.FiestaCreateRequest;
import cl.duoc.AppFiesta.dto.request.FiestaUpdateRequest;
import cl.duoc.AppFiesta.dto.response.FiestaResponse;
import cl.duoc.AppFiesta.exception.ResourceNotFoundException;
import cl.duoc.AppFiesta.model.Comuna;
import cl.duoc.AppFiesta.model.Fiesta;
import cl.duoc.AppFiesta.repository.ComunaRepository;
import cl.duoc.AppFiesta.repository.FiestaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
// Habilita logger SLF4J automáticamente
@Slf4j
public class FiestaService {

    // @Autowired
    private final FiestaRepository fiestaRepository;
    private final ComunaRepository comunaRepository;

    // ── MAPEO PRIVADO: Entidad → ResponseDTO ─────────
    // Solo lo usa este Service. El Controller y el
    // Repository nunca conocen el DTO ni la entidad
    // del otro respectivamente.
    private FiestaResponse mapToDTO(Fiesta fiesta) {
        return new FiestaResponse(
                fiesta.getIdFiesta(),
                fiesta.getNombre(),
                fiesta.getTipoDeFiesta(),
                fiesta.getFechaRealizacion(),
                fiesta.getDireccion(),
                fiesta.getComuna().getNombreComuna(),
                fiesta.getCapacidad());
    }

    // Crear una fiesta
    /*
     * Se comenta al agregar nuevo modelo comuna
     * public FiestaResponse guardarFiesta(FiestaCreateRequest request) {
     * Fiesta fiesta = new Fiesta();
     * 
     * // fiesta.setIdFiesta(request.getIdFiesta());
     * fiesta.setNombre(request.getNombre());
     * fiesta.setTipoDeFiesta(request.getTipoDeFiesta());
     * fiesta.setFechaRealizacion(request.getFechaRealizacion());
     * fiesta.setDireccion(request.getDireccion());
     * fiesta.setCapacidad(request.getCapacidad());
     * 
     * Fiesta fiestaGuardada = fiestaRepository.save(fiesta);
     * return new FiestaResponse(
     * fiesta.getIdFiesta(),
     * fiestaGuardada.getNombre(),
     * fiestaGuardada.getTipoDeFiesta(),
     * fiestaGuardada.getFechaRealizacion(),
     * fiestaGuardada.getDireccion(),
     * fiestaGuardada.getComuna().getNombreComuna(),
     * fiestaGuardada.getCapacidad());
     * }
     */

    public FiestaResponse guardarFiesta(FiestaCreateRequest request) {
        Comuna comuna = comunaRepository.findById(request.getComuna_id().longValue())
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada"));

        // .orElseThrow(...) / Si encuentra la comuna → la devuelve y si no lanza
        // excepción
        /*
         * Es "equivalente" a:
         * if (comuna != null) {
         * return comuna;
         * } else {
         * throw new RuntimeException("Comuna no encontrada");
         * }
         */

        Fiesta fiesta = new Fiesta();
        fiesta.setNombre(request.getNombre());
        fiesta.setTipoDeFiesta(request.getTipoDeFiesta());
        fiesta.setFechaRealizacion(request.getFechaRealizacion());
        fiesta.setDireccion(request.getDireccion());
        fiesta.setComuna(comuna);
        fiesta.setCapacidad(request.getCapacidad());

        Fiesta fiestaGuardada = fiestaRepository.save(fiesta);
        return mapToDTO(fiestaGuardada);
    }

    // Obtener todas las fiestas
    public List<FiestaResponse> obtenerTodasFiestas() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String usuario = auth.getName();
        // Log de inicio del proceso
        log.info("Iniciando búsqueda de todas las fiestas");
        log.info("Usuario={} realizó búsqueda",
                usuario);

        List<Fiesta> fiestas = fiestaRepository.findAll();
        List<FiestaResponse> respuesta = new ArrayList<>();

        // Log indicando cantidad encontrada
        log.info("Cantidad de fiestas encontradas={}", fiestas.size());

        for (Fiesta fiesta : fiestas) {
            FiestaResponse response = new FiestaResponse(
                    fiesta.getIdFiesta(),
                    fiesta.getNombre(),
                    fiesta.getTipoDeFiesta(),
                    fiesta.getFechaRealizacion(),
                    fiesta.getDireccion(),
                    fiesta.getComuna().getNombreComuna(),
                    fiesta.getCapacidad());
            respuesta.add(response);
        }

        return respuesta;
    }

    // Obtener una fiesta por id
    public FiestaResponse obtenerUnaFiesta(Integer idFiesta) {
        Fiesta fiesta = fiestaRepository.findById(idFiesta)
                .orElseThrow(() -> {
                    log.warn("Fiesta no encontrada id={}", idFiesta);
                    return new ResourceNotFoundException("Fiesta no encontrada con id=" + idFiesta);
                });

        if (fiesta == null) {
            return null;
        }

        return new FiestaResponse(
                fiesta.getIdFiesta(),
                fiesta.getNombre(),
                fiesta.getTipoDeFiesta(),
                fiesta.getFechaRealizacion(),
                fiesta.getDireccion(),
                fiesta.getComuna().getNombreComuna(),
                fiesta.getCapacidad());
    }

    // Actualizar una fiesta
    /*
     * Se comenta al agregar nuevo modelo comuna
     * public FiestaResponse actualizarFiesta(Integer idFiesta, FiestaCreateRequest
     * request) {
     * Fiesta fiesta = new Fiesta();
     * 
     * fiesta.setIdFiesta(idFiesta);
     * fiesta.setNombre(request.getNombre());
     * fiesta.setTipoDeFiesta(request.getTipoDeFiesta());
     * fiesta.setFechaRealizacion(request.getFechaRealizacion());
     * fiesta.setDireccion(request.getDireccion());
     * fiesta.setCapacidad(request.getCapacidad());
     * 
     * Fiesta fiestaActualizada = fiestaRepository.save(fiesta);
     * 
     * if (fiestaActualizada == null) {
     * return null;
     * }
     * 
     * return new FiestaResponse(
     * fiesta.getIdFiesta(),
     * fiestaActualizada.getNombre(),
     * fiestaActualizada.getTipoDeFiesta(),
     * fiestaActualizada.getFechaRealizacion(),
     * fiestaActualizada.getDireccion(),
     * fiestaActualizada.getComuna().getNombreComuna(),
     * fiestaActualizada.getCapacidad());
     * }
     */

    public FiestaResponse actualizarFiesta(Integer idFiesta, FiestaUpdateRequest request) {
        Fiesta fiesta = fiestaRepository.findById(idFiesta).orElse(null);

        if (fiesta == null) {
            return null;
        }

        fiesta.setNombre(request.getNombre());
        fiesta.setTipoDeFiesta(request.getTipoDeFiesta());
        fiesta.setFechaRealizacion(request.getFechaRealizacion());
        fiesta.setDireccion(request.getDireccion());
        fiesta.setCapacidad(request.getCapacidad());

        Fiesta fiestaActualizada = fiestaRepository.save(fiesta);
        return mapToDTO(fiestaActualizada);
    }

    // Eliminar una fiesta
    public boolean eliminarFiesta(Integer idFiesta) {
        if (fiestaRepository.existsById(idFiesta)) {
            fiestaRepository.deleteById(idFiesta);
            return true;
        }
        return false;
    }

    // ── BÚSQUEDAS (de Clase 2, adaptadas a ResponseDTO) ──
    public List<FiestaResponse> buscarPorTitulo(String texto) {
        return fiestaRepository.findByNombreContainingIgnoreCase(texto)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    // .stream() "Voy a recorrer cada elemento uno por uno"
    // .map(this::mapToDTO) "Toma cada Fiesta y La transforma en FiestaResponse"
    // .collect(Collectors.toList()) "Convierte el stream de vuelta a una lista"

    public List<FiestaResponse> buscarPorComuna(Long comunaId) {
        return fiestaRepository.findByComunaId(comunaId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<FiestaResponse> buscarBajoCapacidad(Integer capacidadMax) {
        return fiestaRepository.findFiestaBajaCapacidad(capacidadMax)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }
}