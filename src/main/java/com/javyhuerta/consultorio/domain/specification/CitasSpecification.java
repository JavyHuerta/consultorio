package com.javyhuerta.consultorio.domain.specification;

import com.javyhuerta.consultorio.domain.entity.Cita;
import com.javyhuerta.consultorio.domain.entity.Consultorio;
import com.javyhuerta.consultorio.domain.entity.Doctor;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CitasSpecification implements Specification<Cita> {

    private String q;

    @Override
    public Predicate toPredicate(Root<Cita> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        if (!StringUtils.hasText(q)) {
            return null;
        }

        List<Predicate> predicatesOr = new ArrayList<>();

        // Buscar por fecha (formato parcial, por ejemplo "2025-05-09")
        Expression<String> fechaCitaStr = cb.function("TO_CHAR", String.class, root.get("fechaCita"), cb.literal("YYYY-MM-DD"));
        predicatesOr.add(cb.like(fechaCitaStr, "%" + q + "%"));

        // Join con Doctor y buscar por nombre
        Join<Cita, Doctor> doctorJoin = root.join("doctor", JoinType.LEFT);
        predicatesOr.add(cb.like(cb.lower(doctorJoin.get("nombre")), "%" + q.toLowerCase() + "%"));

        // Join con Consultorio y buscar por id como string
        Join<Cita, Consultorio> consultorioJoin = root.join("consultorio", JoinType.LEFT);
        Expression<String> consultorioIdStr = cb.toString(consultorioJoin.get("id"));
        predicatesOr.add(cb.like(consultorioIdStr, "%" + q + "%"));

        return cb.or(predicatesOr.toArray(new Predicate[0]));
    }
}
