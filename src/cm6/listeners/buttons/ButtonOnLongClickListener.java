package cm6.listeners.buttons;

import cm6.items.AI;
import cm6.utils.Methods;
import cm6.utils.Methods_dlg;
import cm6.utils.Tags;
import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;

public class ButtonOnLongClickListener implements OnLongClickListener {

	Activity actv;
	
	Vibrator vib;

	AI ai;
	
	public ButtonOnLongClickListener(Activity actv, AI ai) {
		
		this.actv = actv;
		
		this.ai = ai;
		
		vib = (Vibrator) actv.getSystemService(Context.VIBRATOR_SERVICE);
		
	}//public ButtonOnLongClickListener(Activity actv)

	public boolean onLongClick(View v) {
		/*********************************
		 * 0. Vibrate
		 * 1. Get tag
		 *********************************/
		vib.vibrate(Methods.vibLength_click);
		
		Tags.ButtonTags tag = (Tags.ButtonTags) v.getTag();
		
		// Log
		Log.d("ButtonOnLongClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "tag.name()=" + tag.name());
		
		switch (tag) {
		
		case actv_play_tv_title://---------------------------------
			
			Methods_dlg.dlg_edit_title(actv, ai);
			
			break;// case actv_play_tv_title
			
		case actv_play_tv_memo://---------------------------------
			
			Methods_dlg.dlg_edit_memo(actv, ai);
			
			break;// case actv_play_tv_title
			
			
		default:
			break;
		
		}//switch (tag)
		
		return false;
	}//public boolean onLongClick(View v)

}//public class ButtonOnLongClickListener implements OnLongClickListener
