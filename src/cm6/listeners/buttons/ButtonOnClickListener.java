package cm6.listeners.buttons;

import java.io.File;

import cm6.items.AI;
import cm6.main.ALActv;
import cm6.utils.CONS;
import cm6.utils.Methods;
import cm6.utils.Tags;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ButtonOnClickListener implements OnClickListener {
	/*----------------------------
	 * Fields
		----------------------------*/
	//
	Activity actv;

	//
	Vibrator vib;
	
	//
	int position;
	
	//
	ListView lv;
	
	//
	AI ai;
	
	public ButtonOnClickListener(Activity actv) {
		//
		this.actv = actv;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	public ButtonOnClickListener(Activity actv, int position) {
		//
		this.actv = actv;
		this.position = position;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
		
		
		
	}//public ButtonOnClickListener(Activity actv, int position)

	public ButtonOnClickListener(Activity actv, ListView lv) {
		// 
		this.actv = actv;
		this.lv = lv;
		
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

//	@Override
	public ButtonOnClickListener(Activity actv, AI ai) {
		
		this.actv = actv;
		
		this.ai = ai;
		
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
		
	}//public ButtonOnClickListener(Activity actv, AI ai)

	public void onClick(View v) {
//		//
		Tags.ButtonTags tag = (Tags.ButtonTags) v.getTag();
//
		vib.vibrate(Methods.vibLength_click);
		
		/*********************************
		 * 1. actv_play.xml
		 * 
		 * 2. main.xml
		 *********************************/
		//
		switch (tag) {
		/*********************************
		 * 1. actv_play.xml
		 *********************************/
		case actv_play_bt_play://----------------------------------------------------
			
			Methods.play_file(actv, ai);
			
			break;// case actv_play_bt_play
			
		case actv_play_bt_stop://----------------------------------------------------
			
			Methods.stop_player(actv);
			
//			if (PlayActv.mp != null && PlayActv.mp.isPlaying()) {
//
//				PlayActv.mp.stop();
//				
//				
//				
//			}//if (mp.isPlaying())

			
			break;// case actv_play_bt_stop
			
		case actv_play_bt_back://----------------------------------------------------
			
			actv.finish();
			
			break;// case actv_play_bt_back
		
		/*********************************
		 * 2. main.xml
		 *********************************/
		case ib_up://---------------------------------------------------------
			
			Methods.upDir(actv);
			
			break;

		case ailist_cb://------------------------------------------------------------------------------
			/*----------------------------
			 * Steps
			 * 1. If already checked, unlist from ThumbnailActivity.checkedPositions
			 * 2. If not yet, enlist into it
			 * 3. Then, notify to adapter
				----------------------------*/
			/*----------------------------
			 * 1. If already checked, unlist from ThumbnailActivity.checkedPositions
				----------------------------*/
			case_ailist_cb();
			
			/*----------------------------
			 * 3. Then, notify to adapter
				----------------------------*/
			ALActv.ail_adp_move.notifyDataSetChanged();
			
			break;// case ailist_cb

		case thumb_activity_ib_bottom: //----------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
//			int numOfGroups = TNActv.tiList.size() / lv.getChildCount();
//			
//			int indexOfLastChild = lv.getChildCount() * numOfGroups;
//			
//			lv.setSelection(indexOfLastChild);

			break;// case thumb_activity_ib_bottom 
			
		case thumb_activity_ib_top://--------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
			lv.setSelection(0);
			
			break;// thumb_activity_ib_top

		case image_activity_prev://----------------------------------------------------
			
			image_activity_prev();
			
			
			break;// case image_activity_prev
			
		case image_activity_next://----------------------------------------------------

			image_activity_next();
			
			break;// case image_activity_next

		case thumb_activity_ib_back://----------------------------------------------------

			thumb_activity_ib_back();
			
			break;// case thumb_activity_ib_back
			
		case ailist_cb_search://----------------------------------------------------
			
			case_ailist_cb_search();
			
			break;// case ailist_cb_search
			
		case actv_play_bt_see_bm://----------------------------------------------------
			
			case_actv_play_bt_see_bm();
		
			break;// case actv_play_bt_see_bm
			
		case actv_play_bt_add_bm://----------------------------------------------------
			
			case_actv_play_bt_add_bm();
			
			break;// case actv_play_bt_add_bm
		
		case actv_bm_bt_back://----------------------------------------------------
			
			case_actv_bm_bt_back();
			
			
			break;// case actv_bm_bt_back
			
		default:
			break;
		}//switch (tag)
		
	}//public void onClick(View v)

	private void case_actv_bm_bt_back() {
		// TODO Auto-generated method stub
		
		actv.setResult(CONS.Intent.RESULT_CODE_SEE_BOOKMARKS_CANCEL);
		
		actv.finish();
		
	}

	private void case_actv_play_bt_add_bm() {
		// TODO Auto-generated method stub
		/***************************************
		 * Is the media player playing?
		 ***************************************/
		if (PlayActv.mp == null) {
			
			// Log
			Log.d("ButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "PlayActv.mp == null");
			
			// debug
			Toast.makeText(actv, "MediaPlayer => null", Toast.LENGTH_LONG).show();
			
			return;
			
		} else if (!PlayActv.mp.isPlaying()) {//if (PlayActv.mp == null)
			
			// Log
			Log.d("ButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Player => Not playing");
			
			// debug
			Toast.makeText(actv, "Player => Not playing", Toast.LENGTH_LONG).show();
			
			return;
			
		} else {//if (PlayActv.mp == null)
			
			// Log
			Log.d("ButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Player => Playing");
			
		}//if (PlayActv.mp == null)
		
		/***************************************
		 * Get: Current position
		 ***************************************/
		int currentPosition = PlayActv.mp.getCurrentPosition();
		
		// Log
		Log.d("ButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "currentPosition=" + currentPosition);
		
		/***************************************
		 * Get: Table name and db id of the ai instance
		 ***************************************/
		String tableName = ai.getTable_name();
		long aiDbId = ai.getDb_id();
		
		// Log
		Log.d("ButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]",
				"tableName=" + tableName
				+ "/"
				+ "aiDbId=" + aiDbId);
		
		/***************************************
		 * Insert BM data into db
		 * 1. Build a BM instance
		 * 2. Insert data using the instance
		 ***************************************/
		/***************************************
		 * 1. Build a BM instance
		 ***************************************/
		BM bm = new BM.Builder()
					.setPosition(currentPosition)
					.setTitle(ai.getTitle())
					.setMemo(ai.getMemo())
					.setAiId(ai.getDb_id())
					.setAiTableName(ai.getTable_name())
					.build();
		
		// Log
		Log.d("ButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "ai.getTitle()=" + ai.getTitle());
		// Log
		Log.d("ButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "bm.getTitle()=" + bm.getTitle());
		
		/***************************************
		 * 2. Insert data using the instance
		 ***************************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		boolean res = dbu.insertData_bm(actv, bm);
		
		if (res == true) {
		
			// Log
			Log.d("ButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "res=" + res);
			
			// debug
			Toast.makeText(actv, "Bookmark inserted", Toast.LENGTH_LONG).show();
			
		} else {//if (res == true)

			// Log
			Log.d("ButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "res=" + res);

		}//if (res == true)
		
		
		
		//debug
//		debug_case_actv_play_bt_add_bm();
		
		
		
		
	}//private void case_actv_play_bt_add_bm()

	private void debug_case_actv_play_bt_add_bm() {
		// TODO Auto-generated method stub
		/***************************************
		 * Setup db
		 ***************************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		//
//		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/***************************************
		 * Create table
		 ***************************************/
//		boolean res = dbu.createTable(
//							CONS.DB.tname_BM,
//							CONS.DB.cols_bm,
//							CONS.DB.col_types_bm);

		/***************************************
		 * Add column: aiTableName
		 ***************************************/
		boolean res = Methods.add_column_to_table(
							actv,
							CONS.dbName,
							CONS.DB.tname_BM,
							"aiTableName",
							"TEXT");
		
		// Log
		Log.d("ButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "res=" + res);
		
	}//private void debug_case_actv_play_bt_add_bm()

	private void case_actv_play_bt_see_bm() {
		// TODO Auto-generated method stub

		/***************************************
		 * Validate: Any bookmarks?
		 ***************************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		long numOfBM = dbu.getNumOfEntries_BM(actv, CONS.DB.tname_BM, ai.getDb_id());
		
		if (numOfBM < 1) {
			
			// Log
			Log.d("ButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "numOfBM < 1");
			
			// debug
			Toast.makeText(actv, "No bookmarks", Toast.LENGTH_LONG).show();
			
			return;
			
		} else {//if (numOfBM == condition)
			
			// Log
			Log.d("ButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "numOfBM=" + numOfBM);
			
		}//if (numOfBM == condition)
		
		/***************************************
		 * Intent
		 ***************************************/
		Intent i = new Intent();
		
		i.setClass(actv, BMActv.class);
		
		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		
//		i.putExtra("ai_dbId", ai.getDb_id());
		i.putExtra(CONS.Intent.bmactv_key_ai_id, ai.getDb_id());
		
		i.putExtra(CONS.Intent.bmactv_key_table_name, ai.getTable_name());
		
//		actv.startActivity(i);
		actv.startActivityForResult(i, CONS.Intent.REQUEST_CODE_SEE_BOOKMARKS);

	}//private void case_actv_play_bt_see_bm()

	private void thumb_activity_ib_back() {
		/*********************************
		 * memo
		 *********************************/
		actv.finish();
		
	}//private void thumb_activity_ib_back()

	private void image_activity_next() {
//		/*********************************
//		 * 1. Get prefs => current position
//		 * 2. No more prev?
//		 * 
//		 * 3. Get the previous item in the ti list
//		 * 4. New image file path
//		 * 
//		 * 5. Show the previous image
//		 * 
//		 * 6. Set new pref value
//		 * 
//		 * 7. Update the file name label
//		 * 
//		 * 8. Save history
//		 * 
//		 *********************************/
//		SharedPreferences prefs = actv.getSharedPreferences(
//					MainActv.prefName_tnActv,
//					actv.MODE_PRIVATE);
//		
// 
//		//Methods.PrefenceLabels.thumb_actv.name()
//		
//		//int savedPosition = prefs.getInt("chosen_list_item", -1);
//		int savedPosition = prefs.getInt(
//							MainActv.prefName_tnActv_current_image_position,
//							-1);
//		
//		// Log
//		Log.d("ButtonOnClickListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "savedPosition=" + savedPosition);
//		
//		/*********************************
//		 * 2. No more prev?
//		 *********************************/
////		if (savedPosition == 0) {
//		if (savedPosition >= TNActv.tiList.size() - 1) {
//			
//			// debug
//			Toast.makeText(actv, "No next images", Toast.LENGTH_SHORT).show();
//			
//			return;
//			
//		}//if (savedPosition == 0)
//
//		/*********************************
//		 * 3. Get the previous item in the ti list
//		 *********************************/
//		TI ti_prev = TNActv.tiList.get(savedPosition + 1);
////		
////		
//		/*********************************
//		 * 4. New image file path
//		 *********************************/
//		String image_file_path_new = ti_prev.getFile_path();
//	
//		// Log
//		Log.d("ButtonOnClickListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "image_file_path_new=" + image_file_path_new);
////		
//		/*********************************
//		 * 5. Show the next image
//		 *********************************/
//		ImageActv.bm = BitmapFactory.decodeFile(image_file_path_new);
//		
//		ImageActv.LL.removeView(ImageActv.v);
//		
//		ImageActv.v.setImageBitmap(ImageActv.bm);
//		
//		ImageActv.LL.addView(ImageActv.v);
////		
//		// Log
//		Log.d("ButtonOnClickListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "New image added");
////		
//		/*********************************
//		 * 6. Set new pref value
//		 *********************************/
//		SharedPreferences.Editor editor = prefs.edit();
//		
//		editor.putInt(MainActv.prefName_tnActv_current_image_position, savedPosition + 1);
//		editor.commit();
//
//		// Log
//		Log.d("ButtonOnClickListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Prefs set: " + (savedPosition + 1));
//
//		/*********************************
//		 * 7. Update the file name label
//		 *********************************/
//		ImageActv.tv_file_name.setText(ti_prev.getFile_name());
//
//		/*********************************
//		 * 8. Save history
//		 *********************************/
//		boolean res = Methods.record_history(actv, ti_prev);
		
	}//private void image_activity_next()

	private void image_activity_prev() {
//		/*********************************
//		 * 1. Get prefs => current position
//		 * 2. No more prev?
//		 * 
//		 * 3. Get the previous item in the ti list
//		 * 4. New image file path
//		 * 
//		 * 5. Show the previous image
//		 * 
//		 * 6. Set new pref value
//		 * 
//		 * 7. Update the file name label
//		 * 
//		 * 8. Save history
//		 *********************************/
//		SharedPreferences prefs = actv.getSharedPreferences(
//					MainActv.prefName_tnActv,
//					actv.MODE_PRIVATE);
//		
// 
//		//Methods.PrefenceLabels.thumb_actv.name()
//		
//		//int savedPosition = prefs.getInt("chosen_list_item", -1);
//		int savedPosition = prefs.getInt(
//							MainActv.prefName_tnActv_current_image_position,
//							-1);
//		
//		// Log
//		Log.d("ButtonOnClickListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "savedPosition=" + savedPosition);
//		
//		/*********************************
//		 * 2. No more prev?
//		 *********************************/
//		if (savedPosition == 0) {
//			
//			// debug
//			Toast.makeText(actv, "No previous images", Toast.LENGTH_SHORT).show();
//			
//			return;
//			
//		}//if (savedPosition == 0)
//
//		/*********************************
//		 * 3. Get the previous item in the ti list
//		 *********************************/
//		TI ti_prev = TNActv.tiList.get(savedPosition - 1);
//		
//		
//		/*********************************
//		 * 4. New image file path
//		 *********************************/
//		String image_file_path_new = ti_prev.getFile_path();
//	
//		// Log
//		Log.d("ButtonOnClickListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "image_file_path_new=" + image_file_path_new);
//		
//		/*********************************
//		 * 5. Show the previous image
//		 *********************************/
//		ImageActv.bm = BitmapFactory.decodeFile(image_file_path_new);
//		
//		ImageActv.LL.removeView(ImageActv.v);
//		
//		ImageActv.v.setImageBitmap(ImageActv.bm);
//		
//		ImageActv.LL.addView(ImageActv.v);
//		
//		// Log
//		Log.d("ButtonOnClickListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "New image added");
//		
//		/*********************************
//		 * 6. Set new pref value
//		 *********************************/
//		SharedPreferences.Editor editor = prefs.edit();
//		
//		editor.putInt(MainActv.prefName_tnActv_current_image_position, savedPosition - 1);
//		editor.commit();
//
//		// Log
//		Log.d("ButtonOnClickListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Prefs set: " + (savedPosition - 1));
//		
//		/*********************************
//		 * 7. Update the file name label
//		 *********************************/
//		ImageActv.tv_file_name.setText(ti_prev.getFile_name());
//		
//		/*********************************
//		 * 8. Save history
//		 *********************************/
//		boolean res = Methods.record_history(actv, ti_prev);
		
	}//private void image_activity_prev()

	private void case_ailist_cb() {
		if (ALActv.checkedPositions.contains((int)position)) {

			ALActv.checkedPositions.remove((Integer) position);
			
			// Log
			Log.d("ButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "position removed => " + position);
			
//			ALActv.ail_adp_move.notifyDataSetChanged();

		} else {//if (TNActv.checkedPositions.contains((int)position))
			/*----------------------------
			 * 2. If not yet, enlist into it
				----------------------------*/
			
			ALActv.checkedPositions.add(position);
			
//			ALActv.ail_adp_move.notifyDataSetChanged();
			
			// Log
			String temp = "new position added => " + String.valueOf(position) +
					"(size=" + ALActv.checkedPositions.size() + ")" + "[";
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(temp);
			
			for (int i = 0; i < ALActv.checkedPositions.size(); i++) {
				
				sb.append(ALActv.checkedPositions.get(i) + ",");
				
			}//for (int i = 0; i < ALActv.checkedPositions.size(); i++)
			sb.append("]");
			
			
			Log.d("ButtonOnClickListener.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", sb.toString());
//							.getLineNumber() + "]", "new position added => " + String.valueOf(position) +
//							"(size=" + ALActv.checkedPositions.size() + ")" + "[" +);
			
			
		}//if (ALActv.checkedPositions.contains((int)position))
		
	}//private void case_ailist_cb()

	private void case_ailist_cb_search() {
		if (CONS.Search.checkedPositions.contains((int)position)) {

			CONS.Search.checkedPositions.remove((Integer) position);
			
			// Log
			Log.d("ButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "position removed => " + position);
			
//			CONS.Search.ail_adp_move.notifyDataSetChanged();

		} else {//if (TNActv.checkedPositions.contains((int)position))
			/*----------------------------
			 * 2. If not yet, enlist into it
				----------------------------*/
			
			CONS.Search.checkedPositions.add(position);
			
//			CONS.Search.ail_adp_move.notifyDataSetChanged();
			
			// Log
			String temp = "new position added => " + String.valueOf(position) +
					"(size=" + CONS.Search.checkedPositions.size() + ")" + "[";
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(temp);
			
			for (int i = 0; i < CONS.Search.checkedPositions.size(); i++) {
				
				sb.append(CONS.Search.checkedPositions.get(i) + ",");
				
			}//for (int i = 0; i < CONS.Search.checkedPositions.size(); i++)
			sb.append("]");
			
			
			Log.d("ButtonOnClickListener.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", sb.toString());
//							.getLineNumber() + "]", "new position added => " + String.valueOf(position) +
//							"(size=" + CONS.Search.checkedPositions.size() + ")" + "[" +);
			
			
		}//if (CONS.Search.checkedPositions.contains((int)position))
		
	}//private void case_ailist_cb_search()

}//public class ButtonOnClickListener implements OnClickListener

	