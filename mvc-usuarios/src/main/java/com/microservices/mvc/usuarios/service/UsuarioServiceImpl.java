package com.microservices.mvc.usuarios.service;

import com.microservices.mvc.usuarios.entity.Usuario;
import com.microservices.mvc.usuarios.repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepo usuarioRepo;


    @Override
    public List<Usuario> listarUsuarios() {
        return (List<Usuario>) usuarioRepo.findAll();
    }

    @Override
    public Optional<Usuario> buscarPorId(Long usuarioId) {
        return usuarioRepo.findById(usuarioId);
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepo.save(usuario);
    }

    @Override
    public void eliminarPorId(Long usuarioId) {
        usuarioRepo.deleteById(usuarioId);
    }
}
