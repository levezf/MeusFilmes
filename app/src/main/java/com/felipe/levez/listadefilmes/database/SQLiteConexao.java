package com.felipe.levez.listadefilmes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

class SQLiteConexao extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "databases_filmes";
    private static final int DATABASE_VERSION = 1;
    private static final String SCRIPT_CREATE_TABELA_FILMES = "CREATE TABLE `TB_Filme` ( `Titulo` TEXT, `Poster` TEXT, `Descricao` TEXT, `id` INTEGER PRIMARY KEY, `votoMedio` TEXT, `dataLancamento` TEXT );";
    private static final String SCRIPT_DELTE_TABELA_FILMES = "DROP TABLE IF EXISTS TB_Filme;";
    private static final String LOCAL_DATABASE = Environment.getExternalStorageDirectory().getPath()+"/DB_MOVIES/";

    static final String NOME_TABELA = "TB_Filme";
    static final String COLUNA_TITULO = "Titulo";
    static final String COLUNA_POSTER = "Poster";
    static final String COLUNA_DESCRICAO = "Descricao";
    static final String COLUNA_ID = "id";
    static final String COLUNA_VOTO_MEDIO = "votoMedio";
    static final String COLUNA_DATA_LANCAMENTO = "dataLancamento";

    SQLiteConexao(Context context) {

        super(context,  LOCAL_DATABASE+DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_CREATE_TABELA_FILMES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SCRIPT_DELTE_TABELA_FILMES);
        onCreate(db);
    }

}
