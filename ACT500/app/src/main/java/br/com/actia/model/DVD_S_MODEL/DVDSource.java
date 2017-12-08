package br.com.actia.model.DVD_S_MODEL;

/**
 * Created by Armani on 19/11/2015.
 */
public class DVDSource {
    static public final byte DVD_SOURCE_AUX = 0x00;
    static public final byte DVD_SOURCE_CD = 0x01;
    static public final byte DVD_SOURCE_USB = 0x02;
    static public final byte DVD_SOURCE_RADIO = 0x03;
    static public final byte DVD_SOURCE_MIC = 0x04;
    static public final byte DVD_SOURCE_CDBOX = 0x05;
    static public final byte DVD_SOURCE_NO_CHANGE = 0x07;
    static public final byte DVD_SOURCE_CONFIG = (byte)0xFF;

    private boolean isPassenger;
    private byte value;

    public DVDSource(boolean isPassenger, byte value) {
        this.isPassenger = isPassenger;
        this.value = value;
    }

    public boolean isPassenger() {
        return isPassenger;
    }

    public void setIsPassenger(boolean isPassenger) {
        this.isPassenger = isPassenger;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}
