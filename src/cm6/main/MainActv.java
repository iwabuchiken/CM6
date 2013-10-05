package cm6.main;

import cm6.main.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cm6.items.AI;
import cm6.listeners.CustomOnItemLongClickListener;
import cm6.listeners.buttons.ButtonOnClickListener;
import cm6.listeners.buttons.ButtonOnTouchListener;
import cm6.listeners.dialogues.DialogListener;
import cm6.tasks.RefreshDBTask;
import cm6.utils.CONS;
import cm6.utils.DBUtils;
import cm6.utils.Methods;
import cm6.utils.Methods_dlg;
import cm6.utils.Tags;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;


public class MainActv extends ListActivity {
	
	public static Vibrator vib;

	/*********************************
	 * List-related
	 *********************************/
	// Used => 
	public static List<AI> list_ai = null;

	public static ArrayAdapter<String> adp_dir_list = null;

	public static List<String> list_root_dir = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	/*----------------------------
		 * 1. super
		 * 2. Set content
		 * 2-2. Set title
		 * 3. Initialize => vib
		 * 
		 *  4. Set list
		 *  5. Set listener => Image buttons
		 *  6. Set path label
		 *  
		 *  7. Initialize preferences
		 *  
		 *  8. Refresh DB
			----------------------------*/
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /*----------------------------
		 * 2-2. Set title
			----------------------------*/
		this.setTitle(this.getClass().getName());
        
        vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        
        _onCreate_setup();
		
		/*********************************
		 * Debugs
		 *********************************/
//		do_debug();
        
    }//public void onCreate(Bundle savedInstanceState)

    private void _onCreate_setup() {
		// TODO Auto-generated method stub
		/*----------------------------
		 * 4. Set list
			----------------------------*/
		set_initial_dir_list();
		
		/*----------------------------
		 * 5. Set listener => Image buttons
			----------------------------*/
		set_listeners();

	}//private void _onCreate_setup()

	private void B14_v_1_2_verify_table_name_in_record() {
		
		DBUtils dbu = new DBUtils(this, CONS.dbName);

		//
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		String sql = "SELECT * FROM " + CONS.tname_main;
		
		Cursor c = null;
		
		int count_null = 0;
		int count_in = 0;
		int count_others = 0;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
			if (c.getCount() > 0) {
				
				c.moveToFirst();
				
				for (int i = 0; i < c.getCount(); i++) {
					
					String s = c.getString(8);
					
					if (s == null || s.equals("") || s.equals("null")) {

						count_null += 1;
						
					} else if (s.equals(CONS.tname_main)) {//if (s == null || s.equals("") || s.equals("null"))
						
						count_in += 1;
						
					} else {//if (s == null || s.equals("") || s.equals("null"))
						
						count_others += 1;
						
					}//if (s == null || s.equals("") || s.equals("null"))
					
					c.moveToNext();
					
				}//for (int i = 0; i < c.getCount(); i++)
				
			} else {//if (c.getCount() > 0)
				
				// Log
				Log.d("MainActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "!c.getCount() > 0");
				
			}//if (c.getCount() > 0)
			
			
		} catch (Exception e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
		}//try

		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]",
				"count_null=" + count_null + "/"
				+ "count_in=" + count_in + "/"
				+ "count_others=" + count_others);
		
		rdb.close();
		
	}//private void B14_v_1_2_verify_table_name_in_record()

	private void B13_v_1_0_reset_pref_current_path() {
		// TODO Auto-generated method stub
    	Methods.set_pref(this,
				CONS.pname_current_path,
				CONS.pkey_current_path,
//				MainActv.dname_base);
				CONS.dpath_base);
	}//private void B13_v_1_0_reset_pref_current_path()

	private void drop_table(String table_name) {
    	
		DBUtils dbu = new DBUtils(this, CONS.dbName);

		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		boolean res = dbu.dropTable(this, wdb, table_name);
		
//		res = dbu.dropTable(this, wdb, MainActv.tableName_refreshLog);

		wdb.close();
		
	}//private void drop_table(String tableName_root) {

    private void do_debug() {
		/*********************************
		 * 6. Drop table
		 * 7. Add new col => "last_viewed_at"
		 *********************************/
//		copy_db_file();
//		test_simple_format();
//		restore_db("cm5_backup_20131003_090245.bk");
//		restore_db("cm5_backup_20131004_224923.bk");
		
		_debug_show_db_list();
		
//		check_db();
//		show_column_list("IFM9__Android");
//		10-01 15:05:54.408: D/MainActv.java[260](14946): New col added to: IFM9__Android

    	/*********************************
		 * 6. Drop table
		 *********************************/
//    	Methods.drop_table(this, MainActv.dbName, MainActv.tableName_show_history);
    	
    	/*********************************
		 * 7. Add new col => "last_viewed_at"
		 *********************************/
//    	add_new_col_last_viewed_at();
    	
    	
	}//private void do_debug()

	private void _debug_show_db_list() {
		// TODO Auto-generated method stub
		
		File f = new File(CONS.DB.dPath_dbFile);
		
		String[] file_names = f.list();
		
		for (String name : file_names) {
			
			// Log
			Log.d("["
					+ "MainActv.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + " : "
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "fname=" + name);
			
		}
		
	}

	private void add_new_col_last_viewed_at() {
		/*********************************
		 * 1. Get table list
		 * 2. Add new col
		 *********************************/
		List<String> table_list = Methods.get_table_list(this, "IFM9");
		
//		//debug
//		for (String name : table_list) {
//			
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "t_name=" + name);
//			
//		}//for (String name : table_list)
		
		/*********************************
		 * 2. Add new col
		 *********************************/
		for (String t_name : table_list) {
			
			boolean res = Methods.add_column_to_table(
									this,
									CONS.dbName,
									t_name,
									"last_viewed_at",
									"INTEGER");
			if (res == true) {
				// Log
				Log.d("MainActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "New col added to: " + t_name);
			} else {//if (res == true)
				// Log
				Log.e("MainActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Add new col => Failed: " + t_name);
			}//if (res == true)
			
			
		}//for (String t_name : table_list)
		
		
//		for (String name : table_list) {
//			
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "table name=" + name);
//			
//		}
		
