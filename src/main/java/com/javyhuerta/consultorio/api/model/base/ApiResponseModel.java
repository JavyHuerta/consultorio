package com.javyhuerta.consultorio.api.model.base;

import com.javyhuerta.consultorio.util.CodigoNegocio;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Respuesta base de una operación")
public class ApiResponseModel<T> implements Serializable {

    @Schema(description = "Código de negocio de la operación", example = "LI001")
    private String codigo;

    @Schema(description = "Mensaje informatico",example = "Solicitud exitosa")
    private String mensaje;

    @Schema(description = "Respuesta de la operación")
    private T data;

    @Schema(description = "Metadata de la operación")
    private Metadata metadata;

    public ApiResponseModel(CodigoNegocio codigo, T data) {
        this.codigo = codigo.getCodigo();
        this.mensaje = codigo.getMensaje();
        this.data = data;
    }

}