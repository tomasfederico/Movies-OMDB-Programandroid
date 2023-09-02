package com.example.moviesombdprogramandroid;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {

    public MoviesResponse(){}

    @SerializedName("Search")
    private List<Movie> movies;

    public List<Movie> getMovies(){
        return movies;
    }

    public void setMovies(List<Movie> movies){

    }

    private int totalResults;
    public int getTotalResults() {
        return totalResults;
    }
}
