package cl.duoc.AppFiesta.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiestaCreateRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El tipo de fiesta es obligatorio")
    @Size(max = 50, message = "El tipo no puede superar 50 caracteres")
    private String tipoDeFiesta;

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha debe ser hoy o futura")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaRealizacion;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 150, message = "La direccion no puede superar 150 caracteres")
    private String direccion;

    @Positive(message = "Id debe ser mayor a 0")
    private Long comuna_id;

    @Positive(message = "La capacidad debe ser mayor a 0")
    @Max(value = 100000, message = "Capacidad demasiado alta")
    private Integer capacidad;
}
