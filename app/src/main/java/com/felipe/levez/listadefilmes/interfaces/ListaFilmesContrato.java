package com.felipe.levez.listadefilmes.interfaces;

import com.felipe.levez.listadefilmes.models.Filme;

public interface ListaFilmesContrato {


    interface View{
        void insereFilme(Filme filme);
        void exibeErroRequisicao(String error);
        void visibilidadeProgressBarLista(int visibilidade);
        void exibeMensagemListaVazia();
    }
    interface Presenter{
        void cancelaBuscasApi();
        void adicionaFilme(Filme filme);
        void exibeErroRequisicao(String error);
        void buscaFilmes();
        void visibilidadeProgressBarLista(int visibilidade);
        void buscaFilmes(String query);
        void exibeMensagemListaVazia();
        void insereFilme(Filme f);
    }
    interface Model{
        void cancelaBuscasApi();
        void buscaFilmesFavoritos();
        void insereFilme(Filme f);
        void visibilidadeProgressBarLista(int visibilidade);
        void buscaFilmesApi(String busca);
        void adicionaFilme(Filme filme);
        void exibeErroRequisicao(String message);
        void exibeMensagemListaVazia();
    }

}
