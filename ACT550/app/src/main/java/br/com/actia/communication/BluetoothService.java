package br.com.actia.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Armani(anderson.armani@actia.com.br) on 06/10/2015.
 */
public class BluetoothService {
    private BluetoothAdapter bluetoothAdapter = null;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    public BluetoothService() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
    }

    /**
     * Set the current state of the chat connection
     *
     * @param state An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        //Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getState() {
        return mState;
    }

    public synchronized void stop() {
        System.out.println("### ### BLUETOOTH SERVICE STOP ### ###");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        //Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        setState(STATE_LISTEN);

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        //start connection
        connect(null);
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
        //Log.d(TAG, "connect to: " + device);

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
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        //Log.d(TAG, "connected, Socket Type:" + socketType);

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
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        setState(STATE_CONNECTED);
    }

//################################### METHODS ###################################
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
        // Start the service over to restart listening mode
        BluetoothService.this.start();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        // Start the service over to restart listening mode
        BluetoothService.this.start();
    }

//################################### THREADS ###################################



    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmBthSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            if(device == null) {
                Set<BluetoothDevice> bluetoothDevices =  bluetoothAdapter.getBondedDevices();
                System.out.println("BLUETOOTH SIZE = " + bluetoothDevices.size());
                if(bluetoothDevices.size() > 0) {
                    device = bluetoothDevices.iterator().next();
                    System.out.println("BLUETOOTH NAME = " + device.getName());
                }
            }

            mmDevice = device;
            BluetoothSocket auxBluetoothSocket = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                System.out.println("### NUMERO DE UUIDS = " + device.getUuids().length);
                auxBluetoothSocket = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
                System.out.println("### UUID = " + device.getUuids()[0].getUuid().toString() + " ### <<<<<<<<<<<<");
            } catch (IOException e) {
                System.out.println("### createRfcommSocketToServiceRecord IOException ");
            }
            mmBthSocket = auxBluetoothSocket;
        }

        public void run() {
            //setName("ConnectThread" + mSocketType);

            // Always cancel discovery because it will slow down a connection
            bluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmBthSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmBthSocket.close();
                } catch (IOException e2) {

                }
                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmBthSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmBthSocket.close();
            } catch (IOException e) {
                //Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmBthSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private int initIndex = 0;
        private boolean threadLoop = true;

        public ConnectedThread(BluetoothSocket socket) {
            //Log.d(TAG, "create ConnectedThread: " + socketType);
            mmBthSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            System.out.println("### mmBthSocket CONNECTED? = " + mmBthSocket.isConnected());

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                //Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;

            System.out.println("### CONNECTED THREAD RUNNING...");
        }

        private void elm327Initialize(int index) {
            byte bts[] = null;

            /*try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            switch(index) {
                case 0:
                    //Reset ELM327
                    bts = new byte[]{'A','T','Z',0x0D};
                    break;
                case 1:
                    //AT L1 = Linefeed enable
                    bts = new byte[]{'A','T','L','1',0x0D};
                    break;
                case 2:
                    //AT H1 = Headers enable
                    bts = new byte[]{'A','T','H','1',0x0D};
                    break;
                case 3:
                    //AT S1 = Spaces enable
                    bts = new byte[]{'A','T','S','1',0x0D};
                    break;
                case 4:
                    //AT AL =
                    bts = new byte[]{'A','T','A','L',0x0D};

                    //bts = new byte[]{'A','T','S','H','F','1',0x0D};
                    break;
                case 5:
                    //AT SP0 = Set CAN PROTOCOL to AUTO
                    bts = new byte[]{'A','T','S','P','0',0x0D};
                    break;
                case 6:
                    //AT MA =
                    bts = new byte[]{'A','T','M','A',0x0D};
                    //bts = new byte[]{0x31, 0x32, 0x33, 0x34, 0x0D};
                    break;
            }

            write(bts);
        }

        public void run() {
            //Log.i(TAG, "BEGIN mConnectedThread");
            elm327Initialize(initIndex = 0);
            System.out.println("### CONNECTED THREAD RUNNING... 22 ");
            byte[] buffer = new byte[1024];
            int bytes;

            String strPattern = ">";
            Scanner scan = new Scanner(new InputStreamReader(mmInStream));
            scan.useDelimiter(strPattern);

            // Keep listening to the InputStream while connected
            while (threadLoop) {
                System.out.println("### THREAD LOOPING...");

                try {

                        System.out.println("### READING BYTES...");
                        String strReaded = scan.next();
                        bytes = strReaded.length();
                        buffer = strReaded.getBytes();

                        System.out.println("### BYTES == " + bytes);
                        if(bytes > 0) {
                            for(int i = 0; i < bytes; i++) {
                                System.out.printf("### %c || %x ###\n", buffer[i], buffer[i]);
                            }
                        }

                        if(initIndex < 6) {
                            elm327Initialize(++initIndex);
                        }
                        // Send the obtained bytes to the UI Activity
                        //mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    /*} catch (IOException e) {
                        System.out.println("### READ ERROR ### " + e.getMessage());
                        //Log.e(TAG, "disconnected", e);
                        connectionLost();
                        // Start the service over to restart listening mode
                        //BluetoothService.this.start();
                        break;*/
                    } catch (Exception ex) {
                        System.out.println("### READ ERROR 22 ### " + ex.getMessage());
                        break;
                    }
            }

            System.out.println("### CONNECTED THREAD CLOSING...");
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                System.out.println("### WRITE ### ");
                mmOutStream.write(buffer);
                mmOutStream.flush();

                // Share the sent message back to the UI Activity
                //mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
            } catch (IOException e) {
                System.out.println("### WRITE ERROR ### " + e.getMessage());
                //Log.e(TAG, "Exception during write", e);
            } catch (Exception ex) {
                System.out.println("### WRITE EXCEPTION ### " + ex.getMessage() + " CODIGO " + ex.getStackTrace());
                ex.printStackTrace();
            }
        }

        public void cancel() {
            System.out.println("### CONNECTED THREAD CANCEL...");
            try {
                mmBthSocket.close();
                threadLoop = false;
            } catch (IOException e) {
                //Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}
