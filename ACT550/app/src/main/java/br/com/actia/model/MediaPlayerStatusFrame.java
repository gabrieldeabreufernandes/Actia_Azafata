package br.com.actia.model;

import br.com.actia.model.DVD_TS_MODEL.DVDDeviceStatus;
import br.com.actia.model.DVD_TS_MODEL.DVDFileType;
import br.com.actia.model.DVD_TS_MODEL.DVDMediaSpeed;
import br.com.actia.model.DVD_TS_MODEL.DVDStatus;
import br.com.actia.model.DVD_TS_MODEL.DVDTime;
import br.com.actia.model.DVD_TS_MODEL.DVDTimeInformation;
import br.com.actia.model.DVD_TS_MODEL.DZPower;

/**
 * Created by Armani on 19/11/2015.
 */
public class MediaPlayerStatusFrame {
    private DZPower dzPower;
    private DVDStatus dvdStatus;
    private DVDFileType dvdFileType;
    private DVDStatus dvdFileNumber;
    private DVDStatus dvdFolderNumber;
    private DVDDeviceStatus dvdDeviceStatus;
    //mute a Video System
    private boolean audioMute;
    private DVDMediaSpeed mediaSpeed;
    private DVDTimeInformation dvdTimeInformation;
    private DVDTime dvdTime;


    public MediaPlayerStatusFrame(byte[] data){
        setData(data);
    }

    public void setData(byte[] data) {
        dzPower = new DZPower((byte) (data[0] & 0x01));
        dvdStatus = new DVDStatus((byte) ((data[0] & 0x0E) >> 1));
        dvdFileType = new DVDFileType((byte) ((data[0] & 0xF0) >> 4));
        dvdFileNumber = new DVDStatus(data[1]);
        dvdFolderNumber = new DVDStatus(data[2]);
        dvdDeviceStatus = new DVDDeviceStatus((byte) ((data[3] & 0xE0) >> 5));
        audioMute = (data[3] & 0x10) == 0x10;

        int mediaVal = (data[3] & 0x0F) + (byte)((data[4] & 0xC0) >> 2);
        mediaSpeed = new DVDMediaSpeed(mediaVal);
        dvdTimeInformation = new DVDTimeInformation((byte) ((data[4] & 0x18) >> 3));
        dvdTime = new DVDTime(data[5], data[6], data[7]);
    }

    public DZPower getDzPower() {
        return dzPower;
    }

    public void setDzPower(DZPower dzPower) {
        this.dzPower = dzPower;
    }

    public DVDStatus getDvdStatus() {
        return dvdStatus;
    }

    public void setDvdStatus(DVDStatus dvdStatus) {
        this.dvdStatus = dvdStatus;
    }

    public DVDFileType getDvdFileType() {
        return dvdFileType;
    }

    public void setDvdFileType(DVDFileType dvdFileType) {
        this.dvdFileType = dvdFileType;
    }

    public DVDStatus getDvdFileNumber() {
        return dvdFileNumber;
    }

    public void setDvdFileNumber(DVDStatus dvdFileNumber) {
        this.dvdFileNumber = dvdFileNumber;
    }

    public DVDStatus getDvdFolderNumber() {
        return dvdFolderNumber;
    }

    public void setDvdFolderNumber(DVDStatus dvdFolderNumber) {
        this.dvdFolderNumber = dvdFolderNumber;
    }

    public DVDDeviceStatus getDvdDeviceStatus() {
        return dvdDeviceStatus;
    }

    public void setDvdDeviceStatus(DVDDeviceStatus dvdDeviceStatus) {
        this.dvdDeviceStatus = dvdDeviceStatus;
    }

    public DVDTimeInformation getDvdTimeInformation() {
        return dvdTimeInformation;
    }

    public void setDvdTimeInformation(DVDTimeInformation dvdTimeInformation) {
        this.dvdTimeInformation = dvdTimeInformation;
    }

    public DVDTime getDvdTime() {
        return dvdTime;
    }

    public void setDvdTime(DVDTime dvdTime) {
        this.dvdTime = dvdTime;
    }
}
