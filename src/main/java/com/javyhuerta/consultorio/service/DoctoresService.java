package com.javyhuerta.consultorio.service;

import com.javyhuerta.consultorio.api.model.DoctorModel;
import com.javyhuerta.consultorio.domain.entity.Doctor;
import com.javyhuerta.consultorio.domain.repository.DoctoresRepository;
import com.javyhuerta.consultorio.domain.specification.DoctoresSpecification;
import com.javyhuerta.consultorio.mapper.DoctorMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class DoctoresService {

    private final DoctoresRepository repository;

    public Page<DoctorModel> consultarDoctores(DoctoresSpecification doctoresSpecification, Pageable pageable) {
        log.info("consultorio.DoctoresService.consultarDoctores");

        Page<Doctor> doctoresPage = repository.findAll(doctoresSpecification,pageable);
        List<DoctorModel> response = DoctorMapper.INSTANCE.doctorToDoctorModel(doctoresPage.getContent());

        return new PageImpl<>(response, doctoresPage.getPageable(), doctoresPage.getTotalElements());

    }

}
