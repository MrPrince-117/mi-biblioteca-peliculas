package com.example.mibibliotecapeliculas;

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

import com.example.mibibliotecapeliculas.adapters.PeliculaAdapter;
import com.example.mibibliotecapeliculas.database.DatabaseHelper;
import com.example.mibibliotecapeliculas.R;
import com.example.mibibliotecapeliculas.models.Pelicula;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }

    ListView listViewPeliculas;
    Button btnNuevaPelicula;
    Spinner spinnerGenero, spinnerOrden;

    DatabaseHelper db;
    PeliculaAdapter adapter;
    List<Pelicula> listaPeliculas;

    int idUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias UI
        listViewPeliculas = findViewById(R.id.listViewPeliculas);
        btnNuevaPelicula = findViewById(R.id.btnNuevaPelicula);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        spinnerOrden = findViewById(R.id.spinnerOrden);

        // Base de datos
        db = new DatabaseHelper(this);

        // Recibir usuario logueado
        idUsuario = getIntent().getIntExtra("id_usuario", -1);

        // Cargar spinners
        cargarSpinners();

        // Cargar lista de películas
        listaPeliculas = new ArrayList<>();
        adapter = new PeliculaAdapter(this, listaPeliculas);
        listViewPeliculas.setAdapter(adapter);

        cargarPeliculas();

        // Click en película → detalle
        listViewPeliculas.setOnItemClickListener((parent, view, position, id) -> {
            Pelicula pelicula = adapter.getItem(position);
            Intent intent = new Intent(MainActivity.this, DetalleActivity.class);
            intent.putExtra("id_pelicula", pelicula.getId());
            startActivity(intent);
        });

        // Botón añadir película
        btnNuevaPelicula.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormPeliculaActivity.class);
            intent.putExtra("id_usuario", idUsuario);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPeliculas();
    }

    // =====================================================
    // MÉTODOS AUXILIARES
    // =====================================================

    private void cargarPeliculas() {
        listaPeliculas.clear();
        listaPeliculas.addAll(db.getPeliculasByUsuario(idUsuario));
        adapter.notifyDataSetChanged();
    }

    private void cargarSpinners() {

        // Spinner de géneros
        List<String> generos = db.getAllGeneros();
        generos.add(0, "Todos");

        ArrayAdapter<String> adapterGeneros =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        generos);
        adapterGeneros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapterGeneros);

        // Spinner de ordenación
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
}