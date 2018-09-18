package com.carteira.minha.carteiravirtual.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.carteira.minha.carteiravirtual.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.carteira.minha.carteiravirtual.model.Movimentacao;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView textoSaudacao, textoSaldo;
    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoUsuario = 0.0;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutentificacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacoes;

    private RecyclerView recyclerView;
   // private AdapterMovimentacao adapterMovimentacao;
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
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.menuCategoria :
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    public void configuraCalendarView(){

        // configurar data em portugues
        CharSequence meses[] = {"Janeiro","Fevereiro", "Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
        calendarView.setTitleMonths( meses );

//        CalendarDay dataAtual = calendarView.getCurrentDate();
//        String mesSelecionado = String.format("%02d", (dataAtual.getMonth() + 1) );
//        mesAnoSelecionado = String.valueOf( mesSelecionado + "" + dataAtual.getYear() );
//
//        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
//            @Override
//            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
//                String mesSelecionado = String.format("%02d", (date.getMonth() + 1) );
//                mesAnoSelecionado = String.valueOf( mesSelecionado + "" + date.getYear() );
//
//                movimentacaoRef.removeEventListener( valueEventListenerMovimentacoes );
//                recuperarMovimentacoes();
//            }
//        });

    }



//    direcionar pagina
    public void adicionarDespesa(View view){ startActivity(new Intent(this, DespesasActivity.class)); }
    public void adicionarRenda(View view){
        startActivity(new Intent(this, RendasActivity.class));
    }
    public void adicionarOrcamento(View view){ startActivity(new Intent(this, OrcamentoActivity.class));  }
}
