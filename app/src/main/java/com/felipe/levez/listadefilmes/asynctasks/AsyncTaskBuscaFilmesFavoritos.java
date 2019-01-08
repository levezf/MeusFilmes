package com.felipe.levez.listadefilmes.asynctasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.felipe.levez.listadefilmes.interfaces.ListaFilmesContrato;
import com.felipe.levez.listadefilmes.models.Filme;

public class AsyncTaskBuscaFilmesFavoritos extends AsyncTask<Void, Filme, Void> {

    private final SQLiteDatabase db;
    private final ListaFilmesContrato.Model model;


    public AsyncTaskBuscaFilmesFavoritos(SQLiteDatabase db, ListaFilmesContrato.Model model) {
        this.db = db;
        this.model = model;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String sqlQuery = "SELECT Titulo, Poster, Descricao, Id, votoMedio, dataLancamento FROM TB_Filme;";
        Cursor cursor = db.rawQuery(sqlQuery, null);
        if(cursor.moveToFirst()){
            do {
                publishProgress(new Filme(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));


            }while (cursor.moveToNext());
        }else
            publishProgress( new Filme());
        cursor.close();

        return null;
    }

    @Override
    protected void onProgressUpdate(Filme... values) {
        if(values[0].getTitulo()!=null)
            model.adicionaFilme(values[0]);
        else
            model.exibeMensagemListaVazia();
    }
}
