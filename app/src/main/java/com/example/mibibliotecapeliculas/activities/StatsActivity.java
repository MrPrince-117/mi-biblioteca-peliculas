package com.example.mibibliotecapeliculas.activities;

/**
 * Actividad que muestra estadísticas de las películas del usuario.
 *
 * Muestra el total de películas, distribución por género y media de valoración.
 */

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mibibliotecapeliculas.database.DatabaseHelper;
import com.example.mibibliotecapeliculas.R;

import java.util.Map;
public class StatsActivity extends AppCompatActivity {

    TextView tvTotalPeliculas, tvPorGenero, tvMediaValoracion;
    DatabaseHelper db;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        tvTotalPeliculas = findViewById(R.id.tvTotalPeliculas);
        tvPorGenero = findViewById(R.id.tvPorGenero);
        tvMediaValoracion = findViewById(R.id.tvMediaValoracion);

        // Botón de retroceso
        android.widget.ImageView btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(v -> finish());

        db = new DatabaseHelper(this);

        idUsuario = getIntent().getIntExtra("id_usuario", -1);

        cargarEstadisticas();
    }

    private void cargarEstadisticas() {

        int total = db.getTotalPeliculas(idUsuario);
        double media = db.getMediaValoracion(idUsuario);
        Map<String, Integer> porGenero = db.getPeliculasPorGenero(idUsuario);

        tvTotalPeliculas.setText("Total de películas: " + total);
        tvMediaValoracion.setText("Valoración media: " + String.format("%.2f", media));

        StringBuilder sb = new StringBuilder("Películas por género:\n");
        for (String genero : porGenero.keySet()) {
            sb.append("- ").append(genero)
                    .append(": ").append(porGenero.get(genero)).append("\n");
        }
        tvPorGenero.setText(sb.toString());
    }
}
