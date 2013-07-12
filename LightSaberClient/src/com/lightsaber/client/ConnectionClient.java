package com.lightsaber.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import tang.helper.struct.Heading;

import com.lightsaber.RotationVector;


public class ConnectionClient {

	public static final int CONNECTION_PORT = 6969;

	private Socket 			  		   mSocket;
	private ObjectInputStream 		   mInputStream;
	private boolean 				   mRunning;
	private OnConnectionChangeListener mListener;

	public Heading rotation;

	public ConnectionClient(String ip) throws IOException {
		mSocket = new Socket(ip, CONNECTION_PORT);
		rotation = new Heading();
	}

	public boolean isConnected() {
		return mSocket.isConnected();
	}

	public void connect() {
		try {
			InputStream stream = mSocket.getInputStream();
			mInputStream = new ObjectInputStream(stream);
			if (mListener != null) {
				mListener.onConnect();
			}

			new Thread() {
				public void run() {
					mRunning = true;
					while (mRunning) {
						try {
							receiveMessage();
						} catch (IOException e) {
							e.printStackTrace();
							mRunning = false;
						}
					}
					if (mListener != null) {
						mListener.onDisconnect();
					}
					try {
						mInputStream.close();
						mSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				};
			}.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		mRunning = false;
		if (mSocket != null) {
			try {
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void receiveMessage() throws IOException {
		try {
			RotationVector readObject = (RotationVector) mInputStream.readObject();
			rotation.set(readObject.pitch, readObject.yaw);
			rotation.roll =  readObject.roll;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void setOnConnectionChangeListener(OnConnectionChangeListener l) {
		this.mListener = l;
	}
}
