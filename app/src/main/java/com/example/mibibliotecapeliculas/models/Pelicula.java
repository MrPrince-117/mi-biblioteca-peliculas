package com.example.mibibliotecapeliculas.models;

public class Pelicula {

    private int id;
    private String titulo;
    private int anio;
    private String genero;
    private float valoracion;
    private int portada;

    public Pelicula(int id, String titulo, int anio, String genero, float valoracion, int portada) {
        this.id = id;
        this.titulo = titulo;
        this.anio = anio;
        this.genero = genero;
        this.valoracion = valoracion;
        this.portada = portada;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getAnio() { return anio; }
    public String getGenero() { return genero; }
    public float getValoracion() { return valoracion; }
    public int getPortada() { return portada; }
}
