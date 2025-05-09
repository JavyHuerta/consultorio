package com.javyhuerta.consultorio.service;

import com.javyhuerta.consultorio.api.model.CitaModel;
import com.javyhuerta.consultorio.api.model.DoctorModel;
import com.javyhuerta.consultorio.api.model.RegistrarCitaModel;
import com.javyhuerta.consultorio.domain.entity.Cita;
import com.javyhuerta.consultorio.domain.entity.Consultorio;
import com.javyhuerta.consultorio.domain.entity.Doctor;
import com.javyhuerta.consultorio.domain.entity.EstadoCita;
import com.javyhuerta.consultorio.domain.repository.CitasRepository;
import com.javyhuerta.consultorio.domain.repository.ConsultoriosRepository;
import com.javyhuerta.consultorio.domain.repository.DoctoresRepository;
import com.javyhuerta.consultorio.domain.specification.CitasSpecification;
import com.javyhuerta.consultorio.exception.CitaInvalidaException;
import com.javyhuerta.consultorio.mapper.CitaMapper;
import com.javyhuerta.consultorio.mapper.DoctorMapper;
import com.javyhuerta.consultorio.util.CodigoNegocio;
import com.javyhuerta.consultorio.util.Constantes;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Log4j2
@Service
@AllArgsConstructor
public class CitasService {

    private final CitasRepository citasRepository;
    private final ConsultoriosRepository consultoriosRepository;
    private final DoctoresRepository doctoresRepository;

    public CitaModel registrarCita(RegistrarCitaModel citaModel){
        log.info("consultorio.CitasService.registrarCita");

        validarExisteDoctor(citaModel.getDoctorId());
        validarExisteConsultorio(citaModel.getConsultorioId());

        // Consultar citas por doctor
        List<Cita> doctorCitasAgendadas = citasRepository.findByDoctorId(citaModel.getDoctorId());


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

        Cita citaDb = citasRepository.save(cita);

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
        List<Cita> citasPorConsultorio = citasRepository.findByConsultorioId(consultorioId);

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
        List<Cita> citasPacienteMismoDia = citasRepository.findByNombrePacienteAndFechaCitaBetween(
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
            throw new CitaInvalidaException(CodigoNegocio.CITAS_EXCEDIDAS);
        }
    }

    private void validarExisteDoctor(long idDoctor){
        log.info("consultorio.CitasService.validarExisteDoctor");
        boolean existeDoctor = doctoresRepository.existsById(idDoctor);

        if (!existeDoctor){
            throw new CitaInvalidaException(CodigoNegocio.DOCTOR_NO_EXISTE);
        }
    }

    private void validarExisteConsultorio(long idConsultorio){
        log.info("consultorio.CitasService.validarExisteConsultorio");

        boolean existeConsultorio = consultoriosRepository.existsById(idConsultorio);

        if (!existeConsultorio){
            throw new CitaInvalidaException(CodigoNegocio.CONSULTORIO_NO_EXISTE);
        }
    }

    public Page<CitaModel> consultarCitas(CitasSpecification citasSpecification,Pageable pageable) {
        log.info("consultorio.CitasService.consultarCitas");

        Page<Cita> citaPage = citasRepository.findAll(citasSpecification,pageable);
        List<CitaModel> response = CitaMapper.INSTANCE.citaToCitaModel(citaPage.getContent());

        return new PageImpl<>(response, citaPage.getPageable(), citaPage.getTotalElements());
    }
}
