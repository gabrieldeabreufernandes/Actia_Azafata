package br.com.actia.communication.C2BT;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import br.com.actia.communication.CanMSG;
import br.com.actia.communication.DeviceCOM.DeviceCom;
import br.com.actia.communication.DeviceCOM.DeviceConnectionEvent;
import br.com.actia.communication.DeviceCOM.DeviceConnectionEventListener;
import br.com.actia.communication.DeviceCOM.DeviceReceivedCmdEventListener;
import br.com.actia.communication.DeviceCOM.DeviceReceivedEvent;

import static br.com.actia.communication.C2BT.SentenceBuilder.*;

/**
 * Created by Armani on 26/11/2015.
 */
public class DeviceCom_C2BT implements DeviceCom {
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_SEARCHING = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    private static final String TAG = "Device C2BT";

    private List<CanMSG> canMSGList = null;
    private List<CanMSG> canSendMSGList = null;
    private Vector<DeviceConnectionEventListener> connectionListeners = new Vector<DeviceConnectionEventListener>();
    private Vector<DeviceReceivedCmdEventListener> cmdReceivedListeners = new Vector<DeviceReceivedCmdEventListener>();
    private int deviceState = DeviceCom.DEVICE_STATE_DISCONNECTED;

    // Name for the SDP record when creating server socket
    private static final String NAME_SECURE = "BluetoothChatSecure";
    private static final String NAME_INSECURE = "BluetoothChatInsecure";

    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    private final BluetoothAdapter mAdapter;
    private SearchThread mSearchThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;

    /*ConnectorThread connectorThread = null;
    ReaderThread readerThread = null;
    WriterThread writerThread = null;
    BluetoothDevice bluetoothDevice = null;*/

    public DeviceCom_C2BT() {
        canMSGList = new LinkedList<>();
        canSendMSGList = new LinkedList<>();

        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
    }

    @Override
    public synchronized boolean hasCommand() {
        return !canMSGList.isEmpty();
    }

    @Override
    public synchronized CanMSG getCommand() {
        CanMSG canMSG = null;
        if(!canMSGList.isEmpty()) {
            canMSG = canMSGList.remove(0);
        }

        return canMSG;
    }

    @Override
    public synchronized void sendCommand(CanMSG canMSG) {
        byte buffer[] = new byte[16];
        int msgId = canMSG.getId();
        byte msgData[] = canMSG.getData();
        int checkSum = 0;

        write(getNoCommandBuffer()); //write no command
        //Log.d(TAG, "### SEND NO COMMAND");

        buffer[0] = C2BT.SEND_FLAG;
        buffer[1] = 0x40; //Extended frame
        buffer[2] = (byte)((msgId & 0xFF000000) >> 24);
        buffer[3] = (byte)((msgId & 0x00FF0000) >> 16);
        buffer[4] = (byte)((msgId & 0x0000FF00) >> 8);
        buffer[5] = (byte)((msgId & 0x000000FF));
        buffer[6] = 0x08;
        buffer[7] = msgData[0];
        buffer[8] = msgData[1];
        buffer[9] = msgData[2];
        buffer[10] = msgData[3];
        buffer[11] = msgData[4];
        buffer[12] = msgData[5];
        buffer[13] = msgData[6];
        buffer[14] = msgData[7];

        for(int i = 0; i < buffer.length - 1; i++) {
            checkSum += buffer[i];
            //System.out.printf("DATA[%d] = %X\n", i, buffer[i]);
        }
        buffer[15] = (byte) (checkSum & 0xFF);
        //System.out.printf("DATA[%d] = %X\n", 15, buffer[15]);

        write(buffer);
    }

    private byte[] getNoCommandBuffer() {
        byte buffer[] = new byte[16];

        buffer[0] = C2BT.SEND_FLAG;
        buffer[1] = 0x40; //Extended frame
        buffer[2] = 0x18;
        buffer[3] = (byte)0xFF;
        buffer[4] = 0x64;
        buffer[5] = 0x60;
        buffer[6] = 0x08;
        buffer[7] = 0x00;
        buffer[8] = (byte)0xFF;
        buffer[9] = 0x00;
        buffer[10] = (byte)0xFF;
        buffer[11] = (byte)0xFF;
        buffer[12] = (byte)0xFF;
        buffer[13] = (byte)0xFF;
        buffer[14] = (byte)0xFF;
        buffer[15] = (byte)0x72;

        return buffer;
    }

    @Override
    public void startDevice() {
        Log.d(TAG, "startDevice");
        start();
    }

    @Override
    public void closeDevice() {
        stop();
    }

    private void connectionNotifyListeners(DeviceConnectionEvent event)
    {
        for (DeviceConnectionEventListener one : connectionListeners)
        {
            one.onConnectionEvent(event);
        }
    }

