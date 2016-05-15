package com.manomanitas.tecnicosapp.PresupuestosPackage;

import java.util.ArrayList;
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

    private void initializeData() {
        lista_presupuestos = new ArrayList<>();
        lista_presupuestos.add(new presupuesto("0", "Electro", "Valencia", "Valencia", "asdasdasda", "¡Gratis!", "0.6 Km", "hace 3 dias"));
        lista_presupuestos.add(new presupuesto("1", "Calefaccion", "Valencia", "Valencia", "agdfhfgj", "¡Gratis!", "1.2 Km", "hace 7 dias"));
        lista_presupuestos.add(new presupuesto("2", "Telefonillo", "Valencia", "Valencia", "llkljkh", "¡Gratis!", "3.0 Km", "hace 5 dias"));
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

    public List<presupuesto> getLista_presupuestos() {
        return lista_presupuestos;
    }
}

