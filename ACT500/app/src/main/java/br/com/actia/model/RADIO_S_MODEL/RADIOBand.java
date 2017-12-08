package br.com.actia.model.RADIO_S_MODEL;

/**
 * Created by Actia on 19/11/2015.
 */
public class RADIOBand {
    static public final byte RADIO_BAND_FM1 = 0x00;
    static public final byte RADIO_BAND_FM2 = 0x01;
    static public final byte RADIO_BAND_FM3 = 0x02;
    static public final byte RADIO_BAND_AM1 = 0x03;
    static public final byte RADIO_BAND_AM2 = 0x04;

    private byte band;

    public RADIOBand(byte band) {
        this.band = band;
    }

    public byte getBand() {
        return band;
    }

    public void setBand(byte band) {
        this.band = band;
    }

    public boolean isBandAM() {
        if(band >= RADIO_BAND_AM1 && band <= RADIO_BAND_AM2)
            return true;
        else
            return false;
    }
}
