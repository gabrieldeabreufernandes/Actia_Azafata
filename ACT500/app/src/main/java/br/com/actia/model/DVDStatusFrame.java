package br.com.actia.model;

import br.com.actia.model.DVD_S_MODEL.DVDEqualizer;
import br.com.actia.model.DVD_S_MODEL.DVDPlayMode;
import br.com.actia.model.DVD_S_MODEL.DVDSource;

/**
 * Created by Armani on 19/11/2015.
 */
public class DVDStatusFrame {
    DVDSource dvdSourceDriver;
    DVDSource dvdSourcePassenger;
    DVDPlayMode dvdPlayMode;
    DVDEqualizer dvdEqualizerDriver;
    //Scan e Memorize
    DVDEqualizer dvdEqualizerPassenger;


    public DVDStatusFrame(byte[] data) {
        setData(data);
    }

    public void setData(byte[] data) {
        dvdSourceDriver = new DVDSource(true, (byte) ((data[0] & 0x70) >> 4));
        dvdSourcePassenger = new DVDSource(false, (byte) (data[0] & 0x07));
        dvdPlayMode = new DVDPlayMode((byte) ((data[1] & 0xE0) >> 5));
        dvdEqualizerDriver = new DVDEqualizer((byte) ((data[1] & 0x1C) >> 2));
        dvdEqualizerPassenger = new DVDEqualizer((byte) ((data[2] & 0x70) >> 4));
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
