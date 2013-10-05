package cm6.adapters;

import java.util.ArrayList;
import java.util.List;

import cm5.items.AI;
import cm5.items.HI;
import cm5.items.TI;
import cm5.listeners.CustomOnLongClickListener;
import cm5.listeners.button.ButtonOnClickListener;
import cm5.main.ALActv;
import cm5.main.MainActv;
import cm5.main.TNActv;

import cm5.main.R;
import cm5.utils.CONS;
import cm5.utils.Methods;
import cm5.utils.CONS.MoveMode;
import cm5.utils.Tags;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

// Audio item list => AIL
public class HILAdapter extends ArrayAdapter<HI> {

	/****************************
	 * Class fields
		****************************/
	// Context
	Context con;

	// Inflater
	LayoutInflater inflater;

	//
	CONS.MoveMode moveMode = null;
//	Methods.MoveMode moveMode = Methods.MoveMode.OFF;

//	public static ArrayList<Integer> checkedPositions;
	
	/****************************
	 * Constructor
		****************************/
	//
	public HILAdapter(Context con, int resourceId, List<HI> items) {
		// Super
		super(con, resourceId, items);

		// Context
		this.con = con;

		// Inflater
		inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		

	}//public AIListAdapter(Context con, int resourceId, List<HI> items)


	public HILAdapter(Context con, int resourceId, List<HI> items, 
											CONS.MoveMode moveMode) {
		// Super
		super(con, resourceId, items);

		// Context
		this.con = con;
		this.moveMode = moveMode;

		// Inflater
		inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		

	}//public AIListAdapter(Context con, int resourceId, List<HI> items, Methods.MoveMode moveMode)

	/****************************
	 * Methods
		****************************/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	/****************************
		 * 0. View
		 * 
		 * 1. Set layout
		 * 2. Get view
		 * 
		 * 3. Get item
		 * 
		 * 4. Set file name
		 * 
		 * 5. Set title
		 * 
		 * 9. Return view
			****************************/
    	/****************************
		 * 0. View
			****************************/
    	View v = null;

//    	if (CONS.move_mode == false) {

    		v = move_mode_off(v, position, convertView);
    		
//    	} else {
//
//			v = move_mode_on(v, position, convertView);
//			
//    	}//if (moveMode == null || moveMode == Methods.MoveMode.OFF)

