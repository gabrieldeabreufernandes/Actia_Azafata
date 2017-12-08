package br.com.actia.model;

/**
 * Created by Armani on 27/11/2015.
 */
public class StatusFrame {
    private final byte BAR_STEP = 2;
    private final byte BAR_MIN_VALUE = 0;
    private final byte BAR_MAX_VALUE = 40;
    private final byte VOL_MAX_VALUE = 40;
    private final byte GAIN_STEP = 1;
    private final byte GAIN_MIN_VALUE = 0;
    private final byte GAIN_MAX_VALUE = 30;
    private final byte FADER_STEP = 1;
    private final byte FADER_MIN_VALUE = 0;
    private final byte FADER_MAX_VALUE = 30;

    private byte treble;
    private byte bass;
    private byte balance;
    private byte volume;
    private byte inputGain;
    private byte fader;

    public StatusFrame() {
        treble = 0;
        bass = 0;
        balance = 0;
        volume = 0;
        inputGain = 0;
        fader = 0;
    }

    public StatusFrame(byte[] data) {
        treble      = data[0];
        bass        = data[1];
        balance     = data[2];
        volume      = (byte)(data[3] >> 1);
        inputGain   = data[4];
        fader       = data[5];
    }

    public byte getTreble() {
        return treble;
    }

    public void setTreble(byte treble) {
        if(treble < BAR_MIN_VALUE || treble > BAR_MAX_VALUE)
            return;
        this.treble = treble;
    }

    public void trebleUp(){
        if((treble + BAR_STEP) > BAR_MAX_VALUE)
            return;

        treble += BAR_STEP;
    }

    public void trebleDown(){
        if((treble - BAR_STEP) < BAR_MIN_VALUE)
            return;

        treble -= BAR_STEP;
    }

    public byte getBass() {
        return bass;
    }

    public void setBass(byte bass) {
        if(bass < BAR_MIN_VALUE || bass > BAR_MAX_VALUE)
            return;
        this.bass = bass;
    }

    public void bassUp(){
        if((bass + BAR_STEP) > BAR_MAX_VALUE)
            return;

        bass += BAR_STEP;
    }

    public void bassDown(){
        if((bass - BAR_STEP) < BAR_MIN_VALUE)
            return;

        bass -= BAR_STEP;
    }

    public byte getBalance() {
        return balance;
    }

    public void setBalance(byte balance) {
        if(balance < BAR_MIN_VALUE || balance > BAR_MAX_VALUE)
            return;
        this.balance = balance;
    }

    public void balanceUp(){
        if((balance + BAR_STEP) > BAR_MAX_VALUE)
            return;

        balance += BAR_STEP;
    }

    public void balanceDown(){
        if((balance - BAR_STEP) < BAR_MIN_VALUE)
            return;

        balance -= BAR_STEP;
    }

    public byte getVolume() {
        return volume;
    }

    public void setVolume(byte volume) {
        if(volume < BAR_MIN_VALUE || volume > VOL_MAX_VALUE)
            return;
        this.volume = volume;
    }

    public void volumeUp(){
        if((volume + BAR_STEP) > VOL_MAX_VALUE)
            return;

        volume += BAR_STEP;
    }

    public void volumeDown(){
        if((volume - BAR_STEP) < BAR_MIN_VALUE)
            return;

        volume -= BAR_STEP;
    }

    public byte getInputGain() {
        return inputGain;
    }

    public void setInputGain(byte inputGain) {
        if(inputGain < GAIN_MIN_VALUE || inputGain > GAIN_MAX_VALUE)
            return;
        this.inputGain = inputGain;
    }

    public void inputGainUp(){
        if((inputGain + GAIN_STEP) > GAIN_MAX_VALUE)
            return;

        inputGain += GAIN_STEP;
    }

    public void inputGainDown(){
        if((inputGain - GAIN_STEP) < GAIN_MIN_VALUE)
            return;

        inputGain -= GAIN_STEP;
    }

    public byte getFader() {
        return fader;
    }

    public void setFader(byte fader) {
        this.fader = fader;
    }

    public void faderUp(){
        if((fader + FADER_STEP) > FADER_MAX_VALUE)
            return;

        fader += FADER_STEP;
    }

    public void faderDown(){
        if((fader - FADER_STEP) < FADER_MIN_VALUE)
            return;

        fader -= FADER_STEP;
    }

    public byte[] getBytes() {
        byte objectArray[] = new byte[8];
        objectArray[0] = this.treble;
        objectArray[1] = this.bass;
        objectArray[2] = this.balance;
        objectArray[3] = (byte) (this.volume << 1);
        objectArray[4] = this.inputGain;
        objectArray[5] = this.fader;
        objectArray[6] = 0x00;
        objectArray[7] = 0x00;
        return objectArray;
    }
}