//		String table_5 = table_list.get(5);
//		
//		String[] col_names = Methods.get_column_list(this, MainActv.dbName, table_5);
//		
//		for (String name : col_names) {
//		
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "col name=" + name);
//		
//		}
//
//		String table_6 = table_list.get(6);
//		
//		String[] col_names_6 = Methods.get_column_list(this, MainActv.dbName, table_6);
//		
//		for (String name_6 : col_names_6) {
//		
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "col name=" + name_6);
//
//		}
		
				
		
	}//private void add_new_col_last_viewed_at()

	private void init_prefs() {
    	/*********************************
		 * 1. history_mode
		 *********************************/
//		int current_history_mode = Methods.get_pref(
//				this, 
//				MainActv.pname_mainActv, 
//				MainActv.pname_mainActv_history_mode,
//				-1);
//
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "onCreate: current_history_mode=" + current_history_mode);
		
		boolean res = Methods.set_pref(
				this, 
				CONS.pname_mainActv, 
				CONS.pname_mainActv_history_mode,
				CONS.HISTORY_MODE_OFF);

		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", 
				"history_mode set => MainActv.HISTORY_MODE_OFF"
				+ "(" + CONS.HISTORY_MODE_OFF + ")");
		
	}//private void init_prefs()

	private void show_column_list() {
		/*********************************
		 * memo
		 *********************************/
    	String[] col_names = Methods.get_column_list(this, CONS.dbName, "IFM9");
    	
    	for (String name : col_names) {
			
    		// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "col=" + name);
    		
		}//for (String name : col_names)
		
	}

	private void show_column_list(String table_name) {
		/*********************************
		 * memo
		 *********************************/
    	String[] col_names = Methods.get_column_list(this, CONS.dbName, table_name);
    	
    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Table: " + table_name);
		
    	for (String name : col_names) {
			
    		// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "col=" + name);
    		
		}//for (String name : col_names)
		
	}

	private void check_db() {
//    	/*********************************
//		 * 1. Clear preferences
//		 *********************************/
//		prefs_main = 
//				this.getSharedPreferences(prefs_current_path, MODE_PRIVATE);
//		
//		SharedPreferences.Editor editor = prefs_main.edit();
//
//		editor.clear();
//		editor.commit();
//		
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Prefs cleared");
//
//		
//    	String dst = "/data/data/ifm9.main/databases" + MainActv.dbName;
//    	
//    	File f = new File(dst);
//    	
//    	File db_dir = new File("/data/data/ifm9.main/databases");
//    	
//    	for (String name : db_dir.list()) {
//			
//    		// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "file name=" + name);
//    		
//		}//for (String name : db_dir.list())
//    	
////    	boolean res = f.exists();
////    	
////    	// Log
////		Log.d("MainActv.java" + "["
////				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////				+ "]", "res=" + res);
////
////		// Log
////		Log.d("MainActv.java" + "["
////				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////				+ "]", "f.getAbsolutePath(): " + f.getAbsolutePath());
	}

	private void restore_db() {
    	
    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starting: restore_db()");
    	
		String src = "/mnt/sdcard-ext/IFM9_backup/ifm9_backup_20120929_075009.bk";
		String dst = StringUtils.join(new String[]{"/data/data/ifm9.main/databases", CONS.dbName}, File.separator);
		
//		String dst = "/data/data/ifm9.main/databases" + MainActv.dbName;
		boolean res = Methods.restore_db(this, CONS.dbName, src, dst);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res=" + res);
		
	}//private void restore_db()

	private void restore_db(String dbFile_name) {
    	
    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starting: restore_db()");
    	
		String src = StringUtils.join(
						new String[]{
							CONS.DB.dPath_dbFile_backup,
							dbFile_name},
							
						File.separator);
		
//		String src = "/mnt/sdcard-ext/IFM9_backup/" + dbFile_name;
		String dst = StringUtils.join(
						new String[]{
							CONS.DB.dPath_dbFile,
							CONS.DB.dbName},
						
						File.separator);
		
		// Log
		Log.d("[" + "MainActv.java : "
				+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ " : "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]",
				"src=" + src
				+ " *** "
				+ "dst=" + dst);
		
		boolean res = Methods.restore_db(this, CONS.dbName, src, dst);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "[restore_db()] res=" + res);
		
	}//private void restore_db()

	private void test_simple_format() {
    	
    	long t = Methods.getMillSeconds_now();
    	
    	String time_label = Methods.get_TimeLabel(t);
    	
    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "time_label: " + time_label);
		
    }//private void test_simple_format()
    
	private void set_listeners() {
		/*----------------------------
		 * 1. Get view
		 * 2. Set enables
		 * 
		 * 4. Listener => Long click
			----------------------------*/
		
		ImageButton ib_up = (ImageButton) findViewById(R.id.v1_bt_up);
		
		/*----------------------------
		 * 2. Set enables
			----------------------------*/
		String curDirPath = Methods.get_currentPath_from_prefs(this);
//		=> curDirPath: /mnt/sdcard-ext/cm5

//		=> CONS.dpath_base: /mnt/sdcard-ext/cm5
		
//		if (curDirPath.equals(CONS.dname_base)) {
		if (curDirPath.equals(CONS.dpath_base)) {
			
			ib_up.setEnabled(false);
			
			ib_up.setImageResource(R.drawable.ifm8_up_disenabled);
			
		} else {//if (this.currentDirPath == this.baseDirPath)
		
			ib_up.setEnabled(true);
			
			ib_up.setImageResource(R.drawable.ifm8_up);
		
		}//if (this.currentDirPath == this.baseDirPath)
		
		/*----------------------------
		 * 3. Listeners => Click
			----------------------------*/
		ib_up.setTag(Tags.ButtonTags.ib_up);
		
		ib_up.setOnTouchListener(new ButtonOnTouchListener(this));
		ib_up.setOnClickListener(new ButtonOnClickListener(this));
		
		/*********************************
		 * 4. Set listener => Long click
		 *********************************/
		ListView lv = this.getListView();
		
//		lv.setTag(Methods.ItemTags.dir_list);
		lv.setTag(Tags.ListTags.actv_main_lv);
		
		lv.setOnItemLongClickListener(new CustomOnItemLongClickListener(this));

	}//private void set_listeners()

	private boolean set_initial_dir_list() {
		
		B16_v_1_6_nullify_list_root_dir();
		
		set_initial_dir_list_part1();
//		set_initial_dir_list_part2();
		
		return false;
	}//private boolean set_initial_dir_list()
	

	private void B16_v_1_6_nullify_list_root_dir() {
		
		if (list_root_dir != null) {
			
			list_root_dir = null;
			
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "list_root_dir => Nullified");
			
		} else {//if (this.list_root_dir)
			
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "list_root_dir == null");
			
		}//if (this.list_root_dir)
		
		
	}//private void B16_v_1_6_nullify_list_root_dir()

	private boolean set_initial_dir_list_part1() {
		/*********************************
		 * 1. Create root dir
		 * 1-2. Create "list.txt"
		 * 
		 * 2. Set variables => currentDirPath, baseDirPath
		 * 
		 * 3. Get file list
		 * 3-1. Sort file list
		 * 
		 * 4. Set list to adapter
		 * 
		 * 5. Set adapter to list view
		 * 
		 * 6. Set listener to list
		 * 
		 * 9. Return
		 *********************************/
		/*********************************
		 * 1. Create root dir
		 *********************************/
		File root_dir = create_root_dir();
		
		if (root_dir == null) {
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "root_dir == null");
			
			return false;
		}//if (root_dir == null)
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "root_dir=" + root_dir.getAbsolutePath());
		
		/*********************************
		 * 1-2. Create "list.txt"
		 *********************************/
		boolean res = create_list_file(root_dir);
		
		if (res == false) {
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "res == false");
			
			return false;
		}//if (res == false)

		/*********************************
		 * 2. Set variables => currentDirPath, baseDirPath
		 *********************************/
		this.init_prefs_current_path();

		/*********************************
		 * 3. Get file list
		 *********************************/
		
		if (list_root_dir == null) {
			
			list_root_dir = Methods.get_file_list(root_dir);
			
//			init_file_list(root_dir);
			
		} else {//if (this.list_root_dir == null)
			
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "list_root_dir != null");
			
		}//if (this.list_root_dir == null)

		
		
		/*----------------------------
		 * 4. Set list to adapter
			----------------------------*/
		
		res = set_list_to_adapter();
		
		if (res == false)
			return false;
		

		
		return false;
	}//private boolean set_initial_dir_list_part1()

	private void set_listener_to_list() {
		
		ListView lv = this.getListView();
		
//		lv.setTag(Methods.ItemTags.dir_list);
		lv.setTag(Tags.ListTags.actv_main_lv);
		
		lv.setOnItemLongClickListener(new CustomOnItemLongClickListener(this));
		
	}//private void set_listener_to_list()

	private boolean set_list_to_adapter() {
		
		adp_dir_list = new ArrayAdapter<String>(
				this,
				R.layout.simple_text_view,
//				android.R.layout.simple_list_item_1,
				list_root_dir
				);

		this.setListAdapter(adp_dir_list);
		
//		// Log
//		Log.d("MainActv.java" + "["
//		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//		+ "]", "adp_dir_list => set");
		
		if (adp_dir_list == null) {
//			// Log
//			Log.d("MainActv.java" + "["
//			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//			+ "]", "adp_dir_list => null");
			
			return false;
			
		} else {//if (adp_dir_list == null)
//			// Log
//			Log.d("MainActv.java" + "["
//			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//			+ "]", "adp_dir_list => Not null");
			
			return true;
			
		}//if (adapter == null)

//		return false;

	}//private boolean set_list_to_adapter()

	private void init_file_list(File file) {
		
//		/*----------------------------
//		 * 1. Get file array
//		 * 2. Sort the array
//		 * 3. Return
//			----------------------------*/
//		
////		// Log
////		Log.d("MainActv.java" + "["
////				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////				+ "]", "file path: " + file.getAbsolutePath());
//		
//		File[] files = null;
//		
//		String path_in_prefs = Methods.get_currentPath_from_prefs(this);
//		
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "path_in_prefs: " + path_in_prefs);
//		
//
//		if (path_in_prefs == null) {
//			
//			files = file.listFiles();
//			
//		} else {//if (path_in_prefs == null)
//			
//			files = new File(path_in_prefs).listFiles();
//			
//		}//if (path_in_prefs == null)
//		
//		//debug
//		if (files == null) {
//			
//			// Log
//			Log.e("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "files => null");
//			
//		}//if (files == null)
//		
//		/*----------------------------
//		 * 2. Sort the array
//			----------------------------*/
//		Methods.sortFileList(files);
//		
////		List<String> list_root_dir = new ArrayList<String>();
//		list_root_dir = new ArrayList<String>();
//
//		for (File item : files) {
//			list_root_dir.add(item.getName());
//		}
				
	}//private void init_file_list(File file)

	private void init_prefs_current_path() {
		/*----------------------------
		 * If the preference already set, then no operation
		 * 
		 * 1. Get preference
		 * 0. Prefs set already?
		 * 2. Get editor
		 * 3. Set value
			----------------------------*/
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "MainActv.init_prefs_current_path()");
		
		/*----------------------------
		 * 1. Get preference
			----------------------------*/
		CONS.prefs_main = 
				this.getSharedPreferences(
						CONS.pname_current_path,
						MODE_PRIVATE);

		/*----------------------------
		 * 0. Prefs set already?
		 * 		=> If yes, then return
			----------------------------*/
		String temp = CONS.prefs_main.getString(CONS.pkey_current_path, null);
		
