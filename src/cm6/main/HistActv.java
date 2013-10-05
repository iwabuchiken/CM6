/*********************************
 * ALACtv.java (Audio list activity)
 * 
 *********************************/
package cm6.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cm6.adapters.AILAdapter;
import cm6.adapters.AILAdapter_move;
import cm6.adapters.HILAdapter;
import cm6.adapters.TIListAdapter;
import cm6.items.AI;
import cm6.items.HI;
import cm6.items.TI;
import cm6.listeners.CustomOnItemLongClickListener;
import cm6.listeners.buttons.ButtonOnClickListener;
import cm6.listeners.buttons.ButtonOnTouchListener;
import cm6.listeners.dialogues.DialogListener;
import cm6.tasks.Task_UpdateFileLength;
import cm6.utils.CONS;
import cm6.utils.DBUtils;
import cm6.utils.Methods;
import cm6.utils.Methods_CM5;
import cm6.utils.Methods_dlg;
import cm6.utils.Tags;

import cm6.main.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class HistActv extends ListActivity {

	public static Vibrator vib;

	public static List<TI> tiList;

//	public static TIListAdapter aAdapter;
//	public static TIListAdapter bAdapter;

//	public static boolean move_mode = false;

	/*********************************
	 * Special intent data
	 *********************************/
	public static long[] long_searchedItems; //=> Used in initial_setup()
	
	public static long[] history_file_ids = null;
	
	public static String[] history_table_names = null;
	
	public static String[] string_searchedItems_table_names = null;
	
	/*********************************
	 * List-related
	 *********************************/
	public static ArrayList<Integer> checkedPositions;

	public static List<String> fileNameList;
	
	public static List<AI> aiList;
	
	public static List<AI> ai_list_move;

	public static AILAdapter ail_adp;
	
	public static AILAdapter ail_adp_move;
	
//	public static AILAdapter_move ail_adp_move;
	
	public static ArrayAdapter<String> dirListAdapter;
	
	/****************************
	 * Preference names
		****************************/
	public static String tnactv_selected_item = "tnactv_selected_item";

	/*********************************
	 * Views
	 *********************************/
	public static ListView lv_main;
	
	/****************************************
	 * Methods
	 ****************************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		/****************************
		 * Steps
		 * 1. Super
		 * 2. Set content
		 * 3. Basics
		 * 
		 * 4. Set up
		 * 5. Initialize vars
		****************************/
		super.onCreate(savedInstanceState);

		setContentView(R.layout.actv_hist);
		
		/****************************
		 * 3. Basics
			****************************/
		this.setTitle(this.getClass().getName());
		
		/****************************
		 * 5. Initialize vars
			****************************/
		checkedPositions = new ArrayList<Integer>();

		/*********************************
		 * Current position => Initialize
		 *********************************/
		boolean res = 
				Methods.set_pref(
							this,
							CONS.pname_mainActv,
							CONS.pkey_current_image_position,
							-1);
		
		if (res == true) {
			// Log
			Log.d("HistActv.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]",
					"Pref set: " + CONS.pkey_current_image_position);
			
		} else {//if (result == true)
			// Log
			Log.d("HistActv.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]",
					"Set pref => Failed: " + CONS.pkey_current_image_position);
			
		}//if (result == true)
		
	}//public void onCreate(Bundle savedInstanceState)


	private void B16_v_1_0() {
		
		Display disp = this.getWindowManager().getDefaultDisplay();
		
//		// Log
//		Log.d("HistActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "width=" + disp.getWidth());
		
	}//private void B16_v_1_0()


	private void get_tables_from_db() {
		
		DBUtils dbu = new DBUtils(this, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		// Log
		Log.d("HistActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "rdb.getPath(): " + rdb.getPath());
		
		
		// REF=> http://stackoverflow.com/questions/82875/how-do-i-list-the-tables-in-a-sqlite-database-file
		String sql = "SELECT * FROM sqlite_master WHERE type='table'";
		
		Cursor c = rdb.rawQuery(sql, null);
		
		startManagingCursor(c);
		
		// Log
		Log.d("HistActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Tables: c.getCount()" + c.getCount());
		
		c.moveToFirst();
		
		for (int i = 0; i < c.getCount(); i++) {
			
			// Log
			Log.d("HistActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "name: " + c.getString(1));
			
			c.moveToNext();
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		rdb.close();
		
	}//private void get_tables_from_db()

	private void get_data_from_table_AAA() {
		
		DBUtils dbu = new DBUtils(this, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		String sql = "SELECT * FROM AAA";
		
		Cursor c = rdb.rawQuery(sql, null);
		
		startManagingCursor(c);
		
		// Log
		Log.d("HistActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "AAA: c.getCount()" + c.getCount());
		
		rdb.close();
		
	}//private void get_data_from_table_AAA()


	private void setup_2_setList() {
		/*********************************
		 * 1. Get table name
		 * 
		 * 2. Prep list
		 * 
		 * 3. Sort list
		 * 
		 * 4. Prep adapter
		 * 
		 * 5. Set adapter
		 *********************************/
		/*********************************
		 * 1. Get table name
		 *********************************/
//		String table_name = Methods.convert_path_into_table_name(this);
		
//		// Log
//		Log.d("HistActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "table_name=" + table_name);

		/*********************************
		 * 2. Prep list
		 *********************************/
//		aiList = Methods.get_all_data_ai(this, table_name);
		
		CONS.History.hiList = Methods_CM5.getAllData_HI(this);
		
//		if (CONS.History.hiList == null || CONS.History.hiList.size() < 1) {
		if (CONS.History.hiList == null) {
			
			// debug
			Toast.makeText(this,
					"Can't create a table, or Query failed", Toast.LENGTH_LONG).show();
			
			finish();
			
			return;
			
		}//if (CONS.History.hiList == null)

		if (CONS.History.hiList.size() < 1) {
//		if (CONS.History.hiList == null) {
			
			// debug
			Toast.makeText(this,
					"No history data", Toast.LENGTH_LONG).show();
			
			finish();
			
			return;
			
		}//if (CONS.History.hiList.size() < 1)
		
		/*********************************
		 * 3. Sort list
		 *********************************/
//		Methods.sort_list_ai_created_at(aiList, CONS.SORT_ORDER.DEC);
		Methods_CM5.sortList_HI_createdAt(CONS.History.hiList, CONS.SORT_ORDER.CREATED_AT);

		/*********************************
		 * 4. Prep adapter
		 *********************************/
		CONS.History.adpHIList = new HILAdapter(
				this,
				R.layout.list_row_ai_list,
//				R.layout.actv_al,
				CONS.History.hiList
				);
		
		/*********************************
		 * 5. Set adapter
		 *********************************/
		this.setListAdapter(CONS.History.adpHIList);
		
	}//private void setup_2_set_list()


	private void debug_B20_v_1_0(List<AI> ai_list) {
		
		Methods.sort_list_ai_created_at(ai_list, CONS.SORT_ORDER.DEC);
		
		AI ai = ai_list.get(0);
//		AI ai = ai_list.get(ai_list.size() - 1);
		
		MediaPlayer mp = new MediaPlayer();
		
		String file_full_path = StringUtils.join(
				new String[]{ai.getFile_path(), ai.getFile_name()},
				File.separator);
		
		// File path
		// Log
		Log.d("HistActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "file_full_path=" + file_full_path);
		
		// MediaPlayer
		try {
			mp.setDataSource(file_full_path);
			
			mp.prepare();
			
			int length = mp.getDuration();
			
			if (length < 0) {
				
				// Log
				Log.d("HistActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "length < 0");
				
			} else {//if (length < 0)
				
				// Log
				Log.d("HistActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
//					"Duration=" + Methods.convert_intSec2Digits(mp.getDuration())
					"Duration=" + Methods.convert_intSec2Digits(mp.getDuration() / 1000)
					+ "(" + mp.getDuration() + ")");
				
			}//if (length < 0)
			
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			
	}//private void debug_B20_v_1_0(List<AI> ai_list)


	private List<TI> prep_list() {
		/****************************
		 * Get ThumbnailItem list
		 * 1. Get intent data
		 * 2. Build tiList
			****************************/
		/****************************
		 * 1. Get intent data
			****************************/
		Intent i = this.getIntent();
		
//		long_searchedItems = i.getLongArrayExtra("long_searchedItems");
//		
//		history_file_ids = i.getLongArrayExtra(MainActv.intent_label_file_ids);
//		
//		history_table_names = i.getStringArrayExtra(MainActv.intent_label_table_names);
//		
//		string_searchedItems_table_names = 
//					i.getStringArrayExtra(MainActv.intent_label_searchedItems_table_names);
		
		/****************************
		 * 2. Build tiList
			****************************/
		String tableName = Methods.convert_path_into_table_name(this);
		
		// Log
		Log.d("HistActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "tableName: " + tableName);
		
		
		if (long_searchedItems != null) {

			// Log
			Log.d("HistActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "long_searchedItems.length: " + long_searchedItems.length);

			if (string_searchedItems_table_names != null) {
				
				// Log
				Log.d("HistActv.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Calling: Methods.convert_fileIdArray2tiList()");
				
	//			tiList = Methods.getAllData(this, tableName);
	//			tiList = Methods.convert_fileIdArray2tiList(this, MainActv.dirName_base, long_searchedItems);
				tiList = Methods.convert_fileIdArray2tiList_all_table(
										this,
										long_searchedItems,
										string_searchedItems_table_names);
				
			} else {//if (string_searchedItems_table_names != null)
				
				// Log
				Log.d("HistActv.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Calling: Methods.convert_fileIdArray2tiList()");
				
	//			tiList = Methods.getAllData(this, tableName);
	//			tiList = Methods.convert_fileIdArray2tiList(this, MainActv.dirName_base, long_searchedItems);
				tiList = Methods.convert_fileIdArray2tiList(this, tableName, long_searchedItems);
				
			}//if (string_searchedItems_table_names != null)
			
//				// Log
//				Log.d("HistActv.java" + "["
//						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//						+ "]", "Calling: Methods.convert_fileIdArray2tiList()");
//				
//	//			tiList = Methods.getAllData(this, tableName);
//	//			tiList = Methods.convert_fileIdArray2tiList(this, MainActv.dirName_base, long_searchedItems);
//				tiList = Methods.convert_fileIdArray2tiList(this, tableName, long_searchedItems);

			// Log
			Log.d("HistActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "[prep_list()] tiList.size()=" + tiList.size());
			
			
		} else if (history_file_ids != null) {//if (long_searchedItems == null)

			// Log
			Log.d("HistActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "history_file_ids != null");
			
			// Log
			Log.d("HistActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"file_ids: length=" + history_file_ids.length + 
					"/" + "history_table_names: length=" + history_table_names.length);
			
			tiList = Methods.get_all_data_history(this, history_file_ids, history_table_names);
			
			// Log
			Log.d("HistActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "tiList.size()=" + tiList.size());
			
//			tiList = Methods.convert_fileIdArray2tiList(this, "IFM8", long_searchedItems);
			
		} else {//if (long_searchedItems == null)
			
			// Log
			Log.d("HistActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Calling => Methods.getAllData");
			
			tiList = Methods.getAllData(this, tableName);
			
		}//if (long_searchedItems == null)

		
		
		return tiList;
	}//private List<TI> prep_list()


	private void setup_1_setListeners() {
		/****************************
		 * Steps
		 * 1. "Back" button
		 * 2. LongClick
		 * 3. "Bottom"
		 * 4. "Top"
			****************************/
		//
//		ImageButton ib_back = (ImageButton) findViewById(R.id.actv_al_ib_back);
		ImageButton ib_back = (ImageButton) findViewById(R.id.actv_hist_ib_back);
		
		ib_back.setEnabled(true);
		ib_back.setImageResource(R.drawable.ifm8_thumb_back_50x50);
		
//		ib_back.setTag(Tags.ButtonTags.thumb_activity_ib_back);
		ib_back.setTag(Tags.ButtonTags.actv_hist_ib_back);
		
		ib_back.setOnTouchListener(new ButtonOnTouchListener(this));
		ib_back.setOnClickListener(new ButtonOnClickListener(this));
		
		/****************************
		 * 2. LongClick
			****************************/
//		ListView lv = (ListView) findViewById(android.R.layout.activity_list_item);
		ListView lv = this.getListView();
		
		lv.setTag(Tags.ItemTags.dir_list_actv_hist);
		
		/****************************
		 * 3. "Bottom"
		 * 		1. Set up
		 * 		2. Listeners
			****************************/
		ImageButton bt_bottom = (ImageButton) findViewById(R.id.actv_hist_ib_toBottom);
//		ImageButton bt_bottom = (ImageButton) findViewById(R.id.actv_al_ib_toBottom);
		
		bt_bottom.setEnabled(true);
		bt_bottom.setImageResource(R.drawable.ifm8_thumb_bottom_50x50);
		
		// Tag
//		bt_bottom.setTag(Tags.ButtonTags.thumb_activity_ib_bottom);
		bt_bottom.setTag(Tags.ButtonTags.actv_hist_ib_bottom);
		
		bt_bottom.setOnTouchListener(new ButtonOnTouchListener(this));
		bt_bottom.setOnClickListener(new ButtonOnClickListener(this, lv));
		
		/****************************
		 * 4. "Top"
		 * 		1. Set up
		 * 		2. Listeners
			****************************/
//		ImageButton bt_top = (ImageButton) findViewById(R.id.actv_al_ib_toTop);
		ImageButton bt_top = (ImageButton) findViewById(R.id.actv_hist_ib_toTop);
		
		bt_top.setEnabled(true);
		bt_top.setImageResource(R.drawable.ifm8_thumb_top_50x50);
		
		// Tag
//		bt_top.setTag(Tags.ButtonTags.thumb_activity_ib_top);
		bt_top.setTag(Tags.ButtonTags.actv_hist_ib_top);
		
		/****************************
		 * 4.2. Listeners
			****************************/
		bt_top.setOnTouchListener(new ButtonOnTouchListener(this));
		bt_top.setOnClickListener(new ButtonOnClickListener(this, lv));
		
		
	}//private void setup_1_set_listeners()
	
	@Override
	protected void onPause() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onPause();
	}

	@Override
	protected void onResume() {
		/*********************************
		 * 1. super
		 * 2. Notify adapter
		 * 
		 * 3. Set selection
		 *********************************/
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onResume();
		
		// Log
		Log.d("HistActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onResume()");

//		if (ALActv.aAdapter != null) {
//					
//			ALActv.aAdapter.notifyDataSetChanged();
//			
//		}
//		TNActv.aAdapter.notifyDataSetChanged();

//		/*********************************
//		 * 3. Set selection
//		 *********************************/
//		lv_main = this.getListView();
//		
//		SharedPreferences prefs = this.getSharedPreferences(
//				MainActv.prefName_tnActv,
//				MODE_PRIVATE);
//	
//
//		//Methods.PrefenceLabels.thumb_actv.name()
//		
//		//int savedPosition = prefs.getInt("chosen_list_item", -1);
//		int savedPosition = prefs.getInt(
//							MainActv.prefName_tnActv_current_image_position,
//							-1);
//		
//		int target_position = savedPosition - (lv_main.getChildCount() / 2);
//		
//		if (target_position < 0) {
//			
//			target_position = 0;
//			
//		}//if (target_position == 0)
//
//		// Log
//		Log.d("HistActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "target_position=" + target_position);
//		
//		
//		lv_main.setSelection(target_position);

		
//		// Log
//		Log.d("HistActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "lv_main.getCheckedItemPosition()=" + lv_main.getCheckedItemPosition());
		
	}//protected void onResume()

	@Override
	protected void onStart() {
		/*********************************
		 * 1. super
		 * 
		 * 2. Set up
		 * 
		 * 3. Debug: Store file length data
		 *********************************/
		super.onStart();
		
		// Log
		Log.d("HistActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onStart()");
		
		vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
		
		/****************************
		 * 2. Set up
			****************************/
//		//debug
//		Methods.update_prefs_currentPath(this, MainActv.dirName_base);
		
		setup_0_initVars();
		
		setup_1_setListeners();
		
		setup_2_setList();

	}//protected void onStart()

	private void setup_0_initVars() {
		// TODO Auto-generated method stub
		CONS.History.hiList = new ArrayList<HI>();
	}


	private void debug_B20_v_1_1() {
		
		debug_B20_v_1_1_validateLengthData();
		
		
//		Methods_CM5.updateFileLength(this);
	}//private void debug_B20_v_1_1()


	private void debug_B20_v_1_1_validateLengthData() {
		/*********************************
		 * 01
		 *********************************/
		List<String> tnames = Methods.get_table_list(this, "cm5%");
		
		DBUtils dbu = new DBUtils(this, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		int numYes = 0;
		int numNo = 0;
		int numUnknown = 0;
		
		for (String tname : tnames) {
			/*********************************
			 * 01
			 *********************************/
			String sql = "SELECT * FROM " + tname;
			
			Cursor c = null;
			
			try {
				
				c = rdb.rawQuery(sql, null);
				
			} catch (Exception e) {
				// Log
				Log.e("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Exception => " + e.toString());
				
				rdb.close();
				
				return;
			}

			/*********************************
			 * 02
			 *********************************/
			if (c.getCount() < 1) {
				
				// Log
				Log.d("HistActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Less than 1: " + tname);
				
				continue;
				
			}//if (c.getCount() == condition)
			
			/*********************************
			 * 03
			 *********************************/
			c.moveToFirst();
			
			for (int i = 0; i < c.getCount(); i++) {
				
				int length = (int) c.getLong(9);
				
				if (length < 1) {
					
					numNo += 1;
					
				} else if (length >= 1) {//if (length < 1)
					
					numYes += 1;
					
				} else {//if (length < 1)
					
					numUnknown += 1;
					
				}//if (length < 1)
				
				c.moveToNext();
			}
			
		}//for (String tname : tnames)

		/*********************************
		 * 02
		 *********************************/
		// Log
		Log.d("HistActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]",
				"numYes=" + numYes + "/"
				+ "numNo=" + numNo + "/"
				+ "numUnknown=" + numUnknown);
		
	}//private void debug_B20_v_1_1_validateLengthData()


	private void debug_1_store_file_length() {
		/*********************************
		 * 1. DB setup
		 * 2. Table exists?
		 *********************************/
		/*********************************
		 * 1. DB setup
		 *********************************/
		DBUtils dbu = new DBUtils(this, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		String tname = CONS.tname_main;
		
		/****************************
		 * 2. Table exists?
			****************************/
		boolean res = dbu.tableExists(rdb, tname);
		
		if (res == false) {
			
			// Log
			Log.e("HistActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]",
				"debug_1_store_file_length() => Table doesn't exist: " + tname);
			
			rdb.close();
			
			return;
			
		} else {//if (res == false)
			
//			// Log
//			Log.d("HistActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Table exists => " + tname);
			
		}
		
		/*********************************
		 * Query
		 *********************************/
		String sql = "SELECT * FROM " + tname;
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
			startManagingCursor(c);
			
		} catch (Exception e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return;
		}
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount() => " + c.getCount());

		/****************************
		 * 
			****************************/
//		int data_num = c.getCount();
//		
//		// Log
//		Log.d("HistActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "data_num=" + data_num);
		
		c.moveToFirst();
		
//		String file_full_path = StringUtils.join(
//				(new String[]{
//					c.getString(4),
//					c.getString(3)}),
//				File.separator);

		String[] col_names = 
				Methods.get_column_list(this, CONS.dbName, tname);
		
//		for (String col_name : col_names) {
//			
//			// Log
//			Log.d("HistActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "col_name=" + col_name);
//			
//		}
//		
		
		/*********************************
		 * Add column
		 *********************************/
		res = 
				Methods.add_column_to_table(
						this, CONS.dbName, tname, "length", "INTEGER");
		
		// Log
		Log.d("HistActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res=" + res);
		
		rdb.close();
		
	}//private void debug_1_store_file_length()
	


	@Override
	protected void onStop() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStop();
		
		// Log
		Log.d("HistActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onStop()");

	}

	@Override
	protected void onDestroy() {
		/*********************************
		 * 1. super
		 * 2. move_mode => falsify
		 * 
		 * 3. History mode => Off
		 *********************************/
		
		super.onDestroy();
		
		// Log
		Log.d("HistActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onDestroy()");

		/*********************************
		 * Current position => Clear
		 *********************************/
		boolean res = 
				Methods.set_pref(
							this,
							CONS.History.pname_HistActv,
							CONS.History.pkey_HistActv_position,
							-1);
		
		if (res == true) {
			// Log
			Log.d("HistActv.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]",
					"Pref set: CONS.History.pkey_HistActv_position="
					+ CONS.History.pkey_HistActv_position);
			
		} else {//if (result == true)
			// Log
			Log.d("HistActv.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]",
					"Set pref => Failed: CONS.History.pkey_HistActv_position="
					+ CONS.History.pkey_HistActv_position);
			
		}//if (result == true)
		
		
	}//protected void onDestroy()

	@Override
	public void onBackPressed() {
		/****************************
		 * memo
			****************************/
		this.finish();
		
		overridePendingTransition(0, 0);
		
	}//public void onBackPressed()

	/****************************************
	 * method_name(param_type)
	 * 
	 * <Caller> 1. TNActv.set_list()
	 * 
	 * <Desc> 
	 * 1. Click "OK" button, then TNActv will get finished.
	 * 
	 * 
	 * <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public void show_message_no_data() {
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		
        dialog.setTitle("���");
        dialog.setMessage("���̃t�H���_�ɂ́A�f�[�^�͂���܂���B���̃t�H���_����A�I�v�V�����E���j���[�́u�ړ��v���g���āA�����Ă���܂�");
        
        dialog.setPositiveButton("OK",new DialogListener(this, dialog, 0));
        
        dialog.create();
        dialog.show();
		
	}//public void show_message_no_data()

	@Override
	protected void onListItemClick(ListView lv, View v, int position, long id) {
		/*********************************
		 * 0. Vibrate
		 * 
		 * 1. Get item
		 * 2. Intent
		 * 		2.1. Set data
		 * 
		 * 9. Start intent
		 *********************************/
		/****************************
		 * 0. Vibrate
			****************************/
		vib.vibrate(Methods.vibLength_click);

//		// Log
//		Log.d("HistActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "CONS.move_mode=" + CONS.move_mode);
		
//		//
//		if (CONS.move_mode == true) {
//			
//			checkedPositions.add(position);
//			
//			// Log
//			Log.d("TNActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "New position => " + position +
//					" / " + "(length=" + checkedPositions.size() + ")");
//			
////			ail_adp_move.notifyDataSetChanged();
//			ail_adp_move.notifyDataSetChanged();
//
//			
//		} else if (CONS.move_mode == false) {//if (CONS.move_mode == true)
			
			/*********************************
			 * Save current position to preference
			 *********************************/
			Methods.set_pref(
							this,
							CONS.History.pname_HistActv,
							CONS.History.pkey_HistActv_position,
							position);
			
			// Log
			Log.d("HistActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Position set to pref => " + position);
			
			/****************************
			 * 1. Get item
				****************************/
//			AI ai = (AI) lv.getItemAtPosition(position);
			HI hi = (HI) lv.getItemAtPosition(position);
			
			AI ai = Methods.get_data_ai(this, hi.getAiId(), hi.getAiTableName());

//			/***************************************
//			 * Store history
//			 ***************************************/
//			int res = Methods_CM5.saveHistory(this, ai);
//			
//			// Log
//			Log.d("HistActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ ":"
//					+ Thread.currentThread().getStackTrace()[2].getMethodName()
//					+ "]", "Save history: res=" + res);

			/****************************
			 * 2. Intent
			 * 		2.1. Set data
				****************************/
			Intent i = new Intent();
			
			i.setClass(this, PlayActv.class);
			
			i.putExtra("db_id", ai.getDb_id());
			
			i.putExtra("table_name", ai.getTable_name());
			
			i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			
			
			/*********************************
			 * 9. Start intent
			 *********************************/
//			startActivity(i);
			this.startActivityForResult(i, CONS.Intent.REQUEST_CODE_HISTORY);
			
//		}//if (CONS.move_mode == true)

//		/****************************
//		 * 0. Vibrate
//			****************************/
//		vib.vibrate(Methods.vibLength_click);
//		
//		if (MainActv.move_mode == false) {
//			/****************************
//			 * 1. Get item
//				****************************/
//			TI ti = (TI) lv.getItemAtPosition(position);
//			
//			/****************************
//			 * 2. Intent
//			 * 		2.1. Set data
//				****************************/
//			Intent i = new Intent();
//			
//			i.setClass(this, ImageActv.class);
//			
//			i.putExtra("file_id", ti.getFileId());
//			i.putExtra("file_path", ti.getFile_path());
//			i.putExtra("file_name", ti.getFile_name());
//			
//			i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//			
//			/*********************************
//			 * 2-2. Record history
//			 *********************************/
////			// Log
////			Log.d("HistActv.java" + "["
////					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////					+ "]", "Table name=" + Methods.convert_path_into_table_name(this));
//			int current_history_mode = Methods.get_pref(
//					this, 
//					MainActv.prefName_mainActv, 
//					MainActv.prefName_mainActv_history_mode,
//					-1);
//
//			if (current_history_mode == MainActv.HISTORY_MODE_OFF) {
//				
//				Methods.save_history(
//						this,
//						ti.getFileId(),
//						Methods.convert_path_into_table_name(this));
//				
//				/*********************************
//				 * 2-2-a. Update data
//				 *********************************/
////				// Log
////				Log.d("HistActv.java"
////						+ "["
////						+ Thread.currentThread().getStackTrace()[2]
////								.getLineNumber() + "]",
////						"[onListItemClick] Table name=" + Methods.convert_path_into_table_name(this));
//				
//				DBUtils dbu = new DBUtils(this, MainActv.dbName);
//				
//				//
//				SQLiteDatabase wdb = dbu.getWritableDatabase();
//
//				
//				boolean res = DBUtils.updateData_TI_last_viewed_at(
//									this,
//									wdb,
//									Methods.convert_path_into_table_name(this),
//									ti);
//				
//				if (res == true) {
//					// Log
//					Log.d("HistActv.java"
//							+ "["
//							+ Thread.currentThread().getStackTrace()[2]
//									.getLineNumber() + "]", "Data updated: " + ti.getFile_name());
//				} else {//if (res == true)
//					// Log
//					Log.d("HistActv.java"
//							+ "["
//							+ Thread.currentThread().getStackTrace()[2]
//									.getLineNumber() + "]",
//							"Update data => Failed: " + ti.getFile_name());
//				}//if (res == true)
//				
//				
//				wdb.close();
//				
//			} else {//if (current_move_mode == MainActv.HISTORY_MODE_OFF)
//				
//				// Log
//				Log.d("HistActv.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "History not saved");
//				
//			}//if (current_move_mode == MainActv.HISTORY_MODE_OFF)
////			Methods.save_history(this, ti.getFileId(), Methods.convert_path_into_table_name(this));
//			
//			
//			/*********************************
//			 * 2-3. Save preferences
//			 *********************************/
//			SharedPreferences preference = 
//					getSharedPreferences(
//							MainActv.prefName_tnActv,
//							MODE_PRIVATE);
//
//			SharedPreferences.Editor editor = preference.edit();
//			
//			editor.putInt(MainActv.prefName_tnActv_current_image_position, position);
//			editor.commit();
//
//			
//			// Log
//			Log.d("HistActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Prefs set");
//			
////			aAdapter.notifyDataSetChanged();
//			
//			// Log
//			Log.d("HistActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "aAdapter notified");
//			
//			this.startActivity(i);
//			
//		} else if (MainActv.move_mode == true) {//if (move_mode == false)
//			
//			/****************************
//			 * CheckBox on, then click on the item, then nothing happens (20120717_221403)
//				****************************/
//			
//			TNActv.checkedPositions.add(position);
//			
//			if (bAdapter != null) {
//				
//				bAdapter.notifyDataSetChanged();
//				
//			}//if (bAdapter != null)
//			
//			// Log
//			Log.d("HistActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "New position => " + position +
//					" / " + "(length=" + TNActv.checkedPositions.size() + ")");
//			
//			
//		}//if (move_mode == false)
//		
//		super.onListItemClick(lv, v, position, id);
		
	}//protected void onListItemClick(ListView lv, View v, int position, long id)

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.al_actv_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/****************************
		 * Steps
		 * 1. R.id.thumb_actv_menu_move_mode
		 * 2. R.id.thumb_actv_menu_move_files
			****************************/
		
		
//		case R.id.thumb_actv_menu_move_mode://---------------------------------------
		case R.id.al_actv_menu_move_mode://---------------------------------------
			if (CONS.move_mode == true) {
				
				move_mode_true(item);
				
			} else {// move_mode => false
				
				move_mode_false(item);
				
			}//if (move_mode == true)
			
			break;// case R.id.thumb_actv_menu_move_files
		
//		case R.id.thumb_actv_menu_move_files:	//------------------------------------------
		case R.id.al_actv_menu_move_files:	//------------------------------------------
			
			if (CONS.move_mode == false) {
				
				// debug
				Toast.makeText(this, "Move mode is not on", 2000)
						.show();
				
				return false;
				
			} else if (CONS.move_mode == true) {
				/****************************
				 * Steps
				 * 1. checkedPositions => Has contents?
				 * 2. If yes, show dialog
					****************************/
				if (checkedPositions.size() < 1) {
					
					// debug
					Toast.makeText(HistActv.this, "No item selected", 2000).show();
					
					return false;
					
				}//if (checkedPositions.size() < 1)
				
				
				/****************************
				 * 2. If yes, show dialog
					****************************/
				Methods_dlg.dlg_moveFiles(this);
				
			}//if (move_mode == false)
			
			break;// case R.id.thumb_actv_menu_move_files
			
		}//switch (item.getItemId())
		
		
		
		return super.onOptionsItemSelected(item);
		
	}//public boolean onOptionsItemSxelected(MenuItem item)


	private void move_mode_false(MenuItem item) {
		
		/****************************
		 * Steps: Current mode => false
		 * 1. Set icon => On
		 * 2. move_mode => true
		 * 
		 * 2-1. Set position to preference
		 * 
		 * 3. Update aAdapter
		 * 4. Re-set tiList
			****************************/
		
		item.setIcon(R.drawable.ifm8_thumb_actv_opt_menu_move_mode_on);
		
		CONS.move_mode = true;
		
		/****************************
		 * 4. Re-set tiList
			****************************/
//		String tableName = Methods.convertPathIntoTableName(this);

		String currentPath = Methods.get_currentPath_from_prefs(this);
		
		String tableName = Methods.convert_filePath_into_table_name(this, currentPath);
		
//		// Log
//		Log.d("HistActv.java"
//				+ "["
//				+ Thread.currentThread().getStackTrace()[2]
//						.getLineNumber() + "]", "tableName: " + tableName);
//		
//		// Log
//		Log.d("HistActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "currentPath=" + currentPath);
		
//		if (long_searchedItems == null) {
//
//			tiList = Methods.getAllData(this, tableName);
//			
//		} else {//if (long_searchedItems == null)
//
//			// Log
//			Log.d("HistActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "long_searchedItems != null");
//			
//			tiList = Methods.convert_fileIdArray2tiList(this, tableName, long_searchedItems);
//			
//		}//if (long_searchedItems == null)

		/*********************************
		 * List
		 *********************************/
		if (aiList != null) {
			
			aiList.clear();
			
			// Log
			Log.d("HistActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "ai_list => Cleared");
			
			aiList.addAll(Methods.get_all_data_ai(this, tableName));
			
		} else {//if (move_mode)
			
			aiList = Methods.get_all_data_ai(this, tableName);
			
			// Log
			Log.d("HistActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "ai_list => Initialized");
			
		}//if (move_mode)
		
		// Sort list
		Methods.sort_list_ai_created_at(aiList, CONS.SORT_ORDER.DEC);
		
		/****************************
		 * 3. Update aAdapter
			****************************/

//		ail_adp_move = new AILAdapter_move(
		ail_adp_move = new AILAdapter(
				this,
				R.layout.list_row_checked_box,
//				ai_list_move
				aiList
				);
		
//		ail_adp.clear();
		
//		ListView lv_move = this.getListView();
//		
//		lv_move.setAdapter(ail_adp_move);
		
		setListAdapter(ail_adp_move);

	}//private void move_mode_false(MenuItem item)


	private void move_mode_true(MenuItem item) {
		/****************************
		 * Steps: Current mode => false
		 * 1. Set icon => On
		 * 2. move_mode => false
		 * 2-2. TNActv.checkedPositions => clear()
		 * 
		 * 2-3. Get position from preference
		 * 
		 * 3. Re-set tiList
		 * 4. Update aAdapter
			****************************/
		
		item.setIcon(R.drawable.ifm8_thumb_actv_opt_menu_move_mode_off);
		
		CONS.move_mode = false;

//		/****************************
//		 * 2-2. TNActv.checkedPositions => clear()
//			****************************/
//		ALActv.checkedPositions.clear();
//		
//		/****************************
//		 * 2-3. Get position from preference
//			****************************/
//		int selected_position = Methods.get_pref(this, tnactv_selected_item, 0);
//		
//		/****************************
//		 * 3. Re-set tiList
//			****************************/
//		String tableName = Methods.convertPathIntoTableName(this);
		String currentPath = Methods.get_currentPath_from_prefs(this);
		
		String tableName = Methods.convert_filePath_into_table_name(this, currentPath);

		if (aiList != null) {
			
			aiList.clear();
			
			aiList.addAll(Methods.get_all_data_ai(this, tableName));
			
		} else {//if (move_mode)
			
			aiList = Methods.get_all_data_ai(this, tableName);
			
		}//if (move_mode)
		
		// Sort list
//		Methods.sort_list_ai_created_at(ai_list, CONS.SORT_ORDER.ASC);
		Methods.sort_list_ai_created_at(aiList, CONS.SORT_ORDER.DEC);

//		if (long_searchedItems == null) {
//
//			tiList.addAll(Methods.getAllData(this, tableName));
//			
//		} else {//if (long_searchedItems == null)
//
////			tiList = Methods.getAllData(this, tableName);
////			tiList = Methods.convert_fileIdArray2tiList(this, "IFM8", long_searchedItems);
//			
//		}//if (long_searchedItems == null)

		/****************************
		 * 4. Update aAdapter
			****************************/

		if (ail_adp != null) {
			
			ail_adp.notifyDataSetChanged();
			
			this.setListAdapter(ail_adp);
			
		} else {//if (ail_adp != null)
			
			ail_adp = new AILAdapter(
					this,
					R.layout.list_row_ai_list,
					aiList
					);
			
			this.setListAdapter(ail_adp);
			
		}//if (ail_adp != null)
		

//		aAdapter = 
//				new TIListAdapter(
//						this, 
//						R.layout.thumb_activity, 
//						tiList,
//						Methods.MoveMode.OFF);
//		
//		setListAdapter(aAdapter);
		
//		this.setSelection(selected_position);
		
	}//private void move_mode_true()

}//public class TNActv