    @Override
    public void addConnectionEventListener(DeviceConnectionEventListener listener) {
        connectionListeners.add(listener);
    }

    @Override
    public void removeConnectionEventListener(DeviceConnectionEventListener listener) {
        connectionListeners.remove(listener);
    }

    private void receiveNotifyListeners(DeviceReceivedEvent event)
    {
        for (DeviceReceivedCmdEventListener one : cmdReceivedListeners)
        {
            one.OnReceivedEvent(event);
        }
    }

    @Override
    public void addReceivedCmdEventListener(DeviceReceivedCmdEventListener listener) {
        cmdReceivedListeners.add(listener);
    }

    @Override
    public void removeReceivedCmdEventListener(DeviceReceivedCmdEventListener listener) {
        cmdReceivedListeners.remove(listener);
    }


    //##############################################################################################
    /**
     * Set the current state of the chat connection
     *
     * @param state An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getState() {
        return mState;
    }
    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */

    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if(mSearchThread != null) {
            mSearchThread.cancel();
            mSearchThread = null;
        }

        mSearchThread = new SearchThread();
        mSearchThread.start();
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    public synchronized void connect(BluetoothDevice device, boolean secure) {
        Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device, secure);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device, final String socketType) {
        Log.d(TAG, "connected, Socket Type:" + socketType);

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket, socketType);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        /*Message msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);*/

        setState(STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if(mSearchThread != null) {
            mSearchThread.cancel();
            mSearchThread = null;
        }

        setState(STATE_NONE);
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        // Send a failure message back to the Activity
        /*Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);*/

        Log.d(TAG, "connectionFailed -> Restart");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        // Start the service over to restart listening mode
        DeviceCom_C2BT.this.start();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        // Send a failure message back to the Activity
        /*Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);*/

        //if in shutdown - return
        if(getState() == STATE_NONE)
            return;

        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        // Start the service over to restart listening mode
        DeviceCom_C2BT.this.start();
    }
    //##############################################################################################
    //#####################                     THREAD                         #####################
    //##############################################################################################

    /**
     * This thread runs while search for Bluetooth Device
     */
    private class SearchThread extends Thread {
        public SearchThread() {
        }

        public void run() {
            setState(STATE_SEARCHING);

            Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();
            if(pairedDevices.size() == 1) {
                Log.d(TAG, "1 bluetooth device -> Connect");
                connect(pairedDevices.iterator().next(), true);
            }
            else {
                Log.d(TAG, "More than 1 bluetooth device paired!");
            }
        }

        public void cancel() {
            Log.e(TAG, "SearchThread canceled");
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(
                            MY_UUID_SECURE);
                } else {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(
                            MY_UUID_INSECURE);
                }
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {

                Log.e(TAG, e.toString());
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " + mSocketType +
                            " socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (DeviceCom_C2BT.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            Log.d(TAG, "create ConnectedThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;

            configDevice();
        }

        private void configDevice() {
            try {
                //mmOutStream.write(getResetSequence());
                mmOutStream.write(getASCIIBinSentence(true));
                mmOutStream.write(getBaudrateSentence("250 kBit"));
                mmOutStream.write(getNoFiltersSequence());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    byte[] buffer = new byte[16];
                    bytes = mmInStream.read(buffer,0,1);

                    if(bytes == 1 && buffer[0] == C2BT.RECEPTION_FLAG) {
                        int offset = 1;
                        int length = buffer.length;

                        while (offset < length) {
                            int charsRead = mmInStream.read(buffer, offset, length - offset);
                            if (charsRead <= 0) {
                                throw new IOException("Stream terminated early");
                            }
                            offset += charsRead;
                        }

                        CanMSG canMSG = C2BT.getCanMSG(buffer);

                        receiveNotifyListeners(new DeviceReceivedEvent(canMSG));
                        //System.out.printf("CAN MSG ID = [%X]\n", canMSG.getId());
                        //Log.d(TAG, "RECEIVED CMD = " + canMSG.getId());
                    } else {
                        //Log.d(TAG, "NO START FLAG = " + buffer[0]);
                        System.out.printf("### NO START FLAG = %X", buffer[0]);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);

                    // Start the service over to restart listening mode
                    connectionLost();
                    //DeviceCom_C2BT.this.start();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                /*mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer)
                        .sendToTarget();*/
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }

        private CanMSG getCanMsg(byte[] msgBuffer) {
            CanMSG canMSG = new CanMSG();

            System.out.println("CONNECTED getCanMsg = ");
            for(int i = 0; i < msgBuffer.length; i++)
                System.out.println("getCanMsg DATA = " + msgBuffer[i]);

            return canMSG;
        }
    }
}
