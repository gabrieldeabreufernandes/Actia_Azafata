package br.com.actia.communication.C2BT;

import java.nio.ByteBuffer;
import java.util.Arrays;

import br.com.actia.communication.CanMSG;

/**
 * Created by Armani on 07/12/2015.
 */
public class C2BT {
    public static byte RECEPTION_FLAG = (byte)0xAA;
    public static byte SEND_FLAG = 0x55;
    public static byte CONFIG_DEFAULT = 0x00;
    public static byte QTY_DEFAULT = 0x08;

    private byte startByte;
    private byte configuration;
    private byte identifier[] = new byte[4];
    private byte numberOfBytes;
    private byte data[] = new byte[8];
    private byte checksum;
    private byte configData;

    public C2BT() {
        startByte = SEND_FLAG;
        configuration = CONFIG_DEFAULT;
        numberOfBytes = QTY_DEFAULT;
        checksum = 0x00;
    }

    public byte[] getCmdData(CanMSG canMSG) {
        startByte = SEND_FLAG;
        configuration = getConfiguration(canMSG);
        identifier = ByteBuffer.allocate(4).putInt(canMSG.getId()).array();
        numberOfBytes = QTY_DEFAULT;
        data = canMSG.getData();
        checksum = getCmdChecksum();

        byte dataToSend[] = {startByte,
                configuration,
                identifier[0], identifier[1], identifier[2], identifier[3],
                numberOfBytes,
                data[0], data[1], data[2], data[3],
                data[4], data[5], data[6], data[7],
                checksum
        };
        return dataToSend;
    }

    public byte[] getConfigData(byte configData, byte configuration) {
        startByte = SEND_FLAG;
        this.configuration = configuration;
        this.configData = configData;
        checksum = getConfigChecksum();

        byte dataToSend[] = {startByte, configuration, configData, checksum};

        return dataToSend;
    }

    private byte getCmdChecksum() {
        byte checksum = 0x00;
        int calc = 0x00;

        calc = startByte;
        calc += configuration;
        for(int i = 0; i < identifier.length; i++) {
            calc += identifier[i];
        }
        calc += numberOfBytes;
        for(int i = 0; i < numberOfBytes; i++) {
            calc += data[i];
        }

        checksum = (byte) (calc & 0xff);
        return checksum;
    }

    private byte getConfigChecksum() {
        byte checksum = 0x00;
        int calc = 0x00;

        calc = startByte;
        calc += configuration;
        calc += configData;

        checksum = (byte) (calc & 0xff);
        return checksum;
    }

    private byte getConfiguration(CanMSG canMSG) {
        byte config = 0x00;

        if(canMSG.getType() == CanMSG.MSGTYPE_EXTENDED) {
            config |= 0x40;
        }

        config |= 0x06; //transmission in binary format

        return config;
    }

    public static CanMSG getCanMSG(byte[] data) {
        CanMSG canMSG = new CanMSG();

        int msgType = data[1] & 0xFF;
        int msgID = ((data[2] & 0xFF) << 24) | ((data[3] & 0xFF) << 16) | ((data[4] & 0xFF) << 8) | (data[5] & 0xFF);
        int msgLen = data[6];
        byte msgData[] = Arrays.copyOfRange(data, 7, 15);

        if((msgType & 0x40) == 0x40)
            canMSG.setType(CanMSG.MSGTYPE_EXTENDED);
        else
            canMSG.setType(CanMSG.MSGTYPE_STANDARD);

        canMSG.setId(msgID);
        canMSG.setLength((byte) msgLen);
        canMSG.setData(msgData, (byte) msgLen);

        return canMSG;
    }
}
