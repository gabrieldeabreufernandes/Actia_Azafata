package br.com.actia.model;

/**
 * Created by Armani on 27/11/2015.
 */
public class RadioCTFrame {
    private int hour;
    private int minute;
    private int second;
    private int day;
    private int month;

    public RadioCTFrame(byte[] data) {
        byte bt[] = new byte[2];

        hour = ((data[0] & 0xFF) << 8) + (data[1] & 0xFF);
        minute = ((data[2] & 0xFF) << 8) + (data[3] & 0xFF);
        second = ((data[4] & 0xFF) << 8) + (data[5] & 0xFF);

        day = data[6];
        month = data[7];
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public String toString() {
        String strRet = String.valueOf(hour) +":"+ String.valueOf(minute) +":"+ String.valueOf(second)
                +" "+ String.valueOf(day) +" "+ String.valueOf(month);
        return strRet;
    }
}
