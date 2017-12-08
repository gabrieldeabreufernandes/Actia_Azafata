package br.com.actia.model.DVD_TS_MODEL;

/**
 * Created by Armani andersonaramni@gmail.com on 04/01/17.
 */

public class DVDMediaSpeed {
    final static int SKIP_FORWARDS  = 0x20;
    final static int SKIP_BACKWARDS = 0x10;
    final static int SLOW_REWIND    = 0x08;
    final static int SLOW_FORWARD   = 0x04;
    final static int FAST_REWIND    = 0x02;
    final static int FAST_FORWARD   = 0x01;
    private int value;

    public DVDMediaSpeed(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
