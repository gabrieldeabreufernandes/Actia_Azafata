package br.com.actia.model.DVD_TS_MODEL;

/**
 * Created by Actia on 19/11/2015.
 */
public class DVDTime {
    byte minutes;
    byte seconds;
    byte hours;

    public DVDTime(byte minutes, byte seconds, byte hours) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.hours = hours;
    }

    public byte getMinutes() {
        return minutes;
    }

    public void setMinutes(byte minutes) {
        this.minutes = (byte) (minutes >> 1);
    }

    public byte getSeconds() {
        return seconds;
    }

    public void setSeconds(byte seconds) {
        this.seconds = (byte) (seconds >> 1);
    }

    public byte getHours() {
        return hours;
    }

    public void setHours(byte hours) {
        this.hours = (byte) (hours >> 1);
    }

    @Override
    public String toString() {
        String strTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return strTime;
    }
}
