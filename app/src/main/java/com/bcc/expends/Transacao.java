package com.bcc.expends;

public class Transacao {
    private String descricao;
    private String valor;
    private String data;

    public Transacao(String descricao, String valor, String data) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getValor() {
        return valor;
    }

    public String getData() {
        return data;
    }
}
