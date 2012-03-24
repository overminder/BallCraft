package org.om.tutor.threadedbluetooth;

import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class BluetoothService {
	private String kServiceName = "Ballcraft";
	
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private Thread connectionAccepterThread;
	private BluetoothServerSocket serverSocket;
	private Activity parent;
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context ctx, Intent ite) {
			String act = ite.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(act)) {
				BluetoothDevice device = ite.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				onDeviceFound(device);
			}
			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(act)) {
				onDiscoveryFinished();
			}
		}
	};
	
	Runnable connectionAccepter = new Runnable() {
		public void run() {
			BluetoothServerSocket serverSocket = getServerSocket();
			BluetoothSocket newConnection = null;
			while (stillAcceptingConnection()) {
				try {
					newConnection = serverSocket.accept();
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
    				
				if (newConnection != null) {
					try {
						onClientConnected(getChannelFactory().fromAcceptedConnection(newConnection));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					newConnection = null;
				}
			}
		}
	};

	void startDiscovery(Activity parent) {
		this.parent = parent;
		parent.registerReceiver(broadcastReceiver,
						 		new IntentFilter(BluetoothDevice.ACTION_FOUND));
		parent.registerReceiver(broadcastReceiver,
						 	    new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
    	bluetoothAdapter.startDiscovery();
	}
	
	void destroy() {
		parent.unregisterReceiver(broadcastReceiver);
	}
	
	void startListening() {
		try {
			setServerSocket(bluetoothAdapter.listenUsingRfcommWithServiceRecord(
					kServiceName, getUUID()));
		}
		catch (IOException e) {
			onListeningFailed();
			return;
		}
		connectionAccepterThread = new Thread(connectionAccepter);
		connectionAccepterThread.start();
	}

	
	protected void setServerSocket(BluetoothServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	protected BluetoothServerSocket getServerSocket() {
		return serverSocket;
	}

	BluetoothChannel connectToDevice(BluetoothDevice device) throws IOException {
    	BluetoothSocket connection = device.createRfcommSocketToServiceRecord(getUUID());
    	return getChannelFactory().connectToServer(connection);
	}
	
	abstract UUID getUUID();
	abstract void onDeviceFound(BluetoothDevice device);
	abstract void onDiscoveryFinished();
	abstract void onListeningFailed();
	abstract void onClientConnected(BluetoothChannel client);
	abstract boolean stillAcceptingConnection();
	abstract BluetoothChannelFactory getChannelFactory();

}