//		if (temp != null && !temp.equals("IFM8")) {
		if (temp != null && !temp.equals("IFM8")) {
			
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Prefs already set: " + temp);
			
			return;
			
		}//if (temp == null)
		
		
		/*----------------------------
		 * 2. Get editor
			----------------------------*/
		SharedPreferences.Editor editor = CONS.prefs_main.edit();

		/*----------------------------
		 * 3. Set value
			----------------------------*/

		String base_path = StringUtils.join(
				new String[]{
						CONS.dpath_storage_sdcard, CONS.dname_base
				},
				File.separator);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "base_path=" + base_path);
		
		editor.putString(CONS.pkey_current_path, base_path);
		
		editor.commit();
		
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Prefs init => " + prefs_current_path + "/" + dname_base);
		
	}//private void init_prefs_current_path()

	private boolean create_list_file(File root_dir) {
		File list_file = new File(root_dir, CONS.fname_list);
		
		if (list_file.exists()) {
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "\"list.txt\" => Exists");
			
			return true;
			
		} else {//if (list_file.exists())
			try {
//				BufferedWriter br = new BufferedWriter(new FileWriter(list_file));
//				br.close();
				
				list_file.createNewFile();
				
				// Log
				Log.d("MainActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"File created => " + CONS.fname_list);
				
				return true;
				
			} catch (IOException e) {
				// Log
				Log.e("MainActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"Create file => Failed: " + CONS.fname_list
						+ "(" + e.toString() + ")");
				
				return false;
			}//try
			
		}//if (list_file.exists())
		
	}//private boolean create_list_file()

	/*********************************
	 * <Return>
	 * File object(directory)
	 * null	=> Can't create directory
	 *********************************/
	private File create_root_dir() {
		
		String dpath_base = StringUtils.join(
				new String[]{
						CONS.dpath_storage_sdcard, CONS.dname_base},
				File.separator);

		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "dpath_base=" + dpath_base);
		
		File file = new File(dpath_base);
		
		if (!file.exists()) {
			try {
				file.mkdir();
				
				// Log
				Log.d("MainActv.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "Dir created => " + file.getAbsolutePath());
				
				return file;
				
			} catch (Exception e) {
				// Log
				Log.e("MainActv.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "Exception => " + e.toString());
				
				return null;
			}
			
		} else {//if (file.exists())
			// Log
//			Log.d("MainActv.java"
//			+ "["
//			+ Thread.currentThread().getStackTrace()[2]
//				.getLineNumber() + "]", "Dir exists => " + file.getAbsolutePath());
			
			return file;
		}//if (file.exists())

	}//private void create_root_dir()

	@Override
	protected void onListItemClick(ListView lv, View v, int position, long id) {
		/*********************************
		 * 0. Vibrate
		 * 
		 * 1. Get item name
		 * 2. Get file object
		 * 2-2. File object exists?
		 * 
		 * 3. Is a directory?
		 * 		=> If yes, update the current path
		 * 
		 * 4. Is a "list.txt"?
		 *********************************/
		//
		vib.vibrate(Methods.vibLength_click);
		
		/*********************************
		 * 1. Get item name
		 *********************************/
		String item = (String) lv.getItemAtPosition(position);
		
		if (item != null) {
			
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "item=" + item);
			
		} else {//if (item_)
			
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "item == null");
			
		}//if (item_)
		
		
		/*********************************
		 * 2. Get file object
		 *********************************/
		File target = get_file_object(item);
		
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "target=" + target.getAbsolutePath());
		
		/*********************************
		 * 2-2. File object exists?
		 *********************************/
		if (!target.exists()) {
			// debug
			Toast.makeText(this, "This item doesn't exist in the directory: " + item, 
					2000)
					.show();
			
			// Log
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"This item doesn't exist in the directory: " + item);

			return;
		} else {//if (!target.exists())
			
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Target exists: " + item);
			
		}//if (!target.exists())

		/*********************************
		 * 3. Is a directory?
		 * 		=> If yes, update the current path
		 *********************************/
		if (target.isDirectory()) {
			
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "target=" + target.getAbsolutePath());
			
			Methods.enterDir(this, target);
			
