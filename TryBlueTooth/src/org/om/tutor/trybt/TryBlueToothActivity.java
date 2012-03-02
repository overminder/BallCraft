package org.om.tutor.trybt;

import java.io.IOException;
import java.util.ArrayList;
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
import android.os.Bundle;
import android.util.Log;


public class TryBlueToothActivity extends Activity {
	static final int REQUEST_ENABLE_BT = 0;
	static final int REQUEST_ENABLE_BT_VISIBILITY = 1;
	
	boolean isServer = true;
	BroadcastReceiver rec;
	BluetoothAdapter btAdapter;
	ArrayList<BluetoothDevice> devs = new ArrayList<BluetoothDevice>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        openBluetoothAdapter();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	if (rec != null) {
    		unregisterReceiver(rec);
    		rec = null;
    	}
    }
    
    @Override
    public void onActivityResult(int reqCode, int resCode, Intent ite) {
    	switch (reqCode) {
    	case REQUEST_ENABLE_BT:
    		if (resCode == RESULT_OK) {
    			toast("user allows us to enable bt.");
    			discoverBluetoothDevices();
    		}
    		else {
    			toast("user does not want to enable bt.");
    		}
    		break;
    	case REQUEST_ENABLE_BT_VISIBILITY:
    		if (resCode != RESULT_CANCELED) {
    			toast("user allows us to be discoverable.");
    		}
    		else {
    			toast("user forbidden us to be discoverable.");
    		}
    	}
    }
    
    void toast(String s) {
        // Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    	Log.e("[]", s);
    }
    
    void openBluetoothAdapter() {
    	toast("getting default adapter.");
    	btAdapter = BluetoothAdapter.getDefaultAdapter();
    	if (btAdapter != null) {
    		if (btAdapter.isEnabled()) {
    			discoverBluetoothDevices();
    		}
    		else {
    			toast("btAdapter not enabled?");
    		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    		}
    	}
    	else {
    		toast("null adapter?");
    	}
    }
    
    void discoverBluetoothDevices() {
    	Intent discoverableIntent = new
    			Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
		startActivityForResult(discoverableIntent, REQUEST_ENABLE_BT_VISIBILITY);
    			
		rec = new BroadcastReceiver() {
			public void onReceive(Context ctx, Intent ite) {
				String act = ite.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(act)) {
					BluetoothDevice dev = ite.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					String name = dev.getName();
					TryBlueToothActivity.this.toast("got a device: " + name + " [" + dev.getAddress() + "]");
					if (name != null && name.equals("sanya-htc")) {
						TryBlueToothActivity.this.toast("found server: " + name);
						devs.add(dev);
						TryBlueToothActivity.this.isServer = false;
					}
				}
				else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(act)) {
					TryBlueToothActivity.this.toast("really started discovery.");
				}
				else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(act)) {
					TryBlueToothActivity.this.proceedAfterDiscovery();
				}
			}
		};
	
		registerReceiver(rec, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		registerReceiver(rec, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
		registerReceiver(rec, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
		
    	toast("Yooo start to discovery!");
    	btAdapter.startDiscovery();
    }
    
    void proceedAfterDiscovery() {
    	this.toast("finished discovery, I am a " + (isServer ? "server" : "client") + ".");
    	if (isServer) {
    		toast("[Server] Start listening...");
    		startListening();
    	}
    	else {
    		BluetoothDevice dev = devs.get(0);
    		toast("[Client] Connecting to " + dev.getName() + "...");
    		connectServer();
    	}
    }
    
    UUID getUUID() {
    	return new UUID(10L, 20L);
    }	
    
    void startListening() {
    	// Initialized later.
    	final BluetoothServerSocket serverSock;
		try {
			BluetoothServerSocket tmp = null;
			tmp = btAdapter.listenUsingRfcommWithServiceRecord("serviceName", getUUID());
			serverSock = tmp;
			toast("[Server] socket created.");
		}
		catch (IOException e) {
			toast("[Server] caught IOException when start listening.");
			return;
		}
		
    	Thread clientAccepter = new Thread() {
    		BluetoothSocket sockPair = null;
    		public void run() {
    			while (true) {
    				try {
    					sockPair = serverSock.accept();
    				}
    				catch (IOException e) {
    					TryBlueToothActivity.this.toast("[Server] IOException when accepting...");
    					return;
    				}
    				
    				if (sockPair != null) {
    					// Delegate to another handler thread.
    					TryBlueToothActivity.this.handleClient(sockPair);
    					sockPair = null;
    				}
    			}
    		}
    	};
    	clientAccepter.start();
    }
    
    void handleClient(BluetoothSocket sockPair) {
    	toast("[Server] handling connection from " + sockPair.toString());
    	try {
    		String strMsg = "[Server] Hello";
    		byte[] msg = strMsg.getBytes();
    		sockPair.getOutputStream().write(msg);
    		
    		byte[] buf = new byte[1024];
    		sockPair.getInputStream().read(buf);
    		toast("[Server] received msg from client: `" + new String(buf) + "'");
			sockPair.close();
		} catch (IOException e) {
			toast("[Server] error operating on " + sockPair.toString());
		}
    }
    
    void connectServer() {
	    final BluetoothDevice serverDev = devs.get(0);
	    final BluetoothSocket sock;
    	 
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
        	BluetoothSocket tmp = null;
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = serverDev.createRfcommSocketToServiceRecord(getUUID());
            sock = tmp;
        } catch (IOException e) {
			toast("[Client] caught IOException when creating socket.");
			return;
        }
	    
    	Thread clientThread = new Thread() {
    	    public void run() {
    	        // Cancel discovery because it will slow down the connection
    	        //mBluetoothAdapter.cancelDiscovery();
    	 
    	        try {
    	            // Connect the device through the socket. This will block
    	            // until it succeeds or throws an exception
    	        	// XXX: pair with device?
    	            sock.connect();
    	        } catch (IOException connectException) {
    	            // Unable to connect; close the socket and get out
    	        	toast("[Client] unable to connect");
    	            try {
    	                sock.close();
    	            } catch (IOException closeException) {
    	            	toast("[Client] unable to close huh?");
    	            }
    	            return;
    	        }
    	 
    	        // Do work to manage the connection (in a separate thread)
    	        TryBlueToothActivity.this.chatWithServer(sock);
    	    }
    	};
    	
    	clientThread.start();
    }
    
    void chatWithServer(BluetoothSocket sock) {
    	byte[] buf = new byte[1024];
    	try {
    		sock.getInputStream().read(buf);
			sock.getOutputStream().write("[Client] Hai.".getBytes());
			toast("[Client] server said: `" + new String(buf) + "'");
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			toast("[Client] error sending/receiving data.");
		}
    }
}
