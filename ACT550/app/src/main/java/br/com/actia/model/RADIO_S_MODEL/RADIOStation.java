package br.com.actia.model.RADIO_S_MODEL;

/**
 * Created by Armani on 19/11/2015.
 */
public class RADIOStation {
    static public final byte RADIO_STATION_CHANNEL1  = 0x00;
    static public final byte RADIO_STATION_CHANNEL2  = 0x01;
    static public final byte RADIO_STATION_CHANNEL3  = 0x02;
    static public final byte RADIO_STATION_CHANNEL4  = 0x03;
    static public final byte RADIO_STATION_CHANNEL5  = 0x04;
    static public final byte RADIO_STATION_CHANNEL6  = 0x05;
    static public final byte RADIO_STATION_NO_CHANGE = 0x07;

    private byte channel;

    public RADIOStation(byte channel) {
        this.channel = channel;
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }
}
