package br.com.actia.event;

import br.com.actia.model.RadioPSFrame;

/**
 * Created by Armani on 27/11/2015.
 */
public class RadioPSEvent {
    private RadioPSFrame radioPSFrame = null;

    public RadioPSEvent(byte[] data) {
        radioPSFrame = new RadioPSFrame(data);
    }

    public RadioPSFrame getRadioPSFrame() {
        return radioPSFrame;
    }

    public void setRadioPSFrame(RadioPSFrame radioPSFrame) {
        this.radioPSFrame = radioPSFrame;
    }

    @Override
    public String toString() {
        return "RadioPSEvent["+ radioPSFrame +"]";
    }
}
