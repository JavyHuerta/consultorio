package com.javyhuerta.consultorio.domain.repository;

import com.javyhuerta.consultorio.domain.entity.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ConsultoriosRepository extends JpaRepository<Consultorio, Long>, JpaSpecificationExecutor<Consultorio> {

    boolean existsById(Long id);

}
