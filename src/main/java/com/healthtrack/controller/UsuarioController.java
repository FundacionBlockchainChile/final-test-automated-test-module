package com.healthtrack.controller;

import com.healthtrack.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final Map<Long, Usuario> usuarios = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong();

    @PostMapping
    public ResponseEntity<Usuario> registrarUsuario(@RequestParam String nombre, @RequestParam double peso) {
        Usuario usuario = new Usuario(nombre, peso);
        long id = counter.incrementAndGet();
        usuario.setId(id);
        usuarios.put(id, usuario);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}/peso")
    public ResponseEntity<Usuario> actualizarPeso(@PathVariable Long id, @RequestParam double peso) {
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        usuario.setPeso(peso);
        return ResponseEntity.ok(usuario);
    }
} 