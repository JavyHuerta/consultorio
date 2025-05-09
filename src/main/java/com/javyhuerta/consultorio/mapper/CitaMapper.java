package com.javyhuerta.consultorio.mapper;

import com.javyhuerta.consultorio.api.model.CitaModel;
import com.javyhuerta.consultorio.domain.entity.Cita;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CitaMapper {

    CitaMapper INSTANCE = Mappers.getMapper(CitaMapper.class);

    CitaModel citaToCitaModel(Cita citaDb);
}
