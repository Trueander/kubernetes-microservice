package com.microservices.mvc.cursos.service;

import com.microservices.mvc.cursos.clientes.UsuarioClientRest;
import com.microservices.mvc.cursos.entity.Curso;
import com.microservices.mvc.cursos.entity.CursoUsuario;
import com.microservices.mvc.cursos.entity.Usuario;
import com.microservices.mvc.cursos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioClientRest usuarioClientRest;

    @Override
    public List<Curso> listarCursos() {
        return (List<Curso>) cursoRepository.findAll();
    }

    @Override
    public Optional<Curso> buscarPorId(Long cursoId) {
        return cursoRepository.findById(cursoId);
    }

    @Override
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    public void eliminarPorId(Long cursoId) {
        cursoRepository.deleteById(cursoId);
    }

    @Transactional
    @Override
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if(cursoOpt.isPresent()) {
            Usuario usuarioDB = usuarioClientRest.detalle(usuario.getId());

            Curso cursoGet = cursoOpt.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioDB.getId());
            cursoGet.asignarCursoUsuario(cursoUsuario);
            cursoRepository.save(cursoGet);
            return Optional.of(usuarioDB);
        }


        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<Usuario> crearUsuarioCurso(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if(cursoOpt.isPresent()) {
            Usuario usuarioCreado = usuarioClientRest.crear(usuario);

            Curso cursoGet = cursoOpt.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioCreado.getId());
            cursoGet.asignarCursoUsuario(cursoUsuario);
            cursoRepository.save(cursoGet);
            return Optional.of(usuarioCreado);
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<Usuario> eliminarUsuarioCurso(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if(cursoOpt.isPresent()) {
            Usuario usuarioDB = usuarioClientRest.detalle(usuario.getId());

            Curso cursoGet = cursoOpt.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioDB.getId());
            cursoGet.eliminarCursoUsuario(cursoUsuario);
            cursoRepository.save(cursoGet);
            return Optional.of(usuarioDB);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Curso> buscarPorIdConUsuarios(Long id) {
        Optional<Curso> cursoOpt = cursoRepository.findById(id);

        if(cursoOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            if(!curso.getCursoUsuarios().isEmpty()) {
                List<Long> ids = curso.getCursoUsuarios()
                        .stream()
                        .map(cursoUsuarios -> cursoUsuarios.getUsuarioId())
                        .collect(Collectors.toList());
                List<Usuario> usuarios = usuarioClientRest.listarUsuarioPorCurso(ids);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }

        return Optional.empty();
    }

    @Override
    public void eliminarCursoUsuarioPorId(Long id) {
        cursoRepository.eliminarCursoUsuarioPorId(id);
    }
}
