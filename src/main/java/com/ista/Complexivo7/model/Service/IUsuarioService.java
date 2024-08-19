package com.ista.Complexivo7.model.Service;

import com.ista.Complexivo7.model.entity.Usuario;

import java.util.List;

public interface IUsuarioService {
    public void save(Usuario usuario);
    List<Usuario> findByNombreContainingIgnoreCase(String nombres);
    public Usuario findByUsername(String username);
    public Usuario loadUserByUsername(String username);
}
