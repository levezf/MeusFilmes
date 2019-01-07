package com.felipe.levez.listadefilmes.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.felipe.levez.listadefilmes.R;
import com.felipe.levez.listadefilmes.activitys.DetailsFilmeScrollingActivity;
import com.felipe.levez.listadefilmes.adapters.ListaFilmesAdapter;
import com.felipe.levez.listadefilmes.interfaces.FilmeClickListener;
import com.felipe.levez.listadefilmes.interfaces.ListaFilmesContrato;
import com.felipe.levez.listadefilmes.models.Filme;
import com.felipe.levez.listadefilmes.presenter.ListaFilmesPresenter;

import java.util.ArrayList;
import java.util.Objects;

import static com.felipe.levez.listadefilmes.models.Filme.criaFilmeManualmente;

public class ListaFilmesFragment extends Fragment implements ListaFilmesContrato.View{

    private static final String SAVED_LISTA_FILMES = "lista_filmes";
    private static final String SAVED_PESQUISANDO = "pesquisando";
    private static final String SAVED_SUBMIT = "submit";
    private static final String OPT_FILME = "OPT_FILME";
    private static final String ARG_TIPO_LISTA = "ARG_TIPO_LISTA";
    private static final int ARG_TIPO_BUSCA = 0;
    private static final int ARG_TIPO_FAV = 1;
    private static final int ORDENA_LANCAMENTO = 0;
    private static final int ORDENA_TITULO = 1;
    private static final int REQUEST_CODE = 1;
    private static final String RESULT_ACTIVITY_DETAILS = "result";

    private View view;
    private ArrayList<Filme> lista_filmes = new ArrayList<>();
    private RecyclerView rv_lista_filmes;
    private ListaFilmesPresenter presenter;
    private ListaFilmesAdapter adapter;
    private TextView tv_feedback;
    private int tipoLista;
    private String pesquisando=null;
    private ProgressBar progress_lista;
    private boolean submitSearch = false;
    private int positionItemAessadoNoDetails;
    private FloatingActionButton fab_add;
    private MenuItem menuPesquisa;

    public static ListaFilmesFragment newInstance(){
        return new ListaFilmesFragment();
    }

