package com.example.moviesombdprogramandroid.local;

import android.content.Context;

import androidx.room.Room;

public class DatabaseHandler {

    private MovieLocalDAO movieLocalDao;

    public DatabaseHandler(Context context) {
        MovieDatabase db = Room.databaseBuilder(context, MovieDatabase.class, "movies-database").allowMainThreadQueries().build();
        movieLocalDao = db.getMovieLocalDao();
    }

    public MovieLocalDAO getMovieLocalDao(){
        return movieLocalDao;
    }
}
