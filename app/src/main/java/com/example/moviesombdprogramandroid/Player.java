package com.example.moviesombdprogramandroid;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {


    private final int id;
    private String name;
    private String image;

    private String description;

    public Player(int id, String name, String image, String description) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() { return description;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name) && image.equals(player.image) && description.equals(player.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, image, description);
    }
}
