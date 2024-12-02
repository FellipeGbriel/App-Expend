package com.bcc.expends;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Locale;

public class TransacoesAdapter extends RecyclerView.Adapter<TransacoesAdapter.ViewHolder> {

    public static Context context;
    private ArrayList<Transacao> transacoes;
    private ArrayList<Integer> id;

    public TransacoesAdapter(Context context, ArrayList<Transacao> transacoes, ArrayList<Integer> idTransacao) {
        this.context = context;
        this.transacoes = transacoes;
        this.id = idTransacao;
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
        holder.id = transacao.getId();

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
        Integer id;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDescricao = itemView.findViewById(R.id.descricaoRvVerMais);
            tvValor = itemView.findViewById(R.id.valorRvVerMais);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext().getApplicationContext(), LancamentosActivity.class);
                    intent.putExtra("id_transacao", id);
                    context.startActivity(intent);

                }
            });
        }
    }
}