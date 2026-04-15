package cl.duoc.AppFiesta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.duoc.AppFiesta.model.Fiesta;

@Repository
public interface FiestaRepository extends JpaRepository<Fiesta, Integer> {

    // Busquedas personalizadas
    // ── TIPO 1: QUERY METHODS ────────────────────────
    // Spring analiza el nombre del método y genera SQL.
    // Atributo debe coincidir EXACTAMENTE con el campo
    // de la entidad Java (mayúsculas incluidas).

    // → SELECT * FROM Fiesta WHERE UPPER(nombre) LIKE UPPER('%?%')
    List<Fiesta> findByNombreContainingIgnoreCase(String nombre);

    // → SELECT * FROM Fiesta WHERE capacidad < ?
    List<Fiesta> findByCapacidadLessThan(Integer capacidad);

    // → SELECT * FROM Fiesta WHERE precio BETWEEN ? AND ?
    List<Fiesta> findByCapacidadBetween(Double min, Double max);

    // Ejemplos de otras variantes:
    // findByNombre(String nombre) // exacto
    // findByNombreStartsWith(String nombre) // empieza con
    // findByNombreEndsWith(String nombre) // termina con
    // findByCapacidadGreaterThan(int cap) // mayor que

    // ── TIPO 2: @QUERY CON JPQL ──────────────────────
    // Escrito sobre entidades Java, NO sobre tablas SQL.
    // "Fiesta" = clase Java, "l.comuna.id" = atributo.
    // Hibernate lo traduce al SQL correcto según el dialecto.
    // :param es parámetro nombrado, @Param lo enlaza.
    @Query("SELECT l FROM Fiesta l WHERE l.comuna.id = :comunaId")
    List<Fiesta> findByComunaId(@Param("comunaId") Long comunaId);

    // JPQL con ORDER BY
    @Query("SELECT l FROM Fiesta l WHERE l.capacidad <= :capacidadMax ORDER BY l.capacidad DESC")
    List<Fiesta> findFiestaBajaCapacidad(@Param("capacidadMax") Integer capacidadMax);

    // ── TIPO 3: SQL NATIVO ───────────────────────────
    // nativeQuery=true: Hibernate manda el SQL tal cual a MySQL u Oracle.
    // Usar para funciones específicas de MySQL (CONCAT, DATE_FORMAT...)
    // o consultas muy optimizadas que no deben ser reescritas.
    @Query(value = "SELECT * FROM Fiesta WHERE nombre LIKE CONCAT('%', :texto, '%')", nativeQuery = true)
    List<Fiesta> buscarPorTituloNativo(@Param("texto") String texto);

}
