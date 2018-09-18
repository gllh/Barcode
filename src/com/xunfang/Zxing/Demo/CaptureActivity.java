package com.xunfang.Zxing.Demo;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.xunfang.Zxing.Demo.camera.CameraManager;
import com.xunfang.Zxing.Demo.decoding.CaptureActivityHandler;
import com.xunfang.Zxing.Demo.decoding.InactivityTimer;
import com.xunfang.Zxing.Demo.view.ViewfinderView;
import com.xunfang.Zxing.db.BarcodeSqliteHelper;

public class CaptureActivity extends Activity implements Callback,OnClickListener {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private TextView title;
	private ImageView revert;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//���ô��ڲ���
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        //�Զ��崰����ʽ
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.common_title);
		revert = (ImageView)findViewById(R.id.common_title_revert);
		title = (TextView)findViewById(R.id.common_title_title);
		title.setText(R.string.app_name);
		revert.setOnClickListener(this);
		//��ʼ�� CameraManager
		CameraManager.init(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		//txtResult = (TextView) findViewById(R.id.txtResult);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		// ���ڴ������ݿ��ͳ�ʼ�����ݵ�¼��
		BarcodeSqliteHelper barcodeSqliteHelper = new BarcodeSqliteHelper(CaptureActivity.this);
		SQLiteDatabase db = null;
		try {
			// ���SQLiteDatabaseʵ��
			db = barcodeSqliteHelper.getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
			// ���SQLiteDatabaseʵ��
			db = barcodeSqliteHelper.getReadableDatabase();
		} finally {
			if (db != null)
				db.close();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		//viewfinderView.drawResultBitmap(barcode);
		playBeepSoundAndVibrate();
		//txtResult.setText(obj.getBarcodeFormat().toString() + ":"+obj.getText());
		Intent intent = new Intent(CaptureActivity.this,ResultActivity.class);
		intent.putExtra("barcode_type", obj.getBarcodeFormat().toString());
		intent.putExtra("barcode_content", obj.getText());
		startActivity(intent);
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view instanceof ImageView || view instanceof Button){
			switch (view.getId()) {
			case R.id.common_title_revert:
				AlertDialog.Builder revert_dialog = new AlertDialog.Builder(this);
				revert_dialog.setIcon(android.R.drawable.ic_menu_help);
				revert_dialog.setTitle("ȷ���˳���");
				revert_dialog.setPositiveButton("ȷ��", new OnClickLiner_OK());
				revert_dialog.setNegativeButton("ȡ��", new OnClickLiner_Cancel());
				revert_dialog.show();
				break;
			default:
				break;
			}
		}
	}
	/**
	 * alertDialog�����¼�
	 * 
	 * @author Administrator
	 * 
	 */
	class OnClickLiner_OK implements
			android.content.DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			CaptureActivity.this.finish();
		}

	}

	/**
	 * alertDialog�����¼�
	 * 
	 * @author Administrator
	 * 
	 */
	class OnClickLiner_Cancel implements
			android.content.DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.cancel();
		}
	}
	/**
	 * �������ؼ�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// �жϷ��ؼ��Ƿ񱻰���
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(0);
			ImageView iv = new ImageView(this);
			iv.setImageResource(android.R.drawable.ic_menu_help);
			TextView text = new TextView(this);
			text.setTextSize(20);
			text.setText("ȷ���˳���");
			layout.addView(iv);
			layout.addView(text);
			dialog.setCustomTitle(layout);
			dialog.setPositiveButton("ȷ��", new OnClickLiner_OK());
			dialog.setNegativeButton("ȡ��", new OnClickLiner_Cancel());
			dialog.show();
		}
		return false;
	}
	/**
	 * ����menu��ʱ�Ļص�����
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// Ϊ�˵������ѡ��
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, getText(R.string.history)).setIcon(
				android.R.drawable.ic_menu_edit);
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, getText(R.string.quit)).setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	/**
	 * �˵���ѡ�ѡ��ʱ�Ļص�����
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case Menu.FIRST + 1:// �鿴��ʷ��¼
			Intent historyIntent = new Intent(CaptureActivity.this,HistoryActivity.class);
			startActivity(historyIntent);
			return true;
		case Menu.FIRST + 2:// �˳�
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setIcon(android.R.drawable.ic_menu_help);
			dialog.setTitle("ȷ���˳���");
			dialog.setPositiveButton("ȷ��", new OnClickLiner_OK());
			dialog.setNegativeButton("ȡ��", new OnClickLiner_Cancel());
			dialog.show();
			return true;
		}
		return false;
	}
}