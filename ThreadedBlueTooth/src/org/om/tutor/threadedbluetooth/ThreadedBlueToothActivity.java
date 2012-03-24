package org.om.tutor.threadedbluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;

public class ThreadedBlueToothActivity extends BluetoothAwareActivity {
	class ServerChannel extends BluetoothChannel {
		
		public ServerChannel() { }
		
		@Override
		TYPE getType() {
			return TYPE.SERVER;
		}

		@Override
		void onOpen() {
			Log.e("server", "channel opened");
		}

		@Override
		void onMessage(byte[] message) {
			Log.e("server", "message received");
		}

		@Override
		void onError(Exception e) {
			Log.e("server", "error happened");
		}

		@Override
		void onClose() {
			Log.e("server", "connection closed");
		}
	}
	
	class ClientChannel extends BluetoothChannel {
		
		public ClientChannel() { }
		
		@Override
		TYPE getType() {
			return TYPE.CLIENT;
		}

		@Override
		void onOpen() {
			Log.e("client", "channel opened");
		}

		@Override
		void onMessage(byte[] message) {
			Log.e("client", "message received");
		}

		@Override
		void onError(Exception e) {
			Log.e("client", "caught error");
		}

		@Override
		void onClose() {
			Log.e("client", "conn closed");
		}
	}
	
	BluetoothChannelFactory channelFactory = new BluetoothChannelFactory(
		ServerChannel.class, ClientChannel.class
	);
	
	BluetoothService bluetoothService = new BluetoothService() {
		BluetoothDevice serverDevice;
		
		@Override
		UUID getUUID() {
			// XXX: what is this?
	    	return new UUID(10L, 20L);
		}
	
		@Override
		void onDeviceFound(BluetoothDevice device) {
			String name = device.getName();
			if (name == null) {
				name = "<nil>";
			}
			else if (name.equals("sanya-htc")) {
				serverDevice = device;
				BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
				final BluetoothChannel serverChannel;
				try {
					// I am a client
					serverChannel = connectToDevice(serverDevice);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				new Thread() {
					public void run() {
						try {
							serverChannel.sendMessage("From client.".getBytes());
						} catch (IOException e) {
							throw new RuntimeException();
						}
					}
				}.start();
			}
			Log.e("btServ", "found " + name);
		}

		@Override
		void onDiscoveryFinished() {
			Log.e("btServ", "discovery finished");
			// I am the server
			startListening();
		}

		@Override
		void onListeningFailed() {
			Log.e("btServ", "listen failed");
		}

		@Override
		void onClientConnected(BluetoothChannel client) {
			Log.e("btServ", "someone connected");
			try {
				client.sendMessage("hello, world!".getBytes());
			} catch (IOException e) { }
		}

		@Override
		boolean stillAcceptingConnection() {
			return true;
		}

		@Override
		BluetoothChannelFactory getChannelFactory() {
			return channelFactory;
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initializeBluetooth(0, 1);
    }

	@Override
	void onEnableRequestResult(boolean enabled) {
		if (enabled) {
			Log.e("btServ", "enabled: true");
			setDiscoverableFor(3600);
		}
		else {
			Log.e("btServ", "enabled: false");
		}
	}

	@Override
	void onDiscoverabilityRequestResult(boolean discoverable) {
		if (discoverable) {
			Log.e("btServ", "discoverable: true");
			bluetoothService.startDiscovery(this);
		}
		else {
			Log.e("btServ", "discoverable: false");
		}
	}


}