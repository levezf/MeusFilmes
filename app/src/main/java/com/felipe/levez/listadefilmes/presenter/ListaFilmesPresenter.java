package com.felipe.levez.listadefilmes.presenter;

import android.content.Context;

import com.felipe.levez.listadefilmes.interfaces.ListaFilmesContrato;
import com.felipe.levez.listadefilmes.models.Filme;
import com.felipe.levez.listadefilmes.models.ListaFilmesModel;

import java.util.ArrayList;

public class ListaFilmesPresenter implements ListaFilmesContrato.Presenter {


    private final ArrayList<Filme> filmes;
    private final ListaFilmesModel model;
    private final ListaFilmesContrato.View view;

    public ListaFilmesPresenter(ListaFilmesContrato.View view, ArrayList<Filme> filmes, Context context) {
        this.filmes =filmes;
        this.model = new ListaFilmesModel(this, context);
        this.view = view;
    }

    @Override
    public void cancelaBuscasApi() {
        model.cancelaBuscasApi();
    }

    @Override
    public void adicionaFilme(Filme filme) {
        view.insereFilme(filme);
    }

    @Override
    public void exibeErroRequisicao(String error) {
        view.exibeErroRequisicao(error);
    }

    @Override
    public void buscaFilmes() {
        model.buscaFilmesFavoritos();
    }
    @Override
    public void buscaFilmes(String query) {
        filmes.clear();
        model.buscaFilmesApi(query);
    }

    @Override
    public void exibeMensagemListaVazia() {
        view.exibeMensagemListaVazia();
    }

    @Override
    public void insereFilme(Filme f) {
        model.insereFilme(f);
    }

    @Override
    public void visibilidadeProgressBarLista(int visibilidade) {
        view.visibilidadeProgressBarLista(visibilidade);
    }


}
