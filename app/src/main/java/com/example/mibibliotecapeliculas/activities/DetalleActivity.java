package com.example.mibibliotecapeliculas.activities;

/**
 * Actividad que muestra los detalles completos de una película.
 *
 * Permite ver toda la información de una película y ofrece opciones para editarla o eliminarla.
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mibibliotecapeliculas.database.DatabaseHelper;
import com.example.mibibliotecapeliculas.R;
import com.example.mibibliotecapeliculas.models.Pelicula;

public class DetalleActivity extends AppCompatActivity {

    ImageView imgPortada;
    TextView tvTitulo, tvAnio, tvGenero, tvValoracion, tvDescripcion;
    Button btnEditar, btnEliminar;

    DatabaseHelper db;
    int idPelicula;

    Pelicula pelicula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        // Referencias UI
        imgPortada = findViewById(R.id.imgPortada);
        tvTitulo = findViewById(R.id.tvTitulo);
        tvAnio = findViewById(R.id.tvAnio);
        tvGenero = findViewById(R.id.tvGenero);
        tvValoracion = findViewById(R.id.tvValoracion);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Botón de retroceso
        ImageView btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(v -> finish());

        db = new DatabaseHelper(this);

        // Recibir id de la película
        idPelicula = getIntent().getIntExtra("id_pelicula", -1);

        if (idPelicula == -1) {
            finish();
            return;
        }

        cargarPelicula();

        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(DetalleActivity.this, FormPeliculaActivity.class);
            intent.putExtra("id_pelicula", idPelicula);
            startActivity(intent);
        });

        btnEliminar.setOnClickListener(v -> confirmarEliminacion());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPelicula();
    }

    // =====================================================
    // MÉTODOS
    // =====================================================

    private void cargarPelicula() {
        pelicula = db.getPeliculaById(idPelicula);

        if (pelicula != null) {
            imgPortada.setImageResource(pelicula.getPortada());
            tvTitulo.setText(pelicula.getTitulo());
            tvAnio.setText("Año: " + pelicula.getAnio());
            tvGenero.setText("Género: " + pelicula.getGenero());
            tvValoracion.setText("Valoración: " + pelicula.getValoracion());
            tvDescripcion.setText(pelicula.getDescripcion());
        }
    }

    private void confirmarEliminacion() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar película")
                .setMessage("¿Estás seguro de que quieres eliminar esta película?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    db.deletePelicula(idPelicula);
                    Toast.makeText(this, "Película eliminada", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
