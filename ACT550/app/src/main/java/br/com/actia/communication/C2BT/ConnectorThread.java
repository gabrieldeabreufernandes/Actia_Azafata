package br.com.actia.communication.C2BT;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.Vector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import br.com.actia.communication.DeviceCOM.DeviceConnectionEvent;
import br.com.actia.communication.DeviceCOM.DeviceConnectionEventListener;

public class ConnectorThread extends Thread
{
	private static final String TAG = "C2BT.ConnectorThread";

	/*
	 * Constants
	 */
	/**
	 * Bluetooth connection timeout in milliseconds
	 */
	protected static final long BTCONNECTION_TIMEOUT = 30000;

	
	private BluetoothDevice device;

	private boolean successfulConnected;

	private BluetoothSocket sock;
	
	Vector<DeviceConnectionEventListener> listeners = new Vector<DeviceConnectionEventListener>();

	private Thread watchdogThread;

	public boolean autostart;

	
	public ConnectorThread(BluetoothDevice device)
	{
		if(device == null) {
			// Get the local Bluetooth adapter
			BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

			// Get a set of currently paired devices
			Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

			if(pairedDevices.size() == 0) {
				//start intent
			}
			// If there are paired devices, add each one to the ArrayAdapter
			else if (pairedDevices.size() == 1) {
				this.device = pairedDevices.iterator().next();
			} else {
				Log.d(TAG, "Start chooser activity");
				//Start Activity

				//Implement onResult from intent
				this.device = pairedDevices.iterator().next();
			}
		}
	}
	
	@Override
	public void run()
	{
		try
		{
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			initBluetoothConnection(adapter);
			
//			bluetoothError = false;
		} 
		catch (Exception e)
		{
			DeviceConnectionEvent event = new DeviceConnectionEvent(false, true, "Can't initialize Bluetooth connection.");
			notifyListeners(event);
			Log.d(TAG, "Can't initialize Bluetooth connection");
		}
	}
	
	private void initBluetoothConnection(BluetoothAdapter adapter) throws Exception
	{
//		BluetoothDevice zee = getBondedDevice(adapter);

		Method m = device.getClass().getMethod("createRfcommSocket",
				new Class[] { int.class });
		sock = (BluetoothSocket) m.invoke(device, Integer.valueOf(1));

		successfulConnected = false;
		try
		{
			watchdogThread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						Thread.sleep(BTCONNECTION_TIMEOUT);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if(sock!=null && !successfulConnected)
					{
						try
						{
							sock.close();
							DeviceConnectionEvent event = new DeviceConnectionEvent(false, true, "Connection not possible (timeout).");
							notifyListeners(event);
						}
						catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			watchdogThread.start();
			
			if(autostart)
			{
				Thread.sleep(9000);//10000
			}
			

			sock.connect();
			successfulConnected = true;
			DeviceConnectionEvent event = new DeviceConnectionEvent(true, false, "Connected.");
			notifyListeners(event);
		}
		catch (Exception e) 
		{
			// watchdogThread.dontWarn = true;
			DeviceConnectionEvent event = new DeviceConnectionEvent(false, true, e.getMessage());
			notifyListeners(event);
		}
		
	}
	
	private void notifyListeners(DeviceConnectionEvent event)
	{
		for (DeviceConnectionEventListener one : listeners)
		{
			one.onConnectionEvent(event);
		}
	}
	
	public void addConnectionEventListener(DeviceConnectionEventListener listener)
	{
		listeners.add(listener);
	}

	public void removeConnectionEventListener(DeviceConnectionEventListener listener)
	{
		listeners.remove(listener);
	}

	public BluetoothSocket getSocket()
	{
		return sock;
	}

}
