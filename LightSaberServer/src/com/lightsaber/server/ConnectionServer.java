package com.lightsaber.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;


/**
 * Created by Nivaldo on 12/06/13.
 */
public class ConnectionServer extends Thread
{
    protected static final int CONNECTION_MAX_UNSENT_COUNT   = 20;
	protected static final int CONNECTION_PORT               = 6969;
    protected static final int CONNECTION_TIMEOUT            = 15 * 60 * 1000; // 15 minutes 
    protected static final int CONNECTION_SEND_MESSAGE_DELAY = 40;

    private Socket               mClientSocket;
    private ServerSocket         mServerSocket;
    private ObjectOutputStream   mOutputStream;
    private OnConnectionListener mConnectionListener;
    private boolean              mIsConnected;
	private int                  mUnsentMessages;


    public ConnectionServer(OnConnectionListener l) throws IOException
    {
        mConnectionListener = l;
        mServerSocket = new ServerSocket(CONNECTION_PORT);
        mServerSocket.setSoTimeout(CONNECTION_TIMEOUT);
    }

    public void connect()
    {
    	mIsConnected = true;
        start();
    }

    @Override
    public void run() {
    	super.run();
    	try
    	{
    		mClientSocket = mServerSocket.accept();
            mOutputStream = new ObjectOutputStream(mClientSocket.getOutputStream());
		}
    	catch (Exception e)
    	{
    		mIsConnected = false;
    		Log.w(MainActivity.LOG_TAG, "Could not establish a connection", e);
    		mConnectionListener.onDisconnect();
    		return;
		}

        mConnectionListener.onConnect();
        mIsConnected = true;

        while (mIsConnected)
        {
            try
            {
                sendMessage();
                Thread.sleep(CONNECTION_SEND_MESSAGE_DELAY);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            if (mUnsentMessages > CONNECTION_MAX_UNSENT_COUNT)
            {
            	// something is wrong with the connection,
            	// disconnect
            	mIsConnected = false;
            	mConnectionListener.onDisconnect();
            }
        }
    }

    public void close() throws IOException
    {
    	mIsConnected = false;
        if (mClientSocket != null)
        {
            mClientSocket.close();
        }
        if (mServerSocket != null)
        {
            mServerSocket.close();
        }
    }

    private void sendMessage() throws IOException, IllegalStateException
    {
        if (mOutputStream == null) throw new IllegalStateException("Connection not established");

        mUnsentMessages++;
        mOutputStream.writeObject(mConnectionListener.getPayload());

        mUnsentMessages = 0;
        
    }

    public interface OnConnectionListener
    {
        public void onConnect();
        public void onDisconnect();

        public Object getPayload();
    }
}
