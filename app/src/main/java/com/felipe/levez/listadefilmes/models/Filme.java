package com.felipe.levez.listadefilmes.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Locale;

public class Filme implements Parcelable {


    private final String titulo;
    private final String poster;
    private final String descricao;
    private String id;
    private final String votoMedio;
    private final String dataLancamento;


    public Filme(){
        this(null, null, null, null, null, null);
    }

    public static Filme criaFilmeManualmente(String titulo){
        return new Filme(titulo, null, "Filme Adicionado Manualmente: Não há dados sobre este filme", titulo, "Não há recomendação", "-");
    }
    public Filme(String titulo, String poster, String descricao, String id, String votoMedio, String dataLancamento) {
        this.titulo = titulo;
        this.poster = poster;
        this.descricao = descricao;
        this.id = id;
        this.votoMedio = votoMedio;
        this.dataLancamento = dataLancamento;
    }
    private Filme(Parcel in){
        this.titulo = in.readString();
        this.votoMedio = in.readString();
        this.id =  in.readString();
        this.descricao =  in.readString();
        this.poster =  in.readString();
        this.dataLancamento = in.readString();
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPoster() {
        return poster;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVotoMedio() {
        return votoMedio;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.titulo);
        dest.writeString(this.votoMedio);
        dest.writeString(this.id);
        dest.writeString(this.descricao);
        dest.writeString(this.poster);
        dest.writeString(this.dataLancamento);

    }

    public static final Creator<Filme> CREATOR = new Creator<Filme>() {
        @Override
        public Filme createFromParcel(Parcel in) {
            return new Filme(in);
        }

        @Override
        public Filme[] newArray(int size) {
            return new Filme[size];
        }
    };

    public static final Comparator<Filme> TituloComparator = new Comparator<Filme>() {
        @Override
        public int compare(Filme o1, Filme o2) {
            return (o1.getTitulo().compareToIgnoreCase(o2.getTitulo()));
        }
    };

    public static final Comparator<Filme> DataComparator = new Comparator<Filme>() {
        @Override
        public int compare(Filme o1, Filme o2) {
            try {
               return o1.compare(o2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return -1;
        }
    };

    private int compare(Filme f) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        long atual = format.parse(this.getDataLancamento()).getTime();
        long proximo = format.parse(f.getDataLancamento()).getTime();
        return Long.compare(proximo, atual);
    }

}
