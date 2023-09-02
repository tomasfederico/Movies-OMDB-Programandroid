package com.example.moviesombdprogramandroid;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesEndpoints {

    /* En esta interfaz se definen los endpoints para interactuar con la API */

    /* @funcion y continuacion de la url...en caso que sea "." es sin agregado de nada.
    * lo que esta dentro de @query son los parametros que le solicito a la api.
    * */
    @GET(".")
    Call<MoviesResponse> getMoviesByName(@Query("apikey") String apiKey,
                                         @Query("s") String movieName);

    // OJO CON ESTO, DEPENDE DE COMO ESTE ARMADO EL ARCHIVO JSON EN LA API
    // VA A DEPENDER COMO PODRIA PEDIR 1 SOLO ITEM. EN ESTE CASO, DEBERIA PEDIR DIRECTAMENTE UNA MOVIE EN LUGAR DE UNA MOVIE RESPONSE.
    @GET(".")
    Call<Movie> getSingleMovieByTitleAndYear(@Query("apikey") String apiKey,
                                             @Query("t") String movieName,
                                             @Query("y") String year);

    @GET("purchases/{id}/status")
    Call<Movie> example(@Path("id") String productID);
}
