package cl.duoc.AppFiesta.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiestaResponse {

    private Integer idFiesta;
    private String nombre;
    private String tipoDeFiesta;
    private LocalDate fechaRealizacion;
    private String ubicacion;
    private Integer capacidad;
}
