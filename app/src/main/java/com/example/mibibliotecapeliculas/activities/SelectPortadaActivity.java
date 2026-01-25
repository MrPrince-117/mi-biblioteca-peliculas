package com.example.mibibliotecapeliculas.activities;

/**
 * Actividad para seleccionar una portada de película.
 *
 * Muestra una cuadrícula de imágenes disponibles para que el usuario elija una portada.
 */

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mibibliotecapeliculas.R;

public class SelectPortadaActivity extends AppCompatActivity {

    GridLayout gridPortadas;

    // Array con las portadas disponibles
    int[] portadas = {
            R.drawable.portada1,
            R.drawable.portada2,
            R.drawable.portada3,
            R.drawable.portada4,
            R.drawable.portada5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_portada);

        gridPortadas = findViewById(R.id.gridPortadas);

        // Botón de retroceso
        android.widget.ImageView btBack = findViewById(R.id.btBack);
        if (btBack != null) {
            btBack.setOnClickListener(v -> finish());
        }

        cargarPortadas();
    }

    private void cargarPortadas() {

        // Obtener el ancho de la pantalla
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int imageSize = screenWidth / 2 - 32; // 2 columnas con padding

        for (int portada : portadas) {

            ImageView imageView = new ImageView(this);
            imageView.setImageResource(portada);
            imageView.setPadding(8, 8, 8, 8);

            // Configurar LayoutParams para el GridLayout
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = imageSize;
            params.height = imageSize;
            params.setMargins(8, 8, 8, 8);
            imageView.setLayoutParams(params);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setOnClickListener(v -> {
                Intent result = new Intent();
                result.putExtra("portada", portada);
                setResult(RESULT_OK, result);
                finish();
            });

            gridPortadas.addView(imageView);
        }
    }
}
