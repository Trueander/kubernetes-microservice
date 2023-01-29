package com.microservices.mvc.cursos.controller;

import com.microservices.mvc.cursos.entity.Curso;
import com.microservices.mvc.cursos.entity.Usuario;
import com.microservices.mvc.cursos.service.CursoService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(cursoService.listarCursos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Curso> cursoOpt = cursoService.buscarPorIdConUsuarios(id);
        if(cursoOpt.isPresent()) {
            return ResponseEntity.ok(cursoOpt.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result) {

        if(result.hasErrors()) {
            return validar(result);
        }

        Curso cursoCreado = cursoService.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Curso curso, BindingResult result) {



        Optional<Curso> cursoOpt = cursoService.buscarPorId(id);
        if(cursoOpt.isPresent()) {
            if(result.hasErrors()) {
                return validar(result);
            }
            Curso cursoDB = cursoOpt.get();
            cursoDB.setNombre(curso.getNombre());

            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(cursoDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Curso> cursoOpt = cursoService.buscarPorId(id);
        if(cursoOpt.isPresent()) {
            cursoService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioOpt;
        try{
            usuarioOpt = cursoService.asignarUsuario(usuario, cursoId);
        }catch (FeignException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("mensaje",
                                    "No existe el usuario por el id o error en la comunicación " + e.getMessage()));
        }
        if(usuarioOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioOpt.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioOpt;
        try{
            usuarioOpt = cursoService.crearUsuarioCurso(usuario, cursoId);
        }catch (FeignException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("mensaje",
                                    "No se pudo crear el usuario o error en la comunicación" + e.getMessage()));
        }
        if(usuarioOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioOpt.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioOpt;
        try{
            usuarioOpt = cursoService.eliminarUsuarioCurso(usuario, cursoId);
        }catch (FeignException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("mensaje",
                                    "No existe el usuario por el id o error en la comunicación" + e.getMessage()));
        }
        if(usuarioOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioOpt.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id) {
        cursoService.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
