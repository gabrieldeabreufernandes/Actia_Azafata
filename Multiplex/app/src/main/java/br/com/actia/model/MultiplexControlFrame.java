package br.com.actia.model;

import android.util.Log;

/**
 * Created by Armani andersonaramni@gmail.com on 26/10/16.
 */

public class MultiplexControlFrame implements Cloneable {
    //### BYTE 0 ###
    private DataFourStates  wifiData    = null;
    private DataFourStates  seatNumData = null;
    private DataFourStates  wcData      = null;
    private DataTwoStates   azafataData = null;
    //### BYTE 1 ###
    private DataFourStates  coffeMachineData = null;
    private DataFourStates  refrigeratorData = null;
    private DataFourStates  backDoorData     = null;
    //### BYTE 2 ###
    private DataFourStates  internalLightData   = null;
    private DataFourStates  readingLights       = null;
    private DataFourStates  wisperData          = null;
    //### BYTE 3 ###
    //### BYTE 4 ###
    //### BYTE 5 ###
    private DataChromotherapy   chromotherapyData   = null;

    public MultiplexControlFrame() {
        //### BYTE 0 ###
        wifiData    = new DataFourStates(0);
        seatNumData = new DataFourStates(0);
        wcData      = new DataFourStates(0);
        azafataData = new DataTwoStates(0);
        //Remain 1 bit

        //### BYTE 1 ###
        coffeMachineData    = new DataFourStates(0);
        refrigeratorData    = new DataFourStates(0);
        backDoorData        = new DataFourStates(0);

        //### BYTE 2 ###
        internalLightData   = new DataFourStates(0);
        readingLights       = new DataFourStates(0);
        wisperData          = new DataFourStates(0);

        chromotherapyData = new DataChromotherapy();
    }

    /**
     * Converts a byte array to object
     * @param data
     */
    public MultiplexControlFrame(byte[] data) {
        //### BYTE 0 ###
        wifiData    = new DataFourStates((data[0] & 0xC0) >> 6);
        seatNumData = new DataFourStates((data[0] & 0x30) >> 4);
        wcData      = new DataFourStates((data[0] & 0x0C) >> 2);
        azafataData = new DataTwoStates((data[0] & 0x02) >> 1);
        //Remain 1 bit

        //### BYTE 1 ###
        coffeMachineData    = new DataFourStates((data[1] & 0xC0) >> 6);
        refrigeratorData    = new DataFourStates((data[1] & 0x30) >> 4);
        backDoorData        = new DataFourStates((data[1] & 0x0C) >> 2);

        //### BYTE 2 ###
        internalLightData   = new DataFourStates((data[2] & 0xC0) >> 6);
        readingLights       = new DataFourStates((data[2] & 0x30) >> 4);
        wisperData          = new DataFourStates((data[2] & 0x0C) >> 2);

        byte chromoData[] = new byte[3];
        chromoData[0] = data[3];
        chromoData[1] = data[4];
        chromoData[2] = data[5];

        chromotherapyData   = new DataChromotherapy(chromoData);
    }

    /**
     * Converts this object to byte array
     * @return
     */
    public byte[] getDataArray() {
        Log.v("ControlFrame", "getDataArray");
        byte multiplexArray[] = new byte[8];

        multiplexArray[0] = (byte) ((wifiData.getState() & 0x03) << 6);
        multiplexArray[0] += (byte) ((seatNumData.getState() & 0x03) << 4);
        multiplexArray[0] += (byte) ((wcData.getState() & 0x03) << 2);
        multiplexArray[0] += (byte) ((azafataData.getState() & 0x01) << 1);

        multiplexArray[1] = (byte) ((coffeMachineData.getState() & 0x03) << 6);
        multiplexArray[1] += (byte) ((refrigeratorData.getState() & 0x03) << 4);
        multiplexArray[1] += (byte) ((backDoorData.getState() & 0x03) << 2);

        multiplexArray[2] = (byte) ((internalLightData.getState() & 0x03) << 6);
        multiplexArray[2] += (byte) ((readingLights.getState() & 0x03) << 4);
        multiplexArray[2] += (byte) ((wisperData.getState() & 0x03) << 2);


        multiplexArray[3] += (byte) ((chromotherapyData.getStatus().getState() & 0x03) << 6);
        multiplexArray[3] += (byte) ((chromotherapyData.getTime().getTime() & 0x3F));

        multiplexArray[4] += (byte) (chromotherapyData.getColors().getValue() & 0xFF);

        multiplexArray[5] += (byte) ((chromotherapyData.getColors().getValue() >> 1) & 0x80);
        multiplexArray[5] += (byte) (chromotherapyData.getBrightness().getValue() & 0x7F);

        multiplexArray[6] = (byte) 0;
        multiplexArray[7] = (byte) 0;

        return multiplexArray;
    }

    public DataFourStates getWifiData() {
        return wifiData;
    }

    public void setWifiData(DataFourStates wifiData) {
        this.wifiData = wifiData;
    }

    public DataFourStates getSeatNumData() {
        return seatNumData;
    }

    public void setSeatNumData(DataFourStates seatNumData) {
        this.seatNumData = seatNumData;
    }

    public DataFourStates getWcData() {
        return wcData;
    }

    public void setWcData(DataFourStates wcData) {
        this.wcData = wcData;
    }

    public DataTwoStates getAzafataData() {
        return azafataData;
    }

    public void setAzafataData(DataTwoStates azafataData) {
        this.azafataData = azafataData;
    }

    public DataFourStates getCoffeMachineData() {
        return coffeMachineData;
    }

    public void setCoffeMachineData(DataFourStates coffeMachineData) {
        this.coffeMachineData = coffeMachineData;
    }

    public DataFourStates getRefrigeratorData() {
        return refrigeratorData;
    }

    public void setRefrigeratorData(DataFourStates refrigeratorData) {
        this.refrigeratorData = refrigeratorData;
    }

    public DataFourStates getBackDoorData() {
        return backDoorData;
    }

    public void setBackDoorData(DataFourStates backDoorData) {
        this.backDoorData = backDoorData;
    }

    public DataFourStates getInternalLightData() {
        return internalLightData;
    }

    public void setInternalLightData(DataFourStates internalLightData) {
        this.internalLightData = internalLightData;
    }

    public DataFourStates getReadingLights() {
        return readingLights;
    }

    public void setReadingLights(DataFourStates readingLights) {
        this.readingLights = readingLights;
    }

    public DataFourStates getWisperData() {
        return wisperData;
    }

    public void setWisperData(DataFourStates wisperData) {
        this.wisperData = wisperData;
    }

    public DataChromotherapy getChromotherapyData() {
        return chromotherapyData;
    }

    public void setChromotherapyData(DataChromotherapy chromotherapyData) {
        this.chromotherapyData = chromotherapyData;
    }

    @Override
    public MultiplexControlFrame clone() throws CloneNotSupportedException {
        byte objArray[] = this.getDataArray();
        MultiplexControlFrame cloneFrame = new MultiplexControlFrame(objArray);
        return cloneFrame;
    }
}
