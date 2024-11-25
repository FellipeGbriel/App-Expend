package com.bcc.expends;

import android.database.Cursor;
import android.os.Bundle;
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

        bancoDeDadosHelper = new BancoDeDadosHelper(this);
        descricao = new ArrayList<>();
        valor = new ArrayList<>();
        recyclerView = findViewById(R.id.rvTransacoes);
        TextView tvSaldo = findViewById(R.id.tvSaldo);
        adapter = new RvAdapter(this, valor, descricao);

        String saldo = bancoDeDadosHelper.getSaldo(1);

        tvSaldo.setText("R$ " + saldo);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayData();


    }

    private void displayData() {

        Cursor cursor = bancoDeDadosHelper.getTransacoes(1);
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