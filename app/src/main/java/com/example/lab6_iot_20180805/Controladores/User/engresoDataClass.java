package com.example.lab6_iot_20180805.Controladores.User;

public class engresoDataClass {

    private String nombre;

    private String numSuper;
    private String codigo;
    private String departamento;
    private String provincia;
    private String distrito;
    private Long ubigeo;
    private Long latitud;
    private Long longitud;
    private String tipodezona;
    private String tipodesitio;

    public engresoDataClass(String nombre, String numSuper, String codigo, String departamento, String provincia, String distrito, Long ubigeo, Long latitud, Long longitud, String tipodezona, String tipodesitio) {
        this.nombre = nombre;
        this.numSuper = numSuper;
        this.codigo = codigo;
        this.departamento = departamento;
        this.provincia = provincia;
        this.distrito = distrito;
        this.ubigeo = ubigeo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tipodezona = tipodezona;
        this.tipodesitio = tipodesitio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNumSuper() {
        return numSuper;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public Long getUbigeo() {
        return ubigeo;
    }

    public Long getLatitud() {
        return latitud;
    }

    public Long getLongitud() {
        return longitud;
    }

    public String getTipodezona() {
        return tipodezona;
    }

    public String getTipodesitio() {
        return tipodesitio;
    }
}
