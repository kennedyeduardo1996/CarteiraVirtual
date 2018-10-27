package com.carteira.minha.carteiravirtual.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.carteira.minha.carteiravirtual.R;
import com.carteira.minha.carteiravirtual.model.Notificacao;
import com.carteira.minha.carteiravirtual.model.Orcamento;

public class ConfiguracoesActivity extends AppCompatActivity {

    private EditText editValor;
    private CheckBox checkBox;
    private Notificacao notificacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        editValor = findViewById(R.id.editValor);
        checkBox = findViewById(R.id.checkBox);

    }

    public void  salvarOrcamento(View view){
        notificacao = new Notificacao();



        Double valorRecuperado = Double.parseDouble(editValor.getText().toString());
        String status = checkBox.getText().toString();

        notificacao.setValor( valorRecuperado );


        notificacao.setChecked( status );

        notificacao.salvar();

        finish();

    }


}
