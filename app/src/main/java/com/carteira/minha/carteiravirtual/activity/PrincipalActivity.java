package com.carteira.minha.carteiravirtual.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.carteira.minha.carteiravirtual.adpter.AdapterMovimentacao;
import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.carteira.minha.carteiravirtual.R;
import com.carteira.minha.carteiravirtual.model.Notificacao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.carteira.minha.carteiravirtual.model.Movimentacao;
import com.carteira.minha.carteiravirtual.helper.Base64Custom;
import com.carteira.minha.carteiravirtual.model.Usuario;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView textoSaudacao, textoSaldo,textoNotification;
    private Double despesaTotal = 0.0;
    private Double rendaTotal = 0.0;
    private Double resumoUsuario = 0.0;
    private Double resumoUsuario2 = 0.0;
    private Double valorNotificacao = 0.0;
    private Double valorNotRecuperado = 0.0;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutentificacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();


//    variaveis para para a atualização do banco quando não estiver usando
    private DatabaseReference usuarioRef;
    private DatabaseReference usuarioRef2;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerUsuario2;
    private ValueEventListener valueEventListenerMovimentacoes;

    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private Movimentacao movimentacao;
    private Notificacao notificacao;

    private DatabaseReference movimentacaoRef;
    private String mesAnoSelecionado;

    private ConfiguracoesActivity configuracoesActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textoSaldo = findViewById(R.id.textSaldo);
        textoSaudacao = findViewById(R.id.textSaudacao);
        textoNotification = findViewById(R.id.textNotification);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerMovimentos);
        configuraCalendarView();
        swipe();


        //Configurar adapter para o recyclerview
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes,this);

        //Configurar RecyclerView (lista da tela principal)
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( adapterMovimentacao );

//        linha para manter os dados atualizados offline
//        firebaseRef.keepSynced(true);
//        usuarioRef.keepSynced(true);

    }


//    Recuperar dados d movimentaçoes banco para listar

    public void recuperarMovimentacoes(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        movimentacaoRef = firebaseRef.child("movimentacao")
                .child( idUsuario )
                .child( mesAnoSelecionado );



        //        listagem para evento de click
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Item precionado" + view,
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                ));

        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                movimentacoes.clear();

                //recuperar todos os filhos do movimentações no banco
                for (DataSnapshot dados: dataSnapshot.getChildren() ){



                    Movimentacao movimentacao = dados.getValue( Movimentacao.class );
//                    pegando a chave da movimentacao (id)
                    movimentacao.setKey( dados.getKey() );
//                    cria um array com as movimentações
                    movimentacoes.add( movimentacao );

                }
//                notifica q os dados foram atualizados
                adapterMovimentacao.notifyDataSetChanged();

            }





            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    // arrastar os itens da lista para poder excluir
    public void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                //desativa o movimento para cima e baixo
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
               excluirMovimentacao( viewHolder );
            }
        };

        new ItemTouchHelper( itemTouch ).attachToRecyclerView( recyclerView );

    }





    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configura AlertDialog
        alertDialog.setTitle("Excluir Movimentação da Conta");
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir essa movimentação de sua conta?");
        alertDialog.setCancelable(false);

        //caso aceite a exclusão
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get( position );

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64( emailUsuario );
                movimentacaoRef = firebaseRef.child("movimentacao")
                        .child( idUsuario )
                        .child( mesAnoSelecionado );

                movimentacaoRef.child( movimentacao.getKey() ).removeValue();
                adapterMovimentacao.notifyItemRemoved( position );
                atualizarSaldo();

            }
        });
        //caso negue a exclusão
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PrincipalActivity.this,
                        "Cancelado",
                        Toast.LENGTH_SHORT).show();
                adapterMovimentacao.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();

    }

    //atualiza o total da carteira
    public void atualizarSaldo(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        usuarioRef = firebaseRef.child("usuarios").child( idUsuario );

        if ( movimentacao.getTipo().equals("renda") ){
            rendaTotal = rendaTotal - movimentacao.getValor();
            usuarioRef.child("rendaTotal").setValue(rendaTotal);
        }

        if ( movimentacao.getTipo().equals("despesa") ){
            despesaTotal = despesaTotal - movimentacao.getValor();
            usuarioRef.child("despesaTotal").setValue( despesaTotal );
        }

    }




    //    Recuperar dados d banco para exibir
    public void recuperarResumo(){


        //        recuperando do banco o email do usuario
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
//        convertendo o email para idusuario
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );

        usuarioRef = firebaseRef.child("usuarios").child( idUsuario );

        usuarioRef2 = firebaseRef.child("notificacao").child( idUsuario );



        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                retorna objeto usuario
                Usuario usuario = dataSnapshot.getValue( Usuario.class );

