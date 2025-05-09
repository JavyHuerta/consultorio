package com.javyhuerta.consultorio.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum CodigoNegocio {
    EXITO("CM001","Solicitud exitosa"),
    ERROR("CM002","Solicitud fallida"),
    CITAS_EXECIDAS("CM003","Un mismo doctor no puede tener más de 8 citas en el día"),
    CITAS_EMPALMADA("CM004","No se puede agendar cita para un mismo Dr. a la misma hora"),
    CONSULTORIO_OCUPADO("CM005","No se puede agendar cita en un mismo consultorio a la misma hora"),
    DOCTOR_OCUPADO("CM006","El doctor ya tiene una cita en ese horario"),
    CONFLICTO_HORARIO("CM007","No se puede agendar cita para un paciente a la una misma hora ni con menos de 2 horas de diferencia para el mismo día");
    private final String codigo;
    private final String mensaje;



    public static Optional<CodigoNegocio> findByCode(String codigo) {
        return Arrays.stream(values())
                .filter(e -> e.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }
}