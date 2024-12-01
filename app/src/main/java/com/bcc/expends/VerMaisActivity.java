package com.bcc.expends;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VerMaisActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VerMaisAdapter adapter;
    private BancoDeDadosHelper bancoDeDadosHelper;
    private Map<String, ArrayList<Transacao>> transacoesPorMes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mais);

        recyclerView = findViewById(R.id.recyclerViewVerMais);
        bancoDeDadosHelper = new BancoDeDadosHelper(this);
        transacoesPorMes = new HashMap<>();

        carregarTransacoes();

        adapter = new VerMaisAdapter(this, transacoesPorMes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void carregarTransacoes() {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);

        Cursor cursor = bancoDeDadosHelper.getTransacoesVerMais(userId);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd"); // Supondo que a data esteja no formato YYYY-MM-DD
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM-yyyy");    // Formato desejado: YYYY-MM

        while (cursor.moveToNext()) {
            String descricao = cursor.getString(cursor.getColumnIndex("descricao"));
            String valor = cursor.getString(cursor.getColumnIndex("valor"));
            String dataTransacao = cursor.getString(cursor.getColumnIndex("data_transacao"));

            try {
                Date date = inputFormat.parse(dataTransacao);
                String mesAno = outputFormat.format(date); // Converte para o formato correto
                Transacao transacao = new Transacao(descricao, valor, dataTransacao);

                if (!transacoesPorMes.containsKey(mesAno)) {
                    transacoesPorMes.put(mesAno, new ArrayList<>());
                }
                transacoesPorMes.get(mesAno).add(transacao);
            } catch (Exception e) {
                e.printStackTrace(); // Tratamento de erro para caso a data esteja inválida
            }
        }
        cursor.close();
    }
}