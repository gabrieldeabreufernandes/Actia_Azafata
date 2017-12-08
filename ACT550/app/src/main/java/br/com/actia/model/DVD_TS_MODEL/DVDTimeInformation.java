package br.com.actia.model.DVD_TS_MODEL;

/**
 * Created by Armani on 19/11/2015.
 */
public class DVDTimeInformation {
    static public final byte DVD_TIME_INFORMATION_TOTAL = 0x00;
    static public final byte DVD_TIME_INFORMATION_PLAYING = 0x01;
    static public final byte DVD_TIME_INFORMATION_TOFINISH = 0x02;

    private final byte value;

    public DVDTimeInformation(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
