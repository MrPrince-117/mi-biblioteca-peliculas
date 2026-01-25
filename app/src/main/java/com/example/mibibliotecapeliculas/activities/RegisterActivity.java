package com.example.mibibliotecapeliculas.activities;

/**
 * Actividad de registro de nuevos usuarios.
 *
 * Permite crear una cuenta nueva con nombre de usuario y contraseña.
 */

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mibibliotecapeliculas.database.DatabaseHelper;
import com.example.mibibliotecapeliculas.R;
public class RegisterActivity extends AppCompatActivity {

    EditText etNuevoUsuario, etNuevaPassword;
    Button btnRegistrar;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNuevoUsuario = findViewById(R.id.etNuevoUsuario);
        etNuevaPassword = findViewById(R.id.etNuevaPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Botón de retroceso
        android.widget.ImageView btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(v -> finish());

        db = new DatabaseHelper(this);

        btnRegistrar.setOnClickListener(v -> {
            String usuario = etNuevoUsuario.getText().toString().trim();
            String password = etNuevaPassword.getText().toString().trim();

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            db.addUsuario(usuario, password);
            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