//			// debug
//			Toast.makeText(this, "Enter directory: " + item, 
//					2000)
//					.show();
			
		} else if (target.isFile()) {//if (target.isDirectory())
			
			/*********************************
			 * 4. Is a "list.txt"?
			 *********************************/
			if (!target.getName().equals(CONS.fname_list)) {
				
				// debug
				Toast.makeText(this,
						"list.txt doesn't exist", Toast.LENGTH_SHORT).show();
				
				return;
				
			}//if (!target.getName().equals(ImageFileManager8Activity.fname_list))

//			Methods.startThumbnailActivity(this, target.getName());
			Methods.start_actv_allist(this);
			
		}//if (target.isDirectory())
		
		
		super.onListItemClick(lv, v, position, id);
	}//protected void onListItemClick(ListView l, View v, int position, long id)

	private File get_file_object(String itemName) {
		/*----------------------------
		 * 1. 
			----------------------------*/
		SharedPreferences prefs = 
				this.getSharedPreferences(CONS.pname_current_path, MODE_PRIVATE);

		String path = prefs.getString(CONS.pkey_current_path, null);
		
		if (path == null) {
			
//			init_prefs_current_path();
			Methods.set_pref(this,
					CONS.pname_current_path,
					CONS.pkey_current_path,
//					MainActv.dname_base);
					CONS.dpath_base);
			
			path = CONS.dpath_base;
			
//			path = prefs.getString(prefs_current_path, null);
			
		}//if (path == null)
		
		File target = new File(path, itemName);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "path: " + path);
		
		return target;
		
