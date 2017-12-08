package br.com.actia.model;

/**
 * Created by Armani andersonaramni@gmail.com on 26/10/16.
 */

public class DataFourStates {
    public final static int STATE_DISABLE = 0;
    public final static int STATE_OFF = 1;
    public final static int STATE_50 = 2;
    public final static int STATE_100 = 3;

    private int state = 0;

    public DataFourStates(int value) {
        if(value >= STATE_DISABLE && value <= STATE_100)
            this.state = value;
        else
            this.state = STATE_DISABLE;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
