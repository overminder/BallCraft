package org.om.tutor.threadedbluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothSocket;

public class BluetoothChannelFactory {
	Class<? extends BluetoothChannel> serverChannelClass;
	Class<? extends BluetoothChannel> clientChannelClass;
	
	BluetoothChannelFactory(Class<? extends BluetoothChannel> serverChannelClass,
							Class<? extends BluetoothChannel> clientChannelClass) {
		this.serverChannelClass = serverChannelClass;
		this.clientChannelClass = clientChannelClass;
	}
	
	boolean shallAccept(BluetoothSocket socket) {
		return true;
	}
	
	BluetoothChannel fromAcceptedConnection(final BluetoothSocket socket) throws IOException {
		if (shallAccept(socket)) {
			final BluetoothChannel channel;
			try {
				channel = serverChannelClass.newInstance();
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
			channel.initialize(socket);
			
			Thread openNotifier = new Thread() {
				public void run() {
					channel.onOpen();
				}
			};
			openNotifier.start();
			
			return channel;
		}
		else {
			return null;
		}
	}
	
	BluetoothChannel connectToServer(final BluetoothSocket socket) throws IOException {
		final BluetoothChannel channel;
		try {
			channel = serverChannelClass.newInstance();
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		
		Thread opener = new Thread() {
			public void run() {
				try {
					socket.connect();
				} catch (IOException e) {
					channel.onError(e);
					return;
				}
				channel.onOpen();
				try {
					channel.initialize(socket);
				} catch (IOException e) {
					channel.onError(e);
				}
			}
		};
		opener.start();
		return channel;
	}
}
