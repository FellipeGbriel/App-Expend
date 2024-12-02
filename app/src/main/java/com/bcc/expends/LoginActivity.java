package com.bcc.expends;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private BancoDeDadosHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new BancoDeDadosHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                int userId = getUserId(email, password);
                if (userId != -1) {
                    Toast.makeText(LoginActivity.this, "Login bem-sucedido", Toast.LENGTH_SHORT).show();

                    SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("is_logged_in", true);
                    editor.putInt("user_id", userId);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        View btnRecuperarSenha = findViewById(R.id.forgot_password);
        btnRecuperarSenha.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Recuperacao.class);
            startActivity(intent);

        });
    }

    private int getUserId(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT id_usuario FROM usuarios WHERE email = ? AND senha = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        int userId = -1; // valor padrão para id inválido
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario"));
        }
        cursor.close();
        db.close();
        return userId;
    }


}