package br.com.actia.event;

import br.com.actia.model.DVDStatusFrame;

/**
 * Created by Armani on 27/11/2015.
 */
public class DVDStatusEvent {
    private DVDStatusFrame dvdStatusFrame = null;

    public DVDStatusEvent(byte[] data){
        dvdStatusFrame = new DVDStatusFrame(data);
    }

    public DVDStatusFrame getDvdStatusFrame() {
        return dvdStatusFrame;
    }

    public void setDvdStatusFrame(DVDStatusFrame dvdStatusFrame) {
        this.dvdStatusFrame = dvdStatusFrame;
    }

    @Override
    public String toString() {
        return "DVDStatusEvent["+ dvdStatusFrame +"]";
    }
}
