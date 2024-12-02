package com.bcc.expends;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bcc.expends.Transacao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VerMaisAdapter extends RecyclerView.Adapter<VerMaisAdapter.ViewHolder> {

    private Context context;
    private List<String> meses;

    private ArrayList descricao, valor;
    private ArrayList<Integer> id;
    private Map<String, ArrayList<Transacao>> transacoesPorMes;

    public VerMaisAdapter(Context context, Map<String, ArrayList<Transacao>> transacoesPorMes,ArrayList<Integer> id) {
        this.context = context;
        this.transacoesPorMes = transacoesPorMes;
        this.meses = new ArrayList<>(transacoesPorMes.keySet());
        this.descricao = descricao;
        this.valor = valor;
        this.id = id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_mes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String mesAno = meses.get(position);
        ArrayList<Transacao> transacoes = transacoesPorMes.get(mesAno);

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MM-yyyy", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM 'de' yyyy", new Locale("pt", "BR")); // Mês por extenso em português
            Date date = inputFormat.parse(mesAno);
            String mesAnoFormatado = outputFormat.format(date);
            holder.tvMesAno.setText(mesAnoFormatado.substring(0, 1).toUpperCase() + mesAnoFormatado.substring(1)); // Primeira letra maiúscula
        } catch (Exception e) {
            e.printStackTrace();
            holder.tvMesAno.setText(mesAno); // Fallback
        }

        // Configurar o RecyclerView interno
        TransacoesAdapter transacoesAdapter = new TransacoesAdapter(context, transacoes, id);
        holder.recyclerViewTransacoes.setAdapter(transacoesAdapter);
        holder.recyclerViewTransacoes.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount() {
        return meses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMesAno;
        RecyclerView recyclerViewTransacoes;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMesAno = itemView.findViewById(R.id.tvMesAno);
            recyclerViewTransacoes = itemView.findViewById(R.id.recyclerViewTransacoes); // Referência correta
        }
    }
}
