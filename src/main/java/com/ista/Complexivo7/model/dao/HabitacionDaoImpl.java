package com.ista.Complexivo7.model.dao;

import com.ista.Complexivo7.model.entity.Habitacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class HabitacionDaoImpl implements IHabitacionDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public Habitacion saveApi(Habitacion habitacion) {
        return saveApi(habitacion);
    }

    @Override
    public List<Habitacion> findAll() {
        return em.createQuery("from Habitacion", Habitacion.class).getResultList();
    }

    @Override
    public void save(Habitacion habitacion) {
        if (habitacion.getId()!= null && habitacion.getId()>0){
            em.merge(habitacion);
        }else{
            em.persist(habitacion);
        }

    }

    @Override
    public Habitacion findOne(Long id) {
        return em.find(Habitacion.class, id);
    }

    @Override
    public void delete(Long id) {
        em.remove(findOne(id));

    }

    @Override
    public List<String> findDistinctTipo() {
        String jpql = "SELECT DISTINCT h.categoria FROM Habitacion h";
        return em.createQuery(jpql, String.class).getResultList();
    }

    @Override
    public List<Habitacion> findProductosByFiltros(String numHabitacion, Boolean estado, String tipo, Double precioNoche) {
        String jpql = "SELECT h FROM Habitacion h WHERE " +
                "(:tipo IS NULL OR h.tipo = :tipo) AND " +
                "(:estado IS NULL OR h.estado = :estado) AND " +
                "(:numHabitacion IS NULL OR LOWER(h.numHabitacion) LIKE LOWER(:numHabitacion)) AND " +
                ":precioNoche IS NULL OR ABS(h.precioNoche - :precioNoche) < 0.01";// Cambiado a `p.cantidad = :cantidad` para Integer

        return em.createQuery(jpql, Habitacion.class)
                .setParameter("tipo", tipo)
                .setParameter("estado", estado)
                .setParameter("numHabitacion", numHabitacion != null ? "%" + numHabitacion.toLowerCase() + "%" : null) // Ajuste para LIKE
                .setParameter("precioNoche", precioNoche)
                .getResultList();
    }

}
