package cl.duoc.AppFiesta.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fiesta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fiesta {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Para configurar una secuencia
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fiesta_seq")
    @SequenceGenerator(name = "fiesta_seq", sequenceName = "fiesta_seq", allocationSize = 1)
    private Integer idFiesta;

    @Column(nullable = false) // Para que el valor sea unico: unique = true)
    private String nombre;

    @Column(nullable = false)
    private String tipoDeFiesta;

    @Column(name = "fecha_realizacion", nullable = false)
    private LocalDate fechaRealizacion;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false)
    private Integer capacidad;
}
