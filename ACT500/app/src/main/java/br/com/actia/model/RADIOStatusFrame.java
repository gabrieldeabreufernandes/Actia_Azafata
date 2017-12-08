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
    RADIOFrequency radioFrequency;
    //TA e AF


    public RADIOStatusFrame(byte[] data) {
        radioStation = new RADIOStation((byte) ((data[0] & 0xE0) >> 5));
        radioBand = new RADIOBand((byte) ((data[0] & 0x1C) >> 2));
        radioFrequency = new RADIOFrequency(data[2], data[3]);
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
