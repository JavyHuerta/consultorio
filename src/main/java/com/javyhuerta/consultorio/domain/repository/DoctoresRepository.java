package com.javyhuerta.consultorio.domain.repository;

import com.javyhuerta.consultorio.domain.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctoresRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {
}
