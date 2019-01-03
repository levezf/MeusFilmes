package com.felipe.levez.listadefilmes.models;

import android.content.Context;

import com.felipe.levez.listadefilmes.database.FilmeDAO;
import com.felipe.levez.listadefilmes.interfaces.DetailsFilmeScrollingActivityContrato;

public class DetailsFilmeScrollingActivityModel implements DetailsFilmeScrollingActivityContrato.Model {

    private final FilmeDAO filmeDAO;

    public DetailsFilmeScrollingActivityModel(Context context) {
        this.filmeDAO = new FilmeDAO(context);
    }

    @Override
    public void adicionaFilme(Filme filme) {
        filmeDAO.insert(filme);
    }

    @Override
    public void removeFilme(Filme filme) {
        filmeDAO.remove(filme);
    }

    @Override
    public boolean filmeEstaNosFavoritos(Filme filme) {
        return filmeDAO.getFilme(filme);
    }
}