//		return null;
		
	}//private File get_file_object(String itemName)

	@Override
	protected void onDestroy() {
		/*----------------------------
		 * 1. Reconfirm if the user means to quit
		 * 2. super
		 * 3. Clear => prefs_main
		 * 4. Clear => list_root_dir
			----------------------------*/
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onDestroy()");
		
		super.onDestroy();
		
		/*----------------------------
		 * 3. Clear => prefs_main
			----------------------------*/
		CONS.prefs_main = 
				this.getSharedPreferences(CONS.pname_current_path, MODE_PRIVATE);

		//debug
		String s = Methods.get_pref(this, CONS.pkey_current_path, null);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "CONS.pkey_current_path=" + s);
		
		SharedPreferences.Editor editor = CONS.prefs_main.edit();

		editor.clear();
		editor.commit();
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Prefs cleared: pname_current_path");
		
		/*----------------------------
		 * 4. Clear => list_root_dir
			----------------------------*/
//		list_root_dir = null;
		
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "list_root_dir => Set to null");
		
	}//protected void onDestroy()

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		Methods.confirm_quit(this, keyCode);
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.main_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

//		case R.id.main_opt_menu_refresh_db://---------------------------------------
//			/*----------------------------
//			 * Steps
//			 * 1. Vibrate
//			 * 2. Task
//				----------------------------*/
//			
//			vib.vibrate(Methods.vibLength_click);
//			
//			/*----------------------------
//			 * 2. Task
//				----------------------------*/
////			RefreshDBTask task_ = new RefreshDBTask(this);
//			RefreshDBTask task_ = new RefreshDBTask(this);
//			
//			// debug
//			Toast.makeText(this, "Starting a task...", 2000)
//					.show();
//			
//			task_.execute("Start");
//			
//			break;// case R.id.main_opt_menu_refresh_db
		
		case R.id.main_opt_menu_create_folder://----------------------------------
			
			Methods_dlg.dlg_createFolder(this);
			
			break;// case R.id.main_opt_menu_create_folder
			
		case R.id.main_opt_menu_db_activity://----------------------------------
			
			Methods_dlg.dlg_db_activity(this);
			
			break;// case R.id.main_opt_menu_db_activity

		case R.id.main_opt_menu_search://-----------------------------------------------
			
			Methods_dlg.dlg_seratchItem(this);
			
			break;// case R.id.main_opt_menu_search
