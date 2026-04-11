package cl.duoc.AppFiesta.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.AppFiesta.model.Fiesta;

@Repository
public interface FiestaRepository extends JpaRepository<Fiesta, Integer> {
    

}
