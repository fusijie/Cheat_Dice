package com.jacky.cheatdice;

import java.util.Random;

import com.jacky.cheatdice.ShakeListener.OnShakeListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.ImageView;

public class CheatDice extends Activity {

	private ShakeListener mShakeListener = null;
	private Vibrator mVibrator;
	private ImageView diceImageView;
	private ImageView tusijiImageView;
	private AnimationDrawable animationDrawable=null;
	private MediaPlayer mediaPlayer=null;

	private int diceResult = 0;
	private int X, Y1, Y2 = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cheat_dice);
		diceImageView = (ImageView) findViewById(R.id.imageView_Dice);
		tusijiImageView=(ImageView) findViewById(R.id.imageView_Tusiji);
		
		//���������ļ�
		mediaPlayer=MediaPlayer.create(this, R.raw.shake_sound_male);
		//mediaPlayer.setLooping(true);//����ѭ������
		
		//�ָ���Ļ
		divideDisplayMetrics();

		//��������
		mVibrator = (Vibrator) getApplication().getSystemService(
				VIBRATOR_SERVICE);
		mShakeListener = new ShakeListener(this);
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			@Override
			public void onShake() {
				// TODO Auto-generated method stub
				// ��ͣshake����
				mShakeListener.stop();
				// ��ʼ����
				startAnimation();
				// ��ʼ����
				startMusicPlay();
				// ��ʼ��
				startVibrator();
				// 2������
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						// ֹͣ����
						stopAnimation();
						// ��ʾ��ǰ���
						displayResult();
						// ֹͣ����
						stopMusicPlay();
						// �ر���
						mVibrator.cancel();
						// ���¼���shake
						mShakeListener.start();
					}
				}, 2000);
			}
		});
	}

	// ��ȡ��Ļ�ֱ���,ͬʱ����Ļ�ֳ�6��
	private void divideDisplayMetrics() {
		Display curDisplay = getWindowManager().getDefaultDisplay();
		Y1 = curDisplay.getHeight() / 3;
		Y2 = Y1 * 2;
		X = curDisplay.getWidth() / 2;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		// return super.onTouchEvent(event);
		if (MotionEvent.ACTION_DOWN == event.getAction()) {
			int x_Touch, y_Touch;
			x_Touch = (int) event.getX();
			y_Touch = (int) event.getY();
			if (x_Touch >= 0 && x_Touch <= X && y_Touch >= 0 && y_Touch <= Y1) {
				diceResult = 1;
			} else if (x_Touch >= 0 && x_Touch <= X && y_Touch >= Y1
					&& y_Touch <= Y2) {
				diceResult = 2;
			} else if (x_Touch >= 0 && x_Touch <= X && y_Touch >= Y2) {
				diceResult = 3;
			} else if (x_Touch >= X && y_Touch >= 0 && y_Touch <= Y1) {
				diceResult = 4;
			} else if (x_Touch >= X && y_Touch >= Y1 && y_Touch <= Y2) {
				diceResult = 5;
			} else if (x_Touch >= X && y_Touch >= Y2) {
				diceResult = 6;
			} else {
				diceResult = 0;
			}
		}
		return true;
	}

	private void startAnimation() {
		//��dice_shake��������ImageView
		diceImageView.setImageResource(R.drawable.blank);
		diceImageView.setBackgroundResource(R.drawable.dice_shake_animation);
		animationDrawable=(AnimationDrawable) diceImageView.getBackground();
		animationDrawable.start();
	}
	
	private void stopAnimation() {
		if(animationDrawable!=null)
		{
			animationDrawable.stop();
			animationDrawable=null;
		}
	}
	
	private void startMusicPlay() {
		try {
			if(mediaPlayer!=null)
				mediaPlayer.stop();
			mediaPlayer.prepare();
			mediaPlayer.start();
			//Toast.makeText(getApplicationContext(), "Music Playing...", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	private void stopMusicPlay() {
		if (mediaPlayer!=null) {
			mediaPlayer.stop();
			//Toast.makeText(getApplicationContext(), "Music Stoped...", Toast.LENGTH_SHORT).show();
		}
	}

	private void startVibrator() {
		mVibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1); // ��һ�����������ǽ������飬
																	// �ڶ����������ظ�������-1Ϊ���ظ�����-1��pattern��ָ���±꿪ʼ�ظ�
	}

	private void displayResult() {
		switch (diceResult) {
		case 1:
			diceImageView.setImageResource(R.drawable.dice_1);
			tusijiImageView.setImageResource(R.drawable.tusiiji_1);
			break;
		case 2:
			diceImageView.setImageResource(R.drawable.dice_2);
			tusijiImageView.setImageResource(R.drawable.tusiiji_2);
			break;
		case 3:
			diceImageView.setImageResource(R.drawable.dice_3);
			tusijiImageView.setImageResource(R.drawable.tusiiji_3);
			break;
		case 4:
			diceImageView.setImageResource(R.drawable.dice_4);
			tusijiImageView.setImageResource(R.drawable.tusiiji_4);
			break;
		case 5:
			diceImageView.setImageResource(R.drawable.dice_5);
			tusijiImageView.setImageResource(R.drawable.tusiiji_5);
			break;
		case 6:
			diceImageView.setImageResource(R.drawable.dice_6);
			tusijiImageView.setImageResource(R.drawable.tusiiji_6);
			break;
		default:
			Random random = new Random();
			int t_random = random.nextInt(6);
			diceImageView.setImageResource(R.drawable.dice_1 + t_random);
			tusijiImageView.setImageResource(R.drawable.tusiiji_1+t_random);
			break;
		}

		// ����Ϊ0���Ա������һ���ж�
		diceResult = 0;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mShakeListener != null)
			mShakeListener.stop();
		if (mediaPlayer!=null)
			mediaPlayer.release();
		super.onDestroy();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.cheat_dice, menu);
	// return true;
	// }

}
