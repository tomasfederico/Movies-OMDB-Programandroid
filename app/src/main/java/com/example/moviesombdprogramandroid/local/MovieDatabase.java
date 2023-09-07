package com.example.moviesombdprogramandroid.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {MovieEntity.class})
public abstract class MovieDatabase extends RoomDatabase {

}
