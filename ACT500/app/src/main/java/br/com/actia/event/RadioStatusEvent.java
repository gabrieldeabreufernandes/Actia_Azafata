package br.com.actia.event;

import br.com.actia.model.RADIOStatusFrame;

/**
 * Created by Armani on 26/11/2015.
 */
public class RadioStatusEvent {
    private RADIOStatusFrame radioStatusFrame = null;

    public RadioStatusEvent(byte[] data) {
        radioStatusFrame = new RADIOStatusFrame(data);
    }

    public RADIOStatusFrame getRadioStatusFrame() {
        return radioStatusFrame;
    }

    public void setRadioStatusFrame(RADIOStatusFrame radioStatusFrame) {
        this.radioStatusFrame = radioStatusFrame;
    }

    @Override
    public String toString() {
        return "RadioStatusEvent["+ radioStatusFrame +"]";
    }
}
