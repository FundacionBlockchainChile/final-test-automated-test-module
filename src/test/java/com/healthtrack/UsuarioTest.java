package com.healthtrack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {
    private Usuario usuario;
    private static final String NOMBRE_TEST = "Juan";
    private static final double PESO_INICIAL = 70.0;
    private static final double PESO_NUEVO = 72.0;
    private static final double DELTA = 0.001;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(NOMBRE_TEST, PESO_INICIAL);
    }

    @Test
    void testConstructor() {
        assertEquals(NOMBRE_TEST, usuario.getNombre());
        assertEquals(PESO_INICIAL, usuario.getPeso(), DELTA);
    }

    @Test
    void testGetNombre() {
        assertEquals(NOMBRE_TEST, usuario.getNombre());
    }

    @Test
    void testGetPeso() {
        assertEquals(PESO_INICIAL, usuario.getPeso(), DELTA);
    }

    @Test
    void testActualizarPeso() {
        usuario.actualizarPeso(PESO_NUEVO);
        // El test fallará porque el método tiene un error
        assertEquals(PESO_NUEVO, usuario.getPeso(), DELTA);
    }
} 