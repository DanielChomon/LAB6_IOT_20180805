package com.example.lab6_iot_20180805.entity;

import com.google.firebase.Timestamp;

public class Egreso {
    private String titulo;
    private Double monto;
    private String descripcion;
    private Timestamp fecha;
    private String usuario;

    public Egreso() {
    }

    public Egreso(String titulo, Double monto, String descripcion, Timestamp fecha, String usuario) {
        this.titulo = titulo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.usuario = usuario;
    }

    // Getters
    public String getTitulo() {
        return titulo;
    }

    public Double getMonto() {
        return monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    // Setters
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public void setUsuario(String usuario) {

    }
}