//                buscando os dados no banco atravez do usuario
                despesaTotal = usuario.getDespesaTotal();
                rendaTotal = usuario.getRendaTotal();
                resumoUsuario = rendaTotal - despesaTotal;

//                formatando numero para a exibição
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###,##0.00");
                String resultadoFormatado = decimalFormat.format( resumoUsuario );

                textoSaudacao.setText(  usuario.getNome() +", seu saldo atual é" );
                textoSaldo.setText( "R$ " + resultadoFormatado );



//       *********************************************************************************
//       *********************************************************************************
//       *********************************************************************************
//       *********************************************************************************
//       *********************************************************************************


                valueEventListenerUsuario2 = usuarioRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                retorna objeto usuario
                        Notificacao notificacao = dataSnapshot.getValue( Notificacao.class );

                        final Double valorNotRecuperado = notificacao.getValor();


//                formatando numero para a exibição
                        DecimalFormat decimalFormat = new DecimalFormat("");
                        String resultadoFormatado = decimalFormat.format( valorNotRecuperado );


//                        Notificacao notificacao = new Notificacao();
                        valorNotificacao = notificacao.getValor();


//
                DecimalFormat decimalFormat2 = new DecimalFormat("");
                String valorTotal = decimalFormat2.format( resumoUsuario );
                String ValorNoti = decimalFormat2.format( valorNotificacao );


                if((Double.parseDouble(valorTotal ) == Double.parseDouble( ValorNoti))&&  (notificacao.isCheck())   ){
                    //                formatando numero para a exibição
                    DecimalFormat decimalFormat3 = new DecimalFormat("###,###,###,##0.00");
                    String limite = decimalFormat3.format( valorNotificacao );
                    textoNotification.setText("Você atinguiu seu limite: " + limite );
                }else  if((Double.parseDouble(valorTotal ) < Double.parseDouble( ValorNoti)) && (notificacao.isCheck())  ){
                    //                formatando numero para a exibição
                    DecimalFormat decimalFormat3 = new DecimalFormat("###,###,###,##0.00");
                    String limite = decimalFormat3.format( valorNotificacao );
                    textoNotification.setText("Você ultrapassou seu limite: " + limite );
                }
                if(Double.parseDouble(valorTotal ) > Double.parseDouble( ValorNoti)){
                    textoNotification.setText("");

                }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


//       *********************************************************************************
//       *********************************************************************************
//       *********************************************************************************
//       *********************************************************************************
//       *********************************************************************************













//                Notificacao notificacao = new Notificacao();
//                valorNotificacao = notificacao.getValor();
//                    textoNotification.setText("Você atinguiu seu limite: "+ valorNotificacao);
//
//
//
//                DecimalFormat decimalFormat2 = new DecimalFormat("");
//                String valorTotal = decimalFormat2.format( resumoUsuario );
//                String ValorNoti = decimalFormat2.format( valorNotificacao );
//
//
//                if(Double.parseDouble(valorTotal ) < Double.parseDouble( ValorNoti)){
//                    textoNotification.setText("Você atinguiu seu limite: " );
//                }
//                if(Double.parseDouble(valorTotal ) > Double.parseDouble( ValorNoti)){
//                    textoNotification.setText("aaaa ");
//
//                }


//       *********************************************************************************


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }



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

                textoNotification.setText("Você atinguiu seu limite: "+ valorNotRecuperado);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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







    public void configuraCalendarView(){

        // configurar data em portugues
        CharSequence meses[] = {"Janeiro","Fevereiro", "Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
        calendarView.setTitleMonths( meses );


//        pegando o mes atual do calendarView
        CalendarDay dataAtual = calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth() + 1) );
        mesAnoSelecionado = String.valueOf( mesSelecionado + "" + dataAtual.getYear() );

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", (date.getMonth() + 1) );
                mesAnoSelecionado = String.valueOf( mesSelecionado + "" + date.getYear() );

                //remover o evento da movimentação antes do proximo mes
                movimentacaoRef.removeEventListener( valueEventListenerMovimentacoes );
                //quando o usuario mudar o mes irá sempre recuperar as movimentações
                recuperarMovimentacoes();
            }
        });

    }



//    direcionar pagina
    public void adicionarDespesa(View view){ startActivity(new Intent(this, DespesasActivity.class)); }
    public void adicionarRenda(View view){
        startActivity(new Intent(this, RendasActivity.class));
    }
    public void adicionarOrcamento(View view){ startActivity(new Intent(this, OrcamentoActivity.class));  }

    @Override
    protected void onStart() {

        super.onStart();
        recuperarResumo();
        recuperarMovimentacoes();
    }


    // chamado quando o aplicativo não esta em execução
    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener( valueEventListenerUsuario );
        movimentacaoRef.removeEventListener( valueEventListenerMovimentacoes );
    }
}
