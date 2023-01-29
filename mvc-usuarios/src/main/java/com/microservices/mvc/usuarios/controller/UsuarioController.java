package com.microservices.mvc.usuarios.controller;

import com.microservices.mvc.usuarios.entity.Usuario;
import com.microservices.mvc.usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);

        if(usuario.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(usuario.get());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if(result.hasErrors()) {
            return validar(result);
        }

        if(usuarioService.buscarPorEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(Collections.singletonMap("mensaje", "Ya existe usuario con ese email"));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crearUsuario(usuario));
    }

    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@Valid @PathVariable Long id, @RequestBody Usuario usuario, BindingResult result) {

        if(result.hasErrors()) {
            return validar(result);
        }

        if(usuarioService.buscarPorEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(Collections.singletonMap("mensaje", "Ya existe usuario con ese email"));
        }

        Optional<Usuario> usuarioDB = usuarioService.buscarPorId(id);
        if(usuarioDB.isPresent()) {
            Usuario usuarioGet = usuarioDB.get();

            if(!usuario.getEmail().equalsIgnoreCase(usuarioGet.getEmail())
                    && usuarioService.buscarPorEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(Collections.singletonMap("mensaje", "Ya existe usuario con ese email"));
            }

            usuarioGet.setNombre(usuario.getNombre());
            usuarioGet.setEmail(usuario.getEmail());
            usuarioGet.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(usuarioGet));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);

        if(usuarioOpt.isPresent()) {
            usuarioService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(usuarioService.listarPorIds(ids));
    }

}
