package com.javyhuerta.consultorio.service;

import com.javyhuerta.consultorio.api.model.CitaModel;
import com.javyhuerta.consultorio.api.model.RegistrarCitaModel;
import com.javyhuerta.consultorio.domain.entity.Cita;
import com.javyhuerta.consultorio.domain.entity.Consultorio;
import com.javyhuerta.consultorio.domain.entity.Doctor;
import com.javyhuerta.consultorio.domain.entity.EstadoCita;
import com.javyhuerta.consultorio.domain.repository.CitasRepository;
import com.javyhuerta.consultorio.exception.CitaInvalidaException;
import com.javyhuerta.consultorio.mapper.CitaMapper;
import com.javyhuerta.consultorio.util.CodigoNegocio;
import com.javyhuerta.consultorio.util.Constantes;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Log4j2
@Service
@AllArgsConstructor
public class CitasService {

    private final CitasRepository repository;

    public CitaModel registrarCita(RegistrarCitaModel citaModel){
        log.info("consultorio.CitasService.registrarCita");

        // Consultar citas por doctor
        List<Cita> doctorCitasAgendadas = repository.findByDoctorId(citaModel.getDoctorId());


        validarConsultorioOcupado(citaModel.getConsultorioId(),citaModel.getFechaCita());
        validarCitaMismaHora(doctorCitasAgendadas, citaModel.getFechaCita());
        validarConflictoHorario(citaModel.getNombrePaciente(), citaModel.getFechaCita());
        validarCitasMaximas(doctorCitasAgendadas, citaModel.getFechaCita());

        Cita cita = new Cita();
        cita.setNombrePaciente(citaModel.getNombrePaciente());
        cita.setFechaCita(citaModel.getFechaCita());
        cita.setConsultorio(new Consultorio(citaModel.getConsultorioId()));
        cita.setDoctor(new Doctor(citaModel.getDoctorId()));
        cita.setEstado(EstadoCita.PENDIENTE);

        Cita citaDb = repository.save(cita);

         return CitaMapper.INSTANCE.citaToCitaModel(citaDb);

    }

    /**
     * No se puede agendar cita en un mismo consultorio a la misma hora.
     *
     * @param consultorioId
     * @param fechaCita
     */
    private void validarConsultorioOcupado(long consultorioId, LocalDateTime fechaCita){
        log.info("consultorio.CitasService.validarConsultorioOcupado");

        // Consultar citas por consultorio
        List<Cita> citasPorConsultorio = repository.findByConsultorioId(consultorioId);

        boolean isConsultorioOcupago = citasPorConsultorio
                .stream()
                .anyMatch(cita -> cita.getFechaCita().equals(fechaCita));

        if (isConsultorioOcupago)
            throw new CitaInvalidaException(CodigoNegocio.CONSULTORIO_OCUPADO);

    }

    /**
     * No se puede agendar cita para un mismo Dr. a la misma hora
     *
     * @param doctorCitasAgendadas
     * @param fechaCita
     */
    private void validarCitaMismaHora(List<Cita> doctorCitasAgendadas, LocalDateTime fechaCita){
        log.info("consultorio.CitasService.validarCitaMismaHora");

        boolean existeCita = doctorCitasAgendadas.stream()
                .anyMatch(cita -> cita.getFechaCita().equals(fechaCita));

        if (existeCita) {
            throw new CitaInvalidaException(CodigoNegocio.DOCTOR_OCUPADO);
        }
    }

    /**
     * No se puede agendar cita para un paciente a la una misma hora ni con menos de 2 horas
     * de diferencia para el mismo día. Es decir, si un paciente tiene 1 cita de 2 a 3pm, solo
     * podría tener otra el mismo día a partir de las 5pm
     *
     * @param nombrePaciente
     * @param fechaCita
     */
    private void validarConflictoHorario(String nombrePaciente, LocalDateTime fechaCita){
        log.info("consultorio.CitasService.validarConflictoHorario");

        // Consultar todas las citas del paciente en el mismo dia
        List<Cita> citasPacienteMismoDia = repository.findByNombrePacienteAndFechaCitaBetween(
                nombrePaciente,
                fechaCita.toLocalDate().atStartOfDay(),         // Inicio del día (00:00)
                fechaCita.toLocalDate().atTime(LocalTime.MAX)   // Fin del dia (23:59:59.999)
        );

        boolean pacienteConflictoHorario = citasPacienteMismoDia
                .stream()
                .anyMatch(cita -> Math.abs(Duration.between(cita.getFechaCita(),fechaCita).toHours()) < Constantes.DIFERENCIA_HORAS_ENTRE_CITAS);

        if(pacienteConflictoHorario){
            throw new CitaInvalidaException(CodigoNegocio.CONFLICTO_HORARIO);
        }
    }

    /**
     * Un mismo doctor no puede tener más de 8 citas en el día.
     *
     * @param doctorCitasAgendadas
     * @param fechaCita
     */
    private void validarCitasMaximas( List<Cita> doctorCitasAgendadas, LocalDateTime fechaCita){
        log.info("consultorio.CitasService.validarCitasMaximas");

        long citasAgendadasDoctor = doctorCitasAgendadas
                .stream()
                .filter(cita -> cita.getFechaCita().toLocalDate().equals(fechaCita.toLocalDate()))
                .count();

        if (citasAgendadasDoctor >= Constantes.CITAS_MAXIMAS_POR_DIA){
            throw new CitaInvalidaException(CodigoNegocio.CITAS_EXECIDAS);
        }
    }
}
