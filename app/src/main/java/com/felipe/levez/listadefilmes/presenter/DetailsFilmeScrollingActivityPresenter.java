package com.felipe.levez.listadefilmes.presenter;

import android.content.Context;

import com.felipe.levez.listadefilmes.interfaces.DetailsFilmeScrollingActivityContrato;
import com.felipe.levez.listadefilmes.models.DetailsFilmeScrollingActivityModel;
import com.felipe.levez.listadefilmes.models.Filme;

public class DetailsFilmeScrollingActivityPresenter implements DetailsFilmeScrollingActivityContrato.Presenter {

    private final DetailsFilmeScrollingActivityContrato.Model model;

    public DetailsFilmeScrollingActivityPresenter(Context context) {
        this.model = new DetailsFilmeScrollingActivityModel(context);
    }

    @Override
    public void inserirFilmeFavorito(Filme filme) {
        model.adicionaFilme(filme);
    }

    @Override
    public void removerFilmefavorito(Filme filme) {
        model.removeFilme(filme);
    }

    @Override
    public boolean filmeEstaNosFavoritos(Filme filme) {
        return model.filmeEstaNosFavoritos(filme);
    }
}
