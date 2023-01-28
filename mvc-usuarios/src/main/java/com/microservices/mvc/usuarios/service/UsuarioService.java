package com.microservices.mvc.usuarios.service;

import com.microservices.mvc.usuarios.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listarUsuarios();
    Optional<Usuario> buscarPorId(Long usuarioId);
    Usuario crearUsuario(Usuario usuario);
    void eliminarPorId(Long usuarioId);
}
