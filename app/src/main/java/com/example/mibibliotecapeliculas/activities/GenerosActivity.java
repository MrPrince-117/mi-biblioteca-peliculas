package com.example.mibibliotecapeliculas.activities;

/**
 * Actividad para gestionar los géneros de películas.
 *
 * Permite añadir nuevos géneros y ver la lista de géneros existentes.
 */

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mibibliotecapeliculas.database.DatabaseHelper;
import com.example.mibibliotecapeliculas.R;

import java.util.ArrayList;
import java.util.List;
public class GenerosActivity extends AppCompatActivity {

    EditText etNuevoGenero;
    Button btnAgregarGenero;
    ListView listViewGeneros;

    DatabaseHelper db;
    ArrayAdapter<String> adapter;
    List<String> listaGeneros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generos);

        etNuevoGenero = findViewById(R.id.etNuevoGenero);
        btnAgregarGenero = findViewById(R.id.btnAgregarGenero);
        listViewGeneros = findViewById(R.id.listViewGeneros);

        // Botón de retroceso
        android.widget.ImageView btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(v -> finish());

        db = new DatabaseHelper(this);

        listaGeneros = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaGeneros
        );
        listViewGeneros.setAdapter(adapter);

        cargarGeneros();

        btnAgregarGenero.setOnClickListener(v -> {
            String genero = etNuevoGenero.getText().toString().trim();

            if (genero.isEmpty()) {
                Toast.makeText(this, "Introduce un género", Toast.LENGTH_SHORT).show();
                return;
            }

            db.addGenero(genero);
            etNuevoGenero.setText("");
            cargarGeneros();
        });
    }

    private void cargarGeneros() {
        listaGeneros.clear();
        listaGeneros.addAll(db.getAllGeneros());
        adapter.notifyDataSetChanged();
    }
}
