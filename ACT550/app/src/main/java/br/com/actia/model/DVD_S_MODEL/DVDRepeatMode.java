package br.com.actia.model.DVD_S_MODEL;

/**
 * Created by Armani andersonaramni@gmail.com on 27/12/16.
 */

public class DVDRepeatMode {
    static public final byte REPEAT_MODE_OFF    = 0x00;
    static public final byte REPEAT_MODE_SINGLE = 0x01;
    static public final byte REPEAT_MODE_ALL    = 0x02;
    static public final byte REPEAT_MODE_FOLDER = 0x03;


    private byte value;

    public DVDRepeatMode(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}
