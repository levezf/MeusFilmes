package com.felipe.levez.listadefilmes.interfaces;

import com.felipe.levez.listadefilmes.models.Filme;

public interface DetailsFilmeScrollingActivityContrato {

    interface View{

    }
    interface Presenter{
        void inserirFilmeFavorito(Filme filme);
        void removerFilmefavorito(Filme filme);
        boolean filmeEstaNosFavoritos(Filme filme);
    }
    interface Model{
        void adicionaFilme(Filme filme);
        void removeFilme(Filme filme);
        boolean filmeEstaNosFavoritos(Filme filme);
    }

}
