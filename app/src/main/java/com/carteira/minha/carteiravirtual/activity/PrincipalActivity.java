package com.carteira.minha.carteiravirtual.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.carteira.minha.carteiravirtual.adpter.AdapterMovimentacao;
import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.carteira.minha.carteiravirtual.R;
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
    private TextView textoSaudacao, textoSaldo;
    private Double despesaTotal = 0.0;
    private Double rendaTotal = 0.0;
    private Double resumoUsuario = 0.0;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutentificacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

//    variaveis para para a atualização do banco quando não estiver usando
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacoes;

    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private Movimentacao movimentacao;

    private DatabaseReference movimentacaoRef;
    private String mesAnoSelecionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textoSaldo = findViewById(R.id.textSaldo);
        textoSaudacao = findViewById(R.id.textSaudacao);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerMovimentos);
        configuraCalendarView();


        //Configurar adapter para o recyclerview
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes,this);

        //Configurar RecyclerView (lista da tela principal)
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( adapterMovimentacao );

    }


//    Recuperar dados d movimentaçoes banco para listar

    public void recuperarMovimentacoes(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        movimentacaoRef = firebaseRef.child("movimentacao")
                .child( idUsuario )
                .child( mesAnoSelecionado );

        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                movimentacoes.clear();

                //recuperar todos os filhos do movimentações no banco
                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    Movimentacao movimentacao = dados.getValue( Movimentacao.class );
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


//    Recuperar dados d banco para exibir
    public void recuperarResumo(){

        //        recuperando do banco o email do usuario
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
//        convertendo o email para idusuario
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        usuarioRef = firebaseRef.child("usuarios").child( idUsuario );

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
            case R.id.menuSair :
                autenticacao.signOut();
                startActivity(new Intent(this, CadastroActivity.class));
                finish();
            case R.id.menuCategoria :
                startActivity(new Intent(this, ListaCategoriaDespesaActivity.class));
                finish();
                break;
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

                movimentacaoRef.removeEventListener( valueEventListenerMovimentacoes );
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
