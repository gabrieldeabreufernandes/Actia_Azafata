package br.com.actia.event;

import br.com.actia.model.MediaPlayerStatusFrame;

/**
 * Created by Armani on 27/11/2015.
 */
public class MediaPlayerStatusEvent {
    private MediaPlayerStatusFrame mediaPlayerStatusFrame = null;

    public MediaPlayerStatusEvent(byte[] data) {
        mediaPlayerStatusFrame = new  MediaPlayerStatusFrame(data);
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
