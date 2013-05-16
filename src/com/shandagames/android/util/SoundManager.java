/**
 * 
 */
package com.shandagames.android.util;

import java.util.HashMap;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * @file SoundManager.java
 * @create 2012-9-13 下午2:42:32
 * @author lilong
 * @description 音频管理
		    SoundManager.getInstance();
		    SoundManager.initSounds(this);
		    SoundManager.addSound();
 */
public class SoundManager {

	private static SoundManager _instance;
	private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundPoolMap;
	private AudioManager mAudioManager;
	private Context mContext;

	private SoundManager() {
	}

	/**
	 * Requests the instance of the Sound Manager and creates it if it does not exist.
	 * 
	 * @return Returns the single instance of the SoundManager
	 */
	static synchronized public SoundManager getInstance() {
		if (_instance == null)
			_instance = new SoundManager();
		return _instance;
	}

	/**
	 * Initialises the storage for the sounds
	 * 
	 * @param theContext The Application context
	 */
	public void initSounds(Context theContext) {
		mContext = theContext;
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
	}

	/**
	 * Add a new Sound to the SoundPool
	 * 
	 * @param Index - The Sound Index for Retrieval
	 * @param SoundID - The Android ID for the Sound asset.
	 */
	public void addSound(int Index, int SoundID) {
		mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
	}

	/**
	 * Plays a Sound
	 * 
	 * @param index - The Index of the Sound to be played
	 * @param speed - The Speed to play not, not currently used but included for
	 *                compatibility
	 */
	public void playSound(int index, float speed) {
		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, speed);
	}

	/**
	 * Stop a Sound
	 * 
	 * @param index  - index of the sound to be stopped
	 */
	public void stopSound(int index) {
		mSoundPool.stop(mSoundPoolMap.get(index));
	}

	/**
	 * Deallocates the resources and Instance of SoundManager
	 */
	public void cleanup() {
		mSoundPool.release();
		mSoundPool = null;
		mSoundPoolMap.clear();
		mAudioManager.unloadSoundEffects();
		_instance = null;
	}

}
