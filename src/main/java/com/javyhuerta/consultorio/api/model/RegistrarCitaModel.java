package com.javyhuerta.consultorio.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Representa una solicitud de registro de cita en el sistema")
public class RegistrarCitaModel {

    @Schema(description = "Nombre del paciente", example = "Jane Doe")
    private String nombrePaciente;

    @Schema(description = "Fecha de la cita")
    private LocalDateTime fechaCita;

    @Schema(description = "Identificador del doctor", example = "")
    private Long doctorId;

    @Schema(description = "identificador del consultorio", example = "")
    private Long consultorioId;

}
