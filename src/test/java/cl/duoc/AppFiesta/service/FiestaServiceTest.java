package cl.duoc.AppFiesta.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.AppFiesta.dto.request.FiestaCreateRequest;
import cl.duoc.AppFiesta.dto.request.FiestaUpdateRequest;
import cl.duoc.AppFiesta.dto.response.FiestaResponse;
import cl.duoc.AppFiesta.exception.ResourceNotFoundException;
import cl.duoc.AppFiesta.model.Comuna;
import cl.duoc.AppFiesta.model.Fiesta;
import cl.duoc.AppFiesta.repository.ComunaRepository;
import cl.duoc.AppFiesta.repository.FiestaRepository;

@ExtendWith(MockitoExtension.class)
class FiestaServiceTest {

        @Mock
        private FiestaRepository fiestaRepository;

        @Mock
        private ComunaRepository comunaRepository;

        @InjectMocks
        private FiestaService fiestaService;

        private Comuna comuna;

        @BeforeEach
        void setUp() {
                comuna = new Comuna(1L, "Santiago");
        }

        @Test
        void guardarFiestaDebeGuardarYRetornarResponse() {
                FiestaCreateRequest request = new FiestaCreateRequest(
                                "Cumpleaños Daniel",
                                "Cumpleaños",
                                LocalDate.now().plusDays(20),
                                "Av. Principal 123",
                                1L,
                                100);

                when(comunaRepository.findById(1L))
                                .thenReturn(Optional.of(comuna));

                when(fiestaRepository.save(any(Fiesta.class)))
                                .thenAnswer(invocation -> {
                                        Fiesta fiesta = invocation.getArgument(0);
                                        fiesta.setIdFiesta(10);
                                        return fiesta;
                                });

                FiestaResponse resultado = fiestaService.guardarFiesta(request);

                assertThat(resultado).isNotNull();
                assertThat(resultado.getIdFiesta()).isEqualTo(10);
                assertThat(resultado.getNombre()).isEqualTo("Cumpleaños Daniel");
                assertThat(resultado.getComuna()).isEqualTo("Santiago");
                assertThat(resultado.getCapacidad()).isEqualTo(100);

                ArgumentCaptor<Fiesta> captor = ArgumentCaptor.forClass(Fiesta.class);
                verify(fiestaRepository).save(captor.capture());
                assertThat(captor.getValue().getComuna()).isEqualTo(comuna);
        }

        @Test
        void guardarFiestaDebeFallarCuandoComunaNoExiste() {
                FiestaCreateRequest request = new FiestaCreateRequest(
                                "Fiesta de prueba",
                                "Aniversario",
                                LocalDate.now().plusDays(10),
                                "Calle 123",
                                99L,
                                80);

                when(comunaRepository.findById(99L))
                                .thenReturn(Optional.empty());

                assertThatThrownBy(() -> fiestaService.guardarFiesta(request))
                                .isInstanceOf(RuntimeException.class)
                                .hasMessage("Comuna no encontrada");

                verify(fiestaRepository, never()).save(any(Fiesta.class));
        }

        @Test
        void obtenerTodasFiestasDebeMapearLaLista() {
                Fiesta primera = crearFiesta(1, "Fiesta Uno", 80);
                Fiesta segunda = crearFiesta(2, "Fiesta Dos", 120);

                when(fiestaRepository.findAll())
                                .thenReturn(List.of(primera, segunda));

                List<FiestaResponse> resultado = fiestaService.obtenerTodasFiestas();

                assertThat(resultado).hasSize(2);
                assertThat(resultado.get(0).getNombre()).isEqualTo("Fiesta Uno");
                assertThat(resultado.get(1).getCapacidad()).isEqualTo(120);
                verify(fiestaRepository).findAll();
        }

        @Test
        void obtenerUnaFiestaDebeRetornarResponseCuandoExiste() {
                Fiesta fiesta = crearFiesta(7, "Graduación", 150);
                when(fiestaRepository.findById(7))
                                .thenReturn(Optional.of(fiesta));

                FiestaResponse resultado = fiestaService.obtenerUnaFiesta(7);

                assertThat(resultado).isNotNull();
                assertThat(resultado.getIdFiesta()).isEqualTo(7);
                assertThat(resultado.getNombre()).isEqualTo("Graduación");
        }

        @Test
        void obtenerUnaFiestaDebeRetornarNullCuandoNoExiste() {
                when(fiestaRepository.findById(99))
                                .thenReturn(Optional.empty());

                assertThatThrownBy(() -> fiestaService.obtenerUnaFiesta(99))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage("Fiesta no encontrada con id=99");

                verify(fiestaRepository).findById(99);
        }

        @Test
        void actualizarFiestaDebeModificarYGuardarCuandoExiste() {
                Fiesta existente = crearFiesta(5, "Nombre anterior", 50);
                FiestaUpdateRequest request = new FiestaUpdateRequest(
                                "Nombre actualizado",
                                "Matrimonio",
                                LocalDate.now().plusDays(40),
                                "Nueva dirección 456",
                                200);

                when(fiestaRepository.findById(5))
                                .thenReturn(Optional.of(existente));
                when(fiestaRepository.save(any(Fiesta.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                FiestaResponse resultado = fiestaService.actualizarFiesta(5, request);

                assertThat(resultado).isNotNull();
                assertThat(resultado.getNombre()).isEqualTo("Nombre actualizado");
                assertThat(resultado.getCapacidad()).isEqualTo(200);
                assertThat(existente.getComuna()).isEqualTo(comuna);
                verify(fiestaRepository).save(existente);
        }

        @Test
        void actualizarFiestaDebeRetornarNullCuandoNoExiste() {
                FiestaUpdateRequest request = new FiestaUpdateRequest(
                                "Nombre actualizado",
                                "Matrimonio",
                                LocalDate.now().plusDays(40),
                                "Nueva dirección 456",
                                200);

                when(fiestaRepository.findById(99))
                                .thenReturn(Optional.empty());

                FiestaResponse resultado = fiestaService.actualizarFiesta(99, request);

                assertThat(resultado).isNull();
                verify(fiestaRepository, never()).save(any(Fiesta.class));
        }

        @Test
        void eliminarFiestaDebeEliminarCuandoExiste() {
                when(fiestaRepository.existsById(3)).thenReturn(true);

                boolean resultado = fiestaService.eliminarFiesta(3);

                assertThat(resultado).isTrue();
                verify(fiestaRepository).deleteById(3);
        }

        @Test
        void eliminarFiestaDebeRetornarFalseCuandoNoExiste() {
                when(fiestaRepository.existsById(99)).thenReturn(false);

                boolean resultado = fiestaService.eliminarFiesta(99);

                assertThat(resultado).isFalse();
                verify(fiestaRepository, never()).deleteById(99);
        }

        @Test
        void buscarPorTituloDebeMapearResultadosDelRepository() {
                Fiesta fiesta = crearFiesta(4, "Fiesta Corporativa", 300);
                when(fiestaRepository.findByNombreContainingIgnoreCase("corporativa"))
                                .thenReturn(List.of(fiesta));

                List<FiestaResponse> resultado = fiestaService.buscarPorTitulo("corporativa");

                assertThat(resultado).hasSize(1);
                assertThat(resultado.get(0).getNombre())
                                .isEqualTo("Fiesta Corporativa");
        }

        private Fiesta crearFiesta(Integer id, String nombre, Integer capacidad) {
                return new Fiesta(
                                id,
                                nombre,
                                "Tipo de prueba",
                                LocalDate.now().plusDays(30),
                                "Dirección de prueba",
                                comuna,
                                capacidad);
        }
}