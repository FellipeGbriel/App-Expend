package com.bcc.expends;

import android.view.View;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VerMaisActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VerMaisAdapter adapter;
    private BancoDeDadosHelper bancoDeDadosHelper;
    private Map<String, ArrayList<Transacao>> transacoesPorMesAno;
    private Spinner spinnerMes, spinnerAno;
    private Button btnRemoverFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mais);

        recyclerView = findViewById(R.id.recyclerViewVerMais);
        spinnerMes = findViewById(R.id.spinnerMes);
        spinnerAno = findViewById(R.id.spinnerAno);
        bancoDeDadosHelper = new BancoDeDadosHelper(this);
        transacoesPorMesAno = new HashMap<>();

        carregarTransacoes();
        configurarSpinners();

        adapter = new VerMaisAdapter(this, transacoesPorMesAno);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void carregarTransacoes() {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);

        Cursor cursor = bancoDeDadosHelper.getTransacoesVerMais(userId);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormatMesAno = new SimpleDateFormat("MMMM 'de' yyyy", new Locale("pt", "BR"));

        while (cursor.moveToNext()) {
            String descricao = cursor.getString(cursor.getColumnIndex("descricao"));
            String valor = cursor.getString(cursor.getColumnIndex("valor"));
            String dataTransacao = cursor.getString(cursor.getColumnIndex("data_transacao"));

            try {
                Date date = inputFormat.parse(dataTransacao);
                String mesAno = outputFormatMesAno.format(date); // Formato "Mês de Ano"
                Transacao transacao = new Transacao(descricao, valor, dataTransacao);

                if (!transacoesPorMesAno.containsKey(mesAno)) {
                    transacoesPorMesAno.put(mesAno, new ArrayList<>());
                }
                transacoesPorMesAno.get(mesAno).add(transacao);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();

        // Ordenar as transações mais recentes primeiro
        for (Map.Entry<String, ArrayList<Transacao>> entry : transacoesPorMesAno.entrySet()) {
            // Ordenar transações dentro de cada mês/ano
            Collections.sort(entry.getValue(), (transacao1, transacao2) -> {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = dateFormat.parse(transacao1.getData());
                    Date date2 = dateFormat.parse(transacao2.getData());
                    return date2.compareTo(date1); // Ordenar de forma decrescente (mais recente primeiro)
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            });
        }

        // Ordenar as chaves (mesAno) por data de forma decrescente (mostrar dezembro de 2024 antes de janeiro de 2024, etc.)
        List<String> mesesDisponiveis = new ArrayList<>(transacoesPorMesAno.keySet());
        Collections.sort(mesesDisponiveis, (mesAno1, mesAno2) -> {
            try {
                SimpleDateFormat mesAnoFormat = new SimpleDateFormat("MMMM 'de' yyyy", new Locale("pt", "BR"));
                Date date1 = mesAnoFormat.parse(mesAno1);
                Date date2 = mesAnoFormat.parse(mesAno2);
                return date2.compareTo(date1); // Ordenar de forma decrescente
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        });

        // Recriar o mapa com as chaves ordenadas
        Map<String, ArrayList<Transacao>> transacoesOrdenadas = new LinkedHashMap<>();
        for (String mesAno : mesesDisponiveis) {
            transacoesOrdenadas.put(mesAno, transacoesPorMesAno.get(mesAno));
        }
        transacoesPorMesAno = transacoesOrdenadas;

        // Atualizar os spinners
        List<String> anosDisponiveis = new ArrayList<>();
        List<String> mesesValidos = new ArrayList<>();
        for (String mesAno : mesesDisponiveis) {
            String[] partes = mesAno.split(" de ");
            String mes = partes[0];
            String ano = partes[1];

            if (!anosDisponiveis.contains(ano)) {
                anosDisponiveis.add(ano);
            }

            if (!mesesValidos.contains(mes)) {
                mesesValidos.add(mes);
            }
        }

        // Configuração do Spinner de Meses (apenas meses disponíveis nas transações)
        ArrayAdapter<String> adapterMeses = new ArrayAdapter<>(this, R.layout.spinn, mesesValidos);
        adapterMeses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMes.setAdapter(adapterMeses);

        // Configuração do Spinner de Anos (apenas anos disponíveis nas transações)
        ArrayAdapter<String> adapterAnos = new ArrayAdapter<>(this, R.layout.spinn, anosDisponiveis);
        adapterAnos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAno.setAdapter(adapterAnos);
    }

    private void configurarSpinners() {
        // Extrair combinações de mês e ano únicos das transações
        List<String> mesesDisponiveis = new ArrayList<>(transacoesPorMesAno.keySet());
        Collections.sort(mesesDisponiveis);

        // Criar lista de anos
        List<String> anosDisponiveis = new ArrayList<>();

// Loop invertido para anosDisponiveis
        for (int i = mesesDisponiveis.size() - 1; i >= 0; i--) {
            String mesAno = mesesDisponiveis.get(i);
            String ano = mesAno.split(" de ")[1];
            if (!anosDisponiveis.contains(ano)) {
                anosDisponiveis.add(ano);
            }
        }

// Criar lista de meses válidos
        List<String> mesesValidos = new ArrayList<>();

// Loop invertido para mesesValidos
        for (int i = mesesDisponiveis.size() - 1; i >= 0; i--) {
            String mesAno = mesesDisponiveis.get(i);
            String mes = mesAno.split(" de ")[0];
            if (!mesesValidos.contains(mes)) {
                mesesValidos.add(mes);
            }
        }

        // Configuração do Spinner de Meses (apenas meses disponíveis nas transações)
        ArrayAdapter<String> adapterMeses = new ArrayAdapter<>(this, R.layout.spinn, mesesValidos);
        adapterMeses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMes.setAdapter(adapterMeses);

        // Configuração do Spinner de Anos (apenas anos disponíveis nas transações)
        ArrayAdapter<String> adapterAnos = new ArrayAdapter<>(this, R.layout.spinn, anosDisponiveis);
        adapterAnos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAno.setAdapter(adapterAnos);

        // Lógica para filtro
        spinnerMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrarTransacoes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Sem ação
            }
        });

        spinnerAno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrarTransacoes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Sem ação
            }
        });
    }

    private void filtrarTransacoes() {
        String mesSelecionado = spinnerMes.getSelectedItem().toString();
        String anoSelecionado = spinnerAno.getSelectedItem().toString();

        Map<String, ArrayList<Transacao>> transacoesFiltradas = new HashMap<>();

        for (String mesAno : transacoesPorMesAno.keySet()) {
            if (mesAno.contains(mesSelecionado) && mesAno.contains(anoSelecionado)) {
                transacoesFiltradas.put(mesAno, transacoesPorMesAno.get(mesAno));
            }
        }

        adapter = new VerMaisAdapter(VerMaisActivity.this, transacoesFiltradas);
        recyclerView.setAdapter(adapter);
    }
}