    public ListaFilmesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);

        if(savedInstanceState!=null){
            lista_filmes = savedInstanceState.getParcelableArrayList(SAVED_LISTA_FILMES);
            tipoLista = savedInstanceState.getInt(ARG_TIPO_LISTA);
            pesquisando = savedInstanceState.getString(SAVED_PESQUISANDO);
            submitSearch = savedInstanceState.getBoolean(SAVED_SUBMIT);
        }else{
            assert getArguments() != null;
            tipoLista = getArguments().getInt(ARG_TIPO_LISTA);
        }

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_lista_filmes,container,false);
        this.view = view;
        setupFindViewByIds();
        presenter = new ListaFilmesPresenter(this, lista_filmes, getContext());
        setupListaFilmes();


        if (tipoLista == ARG_TIPO_FAV) {
            fab_add.show();
        } else {
            fab_add.hide();
        }


        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaAlertDialogADDFilme();
            }
        });

        return view;
    }

    private void chamaAlertDialogADDFilme(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        @SuppressLint("InflateParams")
        View view = layoutInflater.inflate(R.layout.filme_add_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialogBuilderUserInput.setView(view);

        final EditText userInputDialogEditText = view.findViewById(R.id.titulo_filme);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(R.string.btn_alert_dialog_adicionar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String textoEditText = userInputDialogEditText.getText().toString();
                        if(userInputDialogEditText.getText()!=null && !textoEditText.isEmpty()){
                            Filme f = criaFilmeManualmente(textoEditText);
                            adapter.insereItem(f);
                            presenter.insereFilme(f);
                        }
                    }
                })

                .setNegativeButton(R.string.btn_alert_dialog_cancelar,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    @Override
    public void visibilidadeProgressBarLista(int visibilidade){
        progress_lista.setVisibility(visibilidade);
    }

    @Override
    public void exibeMensagemListaVazia() {
        tv_feedback.setVisibility(View.VISIBLE);
        tv_feedback.setText(getString(R.string.lista_filmes_vazia));
    }

    private void setupListaFilmes(){

        rv_lista_filmes.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_lista_filmes.setLayoutManager(layoutManager);

        adapter = new ListaFilmesAdapter(lista_filmes);
        rv_lista_filmes.setItemAnimator(null);
        adapter.setOnItemClickListener(new FilmeClickListener() {
            @Override
            public void onFilmeClick(int position) {
               // pesquisando=null;
                Intent intent = new Intent(getContext(), DetailsFilmeScrollingActivity.class);
                intent.putExtra(OPT_FILME, adapter.lista_filmes.get(position));
                positionItemAessadoNoDetails = position;
                intent.putExtra(ARG_TIPO_LISTA, tipoLista);
                startActivityForResult(intent, REQUEST_CODE);
            }

        });
        rv_lista_filmes.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
        rv_lista_filmes.setVisibility(View.VISIBLE);
        rv_lista_filmes.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                boolean result=data.getBooleanExtra(RESULT_ACTIVITY_DETAILS, false);
                if(tipoLista == ARG_TIPO_FAV)
                    if(result){
                        adapter.removeItem(positionItemAessadoNoDetails);
                        if(adapter.lista_filmes.isEmpty()){
                            exibeMensagemListaVazia();
                        }
                    }
            }
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_LISTA_FILMES, lista_filmes);
        outState.putInt(ARG_TIPO_LISTA, tipoLista);
        if(pesquisando != null && pesquisando.isEmpty() && !menuPesquisa.isActionViewExpanded()){
            pesquisando=null;
        }
        outState.putString(SAVED_PESQUISANDO, pesquisando);
        outState.putBoolean(SAVED_SUBMIT, submitSearch);
    }

    private void setupFindViewByIds(){
        rv_lista_filmes = view.findViewById(R.id.lista_recycler);
        tv_feedback = view.findViewById(R.id.tv_lista_vazia);
        progress_lista = view.findViewById(R.id.progress_lista);
        fab_add = view.findViewById(R.id.fab);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState==null){
            if(tipoLista==ARG_TIPO_FAV){
                presenter.buscaFilmes();
            }
        }
        if((pesquisando==null || pesquisando.isEmpty()) && tipoLista==ARG_TIPO_BUSCA){
            msgCollapse();
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate((tipoLista==ARG_TIPO_FAV)?R.menu.menu_filtro:R.menu.menu_busca, menu);
        if(tipoLista==ARG_TIPO_BUSCA){


            menuPesquisa = menu.findItem(R.id.action_pesquisar);
            SearchView sv_busca = (SearchView) menuPesquisa.getActionView();
            sv_busca.setQueryHint(getString(R.string.pesquisar));


            menuPesquisa.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_ALWAYS );
            sv_busca.setMaxWidth(Integer.MAX_VALUE);



            if(pesquisando !=null){
                menuPesquisa.expandActionView();
                if(!pesquisando.isEmpty()) {
                    sv_busca.setQuery(pesquisando, submitSearch);
                    if(!submitSearch){
                        msgExpand();
                    }else{
                        exibeListaDeFilmesCarregada();
                    }

                }else{
                    msgExpand();
                }
            }

            menuPesquisa.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    msgExpand();
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    presenter.cancelaBuscasApi();
                    submitSearch =false;
                    adapter.removeAll();
                    msgCollapse();
                    return true;
                }
            });


            sv_busca.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    if(query.isEmpty()){
                        msgExpand();
                    }else{
                        presenter.cancelaBuscasApi();
                        adapter.removeAll();
                        presenter.buscaFilmes(query);
                        submitSearch=true;
                        exibeListaDeFilmesCarregada();
                        pesquisando = query;

                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    pesquisando=newText;
                    return false;
                }
            });


        }
    }

    
    private void exibeListaDeFilmesCarregada(){
        tv_feedback.setVisibility(View.INVISIBLE);
        rv_lista_filmes.setVisibility(View.VISIBLE);
    }

    private void msgExpand(){
        adapter.removeAll();
        if(adapter.lista_filmes.isEmpty()){
            tv_feedback.setVisibility(View.VISIBLE);
            rv_lista_filmes.setVisibility(View.INVISIBLE);
            tv_feedback.setText(getString(R.string.msg_feedback_expand));
        }else{
            tv_feedback.setVisibility(View.INVISIBLE);
            rv_lista_filmes.setVisibility(View.VISIBLE);
        }
    }
    private void msgCollapse(){
        adapter.removeAll();
        tv_feedback.setText(getString(R.string.msg_feedback_collapse));
        tv_feedback.setVisibility(View.VISIBLE);
        rv_lista_filmes.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_filter_nome:
                adapter.ordena(ORDENA_TITULO);
                break;
            case R.id.menu_filter_lancamento:
                adapter.ordena(ORDENA_LANCAMENTO);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void insereFilme(Filme filme) {
        adapter.insereItem(filme);
    }

    @Override
    public void exibeErroRequisicao(String error) {
        Snackbar.make(view, error, Snackbar.LENGTH_LONG).show();
    }
}
