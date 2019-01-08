package com.felipe.levez.listadefilmes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.felipe.levez.listadefilmes.asynctasks.AsyncTaskBuscaFilmesFavoritos;
import com.felipe.levez.listadefilmes.interfaces.ListaFilmesContrato;
import com.felipe.levez.listadefilmes.models.Filme;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FilmeDAO  extends SQLiteConexao{

    public FilmeDAO(Context context) {
        super(context);
    }

    public void insert(Filme filme) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUNA_TITULO, filme.getTitulo());
        values.put(COLUNA_POSTER, filme.getPoster());
        values.put(COLUNA_DESCRICAO, filme.getDescricao());
        values.put(COLUNA_VOTO_MEDIO,  filme.getVotoMedio());
        values.put(COLUNA_DATA_LANCAMENTO, filme.getDataLancamento());
        if(filme.getId().equals(filme.getTitulo())){
            Cursor cursor = db.rawQuery("SELECT ID FROM TB_Filme ORDER BY ID DESC LIMIT 1;", null);
            if(cursor.moveToFirst()) filme.setId(String.valueOf(Integer.valueOf((cursor.getString(0)) +1)));
            else filme.setId("00000001");
            cursor.close();
        }
        values.put(COLUNA_ID, filme.getId());

        db.insert(NOME_TABELA, null, values);
        db.close();
    }

    public void getAll( ListaFilmesContrato.Model model) {
        ThreadPoolExecutor executor;
        executor= new ThreadPoolExecutor( 1,  Runtime.getRuntime().availableProcessors(), 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        executor.allowCoreThreadTimeOut(true);
        new AsyncTaskBuscaFilmesFavoritos(this.getReadableDatabase(), model).executeOnExecutor(executor);

    }

    public void remove(Filme filme){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOME_TABELA, COLUNA_ID + " = ?",
                new String[]{String.valueOf(filme.getId())});
        db.close();
    }

    public boolean getFilme(Filme filme) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(NOME_TABELA,
                new String[]{COLUNA_ID},
                COLUNA_ID + "=?",
                new String[]{filme.getId()}, null, null, null, null);
        boolean estaNosFavoritos = cursor.moveToFirst();
        cursor.close();

        return estaNosFavoritos;
    }
}
