package com.javyhuerta.consultorio.mapper;

import com.javyhuerta.consultorio.api.model.CitaModel;
import com.javyhuerta.consultorio.domain.entity.Cita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CitaMapper {

    CitaMapper INSTANCE = Mappers.getMapper(CitaMapper.class);

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "consultorio.id", target = "consultorioId")
    CitaModel citaToCitaModel(Cita citaDb);

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "consultorio.id", target = "consultorioId")
    List<CitaModel> citaToCitaModel(List<Cita> citaDb);
}
