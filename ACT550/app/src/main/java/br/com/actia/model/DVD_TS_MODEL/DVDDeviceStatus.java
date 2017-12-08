package br.com.actia.model.DVD_TS_MODEL;

/**
 * Created by Armani on 19/11/2015.
 */
public class DVDDeviceStatus {
    static public final byte DVD_DISK_STATUS_NO_DISK = 0x00;
    static public final byte DVD_DISK_STATUS_INSERTED = 0x01;
    static public final byte DVD_DISK_STATUS_LOADING = 0x02;
    static public final byte DVD_DISK_STATUS_WRONG_DISK = 0x03;
    static public final byte DVD_DISK_STATUS_EJECTING = 0x04;

    public DVDDeviceStatus(byte value){
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    private final byte value;
};
