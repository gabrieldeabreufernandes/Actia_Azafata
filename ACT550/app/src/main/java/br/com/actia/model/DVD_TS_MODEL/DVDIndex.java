package br.com.actia.model.DVD_TS_MODEL;

/**
 * Created by Armani on 19/11/2015.
 */
public class DVDIndex {
    int value;

    DVDIndex(int value){
        if(value < 0 || value > 255)
            value = 0;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
