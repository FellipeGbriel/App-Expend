package com.bcc.expends;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class CadastroActivity extends AppCompatActivity {

    private BancoDeDadosHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        // Configuração de padding para insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new BancoDeDadosHelper(this);

        // Referências aos elementos da UI
        EditText nomeEditText = findViewById(R.id.nomeEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        Spinner daySpinner = findViewById(R.id.daySpinner);
        Spinner monthSpinner = findViewById(R.id.monthSpinner);
        Spinner yearSpinner = findViewById(R.id.yearSpinner);
        EditText rendaEditText = findViewById(R.id.rendaEditText);
        Button submitButton = findViewById(R.id.submitButton);


        setupDateSpinners(daySpinner, monthSpinner, yearSpinner);


        submitButton.setOnClickListener(v -> {
            String nome = nomeEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String senha = passwordEditText.getText().toString().trim();
            String confirmarSenha = confirmPasswordEditText.getText().toString().trim();
            String renda = rendaEditText.getText().toString().trim();

            String dia = daySpinner.getSelectedItem().toString();
            String mes = String.valueOf(monthSpinner.getSelectedItemPosition() + 1);
            String ano = yearSpinner.getSelectedItem().toString();
            String dataNascimento = ano + "-" + mes + "-" + dia;

            // Validação dos campos
            if (email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty() || renda.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senha.equals(confirmarSenha)) {
                Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Salvar no banco de dados
            saveUserToDatabase(nome, email, senha, renda, dataNascimento);
        });
    }

    private void setupDateSpinners(Spinner daySpinner, Spinner monthSpinner, Spinner yearSpinner) {
        // Dias
        ArrayList<String> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            days.add(String.valueOf(i));
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, R.layout.spinn, days);
        daySpinner.setAdapter(dayAdapter);

        // Meses
        String[] months = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, R.layout.spinn, months);
        monthSpinner.setAdapter(monthAdapter);

        // Anos
        ArrayList<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= 1900; i--) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, R.layout.spinn, years);
        yearSpinner.setAdapter(yearAdapter);
    }

    private void saveUserToDatabase(String nome, String email, String senha, String renda, String dataNascimento) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("nome_usuario", nome);
        values.put("senha", senha);
        values.put("email", email);
        values.put("renda_mensal", renda);
        values.put("data_nascimento", dataNascimento);


        long newRowId = db.insert("usuarios", null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Erro ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
        }
    }
}