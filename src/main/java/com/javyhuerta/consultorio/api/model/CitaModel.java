package com.javyhuerta.consultorio.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Representa una solicitud de registro de cita en el sistema")
public class CitaModel {

    @Schema(description = "Identificador de la cita", example = "1")
    private Long id;

    @Schema(description = "Nombre del paciente", example = "Jane Doe")
    private String nombrePaciente;

    @Schema(description = "Fecha de la cita")
    private LocalDateTime fechaCita;

    @Schema(description = "Identificador del doctor", example = "1")
    private Long doctorId;

    @Schema(description = "identificador del consultorio", example = "1")
    private Long consultorioId;

}
