package br.com.actia.model.DVD_TS_MODEL;

/**
 * Created by Armani on 19/11/2015.
 */
public class DVDStatus {
    /**DVD Status Playing. */
    static public final byte DVD_STATUS_PLAYING = 0x01;
    /**DVD Status Paused. */
    static public final byte DVD_STATUS_PAUSED = 0x02;
    /**DVD Status stopped. */
    static public final byte DVD_STATUS_STOPPED = 0x04;

    private final byte value;

    public DVDStatus(byte value)
    {
        this.value = value;
    }

    public byte getValue()
    {
        return this.value;
    }

    public boolean isPlaying() {
        return this.value == DVD_STATUS_PLAYING;
    }
};
