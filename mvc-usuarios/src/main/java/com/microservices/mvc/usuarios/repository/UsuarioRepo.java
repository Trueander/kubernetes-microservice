package com.microservices.mvc.usuarios.repository;

import com.microservices.mvc.usuarios.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepo extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
