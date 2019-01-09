package com.felipe.levez.listadefilmes.activitys;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.felipe.levez.listadefilmes.R;
import com.felipe.levez.listadefilmes.interfaces.DetailsFilmeScrollingActivityContrato;
import com.felipe.levez.listadefilmes.models.Filme;
import com.felipe.levez.listadefilmes.presenter.DetailsFilmeScrollingActivityPresenter;
import com.squareup.picasso.Picasso;
import java.util.Objects;

public class DetailsFilmeScrollingActivity extends AppCompatActivity implements DetailsFilmeScrollingActivityContrato.View {

    private static final String OPT_FILME = "OPT_FILME";
    private static final int ARG_TIPO_BUSCA = 0;
    private static final int ARG_TIPO_FAV = 1;
    private static final String EXTRA_TIPO_LISTA = "ARG_TIPO_LISTA";
    private static final String RESULT_ACTIVITY_DETAILS = "result";

    private FloatingActionButton fab;
    private ImageButton ib_toolbar;
    private Filme filme;
    private TextView descricao;
    private TextView data;
    private TextView votos;
    private Toolbar toolbar;
    private DetailsFilmeScrollingActivityContrato.Presenter presenter;
    private int tipo_lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            filme = Objects.requireNonNull(getIntent().getExtras()).getParcelable(OPT_FILME);
            tipo_lista = getIntent().getExtras().getInt(EXTRA_TIPO_LISTA);
        }
        else{
            filme = savedInstanceState.getParcelable(OPT_FILME);
            tipo_lista = savedInstanceState.getInt(EXTRA_TIPO_LISTA);
        }

        setContentView(R.layout.activity_details_filme_scrolling);
        setupFindViewByIds();
        setupValores();

        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        if(presenter.filmeEstaNosFavoritos(filme)){
            setCorFAB(true);
        }else{
            setCorFAB(false);
        }
        setupActionButtonFABFav();
        actionButtonImagemPoster();
    }

    private void actionButtonImagemPoster(){

        ib_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filme.getPoster()!=null && !filme.getPoster().equals("https://image.tmdb.org/t/p/w500/null")) {
                    chamaIntentImageDetails();
                }else{
                    Snackbar.make(findViewById(android.R.id.content),R.string.msg_poster_indisponivel, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void chamaIntentImageDetails() {
        Intent intent = new Intent(this, ImageDetailsActivity.class);
        intent.putExtra("link_image", filme.getPoster());
        intent.putExtra("nome_filme", filme.getTitulo());
        startActivity(intent);
    }

    private void setupActionButtonFABFav() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tipo_lista == ARG_TIPO_BUSCA){
                    if(Objects.equals(fab.getImageTintList(), ColorStateList.valueOf(getResources().getColor(R.color.colorRed)))){
                        presenter.removerFilmefavorito(filme);
                        setCorFAB(false);
                    }else{
                        presenter.inserirFilmeFavorito(filme);
                        setCorFAB(true);
                    }
                }else{
                    if(Objects.equals(fab.getImageTintList(), ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)))){
                        presenter.inserirFilmeFavorito(filme);
                        setCorFAB(true);
                    }else{
                        presenter.removerFilmefavorito(filme);
                        setCorFAB(false);
                    }
                }
            }
        });
    }

    private void setCorFAB(boolean ehFavorito){
        if(ehFavorito){
            fab.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
        }else {
            fab.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
        }
    }

    private void setupValores(){
        presenter = new DetailsFilmeScrollingActivityPresenter(getApplicationContext());
        toolbar.setTitle(filme.getTitulo());
        if(filme!=null && filme.getPoster()!=null)
            Picasso.get().load(filme.getPoster()).into(ib_toolbar);
        data.setText(filme.getDataLancamento());
        descricao.setText(filme.getDescricao());
        votos.setText(filme.getVotoMedio());
    }

    private void setupFindViewByIds() {
        toolbar =  findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        ib_toolbar = findViewById(R.id.iv_toolbar_collapsing);
        votos = findViewById(R.id.tv_nota);
        data = findViewById(R.id.tv_data);
        descricao = findViewById(R.id.tv_descricao);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(OPT_FILME, filme);
        outState.putInt(EXTRA_TIPO_LISTA, tipo_lista);
    }

    @Override
    public void onBackPressed() {
        Intent data = getIntent();
        boolean remover = ((tipo_lista==ARG_TIPO_FAV && !(fab.getImageTintList()==ColorStateList.valueOf(getResources().getColor(R.color.colorRed)))));
        data.putExtra(RESULT_ACTIVITY_DETAILS ,remover);
        this.setResult(RESULT_OK,data);
        this.finish();
    }
}
