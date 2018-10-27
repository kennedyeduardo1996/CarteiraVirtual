package com.carteira.minha.carteiravirtual.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import com.carteira.minha.carteiravirtual.R;
import com.carteira.minha.carteiravirtual.adpter.AdapterCategoriaDespesa;
import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.carteira.minha.carteiravirtual.helper.Base64Custom;
import com.carteira.minha.carteiravirtual.model.CategoriaDespesa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class ListaCategoriaDespesaActivity extends AppCompatActivity {

    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

    private RecyclerView recyclerView;
    private AdapterCategoriaDespesa adapterCategoriaDespesa;
    private List<CategoriaDespesa> categoriaDespesas = new ArrayList<>();
    private DatabaseReference categoriaDespesaRef;
    private ValueEventListener valueEventListenerCategoriaDespesa;
    private String idDespesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_categoria_despesa);

        //configurar adapter
        adapterCategoriaDespesa = new AdapterCategoriaDespesa(categoriaDespesas, this);

        recyclerView = findViewById(R.id.recyclerListaCategoriaDespesa);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterCategoriaDespesa);
    }

    public void recuperaCategoriaDespesa() {

        categoriaDespesaRef = firebaseRef.child("categoria")
                                         .child("despesa");

        valueEventListenerCategoriaDespesa = categoriaDespesaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoriaDespesas.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Log.i("dados", "retorno" + dados.toString());
                    CategoriaDespesa categoriaDespesa = dados.getValue(CategoriaDespesa.class);
                    Log.i("dadosRetorno", "dados" + categoriaDespesa.getNome());
                    categoriaDespesas.add(categoriaDespesa);

                }
                adapterCategoriaDespesa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart () {
        super.onStart();
        recuperaCategoriaDespesa();

    }

    @Override
    protected void onStop () {
        super.onStop();
        categoriaDespesaRef.removeEventListener(valueEventListenerCategoriaDespesa);
    }
}
