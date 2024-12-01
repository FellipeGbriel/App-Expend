package com.bcc.expends;

import static androidx.core.content.ContextCompat.startActivity;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.inappmessaging.internal.injection.components.AppComponent;

import java.util.ArrayList;
import java.util.Locale;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {

    private Context context;

    private ArrayList descricao, valor;
    private ArrayList<Integer> id;


    public RvAdapter(Context context, ArrayList valor, ArrayList descricao, ArrayList<Integer> id) {
        this.context = context;
        this.descricao = descricao;
        this.valor = valor;
        this.id = id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.rv_item, parent, false);

        return new ViewHolder(v, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvDescricao.setText(String.valueOf(descricao.get(position)));
        holder.tvValor.setText(String.format(Locale.getDefault(), "R$ %.2f", Double.parseDouble(String.valueOf(valor.get(position)))));
        holder.id = id.get(position);

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
        Integer id;

        public ViewHolder(@NonNull View itemView, ViewGroup parent) {
            super(itemView);

            tvDescricao = itemView.findViewById(R.id.descricaoRv);
            tvValor = itemView.findViewById(R.id.valorRv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext().getApplicationContext(), LancamentosActivity.class);
                    intent.putExtra("id_transacao", id);
                    Log.e("goToTransacao: ", "id: " + id);
                    context.startActivity(intent);
                }
            });

        }

    }

}
