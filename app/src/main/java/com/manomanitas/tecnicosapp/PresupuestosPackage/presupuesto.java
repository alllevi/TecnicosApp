package com.manomanitas.tecnicosapp.PresupuestosPackage;

import java.util.List;

/**
 * Created by Alejandro on 12/05/2016.
 */
public class presupuesto {

    private String id;
    private String categoria;
    private String municipio;
    private String provincia;
    private String averia;
    private String precio;
    private String kilometros;
    private String haceDias;

    //Para los presupuestos comprados
    private String nombre;
    private String telefono;
    private String email;

    private List<presupuesto> lista_presupuestos;

    public presupuesto(String id, String categoria, String municipio, String provincia, String averia, String precio, String kilometros, String haceDias) {
        this.id = id;
        this.categoria = categoria;
        this.municipio = municipio;
        this.provincia = provincia;
        this.averia = averia;
        this.precio = precio;
        this.kilometros = kilometros;
        this.haceDias = haceDias;
    }

    public presupuesto(String id, String categoria, String municipio, String provincia, String averia, String kilometros, String haceDias, String nombre, String telefono, String email) {
        this.id = id;
        this.categoria = categoria;
        this.municipio = municipio;
        this.provincia = provincia;
        this.averia = averia;
        this.kilometros = kilometros;
        this.haceDias = haceDias;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getAveria() {
        return averia;
    }

    public String getPrecio() {
        return precio;
    }

    public String getKilometros() {
        return kilometros;
    }

    public String getHaceDias() {
        return haceDias;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}

