package com.example.mibibliotecapeliculas;

/**
 * Actividad principal de la aplicación.
 *
 * Muestra la lista de películas del usuario con opciones de filtrado y ordenación.
 * Permite navegar a otras pantallas para añadir películas, gestionar géneros y ver estadísticas.
 */

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mibibliotecapeliculas.activities.DetalleActivity;
import com.example.mibibliotecapeliculas.activities.FormPeliculaActivity;
import com.example.mibibliotecapeliculas.activities.GenerosActivity;
import com.example.mibibliotecapeliculas.activities.StatsActivity;
import com.example.mibibliotecapeliculas.adapters.PeliculaAdapter;
import com.example.mibibliotecapeliculas.database.DatabaseHelper;
import com.example.mibibliotecapeliculas.R;
import com.example.mibibliotecapeliculas.models.Pelicula;
import android.widget.AdapterView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listViewPeliculas;
    Button btnNuevaPelicula;
    Button btnGestionGeneros;
    Button btnStats;

    Spinner spinnerGenero, spinnerOrden;

    DatabaseHelper db;
    PeliculaAdapter adapter;
    List<Pelicula> listaPeliculas;

    int idUsuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Referencias UI
        listViewPeliculas = findViewById(R.id.listViewPeliculas);
        btnNuevaPelicula = findViewById(R.id.btnNuevaPelicula);
        btnGestionGeneros = findViewById(R.id.btnGestionGeneros);
        btnStats = findViewById(R.id.btnStats);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        spinnerOrden = findViewById(R.id.spinnerOrden);

        // Base de datos
        db = new DatabaseHelper(this);

        //  Usuario logueado
        idUsuario = getIntent().getIntExtra("id_usuario", -1);

        //  Spinners
        cargarSpinners();

        //  ListView
        listaPeliculas = new ArrayList<>();
        adapter = new PeliculaAdapter(this, listaPeliculas);
        listViewPeliculas.setAdapter(adapter);

        //  Listeners
        listViewPeliculas.setOnItemClickListener((parent, view, position, id) -> {
            Pelicula pelicula = adapter.getItem(position);
            Intent intent = new Intent(MainActivity.this, DetalleActivity.class);
            intent.putExtra("id_pelicula", pelicula.getId());
            startActivity(intent);
        });

        btnNuevaPelicula.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormPeliculaActivity.class);
            intent.putExtra("id_usuario", idUsuario);
            startActivity(intent);
        });

        btnGestionGeneros.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GenerosActivity.class));
        });
        btnStats.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatsActivity.class);
            intent.putExtra("id_usuario", idUsuario);
            startActivity(intent);
        });

        spinnerGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerOrden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Primera carga
        aplicarFiltros();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPeliculas();
    }



    // MÉTODOS AUXILIARES
    private void cargarPeliculas() {
        listaPeliculas.clear();
        listaPeliculas.addAll(db.getPeliculasByUsuario(idUsuario));
        adapter.notifyDataSetChanged();
    }

    private void cargarSpinners() {

        // Spinner géneros
        List<String> generos = db.getAllGeneros();
        generos.add(0, "Todos");

        ArrayAdapter<String> adapterGeneros =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        generos);
        adapterGeneros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapterGeneros);

        // Spinner ordenación
        List<String> orden = new ArrayList<>();
        orden.add("Año");
        orden.add("Valoración");

        ArrayAdapter<String> adapterOrden =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        orden);
        adapterOrden.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrden.setAdapter(adapterOrden);
    }

    // para aplicar filtros
    private void aplicarFiltros() {

        String generoSeleccionado = spinnerGenero.getSelectedItem().toString();
        String ordenSeleccionado = spinnerOrden.getSelectedItem().toString();

        listaPeliculas.clear();
        listaPeliculas.addAll(
                db.getPeliculasFiltradas(
                        idUsuario,
                        generoSeleccionado,
                        ordenSeleccionado
                )
        );
        adapter.notifyDataSetChanged();
    }

}