package com.carteira.minha.carteiravirtual.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.carteira.minha.carteiravirtual.R;
import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.carteira.minha.carteiravirtual.helper.Base64Custom;
import com.carteira.minha.carteiravirtual.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth autentificacao;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //passando os valores para variaveis
        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoCadastrar = findViewById(R.id.buttonCadastrar);

        //ao clicar no botao executar
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //convertendo o valor recebido para string
                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                //verificanco se os campos estao vazios
                if (!textoNome.isEmpty()){
                    if (!textoEmail.isEmpty()){
                        if (!textoSenha.isEmpty()){
                            //instanciando o usuario
                            usuario = new Usuario();
                            usuario.setNome(textoNome);
                            usuario.setEmail(textoEmail);
                            usuario.setSenha(textoSenha);
                            cadastrarUsuario();
                        }else{
                            Toast.makeText(CadastroActivity.this, "Preencha a Senha!",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this, "Preencha o Email!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroActivity.this, "Preencha o Nome!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cadastrarUsuario(){
        autentificacao = ConfiguracaoFirebase.getFirebaseAutentificacao();
        //passando os valores para o banco
        autentificacao.createUserWithEmailAndPassword(
            usuario.getEmail(), usuario.getSenha()
                //verificando se foi salvo
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();
                    finish();

                }else{

                    String excecao;
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Digite um email valido";
                    }catch(FirebaseAuthUserCollisionException e){
                        excecao = "Email já cadastrado";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário"+e.getMessage();
                        e.printStackTrace();

                    }
                    Toast.makeText(CadastroActivity.this, excecao ,Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
