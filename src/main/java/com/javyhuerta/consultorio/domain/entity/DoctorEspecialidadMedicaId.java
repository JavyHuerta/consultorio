package com.javyhuerta.consultorio.domain.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorEspecialidadMedicaId {

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private Doctor doctorId;

    @ManyToOne
    @JoinColumn(name = "id_especialidad_medica")
    private EspecialidadMedica especialidadMedicaId;

}
