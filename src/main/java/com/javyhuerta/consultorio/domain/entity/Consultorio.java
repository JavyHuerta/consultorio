package com.javyhuerta.consultorio.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consultorio")
public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consultorio", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "piso", nullable = false)
    private Integer piso;

    @OneToMany(mappedBy = "consultorio")
    private List<Cita> citas;

    public Consultorio(Long consultorioId){
        this.id = consultorioId;
    }
}
