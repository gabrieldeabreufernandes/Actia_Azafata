package br.com.actia.event;

import br.com.actia.model.MultiplexStatusFrame;

/**
 * Created by Armani andersonaramni@gmail.com on 26/10/16.
 */

public class MultiplexStatusEvent {
    private MultiplexStatusFrame multiplexStatusFrame = null;

    public MultiplexStatusEvent(byte[] data) {
        multiplexStatusFrame = new MultiplexStatusFrame(data);
    }

    public MultiplexStatusFrame getMultiplexStatusFrame() {
        return multiplexStatusFrame;
    }

    public void setMultiplexStatusFrame(MultiplexStatusFrame multiplexStatusFrame) {
        this.multiplexStatusFrame = multiplexStatusFrame;
    }

    public String toString() {
        return "MultiplexStatusEvent [" + multiplexStatusFrame + "]";
    }
}
