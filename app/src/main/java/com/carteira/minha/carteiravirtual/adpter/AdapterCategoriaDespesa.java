package com.carteira.minha.carteiravirtual.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carteira.minha.carteiravirtual.R;
import com.carteira.minha.carteiravirtual.model.CategoriaDespesa;

import java.util.List;

/**
 * Created by Jamilton Damasceno
 */

public class AdapterCategoriaDespesa extends RecyclerView.Adapter<AdapterCategoriaDespesa.MyViewHolder> {

    List<CategoriaDespesa> categoriaDespesas;
    Context context;

    public AdapterCategoriaDespesa(List<CategoriaDespesa> categoriaDespesas, Context context) {
        this.categoriaDespesas = categoriaDespesas;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_categoria_despesa, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoriaDespesa categoriaDespesa = categoriaDespesas.get(position);

        holder.nome.setText(categoriaDespesa.getNome());

    }


    @Override
    public int getItemCount() {
        return categoriaDespesas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textAdapterTitulo);

        }

    }

}
