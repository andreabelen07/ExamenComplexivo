package com.ista.Complexivo7.model.Service;

import com.ista.Complexivo7.model.dao.IHabitacionDao;
import com.ista.Complexivo7.model.entity.Habitacion;
import com.ista.Complexivo7.model.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HabitacionServiceImpl implements IHabitacionService {
    @Autowired
    private IHabitacionDao habitacionDao;


    @Override
    public Habitacion saveApi(Habitacion habitacion) {
        return habitacionDao.saveApi(habitacion);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Habitacion> findAll() {
        return habitacionDao.findAll();
    }

    @Transactional
    @Override
    public void save(Habitacion habitacion) {
        habitacionDao.save(habitacion);

    }
    @Transactional(readOnly = true)
    @Override
    public Habitacion findOne(Long id) {
        return habitacionDao.findOne(id);
    }


    @Transactional
    @Override
    public void delete(Long id) {
        habitacionDao.delete(id);

    }
    @Transactional(readOnly = true)
    @Override
    public List<String> findDistinctTipo() {
        return habitacionDao.findDistinctTipo();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Habitacion> findProductosByFiltros(String numHabitacion, Boolean estado, String tipo, Double precioNoche) {
        return habitacionDao.findProductosByFiltros(numHabitacion,estado,tipo,precioNoche);
    }
}
