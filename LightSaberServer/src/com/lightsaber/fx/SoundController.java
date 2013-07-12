package com.lightsaber.fx;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.lightsaber.OnSaberAction;
import com.lightsaber.server.R;


public class SoundController extends BroadcastReceiver implements OnSaberAction
{
	private static final int MAX_STREAMS = 5;

	private static final int LOOP_PLAY_REPEATLY = -1;
	private static final int LOOP_PLAY_ONCE     = 0;

	private static final int SOUND_SABER_ON    = 0;
	private static final int SOUND_SABER_OFF   = 1;
	private static final int SOUND_SABER_STILL = 2;
	private static final int SOUND_SABER_MOVE  = 3;
	private static final int SOUND_SABER_HIT   = 4;

	private static SoundController sInstance = null;
	public static SoundController getInstance(Context c)
	{
		if (sInstance == null) sInstance = new SoundController(c);
		return sInstance;
	}


	private SoundPool mSoundPool;
	private Context   mContext;
	private boolean   mReady   = false;
	private boolean   mOpen    = false;
	private boolean   mHitting = false;
	private boolean   mMoving  = false;

	private SparseIntArray mLoadedSounds = new SparseIntArray();
	private AudioManager   mAudioManager;
	private ComponentName  mComponentName;
	private float          mStreamVolume;


	private SoundController(Context context)
	{
		this.mContext = context;
		this.mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mComponentName = new ComponentName(mContext, getClass());
		mAudioManager.registerMediaButtonEventReceiver(mComponentName);

		setupVolume();

		new Thread() {
			public void run()
			{
				mLoadedSounds.put(SOUND_SABER_ON, mSoundPool.load(mContext, R.raw.saber_on, 1));
				mLoadedSounds.put(SOUND_SABER_STILL, mSoundPool.load(mContext, R.raw.saber_still, 1));
				mLoadedSounds.put(SOUND_SABER_MOVE, mSoundPool.load(mContext, R.raw.saber_move, 1));
				mLoadedSounds.put(SOUND_SABER_HIT, mSoundPool.load(mContext, R.raw.saber_hit, 1));
				mLoadedSounds.put(SOUND_SABER_OFF, mSoundPool.load(mContext, R.raw.saber_off, 1));

				mReady = true;
			}
		}.start();
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		setupVolume();
	}

	private void setupVolume()
	{
		mStreamVolume = (float) mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}

	private void play(int sound, int priority, int loop)
	{
		mSoundPool.play(mLoadedSounds.get(sound), mStreamVolume, mStreamVolume, priority, loop, 1);
	}

	public void release()
	{
		mSoundPool.release();
		mAudioManager.unregisterMediaButtonEventReceiver(mComponentName);
		sInstance = null;
	}
	

	@Override
	public void onSaberOpen()
	{
		if (mReady && !mOpen)
		{
			play(SOUND_SABER_ON, 10, LOOP_PLAY_ONCE);
			play(SOUND_SABER_STILL, 7, LOOP_PLAY_REPEATLY);
			mOpen = true;
		}
	}

	@Override
	public void onSaberClose()
	{
		if (mReady && mOpen)
		{
			mSoundPool.stop(SOUND_SABER_STILL);
			play(SOUND_SABER_OFF, 10, LOOP_PLAY_ONCE);
			mOpen = false;
		}
	}

	@Override
	public void onSaberHit()
	{
		if (mReady && mOpen && !mHitting)
		{
			mHitting = true;
			play(SOUND_SABER_HIT, 3, LOOP_PLAY_ONCE);
			new Timer(300, new Runnable() {
				@Override
				public void run()
				{
					mHitting = false;
				}
			});
		}
	}

	@Override
	public void onSaberMove()
	{
		if (mReady && mOpen && !mMoving)
		{
			mMoving = true;
			play(SOUND_SABER_MOVE, 0, LOOP_PLAY_ONCE);
			new Timer(150, new Runnable() {
				@Override
				public void run()
				{
					mMoving = false;
				}
			});
		}
	}
	
	class Timer extends Thread
	{
		private int mTime;
		private Runnable mAction;
		public Timer(int time, Runnable action)
		{
			this.mTime = time;
			this.mAction = action;
			start();
		}
		@Override
		public void run()
		{
			try
			{
				Thread.sleep(mTime);
			}
			catch (InterruptedException e) {}

			mAction.run();
		}
	}
}
