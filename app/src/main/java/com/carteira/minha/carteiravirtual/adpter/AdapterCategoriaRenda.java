package com.carteira.minha.carteiravirtual.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carteira.minha.carteiravirtual.R;
import com.carteira.minha.carteiravirtual.model.CategoriaRenda;

import java.util.List;

public class AdapterCategoriaRenda extends RecyclerView.Adapter<AdapterCategoriaRenda.MyViewHolder> {

    List<CategoriaRenda> categoriaRendas;
    Context context;

    public AdapterCategoriaRenda(List<CategoriaRenda> categoriaRendas, Context context) {
        this.categoriaRendas = categoriaRendas;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_categoria_renda, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoriaRenda categoriaRenda = categoriaRendas.get(position);

        holder.nome.setText(categoriaRenda.getNome());

    }


    @Override
    public int getItemCount() {
        return categoriaRendas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textAdapterTitulo);

        }

    }

}
