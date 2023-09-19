package com.example.moviesombdprogramandroid.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.moviesombdprogramandroid.Movie;

import java.util.List;

@Dao
public interface MovieLocalDAO {

    @Insert
    void insertAllMovies(List<MovieEntity> movieEntities);

    @Query("SELECT * FROM t_movies") // Select all items from table defined in Movie class.
    List<MovieEntity> getAllMovies();

    @Query("SELECT * FROM t_movies WHERE year LIKE '%199%'") // Select all items from table defined in Movie class.
    List<MovieEntity> getNinetiesMovies();

}
