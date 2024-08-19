package com.ista.Complexivo7.model.Service;

import com.ista.Complexivo7.model.dao.IUsuarioDao;
import com.ista.Complexivo7.model.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class UsuarioServiceComple implements IUsuarioService{
    @Autowired
    private IUsuarioDao usuarioDao;

    @Transactional
    @Override
    public void save(Usuario usuario) {
        usuarioDao.save(usuario);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Usuario> findByNombreContainingIgnoreCase(String nombres) {
        return usuarioDao.findByNombreContainingIgnoreCase(nombres);
    }
    @Transactional(readOnly = true)
    @Override
    public Usuario findByUsername(String username) {
        return usuarioDao.findByUsername(username);
    }
    @Transactional(readOnly = true)
    @Override
    public Usuario loadUserByUsername(String username) {
        return usuarioDao.loadUserByUsername(username);
    }
}
