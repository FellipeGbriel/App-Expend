package com.bcc.expends;

public class Transacao {
    private String descricao;
    private String valor;
    private String data;
    private int id;

    public Transacao(String descricao, String valor, String data, Integer idTransacao) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.id = idTransacao;
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

    public int getId(){return id;}
}
