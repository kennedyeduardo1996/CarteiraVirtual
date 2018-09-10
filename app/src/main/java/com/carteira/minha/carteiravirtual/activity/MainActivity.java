package com.carteira.minha.carteiravirtual.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.carteira.minha.carteiravirtual.R;
import com.carteira.minha.carteiravirtual.activity.CadastroActivity;
import com.carteira.minha.carteiravirtual.activity.LoginActivity;
import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private FirebaseAuth autentificacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        verificarUsuarioLogado();

        //remover botões de navegação
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build()
        );
    }


    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }


    //função chamada quando clicar no entrar
    public void btEntrar(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }
    //função chamada quando clicar no cadastrar

    public void btCadastrar(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }

    //função para verificar se o usuario está logado
    public  void verificarUsuarioLogado(){

        autentificacao = ConfiguracaoFirebase.getFirebaseAutentificacao();

        if (autentificacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    //chamar tela principal
    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
    }

}
