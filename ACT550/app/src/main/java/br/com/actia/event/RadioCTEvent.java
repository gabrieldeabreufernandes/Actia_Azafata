package br.com.actia.event;

import br.com.actia.model.RadioCTFrame;

/**
 * Created by Armani on 27/11/2015.
 */
public class RadioCTEvent {
    private RadioCTFrame radioCTFrame = null;

    public RadioCTEvent(byte[] data) {
        radioCTFrame = new RadioCTFrame(data);
    }

    public RadioCTFrame getRadioCTFrame() {
        return radioCTFrame;
    }

    public void setRadioCTFrame(RadioCTFrame radioCTFrame) {
        this.radioCTFrame = radioCTFrame;
    }

    @Override
    public String toString() {
        return "RadioCTEvent["+ radioCTFrame +"]";
    }
}
