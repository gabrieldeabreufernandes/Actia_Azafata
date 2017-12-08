package br.com.actia.model;

/**
 * Created by Armani andersonaramni@gmail.com on 26/10/16.
 */

public class DataTwoStates {
    public final static int STATE_OFF = 0x00;
    public final static int STATE_ON = 0x01;

    private int state = 0;

    public DataTwoStates(int value) {
        state = (value == STATE_ON) ? STATE_ON : STATE_OFF;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
