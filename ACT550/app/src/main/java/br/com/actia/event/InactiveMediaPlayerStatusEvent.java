package br.com.actia.event;

import br.com.actia.model.MediaPlayerStatusFrame;

/**
 * Created by Armani on 27/11/2015.
 */
public class InactiveMediaPlayerStatusEvent {
    private MediaPlayerStatusFrame mediaPlayerStatusFrame = null;

    public InactiveMediaPlayerStatusEvent(byte[] data) {
        mediaPlayerStatusFrame = new MediaPlayerStatusFrame(data);
    }

    public MediaPlayerStatusFrame getMediaPlayerStatusFrame() {
        return mediaPlayerStatusFrame;
    }

    public void setMediaPlayerStatusFrame(MediaPlayerStatusFrame mediaPlayerStatusFrame) {
        this.mediaPlayerStatusFrame = mediaPlayerStatusFrame;
    }

    @Override
    public String toString() {
        return "MediaPlayerStatusEvent["+ mediaPlayerStatusFrame +"]";
    }
}
