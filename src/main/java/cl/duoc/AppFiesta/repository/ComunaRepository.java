package cl.duoc.AppFiesta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.AppFiesta.model.Comuna;

public interface ComunaRepository extends JpaRepository<Comuna, Long> {
}
