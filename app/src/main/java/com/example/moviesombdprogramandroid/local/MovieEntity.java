package com.example.moviesombdprogramandroid.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "t_movies")
public class MovieEntity implements Serializable {

    public MovieEntity(String id, String title, String image, String year) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.year = year;
    }

    @PrimaryKey
    @ColumnInfo(name = "imdbID")
    private String id;

    @ColumnInfo(name = "title")
    private String title;

    @Ignore
    private String image;

    @ColumnInfo(name = "year")
    private String year;

}
