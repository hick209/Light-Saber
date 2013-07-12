package com.lightsaber.client;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import tang.helper.obj.Model;
import tang.helper.obj.OBJLoader;
import tang.helper.struct.Heading;
import tang.helper.struct.Vector3;


public class Window implements OnConnectionChangeListener {

	private static final String WINDOW_TITLE          = "Light Saber!";
	private static final String MODEL_FILE_LIGHTSABER = "res/LightSaber.obj";
	private static final String MODEL_FILE_ANDROID    = "res/Android.obj";
	private static final String MODEL_FILE_CUBE       = "res/Cube.obj";

	public static final int WINDOW_WIDTH  = 800;
	public static final int WINDOW_HEIGHT = 600;

	public static void main(String[] args) {
		Window window = null;
		try {
			window = new Window();
			window.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Model mLightSaber;
	private Model mAndroid;
	private Model mCube;

	private ConnectionClient mConnection;
	private boolean          mIsConnecting;

	private Vector3        mSaberPosition = new Vector3(WINDOW_WIDTH/2, WINDOW_HEIGHT/5, 0);
	private Heading        mSaberRotation = new Heading();


	public Window() throws LWJGLException, IOException {
		mLightSaber = OBJLoader.loadModel(new File(MODEL_FILE_LIGHTSABER));
		mAndroid = OBJLoader.loadModel(new File(MODEL_FILE_ANDROID));
		mCube = OBJLoader.loadModel(new File(MODEL_FILE_CUBE));

		Display.setDisplayMode(new DisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT));
		Display.setTitle(WINDOW_TITLE);
		Display.create();

		init();
	}


	private void establishConnection() {
		mIsConnecting = true;
		// XXX An AWFUL way of acquire the IP address from the Android
		new Thread("IP Address Input") {
			public void run() {
				String input = JOptionPane.showInputDialog("Enter your IP address", "0.0.0.0");
				try {
					mConnection = new ConnectionClient(input);
					mConnection.setOnConnectionChangeListener(Window.this);
					mConnection.connect();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					mIsConnecting = false;
				}
			}
		}.start();
	}

	public void init() {
		// init OpenGL here
		glClearColor(0.2f, 0.2f, 0.3f, 1f);

		glMatrixMode(GL11.GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WINDOW_WIDTH, 0, WINDOW_HEIGHT, -1000, 1000);

		glMatrixMode(GL11.GL_MODELVIEW);
		glHint(GL_POLYGON_SMOOTH_HINT|GL_LINE_SMOOTH_HINT, GL_FASTEST);
		glEnable(GL_DEPTH_TEST);
	}

	public void run() {
		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			if (mConnection != null && mConnection.isConnected()) {
				// render OpenGL here
				render();
			} else {
				if (mIsConnecting) {
					try {
						synchronized (this) {
							wait(1000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					establishConnection();
				}
			}
		}

		if (mConnection != null && mConnection.isConnected()) {
			mConnection.disconnect();
		}
		close();
	}

	public void render() {
		glMatrixMode(GL_MODELVIEW);

		// Clear The Screen And The Depth Buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// set the transformations
		glLoadIdentity();
		if (mSaberRotation != null) {
			switch (2) {
			case 0:
				mCube.draw(mSaberPosition, mSaberRotation);
				break;
			case 1:
				mAndroid.draw(mSaberPosition, mSaberRotation);
				break;
			case 2:
				mLightSaber.draw(mSaberPosition, mSaberRotation);
				break;
			}
		}

		Display.update();
	}

	public void close() {
		Display.destroy();
	}


	@Override
	public void onConnect() {
		mSaberRotation = mConnection.rotation;
	}

	@Override
	public void onDisconnect() {
		establishConnection();
	}
}
