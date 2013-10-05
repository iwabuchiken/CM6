package cm6.listeners.buttons;

import cm6.utils.Methods;
import cm6.utils.Tags;
import cm6.main.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class ButtonOnTouchListener implements OnTouchListener {

	/*----------------------------
	 * Fields
		----------------------------*/
	//
	Activity actv;

	//
	Vibrator vib;
	
	public ButtonOnTouchListener(Activity actv) {
		//
		this.actv = actv;
		
		vib = (Vibrator) actv.getSystemService(Context.VIBRATOR_SERVICE);
	}

//	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		Tags.ButtonTags tag = (Tags.ButtonTags) v.getTag();
		
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			
			switch (tag) {
			
			case ib_up://----------------------------------------------------
				
				ImageButton ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_up_disenabled);
				
//				v.setBackgroundColor(Color.GRAY);
				
				break;// case ib_up
				
			case thumb_activity_ib_bottom://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_thumb_bottom_50x50_disenabled);
						
				break;// case thumb_activity_ib_bottom

			case thumb_activity_ib_top://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_thumb_top_50x50_disenabled);
				
				break;// case thumb_activity_ib_top

			case image_activity_prev://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_back_disenabled);
				
				break;// case image_activity_prev

			case image_activity_next://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_forward_disenabled);
				
				break;// case image_activity_next

			case actv_play_bt_play:
			case actv_play_bt_stop:
			case actv_play_bt_back:
			case actv_play_bt_see_bm:
			case actv_play_bt_add_bm:
				
				v.setBackgroundColor(Color.GRAY);
				break;
				
			case actv_play_tv_title:
			case actv_play_tv_memo:
			case actv_bm_bt_back:
				
				case_bcBlack_tcWhite(v);
//				v.setBackgroundColor(Color.BLACK);
//
//				((TextView)v).setTextColor(Color.WHITE);
				
				break;

			}//switch (tag)
			
			break;//case MotionEvent.ACTION_DOWN:

			
		case MotionEvent.ACTION_UP:
			switch (tag) {
			case ib_up://----------------------------------------------------
				
				ImageButton ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_up);
				
//				v.setBackgroundColor(Color.WHITE);
				
				break;// case ib_up
				
			case thumb_activity_ib_bottom://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_thumb_bottom_50x50);

				break;// case thumb_activity_ib_bottom
				
			case thumb_activity_ib_top://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_thumb_top_50x50);
				
				break;// case thumb_activity_ib_top

			case image_activity_prev://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_back);
				
				break;// case image_activity_prev

			case image_activity_next://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_forward);
				
				break;// case image_activity_next

			case actv_play_bt_play:
			case actv_play_bt_stop:
			case actv_play_bt_back:
			case actv_play_bt_see_bm:
			case actv_play_bt_add_bm:
				
				v.setBackgroundColor(Color.WHITE);
				break;

			case actv_play_tv_title:
			case actv_play_tv_memo:
			case actv_bm_bt_back:
				
				case_bcWhite_tcBlack(v);
//				v.setBackgroundColor(Color.WHITE);
//				
//				TextView tv = (TextView) v;
//				
//				tv.setTextColor(Color.BLACK);
//				tv.setTextColor(Color.BLUE);
				
				break;

			}//switch (tag)
			
			break;//case MotionEvent.ACTION_UP:
		}//switch (event.getActionMasked())
		return false;
	}

	private void case_bcWhite_tcBlack(View v) {
		// TODO Auto-generated method stub
		v.setBackgroundColor(Color.WHITE);
		
		TextView tv = (TextView) v;
		
		tv.setTextColor(Color.BLACK);
		
	}

	private void case_bcBlack_tcWhite(View v) {
		// TODO Auto-generated method stub
		v.setBackgroundColor(Color.BLACK);

		((TextView)v).setTextColor(Color.WHITE);

	}

}
