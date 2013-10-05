package cm6.adapters;

import java.util.ArrayList;
import java.util.List;

import cm5.items.AI;
import cm5.items.TI;
import cm5.listeners.CustomOnLongClickListener;
import cm5.listeners.button.ButtonOnClickListener;
import cm5.main.ALActv;
import cm5.main.MainActv;
import cm5.main.TNActv;

import cm5.main.R;
import cm5.utils.CONS;
import cm5.utils.Methods;
import cm5.utils.Tags;
import cm5.utils.CONS.MoveMode;

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
public class AILAdapter_move extends ArrayAdapter<AI> {

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
	public AILAdapter_move(Context con, int resourceId, List<AI> items) {
		// Super
		super(con, resourceId, items);

		// Context
		this.con = con;

		// Inflater
		inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		

	}//public AIListAdapter(Context con, int resourceId, List<AI> items)


	public AILAdapter_move(Context con, int resourceId, List<AI> items, 
											CONS.MoveMode moveMode) {
		// Super
		super(con, resourceId, items);

		// Context
		this.con = con;
		this.moveMode = moveMode;

		// Inflater
		inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		

	}//public AIListAdapter(Context con, int resourceId, List<AI> items, Methods.MoveMode moveMode)

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

    	/*********************************
		 * 1. Set layout
		 *********************************/
    	if (convertView != null) {

    		v = convertView;
    		
		} else {//if (convertView != null)

			v = inflater.inflate(R.layout.list_row_checked_box, null);
			
		}//if (convertView != null)
    	
    	/*********************************
		 * 2. Get view
		 *********************************/
    	TextView tv_file_name = (TextView) v.findViewById(R.id.list_row_checked_box_tv_file_name);
    	
    	/*********************************
		 * 3. Get item
		 *********************************/
    	AI ai = (AI) getItem(position);
    	
    	/*********************************
		 * 4. Set file name
		 *********************************/
    	if (ai != null && !ai.getFile_name().equals("")) {

    		tv_file_name.setText(ai.getFile_name());
    		
		} else {//if (ai != null && ai.getFile_name()
			
			tv_file_name.setText("No data");
			
		}//if (ai != null && ai.getFile_name()
		
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
		
//		cb.setTag(Tags.ButtonTags.ailist_cb);
		cb.setTag(Tags.ButtonTags.ailist_cb_search);
		
		if (CONS.Search.checkedPositions.contains((Integer) position)) {
			
			cb.setChecked(true);
			
		} else {//if (ThumbnailActivity.checkedPositions.contains((Integer) position)
			
			cb.setChecked(false);
			
		}//if (ThumbnailActivity.checkedPositions.contains((Integer) position)
		
		cb.setOnClickListener(new ButtonOnClickListener((Activity) con, position));

    	
    	/*********************************
		 * 9. Return view
		 *********************************/
		return v;
		
    }//public View getView(int position, View convertView, ViewGroup parent)

}//public class AILAdapter_move extends ArrayAdapter<AI>
