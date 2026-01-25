package com.example.mibibliotecapeliculas.activities;

/**
 * Actividad de inicio de sesión.
 *
 * Permite a los usuarios autenticarse con su nombre de usuario y contraseña.
 * También proporciona acceso a la pantalla de registro.
 */

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mibibliotecapeliculas.MainActivity;
import com.example.mibibliotecapeliculas.database.DatabaseHelper;
import com.example.mibibliotecapeliculas.R;

public class LoginActivity extends AppCompatActivity {


    EditText etUsuario, etPassword;
    Button btnLogin;
    TextView tvRegistro;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegistro = findViewById(R.id.tvRegistro);

        db = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String usuario = etUsuario.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int idUsuario = db.loginUsuario(usuario, password);

            if (idUsuario != -1) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("id_usuario", idUsuario);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegistro.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
