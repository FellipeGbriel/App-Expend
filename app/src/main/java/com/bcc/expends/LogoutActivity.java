package com.bcc.expends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.Locale;

public class LogoutActivity extends AppCompatActivity {

    BancoDeDadosHelper bancoDeDadosHelper;
    public int btnEditType;

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

        EditText edtNome = findViewById(R.id.edtNome);
        TextView tvNome = findViewById(R.id.tvNome);
        String nome = bancoDeDadosHelper.getNome(userId);
        tvNome.setText(nome);
        edtNome.setText(nome);

        ImageButton btnEdit = findViewById(R.id.btnEdit);
        int btnEditType = 0;// 0 é para editar e 1 para modo salvar

        /*
        edtNome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(hasFocus) {
                } else if ( (view.getId() != edtNome.getId()) && edtNome.getText().toString().isEmpty()){
                    Log.e( "UPDATE_NOME ", "Nome esta vazio");

                    int res = bancoDeDadosHelper.updateNome(view.getContext(), userId, String.valueOf(edtNome.getText() ));
                    if (res!=-1){
                        tvNome.setText(edtNome.getText().toString());
                        cancelarEdicaoNome();
                    }
                }
            }
        }); */

        EditText edtEmail = findViewById(R.id.edtEmail);
        TextView tvEmail = findViewById(R.id.tvEmail);
        String email = bancoDeDadosHelper.getEmail(userId);
        tvEmail.setText(email);
        edtEmail.setText(email);

        EditText edtRenda = findViewById(R.id.edtRenda);
        TextView tvRenda = findViewById(R.id.tvRenda);
        String renda = bancoDeDadosHelper.getRenda(userId);;

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        tvRenda.setText(format.format(Double.parseDouble(renda)));
        edtRenda.setText(renda);

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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getBtnEditType() == 0){
                    // esconde os textos
                    tvNome.setVisibility(View.GONE);
                    tvEmail.setVisibility(View.GONE);
                    tvRenda.setVisibility(View.GONE);

                    //exibe os editores
                    edtNome.setVisibility(View.VISIBLE);
                    edtEmail.setVisibility(View.VISIBLE);
                    edtRenda.setVisibility(View.VISIBLE);

                    //troca para o botão salvar
                    btnEdit.setImageResource(R.drawable.check);
                    setBtnEditType(1);

                } else{
                    //atualiza os dados
                    int resNome = bancoDeDadosHelper.updateNome(view.getContext(), userId, String.valueOf(edtNome.getText() ));
                    int resEmail = bancoDeDadosHelper.updateEmail(view.getContext(), userId, String.valueOf(edtEmail.getText() ));
                    int resRenda = bancoDeDadosHelper.updateRenda(view.getContext(), userId, String.valueOf(edtRenda.getText() ));

                    // esconde os editores
                    edtNome.setVisibility(View.GONE);
                    edtEmail.setVisibility(View.GONE);
                    edtRenda.setVisibility(View.GONE);

                    //atualiza os textos editados
                    tvNome.setText(edtNome.getText());
                    tvEmail.setText(edtEmail.getText());
                    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                    tvRenda.setText(format.format(Double.parseDouble(String.valueOf(edtRenda.getText()))));

                    //exibe os textos
                    tvNome.setVisibility(View.VISIBLE);
                    tvEmail.setVisibility(View.VISIBLE);
                    tvRenda.setVisibility(View.VISIBLE);

                    //troca para o botão Editar
                    setBtnEditType(0);
                    btnEdit.setImageResource(R.drawable.edit);
                }
            }
        });

    }

    public int getBtnEditType(){
        return btnEditType;
    }

    public void setBtnEditType(int btnEditType ){
        this.btnEditType = btnEditType;
    }

}

