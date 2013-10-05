package cm6.listeners;

import cm5.main.PlayActv;
import cm5.services.Service_ShowProgress;
import cm5.utils.Methods;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.widget.Toast;

public class
MPOnCompletionListener implements OnCompletionListener {	// "MP" => MediaPlayer

	//
	Activity actv;
	
	public MPOnCompletionListener(Activity actv) {
		
		this.actv = actv;
		
	}
	
	//@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
//		Methods.stopPlayer(actv);
		/***************************************
		 * Stop: Player
		 ***************************************/
		PlayActv.mp.stop();
		
		/***************************************
		 * Stop: Service
		 ***************************************/
		Intent i = new Intent((Context) actv, Service_ShowProgress.class);

		//
//		i.putExtra("counter", timeLeft);

		// Log
		Log.d("DialogOnItemClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "Stopping service...");

		//
//		actv.startService(i);
		actv.stopService(i);

		
		// debug
		Toast.makeText(actv, "Complete", Toast.LENGTH_LONG).show();
		
	}//public void onCompletion(MediaPlayer mp)

}//MPOnCompletionListener implements OnCompletionListener {
