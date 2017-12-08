package br.com.actia.model.DVD_S_MODEL;

/**
 * Created by Actia on 19/11/2015.
 */
public class DVDEqualizer {
    static public final byte DVD_EQU_FLAT  = 0x00;
    static public final byte DVD_EQU_ROCK  = 0x01;
    static public final byte DVD_EQU_OPERA = 0x02;
    static public final byte DVD_EQU_POP   = 0x03;
    static public final byte DVD_EQU_VOICE = 0x04;

    private byte value;

    public DVDEqualizer(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}
