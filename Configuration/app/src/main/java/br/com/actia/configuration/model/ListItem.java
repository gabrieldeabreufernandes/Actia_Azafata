package br.com.actia.configuration.model;

import java.io.Serializable;

/**
 * Created by Armani andersonaramni@gmail.com on 15/11/16.
 */

public class ListItem implements Serializable {
    public static final int TYPE_SEPARATOR = 0;
    public static final int TYPE_TEXT      = 1;
    public static final int TYPE_COLOR     = 2;

    private int type;
    private String name;
    private String value;

    public ListItem(int type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
