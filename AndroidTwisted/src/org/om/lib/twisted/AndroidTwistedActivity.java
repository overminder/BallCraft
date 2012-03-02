package org.om.lib.twisted;

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

public class AndroidTwistedActivity extends Activity {
	boolean isServer = true;
	BluetoothAdapter btAdapter;
	ArrayList<BluetoothDevice> btDevices = new ArrayList<BluetoothDevice>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        interactUsingBluetooth();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	Reactor.unregisterReceiver(this);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	try {
    		Reactor.handleActivityResult(requestCode, resultCode, intent);
    	} catch (NoSuchRequestCodeError e) {
    		super.onActivityResult(requestCode, resultCode, intent);
    	}
    }

	private void interactUsingBluetooth() {
		// TODO Auto-generated method stub
		prn("getting default adapter");
		btAdapter = BluetoothAdapter.getDefaultAdapter();
    	if (btAdapter != null) {
    		if (btAdapter.isEnabled()) {
    			discoverBluetoothDevices();
    		}
    		else {
    			prn("btAdapter not enabled?");
    		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    		    ActivityResultHandler handler = new ActivityResultHandler() {
    		    	public void handle(int result, Intent intent) {
    		    		if (result == RESULT_OK) {
    		    			prn("bluetooth enabled.");
    		    			discoverBluetoothDevices();
    		    		}
    		    		else {
    		    			prn("bluetooth not enabled by the user.");
    		    		}
    		    	}
    		    };
    		    Reactor.startActivityForResult(enableBtIntent, handler, this);
    		}
    	}
    	else {
    		prn("null adapter?");
    	}
	}

	private void discoverBluetoothDevices() {
    	Intent discoverReq = new
    			Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverReq.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
		
		ActivityResultHandler discoverReqHandler = new ActivityResultHandler() {
			public void handle(int result, Intent intent) {
	    		if (result != RESULT_CANCELED) {
	    			prn("user allows us to be discoverable.");
	    		}
	    		else {
	    			prn("user forbidden us to be discoverable.");
	    		}
	    		// Anyway...
	    		Reactor.registerReceiver(new BroadcastReceiver() {
	    			public void onReceive(Context context, Intent intent) {
						BluetoothDevice dev = intent.getParcelableExtra(
								BluetoothDevice.EXTRA_DEVICE);
						String name = dev.getName();
						prn("got a device: " + name + " ["+
								dev.getAddress() + "]");
						if (name != null && name.equals("sanya-htc")) {
							prn("found server: " + name);
							btDevices.add(dev);
							isServer = false;
						}
	    			}
	    		}, new IntentFilter(BluetoothDevice.ACTION_FOUND), AndroidTwistedActivity.this);
	    		
	    		Reactor.registerReceiver(new BroadcastReceiver() {
	    			public void onReceive(Context _, Intent __) {
	    				prn("really started discovery.");
	    			}
	    		}, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED), AndroidTwistedActivity.this);	    		
	    		
	    		Reactor.registerReceiver(new BroadcastReceiver() {
	    			public void onReceive(Context _, Intent __) {
	    				proceedAfterDiscovery();
	    			}
	    		}, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED), AndroidTwistedActivity.this);
	    		
		    	prn("Calling btAdapter.startDiscovery()!");
		    	btAdapter.startDiscovery();
			}
		};
		Reactor.startActivityForResult(discoverReq, discoverReqHandler, this);
	}
	
    private void proceedAfterDiscovery() {
    	prn("finished discovery, I am a " + (isServer ? "server" : "client") + ".");
    	if (isServer) {
    		prn("[Server] Start listening...");
    		startListening();
    	}
    	else {
    		BluetoothDevice dev = btDevices.get(0);
    		prn("[Client] Connecting to " + dev.getName() + "...");
    		connectServer();
    	}
    }

	private void startListening() {
    	// Initialized later.
    	final BluetoothServerSocket serverSock;
		try {
			BluetoothServerSocket tmp = null;
			tmp = btAdapter.listenUsingRfcommWithServiceRecord("serviceName", getUUID());
			serverSock = tmp;
			prn("[Server] socket created.");
		}
		catch (IOException e) {
			prn("[Server] caught IOException when start listening.");
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
    					prn("[Server] IOException when accepting...");
    					return;
    				}
    				
    				if (sockPair != null) {
    					// Delegate to another handler thread.
    					handleClient(sockPair);
    					sockPair = null;
    				}
    			}
    		}
    	};
    	clientAccepter.start();
	}
	
	protected void handleClient(BluetoothSocket sockPair) {
    	prn("[Server] handling connection from " + sockPair.toString());
    	try {
    		String strMsg = "[Server] Hello";
    		byte[] msg = strMsg.getBytes();
    		sockPair.getOutputStream().write(msg);
    		
    		byte[] buf = new byte[1024];
    		sockPair.getInputStream().read(buf);
    		prn("[Server] received msg from client: `" + new String(buf) + "'");
			sockPair.close();
    		prn("[Server] connection closed.");
		} catch (IOException e) {
			prn("[Server] error operating on " + sockPair.toString());
		}
	}

	private void connectServer() {
	    final BluetoothDevice serverDevice = btDevices.get(0);
	    final BluetoothSocket sock;
    	 
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
        	BluetoothSocket tmp = null;
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = serverDevice.createRfcommSocketToServiceRecord(getUUID());
            sock = tmp;
        } catch (IOException e) {
			prn("[Client] caught IOException when creating socket.");
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
    	        	prn("[Client] unable to connect");
    	            try {
    	                sock.close();
    	            	prn("[Client] connection closed.");
    	            } catch (IOException closeException) {
    	            	prn("[Client] unable to close huh?");
    	            }
    	            return;
    	        }
    	 
    	        // Do work to manage the connection (in a separate thread)
    	        chatWithServer(sock);
    	    }
    	};
    	
    	clientThread.start();
	}

	protected void chatWithServer(BluetoothSocket sock) {
    	byte[] buf = new byte[1024];
    	try {
    		sock.getInputStream().read(buf);
			sock.getOutputStream().write("[Client] Hai.".getBytes());
			prn("[Client] server said: `" + new String(buf) + "'");
			sock.close();
			prn("[Client] connection closed.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			prn("[Client] error sending/receiving data.");
		}
	}

	private void prn(String s) {
		Log.e("[dbg]", s);
	}
	
    private UUID getUUID() {
    	return new UUID(10L, 20L);
    }	
}
