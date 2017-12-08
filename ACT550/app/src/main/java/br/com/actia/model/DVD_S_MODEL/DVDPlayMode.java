package br.com.actia.model.DVD_S_MODEL;

/**
 * Created by Actia on 19/11/2015.
 */
public class DVDPlayMode {
    static public final byte DVD_MODE_NO_MODE  = 0x00;
    static public final byte DVD_MODE_RPT_ALL  = 0x01;
    static public final byte DVD_MODE_RPT_ONE  = 0x02;
    static public final byte DVD_MODE_RND_ALL  = 0x03;
    static public final byte DVD_MODE_RND_FLD  = 0x04;

    private byte value;

    public DVDPlayMode(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}
