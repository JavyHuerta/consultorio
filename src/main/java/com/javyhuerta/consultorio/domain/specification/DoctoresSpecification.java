package com.javyhuerta.consultorio.domain.specification;

import com.javyhuerta.consultorio.domain.entity.Doctor;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
public class DoctoresSpecification implements Specification<Doctor> {

    private String q;

    @Override
    public Predicate toPredicate(Root<Doctor> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicatesOr = new ArrayList<>();

        if (StringUtils.hasText(q)) {
            predicatesOr.add(criteriaBuilder.like(root.get("nombre"), "%" + q + "%"));
            predicatesOr.add(criteriaBuilder.like(root.get("apellidoPaterno"), "%" + q + "%"));
            predicatesOr.add(criteriaBuilder.like(root.get("apellidoMaterno"), "%" + q + "%"));
            predicatesOr.add(criteriaBuilder.like(root.get("especialidad"), "%" + q + "%"));
        }

        if (!predicatesOr.isEmpty()){
            return criteriaBuilder.or(predicatesOr.toArray(new Predicate[0]));
        }


        return null;
    }
}