//			
		case R.id.main_opt_menu_admin://-----------------------------------------------
			
			case_main_opt_menu_admin();
			
			break;// case R.id.main_opt_menu_admin
			
//		case R.id.main_opt_menu_preferences://-----------------------------------------------
//			
//			Methods.start_PrefActv(this);
//			
//			break;// case R.id.main_opt_menu_search
//			
		case R.id.main_opt_menu_history://-----------------------------------------------
			
			Methods.show_history(this);
			
			break;// case R.id.main_opt_menu_history
			
		}//switch (item.getItemId())
		
		return super.onOptionsItemSelected(item);
		
	}//public boolean onOptionsItemSelected(MenuItem item)

	private void case_main_opt_menu_admin() {
		// TODO Auto-generated method stub
		Methods_dlg.dlg_admin(this);
	}
	

	@Override
	protected void onPause() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onPause();

		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onPause()");

//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "prefs_main: " + Methods.get_currentPath_from_prefs(this));
		
		
	}

	@Override
	protected void onResume() {
		/*********************************
		 * 1. super
		 * 2. Set enables
		 *********************************/
		super.onResume();

		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onResume()");

		
		/*********************************
		 * 2. Set enables
		 *********************************/
//		ImageButton ib_up = (ImageButton) findViewById(R.id.v1_bt_up);
//		
//		String curDirPath = Methods.get_currentPath_from_prefs(this);
//		
//		if (curDirPath.equals(dname_base)) {
//			
//			ib_up.setEnabled(false);
//			
//			ib_up.setImageResource(R.drawable.ifm8_up_disenabled);
//			
//		} else {//if (this.currentDirPath == this.dpath_base)
//		
//			ib_up.setEnabled(true);
//
//			
//			ib_up.setImageResource(R.drawable.ifm8_up);
//		
//		}//if (this.currentDirPath == this.dpath_base)
		
	}//protected void onResume()

	private void copy_db_file() {
		/*----------------------------
		 * 1. db setup
		 * 2. Setup files
			----------------------------*/
		
		DBUtils dbu = new DBUtils(this, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "rdb.getPath(): " + rdb.getPath());
		
		String dbPath = rdb.getPath();
		
		rdb.close();
		
		/*----------------------------
		 * 2. Setup files
			----------------------------*/
		
		String dstPath = "/mnt/sdcard-ext";
		
		File src = new File(dbPath);
//		File dst = new File(dstPath);
		File dst = new File(dstPath, src.getName());
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "src: " + src.getAbsolutePath() + "/" + "dst: " + dst.getAbsolutePath());
		
		
		try {
			FileChannel iChannel = new FileInputStream(src).getChannel();
			FileChannel oChannel = new FileOutputStream(dst).getChannel();
			iChannel.transferTo(0, iChannel.size(), oChannel);
			iChannel.close();
			oChannel.close();
			
			// Log
			Log.d("ThumbnailActivity.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "File copied");
			
		} catch (FileNotFoundException e) {
			// Log
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
		} catch (IOException e) {
			// Log
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
		}//try

	}//copy_db_file()

	@Override
	protected void onStart() {
		/*----------------------------
		 * 1. Refresh DB
			----------------------------*/
//		refresh_db();
//		SharedPreferences prefs_main =
//							this.getSharedPreferences(this.getString(R.string.prefs_shared_prefs_name), 0);
//		
////		// Log
////		Log.d("MainActv.java" + "["
////				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////				+ "]", "prefs_main: db_refresh => " + prefs_main.getBoolean(this.getString(R.string.prefs_db_refresh_key), false));
//		
//		if(prefs_main.getBoolean(this.getString(R.string.prefs_db_refresh_key), false)) {
//			
//			Methods.start_refreshDB(this);
//			
//		} else {//if(prefs_main.getBoolean(this.getString(R.string.prefs_db_refresh_key), false))
//			
////			// Log
////			Log.d("MainActv.java" + "["
////					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////					+ "]", "Prefs: db_refresh => " + false);
//			
//		}//if(prefs_main.getBoolean(this.getString(R.string.prefs_db_refresh_key), false))
		
		super.onStart();
	}//protected void onStart()

	private void refresh_db() {
		SharedPreferences prefs =
				this.getSharedPreferences(this.getString(R.string.prefs_shared_prefs_name), 0);

		//// Log
		//Log.d("MainActv.java" + "["
		//	+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		//	+ "]", "prefs: db_refresh => " + prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false));
		
		if(prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false)) {
		
			Methods.start_refreshDB(this);
		
		} else {//if(prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false))
		
		//// Log
		//Log.d("MainActv.java" + "["
		//		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		//		+ "]", "Prefs: db_refresh => " + false);
		
		}//if(prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false))
		
	}

}//public class MainActv extends Activity
