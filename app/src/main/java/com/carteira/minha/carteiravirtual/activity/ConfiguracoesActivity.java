package com.carteira.minha.carteiravirtual.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.carteira.minha.carteiravirtual.R;
import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.carteira.minha.carteiravirtual.helper.Base64Custom;
import com.carteira.minha.carteiravirtual.model.Notificacao;
import com.carteira.minha.carteiravirtual.model.Orcamento;
import com.carteira.minha.carteiravirtual.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class ConfiguracoesActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutentificacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;




    private EditText editValor;
    private CheckBox checkBox;
    private Notificacao notificacao;
    private Double valorNotRecuperado = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        editValor = findViewById(R.id.editValor);
        checkBox = findViewById(R.id.checkBox);


    }

    public void salvarNotificacao(View view){

//        verificaNotificacaoSalva();
        notificacao = new Notificacao();

        Double valorRecuperado = Double.parseDouble(editValor.getText().toString());
        notificacao.setValor( valorRecuperado );
        if (checkBox.isChecked()){
            notificacao.setCheck(true);
        }else{
            notificacao.setCheck(false);
        }
        notificacao.salvar();
        finish();
    }





    //    Recuperar dados d banco para exibir
    public void recuperarNotificacao(){


//        recuperando do banco o email do usuario
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
//        convertendo o email para idusuario
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );

        usuarioRef = firebaseRef.child("notificacao").child( idUsuario );


        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                retorna objeto usuario
                Notificacao notificacao = dataSnapshot.getValue( Notificacao.class );

                valorNotRecuperado = notificacao.getValor();


//                formatando numero para a exibição
                DecimalFormat decimalFormat = new DecimalFormat("");
                String resultadoFormatado = decimalFormat.format( valorNotRecuperado );


                editValor.setText(resultadoFormatado);

                if (notificacao.isCheck()){
                    checkBox.setChecked(true);

                }else{
                    checkBox.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    @Override
    protected void onStart() {

        super.onStart();
        recuperarNotificacao();
    }


}
