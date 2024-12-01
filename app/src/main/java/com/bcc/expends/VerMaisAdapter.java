package com.bcc.expends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bcc.expends.Transacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VerMaisAdapter extends RecyclerView.Adapter<VerMaisAdapter.ViewHolder> {

    private Context context;
    private List<String> meses;
    private Map<String, ArrayList<Transacao>> transacoesPorMes;

    public VerMaisAdapter(Context context, Map<String, ArrayList<Transacao>> transacoesPorMes) {
        this.context = context;
        this.transacoesPorMes = transacoesPorMes;
        this.meses = new ArrayList<>(transacoesPorMes.keySet());
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

        holder.tvMesAno.setText(mesAno);
        StringBuilder detalhes = new StringBuilder();
        for (Transacao t : transacoes) {
            detalhes.append(t.getData()).append(" - ").append(t.getDescricao()).append(": R$ ").append(t.getValor()).append("\n");
        }
        holder.tvDetalhes.setText(detalhes.toString());
    }

    @Override
    public int getItemCount() {
        return meses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMesAno, tvDetalhes;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMesAno = itemView.findViewById(R.id.tvMesAno);
            tvDetalhes = itemView.findViewById(R.id.tvDetalhes);
        }
    }
}
