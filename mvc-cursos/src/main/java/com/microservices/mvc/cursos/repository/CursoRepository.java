package com.microservices.mvc.cursos.repository;

import com.microservices.mvc.cursos.entity.Curso;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso, Long> {

    @Modifying
    @Query("FROM CursoUsuario cu WHERE cu.usuarioId=?1")
    void eliminarCursoUsuarioPorId(Long id);
}
