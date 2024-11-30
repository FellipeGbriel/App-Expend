package com.bcc.expends;

import android.content.ContentValues;
import android.view.View;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.Console;

public class BancoDeDadosHelper extends SQLiteOpenHelper {

    // Nome e versão do banco de dados
    private static final String DATABASE_NAME = "livro_caixa.db";
    private static final int DATABASE_VERSION = 1;

    // Construtor
    public BancoDeDadosHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        // SQL para criar a tabela de usuários
        db.execSQL("CREATE TABLE IF NOT EXISTS usuarios (" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome_usuario TEXT NOT NULL," +
                "senha TEXT NOT NULL," +
                "email TEXT," +
                "data_criado TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "renda_mensal REAL," +
                "data_nascimento TEXT,"  +
                "ultimo_login TIMESTAMP" +
                ");");

        // SQL para criar a tabela de transações
        db.execSQL("CREATE TABLE IF NOT EXISTS transacoes (" +
                "id_transacao INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER," +
                "valor REAL NOT NULL," +
                "tipo_transacao TEXT  CHECK (tipo_transacao IN ('entrada', 'saida', ''))," +
                "descricao TEXT," +
                "data_transacao DATE NOT NULL," +
                "data_criado TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE" +
                ");");

        // SQL para criar a tabela de saldos
        db.execSQL("CREATE TABLE IF NOT EXISTS saldos (" +
                "id_saldos INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER," +
                "saldo_atual REAL NOT NULL DEFAULT 0," +
                "ultima_modificacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE" +
                ");");

        // SQL para criar o trigger de atualização de saldo
        db.execSQL("CREATE TRIGGER atualiza_saldo " +
                "AFTER INSERT ON transacoes " +
                "BEGIN " +
                "    UPDATE saldos " +
                "    SET saldo_atual = saldo_atual + NEW.valor, ultima_modificacao = CURRENT_TIMESTAMP " +
                "    WHERE id_usuario = NEW.id_usuario; " +
                "    " +
                "    INSERT INTO saldos (id_usuario, saldo_atual, ultima_modificacao) " +
                "    SELECT NEW.id_usuario, NEW.valor, CURRENT_TIMESTAMP " +
                "    WHERE NOT EXISTS (SELECT 1 FROM saldos WHERE id_usuario = NEW.id_usuario); " +
                " END;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Caso precise atualizar a estrutura do banco, você pode alterar aqui.
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS transacoes");
        db.execSQL("DROP TABLE IF EXISTS saldos");
        db.execSQL("DROP TRIGGER IF EXISTS atualiza_saldo");
        onCreate(db);
    }

    public Cursor getTransacoesHome(int usuario) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT descricao, valor FROM transacoes WHERE id_usuario =" + usuario + " LIMIT 5", null);

        return cursor;

    }


    public Cursor getTransacoes(int usuario) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT descricao, valor, id_transacao FROM transacoes WHERE id_usuario =" + usuario, null);

        return cursor;


    }

    public String getSaldo(int usuario) {

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT saldo_atual FROM saldos WHERE id_usuario = ?", new String[]{String.valueOf(usuario)});

        String saldo = "0.00";
        if (cursor != null && cursor.moveToFirst()) {

            saldo = cursor.getString(cursor.getColumnIndexOrThrow("saldo_atual"));
            cursor.close();
        }

        return saldo;
    }

    //pega e retorna o valor usuario se for o unico no banco local
    //TODO add tabela de usuario logado
    //
    public int getUsuarioId() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id_usuario FROM usuarios", null);

        // Log.i( "Mytag ::  ", String.valueOf("NUMERO:  " + cursor.getCount()));
        if (cursor.getCount() == 1){
            return 1;
        } else if (cursor.getCount() >= 2) {
            cursor.moveToLast();
            return cursor.getInt(0);
        }

        return -1;
    }

    public void deleteTransacao(int idTransacao){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("DELETE FROM transacoes WHERE id_transacao = " + idTransacao, null);

    }

    public boolean saveLancamentoToDatabase(int idUsuario, double valor, String descricao, String dataLancamento, Context context) {

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put("id_usuario", idUsuario);
        values.put("valor", valor);
        values.put("tipo_transacao", "");
        values.put("descricao", descricao);
        values.put("data_transacao", dataLancamento);

        long newRowId = db.insert("transacoes", null, values);
        //Log.e( "newRowId: ", "return :" + newRowId);
        if (newRowId != -1) {
            //Toast.makeText(context, "Lancamento cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, "Erro ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}