package com.example.moviesombdprogramandroid.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.moviesombdprogramandroid.Movie;

@Database(version = 1, entities = {MovieEntity.class}, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    // para que el handler pueda instanciar DAO
    abstract MovieLocalDAO getMovieLocalDao();

}
