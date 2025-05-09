package com.javyhuerta.consultorio.domain.repository;

import com.javyhuerta.consultorio.domain.entity.Cita;
import com.javyhuerta.consultorio.domain.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitasRepository extends JpaRepository<Cita, Long>, JpaSpecificationExecutor<Cita> {

    List<Cita> findByDoctorId(Long doctorId);
    List<Cita> findByConsultorioId(Long consultorioId);
    List<Cita> findByNombrePacienteAndFechaCitaBetween(String nombrePaciente, LocalDateTime desde, LocalDateTime hasta);

}
