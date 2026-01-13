package com.example.mibibliotecapeliculas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mibibliotecapeliculas.database.DatabaseHelper;
import com.example.mibibliotecapeliculas.R;

import java.util.List;
public class FormPeliculaActivity extends AppCompatActivity {


    EditText etTitulo, etAnio, etValoracion, etDescripcion;
    Spinner spinnerGenero;
    Button btnGuardar, btnSeleccionarPortada;

    DatabaseHelper db;

    int idUsuario;
    int idPelicula = -1;
    int portadaSeleccionada = R.drawable.portada1; // por defecto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pelicula);

        // Referencias UI
        etTitulo = findViewById(R.id.etTitulo);
        etAnio = findViewById(R.id.etAnio);
        etValoracion = findViewById(R.id.etValoracion);
        etDescripcion = findViewById(R.id.etDescripcion);
        spinnerGenero = findViewById(R.id.spinnerGeneroForm);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnSeleccionarPortada = findViewById(R.id.btnSeleccionarPortada);

        db = new DatabaseHelper(this);

        // Datos recibidos
        idUsuario = getIntent().getIntExtra("id_usuario", -1);
        idPelicula = getIntent().getIntExtra("id_pelicula", -1);

        cargarGeneros();

        // Si viene id_pelicula → estamos editando
        if (idPelicula != -1) {
            cargarDatosPelicula();
        }

        btnSeleccionarPortada.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectPortadaActivity.class);
            startActivityForResult(intent, 1);
        });

        btnGuardar.setOnClickListener(v -> guardarPelicula());
    }

    // =====================================================
    // MÉTODOS
    // =====================================================

    private void cargarGeneros() {
        List<String> generos = db.getAllGeneros();
        spinnerGenero.setAdapter(
                new android.widget.ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        generos
                )
        );
    }

    private void guardarPelicula() {

        String titulo = etTitulo.getText().toString().trim();
        String anioStr = etAnio.getText().toString().trim();
        String valoracionStr = etValoracion.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (titulo.isEmpty() || anioStr.isEmpty() || valoracionStr.isEmpty()) {
            Toast.makeText(this, "Rellena los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int anio = Integer.parseInt(anioStr);
        float valoracion = Float.parseFloat(valoracionStr);
        int idGenero = spinnerGenero.getSelectedItemPosition() + 1;
        // (+1 porque los ids empiezan en 1)

        if (idPelicula == -1) {
            // INSERT
            db.addPelicula(
                    titulo,
                    anio,
                    idGenero,
                    valoracion,
                    descripcion,
                    portadaSeleccionada,
                    idUsuario
            );
            Toast.makeText(this, "Película añadida", Toast.LENGTH_SHORT).show();
        } else {
            // UPDATE
            db.updatePelicula(
                    idPelicula,
                    titulo,
                    anio,
                    idGenero,
                    valoracion,
                    descripcion,
                    portadaSeleccionada
            );
            Toast.makeText(this, "Película actualizada", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void cargarDatosPelicula() {
        // (Opcional para ahora, se puede hacer después)
        // Aquí cargarías los datos si editas
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            portadaSeleccionada = data.getIntExtra("portada", R.drawable.portada1);
        }
    }
}
