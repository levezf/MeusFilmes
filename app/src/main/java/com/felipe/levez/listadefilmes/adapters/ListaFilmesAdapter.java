package com.felipe.levez.listadefilmes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.felipe.levez.listadefilmes.R;
import com.felipe.levez.listadefilmes.interfaces.FilmeClickListener;
import com.felipe.levez.listadefilmes.models.Filme;

import java.util.ArrayList;
import java.util.Collections;


public class ListaFilmesAdapter extends RecyclerView.Adapter<ListaFilmesAdapter.ViewHolder> {

    private static final int ORDENA_LANCAMENTO = 0;
    private static final int ORDENA_TITULO = 1;

    public final ArrayList<Filme> lista_filmes;
    private FilmeClickListener filmeClickListener;

    public ListaFilmesAdapter(ArrayList<Filme> filmes) {
        this.lista_filmes = filmes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_filmes,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if (lista_filmes.get(i).getTitulo()!=null){
            viewHolder.pb_dados.setVisibility(View.INVISIBLE);

            viewHolder.tv_titulo.setText(lista_filmes.get(i).getTitulo());
            viewHolder.tv_nota.setText((lista_filmes.get(i).getVotoMedio()==null)? "Não há recomendação":lista_filmes.get(i).getVotoMedio());

            viewHolder.tv_titulo.setVisibility(View.VISIBLE);
            viewHolder.tv_nota.setVisibility(View.VISIBLE);

        }else{
            viewHolder.pb_dados.setVisibility(View.VISIBLE);

            viewHolder.tv_titulo.setVisibility(View.INVISIBLE);
            viewHolder.tv_nota.setVisibility(View.INVISIBLE);
        }

        viewHolder.bindClick(i);
    }

    public void ordena(int tipo_ordenacao){
        Collections.sort(lista_filmes, tipo_ordenacao != ORDENA_LANCAMENTO ? Filme.TituloComparator : Filme.DataComparator);
        notifyDataSetChanged();
    }

    public void insereItem(Filme filme){
        this.lista_filmes.add(filme);
        notifyItemInserted(getItemCount());
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        this.lista_filmes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, lista_filmes.size());
    }

    @Override
    public int getItemCount() {
        return (lista_filmes==null)?0:lista_filmes.size();
    }

    public void setOnItemClickListener(FilmeClickListener filmeClickListener) {
        this.filmeClickListener = filmeClickListener;
    }

    public void removeAll() {
        this.lista_filmes.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView tv_titulo;
        private final TextView tv_nota;
        private final ProgressBar pb_dados;
        private final View itemView;


         ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nota = itemView.findViewById(R.id.tv_nota);
            tv_titulo = itemView.findViewById(R.id.tv_titulo);
            pb_dados = itemView.findViewById(R.id.progress_dados);
            this.itemView = itemView;
        }

        private void bindClick(final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filmeClickListener.onFilmeClick(position);
                }
            });
        }

    }
}
