package br.com.actia.event;

import br.com.actia.model.DVDTrackStatusFrame;

/**
 * Created by Armani on 27/11/2015.
 */
public class DVDTrackStatusEvent {
    private DVDTrackStatusFrame dvdTrackStatusFrame = null;

    public DVDTrackStatusEvent(byte[] data) {
        dvdTrackStatusFrame = new DVDTrackStatusFrame(data);
    }

    public DVDTrackStatusFrame getDvdTrackStatusFrame() {
        return dvdTrackStatusFrame;
    }

    public void setDvdTrackStatusFrame(DVDTrackStatusFrame dvdTrackStatusFrame) {
        this.dvdTrackStatusFrame = dvdTrackStatusFrame;
    }

    @Override
    public String toString() {
        return "DVDTrackStatusEvent["+ dvdTrackStatusFrame +"]";
    }
}
