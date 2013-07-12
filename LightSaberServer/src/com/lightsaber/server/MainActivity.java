package com.lightsaber.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.lightsaber.OnSaberAction;
import com.lightsaber.RotationVector;
import com.lightsaber.fx.SoundController;


public class MainActivity extends Activity implements ConnectionServer.OnConnectionListener, SensorEventListener
{
	private static final int SABER_THRESHOLD_MOVE = 2;
	private static final int SABER_THRESHOLD_HIT  = 10;

	public static final  String LOG_TAG = "Light Saber";

	private static final int VIEW_DISCONNECTED = 0;
	private static final int VIEW_CONNECTING   = 1;
	private static final int VIEW_CONNECTED    = 2;

	private Handler          mHandler;
	private ViewAnimator     mViewAnimator;

	private SensorManager    mSensorManager;
	private ConnectionServer mConnectionServer;

	private RotationVector   mRotationVector;
	private float[]			 mGravity     = new float[3];
	private float[]			 mGeomagnetic = new float[3];
	private float[] 		 mOrientation = new float[3];

	private OnSaberAction mSaber;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mViewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		mHandler = new Handler();

		mSaber = SoundController.getInstance(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);

		((TextView) findViewById(R.id.ipAddress)).setText(getLocalIpAddress());
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		mSaber.onSaberClose();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (mConnectionServer != null)
		{
			try
			{
				mConnectionServer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		SoundController.getInstance(this).release();
	}

	private String getLocalIpAddress()
	{
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); )
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); )
				{
					final InetAddress inetAddress = enumIpAddr.nextElement();
					String address = inetAddress.getHostAddress();
					if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(address))
					{
						return address;
					}
				}
			}
		}
		catch (Exception e)
		{
			Log.e(LOG_TAG, e.toString());
		}
		return null;
	}

	public void attemptConnection(View v)
	{
		mViewAnimator.setDisplayedChild(VIEW_CONNECTING);
		try
		{
			mConnectionServer = new ConnectionServer(this);
			mConnectionServer.connect();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onConnect()
	{
		mHandler.post(new Runnable() {
			@Override
			public void run()
			{
				mViewAnimator.setDisplayedChild(VIEW_CONNECTED);
			}
		});
		mSaber.onSaberOpen();
	}

	@Override
	public void onDisconnect()
	{
		mHandler.post(new Runnable() {
			@Override
			public void run()
			{
				mViewAnimator.setDisplayedChild(VIEW_DISCONNECTED);
			}
		});

		if (mConnectionServer != null)
		{
			try
			{
				mConnectionServer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		mSaber.onSaberClose();
	}

	@Override
	public Object getPayload()
	{
		return mRotationVector;
	}


	@Override
	public void onSensorChanged(SensorEvent event)
	{
		// Smoothing the sensor data a bit
		// using a low pass filter
		final float alpha1 = 0.95f;
		final float alpha2 = 1-alpha1;

		float [] vector;
		switch (event.sensor.getType())
		{
		case Sensor.TYPE_ACCELEROMETER:
			vector = mGravity;
			break;

		case Sensor.TYPE_MAGNETIC_FIELD:
			vector = mGeomagnetic;
			break;

		default:
			return;
		}

		for (int i = 0; i < 3; i++)
		{
			vector[i] = vector[i]*alpha1 + event.values[i]*alpha2;
//			vector[i] = event.values[i];
		}

		float[] rotationMatrix = new float[16];
		float[] inclinationMatrix = new float[16];
		SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, mGravity, mGeomagnetic);
		SensorManager.remapCoordinateSystem(
				rotationMatrix,
				SensorManager.AXIS_X,
				SensorManager.AXIS_Z,
				rotationMatrix
		);

		SensorManager.getOrientation(rotationMatrix, mOrientation);
        float inclination = SensorManager.getInclination(inclinationMatrix);

        final float rad2deg = (float)(180.0f/Math.PI);
        float yaw   =  mOrientation[0]*rad2deg;
		float pitch = -mOrientation[1]*rad2deg;
		float roll  =  mOrientation[2]*rad2deg;
		inclination =  inclination*rad2deg;

		if (mRotationVector != null)
		{
			float movement = mRotationVector.inclination - inclination;
			if (movement > SABER_THRESHOLD_HIT)
			{
//				System.out.println("Hit! "+movement);
				mSaber.onSaberHit();
			}
			else if (movement > SABER_THRESHOLD_MOVE)
			{
//				System.out.println("Moved! "+movement);
				mSaber.onSaberMove();
			}
		}
        mRotationVector = new RotationVector();
		mRotationVector.setValues(pitch, yaw, roll);
		mRotationVector.inclination = inclination;

//        if (mCount++ > 50) {
//            mCount = 0;
//			Log.d("Compass", "yaw: " + (int)yaw +
//                    "  pitch: " + (int)pitch +
//                    "  roll: " + (int)roll +
//                    "  incl: " + (int)inclination
//                    );
//        }
	}
//	private int mCount;


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}