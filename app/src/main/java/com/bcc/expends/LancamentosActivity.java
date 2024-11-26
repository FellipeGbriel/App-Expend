package com.bcc.expends;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamentos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(Type.systemBars());
            v.setPadding(systemBars.left, ((androidx.core.graphics.Insets) systemBars).top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialize os EditTexts
        descriptionEditText = findViewById(R.id.descriptionEditText);
        valueEditText = findViewById(R.id.valueEditText);

        // Inicialize os Spinners
        daySpinner = findViewById(R.id.daySpinner);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        typeSpinner = findViewById(R.id.typeSpinner);

        ArrayList<String> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            days.add(String.valueOf(i));
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, R.layout.spinn, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        // Configuração para o Spinner de meses
        String[] months = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, R.layout.spinn, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        // Configuração para o Spinner de anos
        ArrayList<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= 1900; i--) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, R.layout.spinn, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // Configuração para o Spinner de tipo
        String[] types = {"Gastos", "Ganhos"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, R.layout.spinn, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
    }
}
