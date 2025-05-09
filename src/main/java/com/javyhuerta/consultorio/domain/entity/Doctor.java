package com.javyhuerta.consultorio.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctor")
public class Doctor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doctor", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido_paterno",nullable = false)
    private String apellidoPaterno;

    @Column(name = "apellido_materno",nullable = false)
    private String apellidoMaterno;

    @Column(name = "especialidad",nullable = false)
    private String especialidad;

    @OneToMany(mappedBy = "doctor")
    private List<Cita> citas;

    public Doctor(Long doctorId){
        this.id = doctorId;
    }

}
