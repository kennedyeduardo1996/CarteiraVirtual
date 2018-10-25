package com.carteira.minha.carteiravirtual.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.carteira.minha.carteiravirtual.R;
import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.carteira.minha.carteiravirtual.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {
    private EditText campoEmail, campoSenha;
    private Button botaoEntrar;
    private Usuario usuario;
    private FirebaseAuth autentificacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //passando os valores para variaveis

        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoEntrar = findViewById(R.id.buttonLogin);

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if (!textoEmail.isEmpty()){
                    if (!textoSenha.isEmpty()){
                        usuario = new Usuario();
                        usuario.setEmail(textoEmail);
                        usuario.setSenha(textoSenha);
                        validarLogin();

                    }else{
                        Toast.makeText(LoginActivity.this, "Preencha a Senha!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Preencha o Email!",Toast.LENGTH_SHORT).show();
                }



            }
        });
    }
    public void validarLogin(){
        autentificacao = ConfiguracaoFirebase.getFirebaseAutentificacao();
        //passando os valores para o banco
        autentificacao.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
                //verificando se foi salvo
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //chamar tela principal
                    abrirTelaPrincipal();
                }else{
                    String excecao = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Email e senha não correspondem";
                    }catch(FirebaseAuthInvalidUserException e){
                        excecao = "Usuário não esta cadastrado";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário"+e.getMessage();
                        e.printStackTrace();

                    }
                    Toast.makeText(LoginActivity.this, excecao,Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    //chamar tela principal
    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, NovoPrincipal.class));
        finish();
    }
}
