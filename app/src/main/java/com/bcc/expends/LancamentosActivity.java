package com.bcc.expends;

import static android.text.TextUtils.isEmpty;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class LancamentosActivity extends AppCompatActivity {

    private final String TAG = "AppExpendLog";
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
         dbHelper = new BancoDeDadosHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(Type.systemBars());
            v.setPadding(systemBars.left, ((androidx.core.graphics.Insets) systemBars).top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Integer idTransacao = getIntent().getIntExtra("id_transacao",0);
        Log.e(TAG, "id transacao from extra: "+idTransacao);

        // Inicialize os EditTexts
        descriptionEditText = findViewById(R.id.descriptionEditText);
        valueEditText = findViewById(R.id.valueEditText);

        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);

        // Inicialize os Spinners
        daySpinner = findViewById(R.id.daySpinner);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        typeSpinner = findViewById(R.id.typeSpinner);

        //Inicializar os Botões
        Button saveButton = findViewById(R.id.saveButton);
        ImageButton deleteButton = findViewById(R.id.lixeiraIcon);

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
        for (int i = currentYear; i >= 1970; i--) {
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

        //trigger do botão ao clicar create and delete

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteTransacao(idTransacao);
                Log.e(TAG, "DELETE "+idTransacao);
                finish();
            }
        });

        Log.e(TAG, "inicio do caregamento ");
        if (idTransacao != 0 && idTransacao > 0){

            Cursor dados = dbHelper.getTransacao(idTransacao);//idTransacao=
            dados.moveToFirst();
            Log.e(TAG, "teste consulta id:" + Arrays.toString(dados.getColumnNames()));
            descriptionEditText.setText(dados.getString(3));
            valueEditText.setText(dados.getString(2));

            List<String> data = Arrays.asList(dados.getString(4).split("-"));
            Log.e(TAG, "Data e assim: "+ data.get(0)+ " "+ data.get(1)+ " " + data.get(2) );
            daySpinner.setSelection( Integer.parseInt(data.get(2)) - 1);
            monthSpinner.setSelection(Integer.parseInt(data.get(1)) -1);
            yearSpinner.setSelection(years.indexOf(data.get(0)));

            if (dados.getInt(2) > 0){typeSpinner.setSelection(0);} else {typeSpinner.setSelection(0);}
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double valor;
                String descricao;

                String dia = daySpinner.getSelectedItem().toString();
                String mes = String.valueOf(monthSpinner.getSelectedItemPosition() + 1);
                String ano = yearSpinner.getSelectedItem().toString();
                String dataLancamento = ano + "-" + mes + "-" + dia;

                String tipo = typeSpinner.getSelectedItem().toString();

                if (tipo.equals("Despesa")) {
                    valor = Double.parseDouble(valueEditText.getText().toString().trim()) * -1;
                } else {
                    valor = Math.abs(Double.parseDouble(valueEditText.getText().toString().trim()));
                }

                descricao = descriptionEditText.getText().toString().trim();

                if ((valueEditText.getText().toString() == "R$ 0,00") || (valueEditText.getText().toString().trim().isEmpty())) {
                    Toast.makeText(LancamentosActivity.this, "Preencha o valor", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (descriptionEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LancamentosActivity.this, "Preencha a descricao", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if(idTransacao!=0) {
                    boolean isUpdate = dbHelper.updateLancamentoToDatabase(valor, descricao, dataLancamento,LancamentosActivity.this, userId);
                    if (isUpdate){
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    boolean isCadastrado = dbHelper.saveLancamentoToDatabase(userId, valor, descricao, dataLancamento,LancamentosActivity.this);
                    if (isCadastrado){
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        }
                }
        }});

    }


}
