package com.bcc.expends;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.Locale;

public class LogoutActivity extends AppCompatActivity {

    BancoDeDadosHelper bancoDeDadosHelper;

    Button voltar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.logout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bancoDeDadosHelper = new BancoDeDadosHelper(this);

        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);

        TextView tvNome = findViewById(R.id.tvNome);
        String nome = bancoDeDadosHelper.getNome(userId);
        tvNome.setText(nome);

        TextView tvEmail = findViewById(R.id.tvEmail);
        String email = bancoDeDadosHelper.getEmail(userId);
        tvEmail.setText(email);

        TextView tvRenda = findViewById(R.id.tvRenda);
        String renda = bancoDeDadosHelper.getRenda(userId);;

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        tvRenda.setText(format.format(Double.parseDouble(renda)));

        Button voltar = findViewById(R.id.btnVoltar);

        voltar.setOnClickListener((View view) -> {
            finish();
        });

        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener((View view) -> {
            // Cria o AlertDialog para confirmação
            new AlertDialog.Builder(this)
                    .setTitle("Confirmar Exclusão")
                    .setMessage("Tem certeza de que deseja excluir sua conta? Esta ação não pode ser desfeita.")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        // Ação quando o usuário confirma
                        SharedPreferences.Editor editor = preferences.edit();

                        bancoDeDadosHelper.deletarConta(userId);

                        editor.remove("is_logged_in"); // Remove a flag de login
                        editor.remove("user_id");      // Remove o ID do usuário
                        editor.apply(); // Aplica as mudanças

                        // Redireciona o usuário para a tela de login ou outra ação
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish(); // Fecha a atividade atual
                    })
                    .setNegativeButton("Não", (dialog, which) -> {
                        // Ação quando o usuário cancela
                        dialog.dismiss(); // Apenas fecha o diálogo sem fazer nada
                    })
                    .show(); // Exibe o diálogo
        });

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener((View view) -> {

                // Acessa as SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();

                // Remove ou redefine as chaves relacionadas ao login
                editor.remove("is_logged_in"); // Remove a flag de login
                editor.remove("user_id");      // Remove o ID do usuário
                editor.apply(); // Aplica as mudanças

                // Redireciona o usuário para a tela de login ou outra ação
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Fecha a atividade atual

        });
    }
}