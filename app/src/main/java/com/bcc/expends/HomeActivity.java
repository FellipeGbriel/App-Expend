package com.bcc.expends;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> descricao, valor;

    BancoDeDadosHelper bancoDeDadosHelper;

    RvAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);

        bancoDeDadosHelper = new BancoDeDadosHelper(this);
        descricao = new ArrayList<>();
        valor = new ArrayList<>();
        recyclerView = findViewById(R.id.rvTransacoes);
        TextView tvSaldo = findViewById(R.id.tvSaldo);
        adapter = new RvAdapter(this, valor, descricao);

        String saldo = bancoDeDadosHelper.getSaldo(userId);

        tvSaldo.setText("R$ " + saldo);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayData();

        Button buttonLancar = findViewById(R.id.adicionar_button);
        buttonLancar.setOnClickListener((View view) -> {
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
            Toast.makeText(HomeActivity.this, "botão apertado", Toast.LENGTH_SHORT).show();
        });

        Button buttonPerfil = findViewById(R.id.perfil_button);
        buttonPerfil.setOnClickListener((View view) -> {
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
            Toast.makeText(HomeActivity.this, "botão apertado", Toast.LENGTH_SHORT).show();
        });
    }

    private void displayData() {

        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);

        Cursor cursor = bancoDeDadosHelper.getTransacoesHome(userId);
        if (cursor.getCount() == 0) {

            Toast.makeText(this, "Nenhuma transação encontrada", Toast.LENGTH_SHORT).show();
            return;
        }else {

            while (cursor.moveToNext()) {

                descricao.add(cursor.getString(0));


                valor.add(cursor.getString(1));

            }
        }
    }
}