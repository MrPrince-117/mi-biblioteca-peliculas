package com.example.mibibliotecapeliculas.models;

public class Pelicula {

    private int id;
    private String titulo;
    private int anio;
    private String genero;
    private float valoracion;
    private String descripcion;
    private int portada;

    // Constructor principal
    public Pelicula(int id, String titulo, int anio, String genero, float valoracion, int portada) {
        this.id = id;
        this.titulo = titulo;
        this.anio = anio;
        this.genero = genero;
        this.valoracion = valoracion;
        this.portada = portada;
    }

    //GETTERS
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getAnio() { return anio; }
    public String getGenero() { return genero; }
    public float getValoracion() { return valoracion; }
    public String getDescripcion() { return descripcion; }
    public int getPortada() { return portada; }

    //SETTERS
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
