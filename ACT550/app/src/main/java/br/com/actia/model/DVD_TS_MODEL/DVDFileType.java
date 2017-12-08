package br.com.actia.model.DVD_TS_MODEL;

/**
 * Created by Armani on 19/11/2015.
 */
public class DVDFileType {
    static public final byte DVD_FILE_TYPE_CD = 0x01;
    static public final byte DVD_FILE_TYPE_DVD = 0x02;
    static public final byte DVD_FILE_TYPE_MP3 = 0x04;
    static public final byte DVD_FILE_TYPE_DIVIX = 0x08;

    public DVDFileType(byte value)
    {
        this.value = value;
    }

    public byte getValue()
    {
        return this.value;
    }
    private final byte value;
};
