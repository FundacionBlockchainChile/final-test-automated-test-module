package com.healthtrack;

public class Usuario {
    private long id;
    private String nombre;
    private double peso;

    public Usuario(String nombre, double peso) {
        this.nombre = nombre;
        this.peso = peso;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void actualizarPeso(double nuevoPeso) {
        // Corregido: Asignar el nuevo peso en lugar de restar 1kg
        this.peso = nuevoPeso;
    }

    public void mostrarInformacion() {
        System.out.println("Usuario: " + nombre + ", Peso Actual: " + peso + " kg");
    }
} 