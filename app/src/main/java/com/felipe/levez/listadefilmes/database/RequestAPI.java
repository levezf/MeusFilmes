package com.felipe.levez.listadefilmes.database;

import android.content.Context;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.felipe.levez.listadefilmes.R;
import com.felipe.levez.listadefilmes.interfaces.ListaFilmesContrato;
import com.felipe.levez.listadefilmes.models.Filme;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestAPI {

    private final ListaFilmesContrato.Model model;
    private final String msg_erro_nao_enocntrado;
    private final RequestQueue queue;
    private final String msg_erro_internet;

    public RequestAPI(ListaFilmesContrato.Model model, Context context) {
        this.model = model;
        queue = Volley.newRequestQueue(context);
        msg_erro_internet = context.getString(R.string.msg_erro_problema_conexao_internet);
        msg_erro_nao_enocntrado = context.getString(R.string.msg_erro_nenhum_encontrado);
    }

    public void executeSearch(int page, final String busca) {
        if(page == 1)
            model.visibilidadeProgressBarLista(View.VISIBLE);
        String url = "https://api.themoviedb.org/3/search/movie?api_key=8ae03112f333d6386161cffe6268c009&language=pt-BR&query=";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + busca + ("&page=" + (String.valueOf(page))),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String image = "https://image.tmdb.org/t/p/w500/";
                        try {

                            JSONObject response_json = new JSONObject(response);
                            int page_max = response_json.getInt("total_pages");
                            int page_atual = response_json.getInt("page");
                            int total_results = response_json.getInt("total_results");

                            JSONArray array_results = response_json.getJSONArray("results");

                            if(total_results == 0){

                                model.exibeErroRequisicao(msg_erro_nao_enocntrado);

                            }else {
                                for (int i = 0; i < array_results.length(); i++) {
                                    JSONObject filme = array_results.getJSONObject(i);
                                    model.adicionaFilme(
                                            new Filme(
                                                    filme.getString("title"),
                                                    image + filme.getString("poster_path"),
                                                    filme.getString("overview"),
                                                    filme.getString("id"),
                                                    filme.getString("vote_average"),
                                                    filme.getString("release_date")
                                            ));
                                }
                            }



                            if(page_atual ==1){
                                model.visibilidadeProgressBarLista(View.INVISIBLE);
                            }
                            if (page_atual < page_max) {
                                executeSearch((page_atual + 1), busca);
                            }

                        } catch (JSONException e) {
                            model.exibeErroRequisicao(e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                model.exibeErroRequisicao(msg_erro_internet);
            }
        });
        queue.add(stringRequest);

    }

    public void cancelRequest(){
        queue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }


}