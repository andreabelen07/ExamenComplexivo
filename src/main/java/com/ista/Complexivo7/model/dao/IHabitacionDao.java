package com.ista.Complexivo7.model.dao;

import com.ista.Complexivo7.model.entity.Habitacion;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IHabitacionDao {
    public Habitacion saveApi(Habitacion habitacion);
    public List<Habitacion> findAll();
    public void save(Habitacion habitacion);
    public Habitacion findOne(Long id);
    public void delete(Long id);

    @Query("SELECT DISTINCT h.tipo FROM Habitacion h")
    List<String> findDistinctTipo();
    List<Habitacion> findProductosByFiltros(String numHabitacion, Boolean estado, String tipo, Double precioNoche);

}
