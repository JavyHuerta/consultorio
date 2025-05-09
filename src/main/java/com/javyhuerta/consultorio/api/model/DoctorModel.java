package com.javyhuerta.consultorio.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Representa un doctor en el sistema")
public class DoctorModel {

    @Schema(description = "Identificador del doctor", example = "1")
    private Long id;

    @Schema(description = "Nombre del doctor", example = "Lucia")
    private String nombre;

    @Schema(description = "Apellido paternos del doctor", example = "Casas")
    private String apellidoPaterno;

    @Schema(description = "Apellido materno del doctor", example = "Guevara")
    private String apellidoMaterno;

    @Schema(description = "Especialidad del doctor", example = "Cardiología")
    private String especialidad;
}
