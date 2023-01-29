package com.microservices.mvc.cursos.service;

import com.microservices.mvc.cursos.entity.Curso;
import com.microservices.mvc.cursos.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listarCursos();
    Optional<Curso> buscarPorId(Long cursoId);
    Curso guardar(Curso curso);
    void eliminarPorId(Long cursoId);

    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId);
    Optional<Usuario> crearUsuarioCurso(Usuario usuario, Long cursoId);
    Optional<Usuario> eliminarUsuarioCurso(Usuario usuario, Long cursoId);
    Optional<Curso> buscarPorIdConUsuarios(Long id);

    void eliminarCursoUsuarioPorId(Long id);
}
