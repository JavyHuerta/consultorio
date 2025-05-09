package com.javyhuerta.consultorio.mapper;

import com.javyhuerta.consultorio.api.model.DoctorModel;
import com.javyhuerta.consultorio.domain.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DoctorMapper {
    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    List<DoctorModel> doctorToDoctorModel(List<Doctor> doctores);
}
