package br.com.actia.model;

import br.com.actia.model.RADIO_S_MODEL.RADIOBand;
import br.com.actia.model.RADIO_S_MODEL.RADIOFrequency;
import br.com.actia.model.RADIO_S_MODEL.RADIOStation;

/**
 * Created by Armani on 19/11/2015.
 */
public class RADIOStatusFrame {
    RADIOStation radioStation;
    RADIOBand radioBand;
    boolean trafficAnnouncement;    //TA
    boolean alternateFrequency;     //AF
    boolean rds;
    RADIOFrequency radioFrequency;

    public RADIOStatusFrame(byte[] data) {
        radioStation = new RADIOStation((byte) ((data[0] & 0xE0) >> 5));
        radioBand = new RADIOBand((byte) ((data[0] & 0x1C) >> 2));

        trafficAnnouncement = (data[0] & 0x02) == 0x02;
        alternateFrequency = (data[0] & 0x01) == 0x01;
        rds = (data[1] & 0x80) == 0x80;

        radioFrequency = new RADIOFrequency(data[2], data[3]);
    }

    public boolean isTrafficAnnouncement() {
        return trafficAnnouncement;
    }

    public void setTrafficAnnouncement(boolean trafficAnnouncement) {
        this.trafficAnnouncement = trafficAnnouncement;
    }

    public boolean isAlternateFrequency() {
        return alternateFrequency;
    }

    public void setAlternateFrequency(boolean alternateFrequency) {
        this.alternateFrequency = alternateFrequency;
    }

    public boolean isRds() {
        return rds;
    }

    public void setRds(boolean rds) {
        this.rds = rds;
    }

    public RADIOStation getRadioStation() {
        return radioStation;
    }

    public void setRadioStation(RADIOStation radioStation) {
        this.radioStation = radioStation;
    }

    public RADIOBand getRadioBand() {
        return radioBand;
    }

    public void setRadioBand(RADIOBand radioBand) {
        this.radioBand = radioBand;
    }

    public RADIOFrequency getRadioFrequency() {
        return radioFrequency;
    }

    public void setRadioFrequency(RADIOFrequency radioFrequency) {
        this.radioFrequency = radioFrequency;
    }
}
