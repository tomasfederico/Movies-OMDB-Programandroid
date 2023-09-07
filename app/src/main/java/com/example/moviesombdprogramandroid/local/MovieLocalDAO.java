package com.example.moviesombdprogramandroid.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.moviesombdprogramandroid.Movie;

import java.util.List;

@Dao
public interface MovieLocalDAO {

    @Insert
    void insertAllMovies(List<Movie> movies);

    @Query("SELECT * FROM t_movies") // Select all items from table defined in MovieEntity class.
    List<MovieEntity> getAll();

}
