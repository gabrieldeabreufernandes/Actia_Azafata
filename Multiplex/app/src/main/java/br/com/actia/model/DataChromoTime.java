package br.com.actia.model;

/**
 * Created by Armani andersonaramni@gmail.com on 26/10/16.
 */
public class DataChromoTime {
    public final static int MIN_TIME = 0x04;
    public final static int MAX_TIME = 0x32;
    private int time;

    public DataChromoTime(int value) {
        setTime(value);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = (time >= MIN_TIME && time <= MAX_TIME) ? time : MIN_TIME;
    }
}
