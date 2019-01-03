package com.felipe.levez.listadefilmes.models;

import android.content.Context;

import com.felipe.levez.listadefilmes.database.FilmeDAO;
import com.felipe.levez.listadefilmes.database.RequestAPI;
import com.felipe.levez.listadefilmes.interfaces.ListaFilmesContrato;


public class ListaFilmesModel implements ListaFilmesContrato.Model {

    private final FilmeDAO filmeDAO;
    private final ListaFilmesContrato.Presenter presenter;
    private final RequestAPI api;

    public ListaFilmesModel(ListaFilmesContrato.Presenter presenter, Context context) {
        this.presenter = presenter;
        this.filmeDAO = new FilmeDAO(context);
        api = new RequestAPI(this, context);
    }

    @Override
    public void buscaFilmesFavoritos() {
        filmeDAO.getAll(this);
    }

    @Override
    public void insereFilme(Filme f) {
        filmeDAO.insert(f);
    }

    @Override
    public void visibilidadeProgressBarLista(int visibilidade) {
        presenter.visibilidadeProgressBarLista(visibilidade);
    }

    @Override
    public void buscaFilmesApi(String busca) {
        api.executeSearch( 1, busca);
    }

    @Override
    public void cancelaBuscasApi(){
        api.cancelRequest();
    }

    @Override
    public void adicionaFilme(Filme filme) {
        presenter.adicionaFilme(filme);
    }


    @Override
    public void exibeErroRequisicao(String message) {
        presenter.exibeErroRequisicao(message);
    }

    @Override
    public void exibeMensagemListaVazia() {
        presenter.exibeMensagemListaVazia();
    }

}
