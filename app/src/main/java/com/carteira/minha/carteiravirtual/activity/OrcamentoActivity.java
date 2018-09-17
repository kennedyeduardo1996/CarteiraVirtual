package com.carteira.minha.carteiravirtual.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.carteira.minha.carteiravirtual.R;
import com.carteira.minha.carteiravirtual.model.Orcamento;

public class OrcamentoActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria;
    private EditText campoValor;
    private Orcamento orcamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcamento);

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);


    }

    public void  salvarOrcamento(View view){
        orcamento = new Orcamento();

        String data = campoData.getText().toString();
        Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
        orcamento.setValor( valorRecuperado );
        orcamento.setCategoria( campoCategoria.getText().toString() );
        orcamento.setData( data );

        orcamento.salvar( data );

    }
}
