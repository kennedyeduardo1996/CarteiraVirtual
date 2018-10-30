package com.carteira.minha.carteiravirtual.activity;

import android.app.DatePickerDialog;

import android.content.Intent;
import com.carteira.minha.carteiravirtual.activity.RendasActivity;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.carteira.minha.carteiravirtual.R;
import com.carteira.minha.carteiravirtual.helper.Base64Custom;
import com.carteira.minha.carteiravirtual.helper.DateCustom;
import com.carteira.minha.carteiravirtual.model.Movimentacao;
import com.carteira.minha.carteiravirtual.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;




public class DespesasActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutentificacao();
    private Double despesaTotal;
    private Double rendaTotal;

    private RendasActivity rendasActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);

        //Preenche o campo data com a date atual
        campoData.setText( DateCustom.dataAtual() );

//        busca a despesas total e salva numa var
        recuperarDespesaTotal();

        recuperarRendaTotal();

        campoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFreagment();
                datePicker.show(getSupportFragmentManager(), "date picker");


            }
        });

        Intent incomingIntent = getIntent();
        String categoria = incomingIntent.getStringExtra("categoria");
        campoCategoria.setText(categoria);
    }

    //passar a data pelo calendario


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        String dataselecionada = (dia < 10 ? "0" + dia : dia) + "/" + ((mes+1) < 10 ? "0" + (mes+1) : (mes+1)) +"/" + ano;
        campoData.setText( dataselecionada );

    }


        public void salvarDespesa(View view){


            if ( validarCamposDespesa() ){

                movimentacao = new Movimentacao();
                String data = campoData.getText().toString();
                Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

                movimentacao.setValor( valorRecuperado );
                movimentacao.setCategoria( campoCategoria.getText().toString() );
                movimentacao.setDescricao( campoDescricao.getText().toString() );
                movimentacao.setData( data );
                movimentacao.setTipo( "despesa" );

//            antes de salva a movimentação deve-se primeiro atualizar a despesa
                Double despesaAtualizada = despesaTotal + valorRecuperado;
                atualizarDespesa( despesaAtualizada );


                movimentacao.salvar( data );

                finish();

            }


        }

        //    verifica se os campos estão preenchido e retorna true ou false
        public Boolean validarCamposDespesa(){

            String textoValor = campoValor.getText().toString();
            String textoData = campoData.getText().toString();
            String textoCategoria = campoCategoria.getText().toString();
            String textoDescricao = campoDescricao.getText().toString();

            if ( !textoValor.isEmpty() ){
                if ( !textoData.isEmpty() ){
                    if ( !textoCategoria.isEmpty() ){
                        if ( !textoDescricao.isEmpty() ){
                            return true;
                        }else {
                            Toast.makeText(DespesasActivity.this,
                                    "Descrição não foi preenchida!",
                                    Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }else {
                        Toast.makeText(DespesasActivity.this,
                                "Categoria não foi preenchida!",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else {
                    Toast.makeText(DespesasActivity.this,
                            "Data não foi preenchida!",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(DespesasActivity.this,
                        "Valor não foi preenchido!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }


        }



        public void recuperarDespesaTotal(){

//        recuperando do banco o email do usuario
            String emailUsuario = autenticacao.getCurrentUser().getEmail();
//        convertendo o email para idusuario
            String idUsuario = Base64Custom.codificarBase64( emailUsuario );
            DatabaseReference usuarioRef = firebaseRef.child("usuarios").child( idUsuario );

            usuarioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                pegando dados do banco e transformando em um array(usuario)
                    Usuario usuario = dataSnapshot.getValue( Usuario.class );

                    despesaTotal = usuario.getDespesaTotal();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        //    atualizar despesa dentro do banco de dados
        public void atualizarDespesa(Double despesa){

            String emailUsuario = autenticacao.getCurrentUser().getEmail();
            String idUsuario = Base64Custom.codificarBase64( emailUsuario );
            DatabaseReference usuarioRef = firebaseRef.child("usuarios").child( idUsuario );

            usuarioRef.child("despesaTotal").setValue(despesa);

        }

        public void listaCategoriaDespesa(View view) {
            startActivity(new Intent(this, ListaCategoriaDespesaActivity.class));
        }



    public void salvarRenda(View view){

        if ( validarCamposDespesa() ){

            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

            movimentacao.setValor( valorRecuperado );
            movimentacao.setCategoria( campoCategoria.getText().toString() );
            movimentacao.setDescricao( campoDescricao.getText().toString() );
            movimentacao.setData( data );
            movimentacao.setTipo( "renda" );
            Double rendaAtualizada = rendaTotal + valorRecuperado;
            atualizarRenda( rendaAtualizada );
            movimentacao.salvar( data );
            finish();
        }


    }

//    public Boolean validarCamposRenda(){
//
//        String textoValor = campoValor.getText().toString();
//        String textoData = campoData.getText().toString();
//        String textoCategoria = campoCategoria.getText().toString();
//        String textoDescricao = campoDescricao.getText().toString();
//
//        if ( !textoValor.isEmpty() ){
//            if ( !textoData.isEmpty() ){
//                if ( !textoCategoria.isEmpty() ){
//                    if ( !textoDescricao.isEmpty() ){
//                        return true;
//                    }else {
//                        Toast.makeText(RendasActivity.this,
//                                "Descrição não foi preenchida!",
//                                Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//                }else {
//                    Toast.makeText(RendasActivity.this,
//                            "Categoria não foi preenchida!",
//                            Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            }else {
//                Toast.makeText(RendasActivity.this,
//                        "Data não foi preenchida!",
//                        Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        }else {
//            Toast.makeText(RendasActivity.this,
//                    "Valor não foi preenchido!",
//                    Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//
//    }


    public void recuperarRendaTotal(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child( idUsuario );

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue( Usuario.class );
                rendaTotal = usuario.getRendaTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void atualizarRenda(Double renda){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child( idUsuario );

        usuarioRef.child("rendaTotal").setValue(renda);

    }

    public void listaCategoriaRenda(View view) {
        startActivity(new Intent(this, ListaCategoriaRendaActivity.class));
    }


    }
