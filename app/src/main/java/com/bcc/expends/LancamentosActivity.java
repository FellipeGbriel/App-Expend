package com.bcc.expends;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat.Type;

import java.util.ArrayList;
import java.util.Calendar;

public class LancamentosActivity extends AppCompatActivity {

    private EditText descriptionEditText;
    private EditText valueEditText;
    private Spinner daySpinner;
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private Spinner typeSpinner;
    private BancoDeDadosHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamentos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(Type.systemBars());
            v.setPadding(systemBars.left, ((androidx.core.graphics.Insets) systemBars).top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa Helper de BD
        dbHelper = new BancoDeDadosHelper(this);

        // Inicialize os EditTexts
        descriptionEditText = findViewById(R.id.descriptionEditText);
        valueEditText = findViewById(R.id.valueEditText);

        // Inicialize os Spinners
        daySpinner = findViewById(R.id.daySpinner);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        typeSpinner = findViewById(R.id.typeSpinner);

        //Inicializar os Botões
        Button saveButton = findViewById(R.id.saveButton);
        ImageButton excludeButton = findViewById(R.id.lixeiraIcon);

        // Configuração para o Spinner de Dias
        ArrayList<String> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            days.add(String.valueOf(i));
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, R.layout.spinn, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        // Configuração para o Spinner de Meses
        String[] months = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, R.layout.spinn, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        // Configuração para o Spinner de Anos
        ArrayList<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= 1900; i--) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, R.layout.spinn, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // Configuração para o Spinner de tipo
        String[] types = {"Despesa", "Ganho"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, R.layout.spinn, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        //trigger do botão ao clicar

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idUsuario;
                double valor;
                String descricao;

                String dia = daySpinner.getSelectedItem().toString();
                String mes = String.valueOf(monthSpinner.getSelectedItemPosition() + 1);
                String ano = yearSpinner.getSelectedItem().toString();
                String dataLancamento = ano + "-" + mes + "-" + dia;

                idUsuario = dbHelper.getUsuarioId();

                descricao = descriptionEditText.getText().toString().trim();

                if ((valueEditText.getText().toString() == "0.0") || (valueEditText.getText().toString().trim().isEmpty())) {
                    Toast.makeText(LancamentosActivity.this, "Preencha o valor", Toast.LENGTH_SHORT).show();
                    return;
                }
                valor = Double.parseDouble(valueEditText.getText().toString().trim());

                if (descricao == "" || descriptionEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LancamentosActivity.this, "Preencha a descricao", Toast.LENGTH_SHORT).show();
                    return ;
                }

                saveLancamentoToDatabase(idUsuario, valor, descricao, dataLancamento);
                Intent intent = getIntent();
                startActivity(intent);
                finish();
        }});
    }

    private void saveLancamentoToDatabase(int idUsuario, double valor, String descricao, String dataLancamento) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id_usuario", idUsuario);
        values.put("valor", valor);
        values.put("tipo_transacao", "");
        values.put("descricao", descricao);
        values.put("data_transacao", dataLancamento);


        long newRowId = db.insert("transacoes", null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Lancamento cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//            finish();
        } else {
            Toast.makeText(this, "Erro ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
        }
    }

}
