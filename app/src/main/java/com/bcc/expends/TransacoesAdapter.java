package com.bcc.expends;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Locale;

public class TransacoesAdapter extends RecyclerView.Adapter<TransacoesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Transacao> transacoes;

    public TransacoesAdapter(Context context, ArrayList<Transacao> transacoes) {
        this.context = context;
        this.transacoes = transacoes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transacao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transacao transacao = transacoes.get(position);
        holder.tvDescricao.setText(transacao.getDescricao());
        holder.tvValor.setText(String.format(Locale.getDefault(), "R$ %.2f", Double.parseDouble(transacao.getValor())));

        if (Double.parseDouble(transacao.getValor()) < 0) {
            holder.tvValor.setTextColor(Color.RED);
        } else {
            holder.tvValor.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return transacoes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescricao, tvValor;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDescricao = itemView.findViewById(R.id.descricaoRvVerMais);
            tvValor = itemView.findViewById(R.id.valorRvVerMais);
        }
    }
}