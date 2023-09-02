package com.example.moviesombdprogramandroid;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* CREAR UNA NUEVA CLASE TIPO DAO, MOVIES DATA ACCESS OBJECT.*/
public class MoviesDAO {

    private MoviesEndpoints moviesEndpoints;
    private Retrofit retrofit;

    /* CONSTRUCTOR DE CLASE CON METODOLOGIA "BUILDER" donde instancio al retrofit y con este objeto me apalanco para crear endpoints. */
    public MoviesDAO(){
        /* Se agrega la url de la api
        Se agrega metodo de conversion gson para que automatice la traduccion entre app y api */
        String baseUrl = "https://www.omdbapi.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        moviesEndpoints = retrofit.create(MoviesEndpoints.class);
    }

    /* Getter para Endpoints generados en la interface
    * puede haber mas de un endpoint en la interfaz, por buena practica de diseno debieran de tener relacion entre si */
    public MoviesEndpoints getMoviesEndpoints(){
        return moviesEndpoints;
    }
}
