package br.com.actia.model;

import br.com.actia.model.DVD_S_MODEL.DVDEqualizer;
import br.com.actia.model.DVD_S_MODEL.DVDPlayMode;
import br.com.actia.model.DVD_S_MODEL.DVDRepeatMode;
import br.com.actia.model.DVD_S_MODEL.DVDSource;
import br.com.actia.model.DVD_S_MODEL.FirmwareVersion;

/**
 * Created by Armani on 19/11/2015.
 */
public class EquipmentStatusFrame {
    public final static boolean ZONE_DRIVER    = false;
    public final static boolean ZONE_PASSENGER = true;

    boolean         zone;
    DVDSource       dvdSourceDriver;
    DVDSource       dvdSourcePassenger;
    DVDPlayMode     dvdPlayMode;
    DVDEqualizer    dvdEqualizerDriver;

    boolean         RadioStationScan;
    boolean         RadioAutoMemoryScan;
    //Scan e Memorize
    DVDEqualizer    dvdEqualizerPassenger;

    boolean         randomMode;
    DVDRepeatMode   dvdRepeatMode;
    FirmwareVersion firmwareVersion;

    public EquipmentStatusFrame(byte[] data) {
        setData(data);
    }

    public void setData(byte[] data) {
        zone = (data[0] & 0x80) == 0x80;
        dvdSourceDriver = new DVDSource(true, (byte) ((data[0] & 0x70) >> 4));
        dvdSourcePassenger = new DVDSource(false, (byte) (data[0] & 0x07));
        dvdPlayMode = new DVDPlayMode((byte) ((data[1] & 0xE0) >> 5));
        dvdEqualizerDriver = new DVDEqualizer((byte) ((data[1] & 0x1C) >> 2));

        RadioStationScan    = (data[1] & 0x02) == 0x02;
        RadioAutoMemoryScan = (data[1] & 0x01) == 0x01;

        dvdEqualizerPassenger = new DVDEqualizer((byte) ((data[2] & 0x70) >> 4));

        randomMode = (data[3] & 0x80) == 0x80;
        dvdRepeatMode = new DVDRepeatMode((byte)((data[3] & 0x60) >> 5));

        byte fwValue[] = {data[4], data[5], data[6], data[7]};
        firmwareVersion = new FirmwareVersion(fwValue);
    }

    public Boolean getZone() {
        return zone;
    }

    public void setZone(Boolean zone) {
        this.zone = zone;
    }

    public DVDSource getDvdSourceDriver() {
        return dvdSourceDriver;
    }

    public void setDvdSourceDriver(DVDSource dvdSourceDriver) {
        this.dvdSourceDriver = dvdSourceDriver;
    }

    public DVDSource getDvdSourcePassenger() {
        return dvdSourcePassenger;
    }

    public void setDvdSourcePassenger(DVDSource dvdSourcePassenger) {
        this.dvdSourcePassenger = dvdSourcePassenger;
    }

    public DVDPlayMode getDvdPlayMode() {
        return dvdPlayMode;
    }

    public void setDvdPlayMode(DVDPlayMode dvdPlayMode) {
        this.dvdPlayMode = dvdPlayMode;
    }

    public DVDEqualizer getDvdEqualizerDriver() {
        return dvdEqualizerDriver;
    }

    public void setDvdEqualizerDriver(DVDEqualizer dvdEqualizerDriver) {
        this.dvdEqualizerDriver = dvdEqualizerDriver;
    }

    public DVDEqualizer getDvdEqualizerPassenger() {
        return dvdEqualizerPassenger;
    }

    public void setDvdEqualizerPassenger(DVDEqualizer dvdEqualizerPassenger) {
        this.dvdEqualizerPassenger = dvdEqualizerPassenger;
    }
}
