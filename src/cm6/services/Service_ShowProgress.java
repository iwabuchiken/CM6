package cm6.services;

import java.util.Timer;
import java.util.TimerTask;

import cm6.main.PlayActv;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Service_ShowProgress extends Service {

	Timer timer;

	int counter;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		// Log
		Log.d("Service_ShowProgress.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "onBind()");
		
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		// Log
		Log.d("Service_ShowProgress.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "onCreate()");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		// Log
		Log.d("Service_ShowProgress.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "onDestroy()");

		/***************************************
		 * Cancel counting
		 ***************************************/
		timer.cancel();
		
		// Log
		Log.d("Service_ShowProgress.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "Timer => Cancelled");
		
	}//public void onDestroy()

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		// Log
		Log.d("Service_ShowProgress.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "onStart()");
		
		startCount();

	}//public void onStart(Intent intent, int startId)

	private void startCount() {
		
		final android.os.Handler handler = new android.os.Handler();
		
		counter = 0;
		
		//
		if (timer != null) {
			timer.cancel();
		}//if (timer != null)
		
		timer = new Timer();

		timer.schedule(
				new TimerTask(){

					@Override
					public void run() {
						

						if (PlayActv.mp != null) {
							
							handler.post(new Runnable(){

//								@Override
								public void run() {
									/***************************************
									 * Update: Progress label
									 ***************************************/
//									PlayActv.updateProgressLabel(actv);
									new PlayActv().updateProgressLabel();
									
									/***************************************
									 * Update: Seekbar
									 ***************************************/
									if (PlayActv.mp != null
//											&& !PlayActv.sb.isInTouchMode()) {
											&& !PlayActv.sb.isPressed()) {
										
										// Log
										Log.d("Service_ShowProgress.java"
												+ "["
												+ Thread.currentThread()
														.getStackTrace()[2]
														.getLineNumber()
												+ ":"
												+ Thread.currentThread()
														.getStackTrace()[2]
														.getMethodName() + "]",
												"PlayActv.mp != null && " +
//												"!PlayActv.sb.isInTouchMode()");
												"!PlayActv.sb.isPressed()");
										
										int currentPosition = PlayActv.mp.getCurrentPosition();
										long length = PlayActv.ai.getLength();
										
										int seekPositon = (int)
//													((currentPosition / length)
													(((float)currentPosition / length)
															* PlayActv.sb.getMax());
//										
										PlayActv.sb.setProgress(seekPositon);
										
									} else {//if (PlayActv.mp == null)
										
										// Log
										Log.d("Service_ShowProgress.java"
												+ "["
												+ Thread.currentThread()
														.getStackTrace()[2]
														.getLineNumber()
												+ ":"
												+ Thread.currentThread()
														.getStackTrace()[2]
														.getMethodName() + "]",
												"NO");
										
									}//if (PlayActv.mp == null)
									
//									//debug
//									if (PlayActv.mp != null) {
//										
//										// Log
//										Log.d("Service_ShowProgress.java"
//												+ "["
//												+ Thread.currentThread()
//														.getStackTrace()[2]
//														.getLineNumber()
//												+ ":"
//												+ Thread.currentThread()
//														.getStackTrace()[2]
//														.getMethodName() + "]",
//												"PlayActv.mp != null");
//										
//									} else {//if (PlayActv.mp != null)
//										
//										// Log
//										Log.d("Service_ShowProgress.java"
//												+ "["
//												+ Thread.currentThread()
//														.getStackTrace()[2]
//														.getLineNumber()
//												+ ":"
//												+ Thread.currentThread()
//														.getStackTrace()[2]
//														.getMethodName() + "]",
//												"PlayActv.mp == null");
//										
//									}//if (PlayActv.mp != null)
//									
									
//									//debug
//									if (!PlayActv.sb.isInTouchMode()) {
//										
//										// Log
//										Log.d("Service_ShowProgress.java"
//												+ "["
//												+ Thread.currentThread()
//														.getStackTrace()[2]
//														.getLineNumber()
//												+ ":"
//												+ Thread.currentThread()
//														.getStackTrace()[2]
//														.getMethodName() + "]",
//												"!PlayActv.sb.isInTouchMode()");
//										
//									} else {//if (!PlayActv.sb.isInTouchMode())
//
//										// Log
//										Log.d("Service_ShowProgress.java"
//												+ "["
//												+ Thread.currentThread()
//														.getStackTrace()[2]
//														.getLineNumber()
//												+ ":"
//												+ Thread.currentThread()
//														.getStackTrace()[2]
//														.getMethodName() + "]",
//												"PlayActv.sb.isInTouchMode()");
//
//									}//if (!PlayActv.sb.isInTouchMode())
									
//									if (!PlayActv.sb.isFocused()) {	//=> true when touching with the finger
//									if (!PlayActv.sb.isPressed()) {	//=> When touching with the finger, "isPressed()" gets true
//										
//										// Log
//										Log.d("Service_ShowProgress.java"
//												+ "["
//												+ Thread.currentThread()
//														.getStackTrace()[2]
//														.getLineNumber()
//												+ ":"
//												+ Thread.currentThread()
//														.getStackTrace()[2]
//														.getMethodName() + "]",
//												"!PlayActv.sb.isPressed()");
//										
//									} else {//if (!PlayActv.sb.isPressed())
//
//										// Log
//										Log.d("Service_ShowProgress.java"
//												+ "["
//												+ Thread.currentThread()
//														.getStackTrace()[2]
//														.getLineNumber()
//												+ ":"
//												+ Thread.currentThread()
//														.getStackTrace()[2]
//														.getMethodName() + "]",
//												"PlayActv.sb.isPressed()");
//
//									}//if (!PlayActv.sb.isFocused())
									
									
								}//public void run() // Runnable
								
							});//handler.post()
							
							
						}//if (CONS.P == condition)
						
						counter += 1;
						
					}//public void run()
					
				},//new TimerTask()
				0,
				1000
		);//timer.schedule
		
	}//private void startCount()
	

}//public class Service_ShowProgress extends Service
