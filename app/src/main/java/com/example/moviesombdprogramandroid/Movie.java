package com.example.moviesombdprogramandroid;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Movie implements Serializable {

    @SerializedName("Title")
    private String title;

    @SerializedName("Poster")
    private String image;

    @SerializedName("Year")
    private String year;

    @SerializedName("imdbID")
    private String id;

    public Movie(String title, String image, String year, String id) {
        this.title = title;
        this.image = image;
        this.year = year;
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getId() {return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id.equals(movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
