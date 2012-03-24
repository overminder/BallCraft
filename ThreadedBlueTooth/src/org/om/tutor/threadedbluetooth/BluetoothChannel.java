package org.om.tutor.threadedbluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;

public abstract class BluetoothChannel {
	enum TYPE {
		SERVER, CLIENT
	}
	
	private BluetoothSocket socket;
	private InputStream rfile;
	private OutputStream wfile;
	
	Thread messageReceiver = new Thread() {
		public void run() {
			while (true) {
				try {
					receiveOnce();
				} catch (IOException e) {
					onClose();
					break;
				}
			}
		}
		
		void receiveOnce() throws IOException {
			int messageLength = readInt();
			byte[] message = new byte[messageLength];
			int numBytesRead = 0;
			while (numBytesRead < messageLength) {
				numBytesRead += rfile.read(message, numBytesRead, messageLength - numBytesRead);
			}
			onMessage(message);
		}
		
		int readInt() throws IOException {
			int[] results = new int[4];
			for (int i = 0; i < 4; ++i) {
				int charGot = rfile.read();
				if (charGot == -1) {
					throw new IOException();
				}
				results[i] = charGot;
			}
			return results[0] | (results[1] << 8) |
					(results[2] << 16) | (results[3] << 24);
		}
	};
	
	public void initialize(BluetoothSocket socket) throws IOException {
		this.socket = socket;
		rfile = this.socket.getInputStream();
		wfile = this.socket.getOutputStream();
		
		messageReceiver.start();
	}
	
	void sendMessage(byte[] message) throws IOException {
		int len = message.length;
		byte[] encodedLength = {
			(byte) (len & 0xff),
			(byte) ((len >> 8) & 0xff),
			(byte) ((len >> 16) & 0xff),
			(byte) ((len >> 24) & 0xff)
		};
		wfile.write(encodedLength);
		wfile.write(message);
	}
	
	abstract TYPE getType();
	abstract void onOpen();
	abstract void onMessage(byte[] message);
	abstract void onError(Exception e);
	abstract void onClose();
}
