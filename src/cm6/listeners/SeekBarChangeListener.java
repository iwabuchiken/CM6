package cm6.listeners;

import cm6.main.PlayActv;
import cm6.main.R;
import cm6.utils.CONS;
import cm6.utils.Methods;
import android.app.Activity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SeekBarChangeListener implements OnSeekBarChangeListener {

	Activity actv;
	SeekBar sb;
	
	public SeekBarChangeListener(Activity actv) {
		// TODO Auto-generated constructor stub
		this.actv = actv;
	}

	public SeekBarChangeListener(Activity actv, SeekBar sb) {
		// TODO Auto-generated constructor stub
		this.actv = actv;
		this.sb = sb;
	}

	public void
	onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		
//		// Log
//		Log.d("SeekBarChangeListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ ":"
//				+ Thread.currentThread().getStackTrace()[2].getMethodName()
//				+ "]", "progress=" + progress);
//		
//		// Log
//		Log.d("SeekBarChangeListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ ":"
//				+ Thread.currentThread().getStackTrace()[2].getMethodName()
//				+ "]", "fromUser=" + fromUser);
		
//		// Log
//		Log.d("SeekBarChangeListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ ":"
//				+ Thread.currentThread().getStackTrace()[2].getMethodName()
//				+ "]",
//				"PlayActv.ai.getLength()="
//				+ Methods.convert_intSec2Digits_lessThanHour((int)PlayActv.ai.getLength() / 1000));
		
//		int seekedPosition = (int) ((float)progress / sb.getMax() * (float)PlayActv.ai.getLength());
		int seekedPosition = (int) ((float)progress * 1.000f / sb.getMax() * (float)PlayActv.ai.getLength());
		
//		// Log
//		Log.d("SeekBarChangeListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ ":"
//				+ Thread.currentThread().getStackTrace()[2].getMethodName()
////				+ "]", "(float)progress / sb.getMax() = " + ((float)progress / sb.getMax()));
//				+ "]", "(double)progress / sb.getMax() = " + String.format("%.5f", ((double)progress / sb.getMax())));
//		
//		// Log
//		Log.d("SeekBarChangeListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ ":"
//				+ Thread.currentThread().getStackTrace()[2].getMethodName()
//				+ "]",
//				"seekedPosition=" + seekedPosition
//				+ "("
//				+ Methods.convert_intSec2Digits_lessThanHour(seekedPosition / 1000)
//				+ ")");

		/***************************************
		 * Set: Current position to the view
		 ***************************************/
		if (CONS.PlayActv.tvCurrentPosition == null) {

			CONS.PlayActv.tvCurrentPosition = (TextView) actv.findViewById(R.id.actv_play_tv_current_position);
			
		}//if (CONS.PlayActv.tvCurrentPosition == null)
//		CONS.PlayActv.tvCurrentPosition = (TextView) this.findViewById(R.id.actv_play_tv_current_position);
		
		
		CONS.PlayActv.tvCurrentPosition.setText(Methods.convert_intSec2Digits_lessThanHour(seekedPosition / 1000));

		
	}//onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)

	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		// Log
		Log.d("SeekBarChangeListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "onStartTrackingTouch()");
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
//		// Log
//		Log.d("SeekBarChangeListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ ":"
//				+ Thread.currentThread().getStackTrace()[2].getMethodName()
//				+ "]", "onStopTrackingTouch()");
		
		// Log
		Log.d("SeekBarChangeListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]",
				"seekBar.getProgress()=" + seekBar.getProgress());

		/***************************************
		 * Set: Current position to preference
		 ***************************************/
		long seekedPosition =
				(long) ((float) seekBar.getProgress() * 1.000f / sb.getMax()
								* (float)PlayActv.ai.getLength());
		

		boolean res = 
				Methods.setPref_long(
						actv,
						CONS.Pref.pname_PlayActv,
						CONS.Pref.pkey_PlayActv_position,
						seekedPosition);
		// Log
		Log.d("PlayActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "Position => Stored in a preference");
		
		
	}//public void onStopTrackingTouch(SeekBar seekBar)

}//public class SeekBarChangeListener implements OnSeekBarChangeListener
