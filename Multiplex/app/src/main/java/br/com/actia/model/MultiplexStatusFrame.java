package br.com.actia.model;

/**
 * Created by Armani andersonaramni@gmail.com on 26/10/16.
 */

public class MultiplexStatusFrame {
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

    //DATA BACKUP
    private byte data[] = new byte[8];

    public MultiplexStatusFrame(byte[] data) {
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

        //### BYTE 3, 4, 5 ###
        byte chromoArr[] = new byte[3];
        chromoArr[0] = data[3];
        chromoArr[1] = data[4];
        chromoArr[2] = data[5];
        chromotherapyData = new DataChromotherapy(chromoArr);

        //COPY BYTE
        this.data[0] = data[0];
        this.data[1] = data[1];
        this.data[2] = data[2];
        this.data[3] = data[3];
        this.data[4] = data[4];
        this.data[5] = data[5];
        this.data[6] = data[6];
        this.data[7] = data[7];
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

    public byte[] getData() {
        return data;
    }

    public boolean dataIsEqual(byte testData[]) {
        int dataSize;
        boolean ret = true;

        dataSize = this.data.length > testData.length ? testData.length : this.data.length;

        for(int i = 0; i < dataSize; i++) {
            if(this.data[i] != testData[i]) {
                ret = false;
                break;
            }
        }

        return ret;
    }
}
