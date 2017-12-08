package br.com.actia.model;

/**
 * Created by Armani on 27/11/2015.
 */
public class RadioPSFrame {
    private String rds;

    public RadioPSFrame(byte[] data) {
        rds = new String(data);
    }

    public String getRds() {
        return rds;
    }

    public void setRds(String rds) {
        this.rds = rds;
    }
}
