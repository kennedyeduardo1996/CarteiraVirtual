package com.carteira.minha.carteiravirtual.activity;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import com.carteira.minha.carteiravirtual.R;
import com.carteira.minha.carteiravirtual.adpter.AdapterCategoriaRenda;
import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.carteira.minha.carteiravirtual.helper.Base64Custom;
import com.carteira.minha.carteiravirtual.model.CategoriaRenda;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.carteira.minha.carteiravirtual.R;

public class ListaCategoriaRendaActivity extends Activity {

    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

    private RecyclerView recyclerView;
    private AdapterCategoriaRenda adapterCategoriaRenda;
    private List<CategoriaRenda> categoriaRendas = new ArrayList<>();
    private DatabaseReference categoriaRendaRef;
    private ValueEventListener valueEventListenerCategoriaRenda;
    private String idRenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_categoria_renda);

        //configurar adapter
        adapterCategoriaRenda = new AdapterCategoriaRenda(categoriaRendas, this);

        recyclerView = findViewById(R.id.recyclerListaCategoriaRenda);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterCategoriaRenda);
    }

    public void recuperaCategoriaRenda() {

        categoriaRendaRef = firebaseRef.child("categoria")
                .child("renda");

        valueEventListenerCategoriaRenda = categoriaRendaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoriaRendas.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Log.i("dados", "retorno" + dados.toString());
                    CategoriaRenda categoriaRenda = dados.getValue(CategoriaRenda.class);
                    Log.i("dadosRetorno", "dados" + categoriaRenda.getNome());
                    categoriaRendas.add(categoriaRenda);

                }
                adapterCategoriaRenda.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart () {
        super.onStart();
        recuperaCategoriaRenda();

    }

    @Override
    protected void onStop () {
        super.onStop();
        categoriaRendaRef.removeEventListener(valueEventListenerCategoriaRenda);
    }

}
