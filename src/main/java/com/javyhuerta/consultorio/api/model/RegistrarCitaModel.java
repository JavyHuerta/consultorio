package com.javyhuerta.consultorio.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Representa una solicitud de registro de cita en el sistema")
public class RegistrarCitaModel {

    @NotBlank(message = "El nombre del paciente no puede estar vacío")
    @Schema(description = "Nombre del paciente", example = "Jane Doe")
    private String nombrePaciente;

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha debe ser el día actual o mayor")
    @Schema(description = "Fecha de la cita")
    private LocalDateTime fechaCita;

    @NotNull(message = "El campo doctorId no puede ser nulo")
    @Min(value = 1, message = "El identificador del doctor debe ser mayor a cero")
    @Schema(description = "Identificador del doctor", example = "1")
    private Long doctorId;

    @NotNull(message = "El campo consultorioId no puede ser nulo")
    @Min(value = 1, message = "El identificador del consultorio debe ser mayor a cero")
    @Schema(description = "Identificador del consultorio", example = "1")
    private Long consultorioId;

}
