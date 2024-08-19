package com.ista.Complexivo7.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "habitacion")
public class Habitacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numHabitacion;

    private String tipo;
    @Size(max = 1500)
    private String descripcion;
    private Double precioNoche;

    @Column(nullable = false)
    private Boolean estado=true;



}
