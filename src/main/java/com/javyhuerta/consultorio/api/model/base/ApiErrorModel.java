package com.javyhuerta.consultorio.api.model.base;

import com.javyhuerta.consultorio.util.CodigoNegocio;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Respuesta base de error para una operación")
public class ApiErrorModel implements Serializable {

    @Schema(description = "Código de negocio de la operación", example = "CM001")
    private String codigo;

    @Schema(description = "Mensaje informatico",example = "Solicitud exitosa")
    private String mensaje;

    @Schema(description = "Errores de la operación")
    private List<String> errores;

    @Schema(description = "Respuesta de la operación")
    private LocalDateTime fecha;

    public ApiErrorModel(CodigoNegocio codigo, List<String> errores) {
        this.codigo = codigo.getCodigo();
        this.mensaje = codigo.getMensaje();
        this.errores = errores;
        this.fecha = LocalDateTime.now();
    }

}

