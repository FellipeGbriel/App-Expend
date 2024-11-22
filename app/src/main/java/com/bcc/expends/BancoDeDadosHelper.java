package com.bcc.expends;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

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
                "tipo_transacao TEXT NOT NULL CHECK (tipo_transacao IN ('entrada', 'saida'))," +
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
                "    SET saldo_atual = saldo_atual + " +
                "        CASE " +
                "            WHEN NEW.tipo_transacao = 'entrada' THEN NEW.valor " +
                "            ELSE -NEW.valor " +
                "        END, " +
                "        ultima_modificacao = CURRENT_TIMESTAMP " +
                "    WHERE id_usuario = NEW.id_usuario; " +
                "    " +
                "    INSERT INTO saldos (id_usuario, saldo_atual, ultima_modificacao) " +
                "    SELECT NEW.id_usuario, " +
                "        CASE " +
                "            WHEN NEW.tipo_transacao = 'entrada' THEN NEW.valor " +
                "            ELSE -NEW.valor " +
                "        END, " +
                "        CURRENT_TIMESTAMP " +
                "    WHERE NOT EXISTS (SELECT 1 FROM saldos WHERE id_usuario = NEW.id_usuario); " +
                "END;");
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
}