    	/*********************************
		 * 9. Return view
		 *********************************/
		return v;
		
    }//public View getView(int position, View convertView, ViewGroup parent)


	private View move_mode_on(View v, int position, View convertView) {
		
    	/*********************************
		 * 1. Set layout
		 *********************************/
    	if (convertView != null) {

    		v = convertView;
    		
		} else {//if (convertView != null)

			v = inflater.inflate(R.layout.list_row_checked_box, null);
			
		}//if (convertView != null)

    	/*********************************
		 * 3. Get item
		 *********************************/
    	HI hi = (HI) getItem(position);
    	
    	/***************************************
		 * Get: AI instance
		 ***************************************/
    	AI ai = Methods.get_data_ai((Activity) con, hi.getAiId(), hi.getAiTableName());
    	
    	/*********************************
		 * 2. Get view
		 *********************************/
    	TextView tv_file_name = (TextView) v.findViewById(R.id.list_row_checked_box_tv_file_name);
    	
    	/*********************************
		 * 4. Set file name
		 *********************************/
    	if (ai != null && !ai.getFile_name().equals("")) {

    		tv_file_name.setText(ai.getFile_name());
    		
		} else {//if (ai != null && ai.getFile_name()
			
			tv_file_name.setText("No data");
			
		}//if (ai != null && ai.getFile_name()
		
    	
    	/*********************************
		 * Checked position
		 *********************************/
		if (ALActv.checkedPositions.contains((Integer) position)) {
			
			tv_file_name.setBackgroundColor(Color.BLUE);
			
		} else {//if (ThumbnailActivity.move_mode == true)
				
			tv_file_name.setBackgroundColor(Color.BLACK);
				
		}
    	
    	/*********************************
		 * 5. Set title
		 *********************************/
    	TextView tv_title = (TextView) v.findViewById(R.id.list_row_checked_box_tv_title);
    	
    	if (ai != null && !ai.getTitle().equals("")) {

    		tv_title.setText(ai.getTitle());
    		
		} else {//if (ai != null && ai.getFile_name()
			
//			tv.setText("No data");
			tv_title.setText("");
			
		}//if (ai != null && ai.getFile_name()
    	
    	/*********************************
		 * Check box
		 *********************************/
		CheckBox cb = (CheckBox) v.findViewById(R.id.list_row_checked_box_cb);
		
		cb.setTag(Tags.ButtonTags.ailist_cb);
		
		if (ALActv.checkedPositions.contains((Integer) position)) {
			
			cb.setChecked(true);
			
		} else {//if (ThumbnailActivity.checkedPositions.contains((Integer) position)
			
			cb.setChecked(false);
			
		}//if (ThumbnailActivity.checkedPositions.contains((Integer) position)
		
		cb.setOnClickListener(new ButtonOnClickListener((Activity) con, position));
		
		
//		cb.setOnLongClickListener(
//					new CustomOnLongClickListener(
//									(Activity) con, position, Methods.ItemTags.tilist_checkbox));

    	
    	/*********************************
		 * 9. Return view
		 *********************************/
		return v;
		
	}//private View move_mode_on(View v, int position, View convertView)


	private View move_mode_off(View v, int position, View convertView) {
		
    	if (convertView != null) {
    		
			v = convertView;
			
		} else {//if (convertView != null)

			v = inflater.inflate(R.layout.list_row_ai_list, null);
			
		}//if (convertView != null)


    	/*********************************
		 * 2. Get view
		 *********************************/
    	TextView tv_file_name = (TextView) v.findViewById(R.id.list_row_ai_list_tv_file_name);
    	
    	/*********************************
		 * 3. Get item
		 *********************************/
    	HI hi = (HI) getItem(position);
    	
    	/***************************************
		 * Get: AI instance
		 ***************************************/
    	AI ai = Methods.get_data_ai((Activity) con, hi.getAiId(), hi.getAiTableName());
    	
    	/*********************************
		 * Current position
		 *********************************/
    	int pref_currentPosition = 
    			Methods.get_pref(
    					(Activity)con,
    					CONS.History.pname_HistActv,
    					CONS.History.pkey_HistActv_position,
    					-1);
    	
    	/*********************************
		 * 4. Set file name
		 *********************************/
    	if (ai != null && !ai.getFile_name().equals("")) {

    		tv_file_name.setText(ai.getFile_name());
    		
    		if (position == pref_currentPosition) {
				
    			tv_file_name.setBackgroundColor(Color.BLUE);
    			tv_file_name.setTextColor(Color.WHITE);
    			
			} else {//if (position == )
				
				tv_file_name.setBackgroundColor(Color.WHITE);
				tv_file_name.setTextColor(Color.BLACK);
	
			}//if (position == )
    		
		} else {//if (ai != null && ai.getFile_name()
			
			tv_file_name.setText("No data");
			
		}//if (ai != null && ai.getFile_name()
		
    	/*********************************
		 * 5. Set title
		 *********************************/
    	TextView tv_title = (TextView) v.findViewById(R.id.list_row_ai_list_tv_title);
    	
    	if (ai != null && !ai.getTitle().equals("")) {

    		tv_title.setText(ai.getTitle());
    		
		} else {//if (ai != null && ai.getFile_name()
			
//			tv.setText("No data");
			tv_title.setText("");
			
		}//if (ai != null && ai.getFile_name()

    	/*********************************
		 * Set length
		 *********************************/
    	TextView tv_length = (TextView) v.findViewById(R.id.list_row_ai_list_tv_file_length);
    	
    	tv_length.setText(
    			Methods.convert_intSec2Digits_lessThanHour((int) (ai.getLength() / 1000)));
    	
		return v;
		
	}//private View move_mode_off(View v, int position, View convertView)

}//public class AILAdapter extends ArrayAdapter<HI>
