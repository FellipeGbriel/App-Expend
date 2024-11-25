package com.bcc.expends;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {

    private Context context;

    private ArrayList descricao, valor;

    public RvAdapter(Context context, ArrayList valor, ArrayList descricao) {
        this.context = context;
        this.descricao = descricao;
        this.valor = valor;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.rv_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvDescricao.setText(String.valueOf(descricao.get(position)));
        holder.tvValor.setText(String.format(Locale.getDefault(), "R$ %.2f", Double.parseDouble(String.valueOf(valor.get(position)))));


        if (Double.parseDouble(String.valueOf(valor.get(position))) < 0) {
            holder.tvValor.setTextColor(Color.RED);
        } else {
            holder.tvValor.setTextColor(Color.GREEN);
        }

    }

    @Override
    public int getItemCount() {
        return descricao.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescricao, tvValor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDescricao = itemView.findViewById(R.id.descricaoRv);
            tvValor = itemView.findViewById(R.id.valorRv);
        }
    }
}
