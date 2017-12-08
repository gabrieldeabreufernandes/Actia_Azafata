package br.com.actia.model.PLAYLIST;

/**
 * Created by Armani andersonaramni@gmail.com on 19/02/17.
 */

public class MediaFile {
    private int     id;
    private String  name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
