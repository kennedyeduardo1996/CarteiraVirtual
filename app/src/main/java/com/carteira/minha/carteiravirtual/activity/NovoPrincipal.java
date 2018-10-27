package com.carteira.minha.carteiravirtual.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.carteira.minha.carteiravirtual.R;
import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class NovoPrincipal extends AppCompatActivity {


    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutentificacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_principal);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Tela Principal");
        setSupportActionBar(toolbar);

    }

    public void adicionarMovimentacao(View view){
        startActivity(new Intent(this, DespesasActivity.class));
    }

    public void abrirResumo(View view){
        startActivity(new Intent(this, PrincipalActivity.class));
    }


    public void abrirConfiguracoes(View view){
        startActivity(new Intent(this, ConfiguracoesActivity.class));
    }




    //    para poder exibir o menu do lado de cima
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //converte o arquivo menu.xml em uma view
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }





    //responsavel por tratar os item do manu superior
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair : {
                autenticacao.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            }

            case R.id.menuCategoria : {
                startActivity(new Intent(this, ListaCategoriaDespesaActivity.class));
                finish();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

}