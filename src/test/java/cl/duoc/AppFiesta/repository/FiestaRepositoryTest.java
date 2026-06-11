package cl.duoc.AppFiesta.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import cl.duoc.AppFiesta.model.Comuna;
import cl.duoc.AppFiesta.model.Fiesta;

@DataJpaTest
@ActiveProfiles("test")
class FiestaRepositoryTest {

    @Autowired
    private FiestaRepository fiestaRepository;

    @Autowired
    private ComunaRepository comunaRepository;

    private Comuna santiago;
    private Comuna providencia;

    @BeforeEach
    void setUp() {
        santiago = comunaRepository.save(
                new Comuna(null, "Santiago"));
        providencia = comunaRepository.save(
                new Comuna(null, "Providencia"));
    }

    @Test
    void debeBuscarPorNombreIgnorandoMayusculas() {
        fiestaRepository.save(
                crearFiesta("Fiesta Corporativa", 150, santiago));
        fiestaRepository.save(
                crearFiesta("Matrimonio", 200, providencia));

        List<Fiesta> resultado = fiestaRepository.findByNombreContainingIgnoreCase("corporativa");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre())
                .isEqualTo("Fiesta Corporativa");
    }

    @Test
    void debeBuscarPorComuna() {
        fiestaRepository.save(
                crearFiesta("Fiesta Santiago", 100, santiago));
        fiestaRepository.save(
                crearFiesta("Fiesta Providencia", 100, providencia));

        List<Fiesta> resultado = fiestaRepository.findByComunaId(santiago.getId());

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getComuna().getNombreComuna())
                .isEqualTo("Santiago");
    }

    @Test
    void debeBuscarBajoCapacidadOrdenandoDeMayorAMenor() {
        fiestaRepository.save(
                crearFiesta("Pequeña", 50, santiago));
        fiestaRepository.save(
                crearFiesta("Mediana", 120, santiago));
        fiestaRepository.save(
                crearFiesta("Grande", 300, santiago));

        List<Fiesta> resultado = fiestaRepository.findFiestaBajaCapacidad(150);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getCapacidad()).isEqualTo(120);
        assertThat(resultado.get(1).getCapacidad()).isEqualTo(50);
    }

    @Test
    void debeBuscarCapacidadEntreDosValores() {
        fiestaRepository.save(
                crearFiesta("Pequeña", 50, santiago));
        fiestaRepository.save(
                crearFiesta("Mediana", 120, santiago));
        fiestaRepository.save(
                crearFiesta("Grande", 300, santiago));

        List<Fiesta> resultado = fiestaRepository.findByCapacidadBetween(100, 200);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Mediana");
    }

    @Test
    void debeBuscarPorTituloConConsultaNativa() {
        fiestaRepository.save(
                crearFiesta("Graduación Ingeniería", 220, santiago));

        List<Fiesta> resultado = fiestaRepository.buscarPorTituloNativo("Ingeniería");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre())
                .isEqualTo("Graduación Ingeniería");
    }

    private Fiesta crearFiesta(
            String nombre,
            Integer capacidad,
            Comuna comuna) {

        return new Fiesta(
                null,
                nombre,
                "Tipo de prueba",
                LocalDate.now().plusDays(30),
                "Dirección de prueba",
                comuna,
                capacidad);
    }
}
