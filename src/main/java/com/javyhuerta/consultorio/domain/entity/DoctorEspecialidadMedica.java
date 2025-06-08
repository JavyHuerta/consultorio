package com.javyhuerta.consultorio.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "doctor_especialidad_medica")
public class DoctorEspecialidadMedica {
    
    @EmbeddedId
    private DoctorEspecialidadMedicaId id;

}
