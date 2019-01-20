package com.example.a1.mykeyboard;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;


import java.util.Map;

public class SimpleIME extends InputMethodService
	implements OnKeyboardActionListener {
	public final static String TAG = "SimpleIME";

	private KeyboardView kv;
	private Keyboard keyboard;
	
	private boolean caps = false;

	@Override
	public void onCreate() {
		super.onCreate();






	}

	@Override
	public View onCreateInputView() {
		Log.i(TAG,"onCreateInputView");

		kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
		keyboard = new Keyboard(this, R.xml.qwerty);
		kv.setKeyboard(keyboard);
		kv.setOnKeyboardActionListener(this);
		kv.invalidateAllKeys();
		return kv;
	}
	@Override
	public void onStartInputView(EditorInfo info, boolean restarting) {
		super.onStartInputView(info, restarting);
		Toast.makeText(SimpleIME.this,"packageName:"+info.packageName,Toast.LENGTH_SHORT).show();
		Toast.makeText(SimpleIME.this,"inputType:"+String.valueOf(info.inputType),Toast.LENGTH_SHORT).show();
		convertInputType(info.inputType);
		Log.i(TAG,"onStartInputView");
	}

	@Override
	public void onStartInput(EditorInfo attribute, boolean restarting) {
		super.onStartInput(attribute, restarting);
		Log.i(TAG,"onStartInput");
	}

	@Override
	public void onFinishInputView(boolean finishingInput) {
		super.onFinishInputView(finishingInput);
		Log.i(TAG,"onFinishInputView");
	}

	@Override
	public void onFinishInput() {
		super.onFinishInput();
		Log.i(TAG,"onFinishInput");
	}

	@Override
	public void onWindowShown() {
		super.onWindowShown();
		Log.i(TAG,"onWindowShown");

	}

	@Override
	public void onWindowHidden() {
		super.onWindowHidden();
		updateFullscreenMode();
		Log.i(TAG,"onWindowHidden");
	}

	@Override
	public void updateFullscreenMode() {
		super.updateFullscreenMode();
	}

	@Override
	public boolean onEvaluateFullscreenMode() {
		Log.i(TAG,"onEvaluateFullscreenMode");
		boolean onEvaluateFullscreenMode =super.onEvaluateFullscreenMode();
		return onEvaluateFullscreenMode;
	}

	@Override
	public void onKey(int primaryCode, int[] keyCodes) {
		InputConnection ic = getCurrentInputConnection();
		playClick(primaryCode);
		switch(primaryCode){
		case Keyboard.KEYCODE_DELETE :
			ic.deleteSurroundingText(1, 0);
			break;
		case Keyboard.KEYCODE_SHIFT:
			caps = !caps;
			keyboard.setShifted(caps);
			kv.invalidateAllKeys();
			break;
		case Keyboard.KEYCODE_DONE:
			ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
			break;
		default:
			char code = (char)primaryCode;
			if(Character.isLetter(code) && caps){
				code = Character.toUpperCase(code);
			}
			String beforeCursor = getCurrentInputConnection().getTextBeforeCursor(1024,0).toString();
			Log.i("zzz","beforeCursor:"+beforeCursor);
			ic.commitText(String.valueOf(code),1);
//			ic.setComposingRegion(0,5);
//			ic.setComposingText(beforeCursor + String.valueOf(code) , 1);


		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Toast.makeText(SimpleIME.this,"local："+String.valueOf(newConfig.locale),Toast.LENGTH_LONG).show();
	}

	private void playClick(int keyCode){
		AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
		switch(keyCode){
		case 32:
			am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
			break;
		case Keyboard.KEYCODE_DONE:
		case 10:
			am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
			break;
		case Keyboard.KEYCODE_DELETE:
			am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
			break;
		default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
		}
	}

	@Override
	public void onPress(int primaryCode) {
	}

	@Override
	public void onRelease(int primaryCode) {
	}

	@Override
	public void onText(CharSequence text) {
	}

	@Override
	public void swipeDown() {
	}

	@Override
	public void swipeLeft() {
	}

	@Override
	public void swipeRight() {
	}

	@Override
	public void swipeUp() {
	}

	@Override
	public View onCreateCandidatesView() {

		return super.onCreateCandidatesView();
	}

	@Override
	public void showWindow(boolean showInput) {
		Log.i(TAG,"showWindow");
		super.showWindow(showInput);
	}



	public static final int TYPE_OTHER = 0x00000000;
	public static final int TYPE_TEXT = 0x00000000;
	public static final int TYPE_NUMBER = 0x00000001;
	public static final int TYPE_PHONE = 0x00000002;
	public static final int TYPE_URI = 0x00000004;
	public static final int TYPE_EMAIL = 0x00000008;
	public static final int TYPE_DATE = 0x00000010;
	public static final int TYPE_PASSWORD = 0x00000020;
	public static final int TYPE_MATH = 0x00000040;

	public int convertInputType(int inputType) {
		int ret = TYPE_OTHER;
		int typeClass = inputType & EditorInfo.TYPE_MASK_CLASS;
		if (typeClass == EditorInfo.TYPE_CLASS_PHONE){
			ret = TYPE_PHONE;
		} else if (typeClass == EditorInfo.TYPE_CLASS_NUMBER){
			ret = TYPE_NUMBER;
		} else if (typeClass == EditorInfo.TYPE_CLASS_DATETIME){
			ret = TYPE_DATE;
		} else {
			int variation = inputType & EditorInfo.TYPE_MASK_VARIATION;
			if (variation == EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
					|| (Build.VERSION.SDK_INT >= 11 /* Build.VERSION_CODES.HONEYCOMB */
					&& variation == 208  /* EditorInfo.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS */)){
				ret = TYPE_EMAIL;
			} else if (variation == EditorInfo.TYPE_TEXT_VARIATION_URI){
				ret = TYPE_URI;
			} else if (variation == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
					|| variation == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
					|| (Build.VERSION.SDK_INT >= 11 /* Build.VERSION_CODES.HONEYCOMB */
					&& variation == 224 /* EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD */)){
				ret = TYPE_PASSWORD;

				}
			}

			switch (ret) {
				case TYPE_TEXT:
					Toast.makeText(SimpleIME.this,"convertInputType: TYPE_TEXT",Toast.LENGTH_SHORT).show();
					break;
				case TYPE_NUMBER:
					Toast.makeText(SimpleIME.this,"convertInputType: TYPE_NUMBER",Toast.LENGTH_SHORT).show();
					break;
				case TYPE_PHONE:
					Toast.makeText(SimpleIME.this,"convertInputType: TYPE_PHONE",Toast.LENGTH_SHORT).show();
					break;
				case TYPE_URI:
					Toast.makeText(SimpleIME.this,"convertInputType: TYPE_URI",Toast.LENGTH_SHORT).show();
					break;
				case TYPE_EMAIL:
					Toast.makeText(SimpleIME.this,"convertInputType: TYPE_EMAIL",Toast.LENGTH_SHORT).show();
					break;
				case TYPE_DATE:
					Toast.makeText(SimpleIME.this,"convertInputType: TYPE_DATE",Toast.LENGTH_SHORT).show();
					break;
				case TYPE_PASSWORD:
					Toast.makeText(SimpleIME.this,"convertInputType: TYPE_PASSWORD",Toast.LENGTH_SHORT).show();
					break;
				case TYPE_MATH:
					Toast.makeText(SimpleIME.this,"convertInputType: TYPE_MATH",Toast.LENGTH_SHORT).show();
					break;
			}
		// always return type_text for there doesn't exist special surface.
		return ret;
	}

}
