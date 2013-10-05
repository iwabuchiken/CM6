package cm6.utils;


import cm6.main.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import android.os.AsyncTask;

// Apache
import org.apache.commons.lang.StringUtils;

import cm6.items.AI;
import cm6.items.BM;
import cm6.items.HI;
import cm6.items.SearchedItem;
import cm6.items.TI;
import cm6.listeners.CustomOnItemLongClickListener;
import cm6.listeners.MPOnCompletionListener;
import cm6.listeners.dialogues.DialogButtonOnClickListener;
import cm6.listeners.dialogues.DialogButtonOnTouchListener;
import cm6.listeners.dialogues.DialogListener;
import cm6.listeners.dialogues.DialogOnItemClickListener;
import cm6.listeners.dialogues.DialogOnItemLongClickListener;
import cm6.main.ALActv;
import cm6.main.MainActv;
import cm6.utils.CONS.BMActv.SortOrder;
import cm6.utils.CONS.SORT_ORDER;
import cm6.main.HistActv;
import cm6.main.PlayActv;
import cm6.main.PrefActv;
import cm6.main.SearchActv;
import cm6.main.TNActv;
import cm6.services.Service_ShowProgress;
import cm6.tasks.RefreshDBTask;
import cm6.tasks.SearchTask;

// REF=> http://commons.apache.org/net/download_net.cgi
//REF=> http://www.searchman.info/tips/2640.html

//import org.apache.commons.net.ftp.FTPReply;

public class Methods {

	static int counter;		// Used => sortFileList()
	
	
	/****************************************
	 * Vars
	 ****************************************/
	public static final int vibLength_click = 35;

	static int tempRecordNum = 20;

	/****************************************
	 * Methods
	 ****************************************/
	public static void sort_list_files(File[] files) {
		// REF=> http://android-coding.blogspot.jp/2011/10/sort-file-list-in-order-by-implementing.html
		/****************************
		 * 1. Prep => Comparator
		 * 2. Sort
			****************************/
		
		/****************************
		 * 1. Prep => Comparator
			****************************/
		Comparator<? super File> filecomparator = new Comparator<File>(){
			
			public int compare(File file1, File file2) {
				/****************************
				 * 1. Prep => Directory
				 * 2. Calculate
				 * 3. Return
					****************************/
				
				int pad1=0;
				int pad2=0;
				
				if(file1.isDirectory())pad1=-65536;
				if(file2.isDirectory())pad2=-65536;
				
				int res = pad2-pad1+file1.getName().compareToIgnoreCase(file2.getName());
				
				return res;
			} 
		 };//Comparator<? super File> filecomparator = new Comparator<File>()
		 
		/****************************
		 * 2. Sort
			****************************/
		Arrays.sort(files, filecomparator);

	}//public static void sort_list_files(File[] files)

	/****************************************
	 *
	 * 
	 * <Caller> 1. MainActv.onListItemClick()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static void enterDir(Activity actv, File newDir) {
		/****************************
		 * Steps
		 * 1. Update the current path
		 * 2. Refresh list view
		 * 3. Update path view
		 * 
		 * 4. Update image buttons
		 * 
			****************************/
		/****************************
		 * 1. Update the current path
			****************************/
		
//		MainActv.dirPath_current = newDir.getAbsolutePath();
		CONS.dpath_current = newDir.getAbsolutePath();
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "MainActv.dpath_current: " + CONS.dpath_current);
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Calling: Methods.update_prefs_currentPath(actv, CONS.dpath_current)");

//		//debug
//		String cur_pref_path = Methods.get_pref(actv, CONS.pname_current_path, null);
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "cur_pref_path=" + cur_pref_path);
		
		Methods.update_prefs_currentPath(actv, CONS.dpath_current);
//		
//		/****************************
//		 * 2. Refresh list view
//			****************************/
		Methods.refresh_list_view(actv);

		/****************************
		 * 3. Update path view
			****************************/
		Methods.updatePathLabel(actv);
		
		/*********************************
		 * 4. Update image buttons
		 *********************************/
		Methods.update_image_buttons(actv, CONS.dpath_current);
		
		
	}//public static void enterDir(Activity actv, File newDir)

	/****************************************
	 *
	 * 
	 * <Caller> 1. Methods.enterDir()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static boolean update_prefs_currentPath(Activity actv, String newPath) {
		
		SharedPreferences prefs = 
				actv.getSharedPreferences(
						CONS.pname_current_path,
						actv.MODE_PRIVATE);

		/****************************
		 * 2. Get editor
			****************************/
		SharedPreferences.Editor editor = prefs.edit();

		/****************************
		 * 3. Set value
			****************************/
		editor.putString(CONS.pkey_current_path, newPath);
		
		try {
			editor.commit();
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean update_prefs_current_path(Activity actv, Strin newPath)

	/****************************************
	 *
	 * 
	 * <Caller> 1. Methods.enterDir()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static boolean clear_prefs_currentPath(
							Activity actv,String newPath) {
		
		SharedPreferences prefs = 
				actv.getSharedPreferences(
						CONS.pname_current_path,
						actv.MODE_PRIVATE);

		/****************************
		 * 2. Get editor
			****************************/
		SharedPreferences.Editor editor = prefs.edit();

		/****************************
		 * 3. Clear
			****************************/
		try {
			
			editor.clear();
			editor.commit();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Prefs cleared");
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean clear_prefs_current_path(Activity actv, Strin newPath)

	public static boolean
	clearPref (Activity actv,String prefName) {

		SharedPreferences prefs = 
						actv.getSharedPreferences(
								prefName,
								actv.MODE_PRIVATE);
		
		/****************************
		* 2. Get editor
		****************************/
		SharedPreferences.Editor editor = prefs.edit();
		
		/****************************
		* 3. Clear
		****************************/
		try {
		
			editor.clear();
			editor.commit();
			
			// Log
			Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Prefs cleared");
			
			return true;
		
		} catch (Exception e) {
		
			// Log
			Log.e("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean clear_prefs_current_path(Activity actv, Strin newPath)

	
	/****************************************
	 *
	 * 
	 * <Caller> 1. Methods.enterDir()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static String get_currentPath_from_prefs(Activity actv) {
		
		SharedPreferences prefs = 
				actv.getSharedPreferences(
						CONS.pname_current_path,
						actv.MODE_PRIVATE);

		return prefs.getString(CONS.pkey_current_path, null);
		
	}//public static String get_currentPath_from_prefs(Activity actv)

	
	/****************************************
	 * 		refresh_list_view(Activity actv)
	 * 
	 * <Caller> 
	 * 1. Methods.enterDir()
	 * 
	 * <Desc> 
	 * 1. 
	 * 
	 * 
	 * <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static void refresh_list_view(Activity actv) {
		/****************************
		 * Steps
		 * 1. Get currentPath
		 * 2. Get file array
		 * 3. Sort file array
		 * 
		 * 4. Add file names to list
		 * 5. Notify adapter of changes
		 * 6. Update image buttons
		 * 
		 * 
		 * 1. Get file list
		 * 1-2. Sort list
		 * 2. Clear => ImageFileManager8Activity.file_names
		 * 3. Add file names to => ImageFileManager8Activity.file_names
		 * 4. Notify adapter of changes
		 * 
		 * 
		 * 
		 * 6. Update image buttons
			****************************/
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Methods.refresh_list_view()");
		
		/****************************
		 * 1. Get currentPath
			****************************/
		String currentPath = Methods.get_currentPath_from_prefs(actv);
		
		if (currentPath == null) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Methods.get_currentPath_from_prefs(actv) => null");
			
			return;
			
		} else {//if (currentPath == null)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "currentPath=" + currentPath);
			
		}//if (currentPath == null)
		
//		//debug
//		return;
		
		// 
		/****************************
		 * 2. Get file array
			****************************/
		File currentDir = new File(currentPath);
		
		File[] files = currentDir.listFiles();

		Methods.sort_list_files(files);
		
		/****************************
		 * 4. Add file names to list
			****************************/
		// Reset file_names
		if(MainActv.list_root_dir != null) {
			
			MainActv.list_root_dir.clear();
			
		} else {
			
			MainActv.list_root_dir = new ArrayList<String>();
			
		}
		
		// Add names
		for (File item : files) {
			
			MainActv.list_root_dir.add(item.getName());
			
		}
		
		/****************************
		 * 5. Notify adapter of changes
			****************************/
		if (MainActv.adp_dir_list != null) {
			
			MainActv.adp_dir_list.notifyDataSetChanged();
			
		} else {//if (condition)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "MainActv.adp_dir_list => null");
			
			// debug
//			Toast.makeText(actv, "MainActv.adp_dir_list => null", Toast.LENGTH_LONG).show();
			Toast.makeText(actv, "MainActv.adp_dir_list => null", Toast.LENGTH_LONG).show();

		}//if (condition)
		
	}//private static void refresh_list_view()

	private static void update_image_buttons(
					Activity actv, String currentPath) {
		
		ImageButton ib_up = (ImageButton) actv.findViewById(R.id.v1_bt_up);
		
//		if (currentPath.equals(MainActv.dirPath_base)) {
		if (currentPath.equals(CONS.dpath_base)) {
			
			ib_up.setImageResource(R.drawable.ifm8_up_disenabled);
			ib_up.setEnabled(false);
			
		} else {//if (currentPath.equals(MainActv.dpath_base))

			ib_up.setImageResource(R.drawable.ifm8_up);
			ib_up.setEnabled(true);
			
		}//if (currentPath.equals(MainActv.dpath_base))
		
	}//private static void update_image_buttons()

	/****************************************
	 *
	 * 
	 * <Caller> 1.  Methods.enterDir(this, target)
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static void updatePathLabel(Activity actv) {
		// 
		TextView tv = (TextView) actv.findViewById(R.id.v1_tv_dir_path);
		
		tv.setText(convert_prefs_into_path_label(actv));
		
	}//public static void updatePathLabel(Activity actv)

	/****************************************
	 *
	 * 
	 * <Caller> 1. Methods.updatePathLabel(Activity actv)
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
//	public static String getCurrentPathLabel(Activity actv) {
	public static String convert_prefs_into_path_label(Activity actv) {
		/****************************
		 * 1. Prep => pathArray, currentBaseDirName
		 * 2. Detect loation of "IFM8"
		 * 3. Build path label
			****************************/
		
		String currentPath = Methods.get_currentPath_from_prefs(actv);
//			=> /mnt/sdcard-ext/cm5/bb

		String[] pathArray = currentPath.split(File.separator);

		String currentBaseDirName = pathArray[pathArray.length - 1];
//		=> bb
		
		/****************************
		 * 2. Detect loation of "IFM8"
			****************************/
		int location = -1;
		
//		=> CONS.dpath_base: /mnt/sdcard-ext/cm5

		for (int i = 0; i < pathArray.length; i++) {
			
//			if (pathArray[i].equals(CONS.dpath_base)) {
			if (pathArray[i].equals(CONS.dname_base)) {
				
				location = i;
				
				break;
				
			}//if (pathArray[i].equals(ImageFileManager8Activity.baseDirName))
			
		}//for (int i = 0; i < pathArray.length; i++)
		
		/****************************
		 * 3. Build path label
			****************************/
		if (location < 0) {
			
			return "Unknown path";
					
		}//if (location == condition)
		
		//REF=> http://stackoverflow.com/questions/4439595/how-to-create-a-sub-array-from-another-array-in-java
		String[] newPath = Arrays.copyOfRange(pathArray, location, pathArray.length);
		
		String s_newPath = StringUtils.join(newPath, File.separator);
		
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "s_newPath => " + s_newPath);
		
		return s_newPath;
		
	}//public static String getCurrentPathLabel(Activity actv)

	public static String convert_prefs_into_path_label(Activity actv, String path) {
		/****************************
		 * 1. Prep => pathArray, currentBaseDirName
		 * 2. Detect loation of "IFM8"
		 * 3. Build path label
			****************************/
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Starting => convert_prefs_into_path_label()");
		
		String currentPath = path;
		
		String[] pathArray = currentPath.split(File.separator);
		
		String currentBaseDirName = pathArray[pathArray.length - 1];
		
		/****************************
		 * 2. Detect loation of "IFM8"
			****************************/
		int location = -1;
		
		for (int i = 0; i < pathArray.length; i++) {
//			if (pathArray[i].equals(MainActv.dpath_base)) {
			if (pathArray[i].equals(CONS.dname_base)) {
				location = i;
				break;
			}//if (pathArray[i].equals(ImageFileManager8Activity.baseDirName))
		}//for (int i = 0; i < pathArray.length; i++)
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//
//				+ "]", "currentPath");
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "MainActv.dpath_base=" + CONS.dpath_base);
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "location=" + location);
		
		/****************************
		 * 3. Build path label
			****************************/
		//REF=> http://stackoverflow.com/questions/4439595/how-to-create-a-sub-array-from-another-array-in-java
		String[] newPath = Arrays.copyOfRange(pathArray, location, pathArray.length);
		
		String s_newPath = StringUtils.join(newPath, File.separator);
		
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "s_newPath => " + s_newPath);
		
		return s_newPath;
		
	}//public static String getCurrentPathLabel(Activity actv)

	public static void upDir(Activity actv) {
		/****************************
		 * Steps
		 * 1. Get the current path from preference
		 * 2. Is the current path "roof"?
		 * 3. Go up the path
		 * 3-2. New path => Equal to base dir path?
		 * 
		 * 4. Refresh list
		 * 4-2. Update buttons
		 * 5. Update path view
			****************************/
		/****************************
		 * 1. Get the current path from preference
			****************************/
		String currentPath = Methods.get_currentPath_from_prefs(actv);
//		=> currentPath: /mnt/sdcard-ext/cm5/bb

		/****************************
		 * 2. Is the current path "roof"?
			****************************/
		if (currentPath.equals(CONS.dpath_base)) {
			
			// debug
			Toast.makeText(actv, "�g�b�v�E�t�H���_�ɂ��܂�", 2000).show();
		
			return;
		}//if (ImageFileManager8Activity.currentDirPath == ImageFileManager8Activity.baseDirPath)
		
		/****************************
		 * 3. Update the current path
			****************************/
		Methods.update_prefs_currentPath(actv, new File(currentPath).getParent());
		
		/****************************
		 * 4. Refresh list
			****************************/
		Methods.refresh_list_view(actv);
		
		/****************************
		 * 4-2. Update buttons
			****************************/
		currentPath = Methods.get_pref(
							actv,
							CONS.pname_current_path,
							CONS.pkey_current_path,
							null);
//		=> currentPath: /mnt/sdcard-ext/cm5

		if (currentPath != null) {
			
			Methods.update_image_buttons(actv, currentPath);
			
		}//if (currentPath != null)
		
		/****************************
		 * 5. Update path view
			****************************/
		Methods.updatePathLabel(actv);
		
	}//public static void upDir(Activity actv)

	public static void startThumbnailActivity(
						Activity actv, String targetFileName) {
		/****************************
		 * Steps
		 * 1. "list.txt"?
		 * 2. If yes, start activity
			****************************/
//		if (!targetFileName.equals(MainActv.listFileName)) {
		if (!targetFileName.equals(CONS.fname_list)) {
			
			// debug
			Toast.makeText(actv, "list.txt �ł͂���܂���", 2000).show();
			
			return;
		}//if (!target.getName().equals(ImageFileManager8Activity.fname_list))
		
		/****************************
		 * 2. If yes, start activity
			****************************/
		Intent i = new Intent();
		
		i.setClass(actv, TNActv.class);
		
		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		
		actv.startActivity(i);
		
	}//public static void startThumbnailActivity(Activity actv, File target)

	public static void start_actv_allist(Activity actv) {
		/*********************************
		 * memo
		 *********************************/
		Intent i = new Intent();
		
		i.setClass(actv, ALActv.class);
		
		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		
		actv.startActivity(i);

		
	}//public static void start_actv_allist(Activity actv)


	public static void start_PrefActv(Activity actv) {
		
		Intent i = new Intent();
		
		i.setClass(actv, PrefActv.class);
		
//		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		
		actv.startActivity(i);
		
	}//public static void start_PrefActv(Activity actv)

	public static void start_refreshDB(Activity actv) {
		
		RefreshDBTask task_ = new RefreshDBTask(actv);
		
		// debug
		Toast.makeText(actv, "Starting a task...", 2000)
				.show();
		
		task_.execute("Start");

	}//public static void start_refreshDB(Activity actv)

	public static void confirm_quit(Activity actv, int keyCode) {
		
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			
			AlertDialog.Builder dialog=new AlertDialog.Builder(actv);
			
//	        dialog.setTitle("�A�v���̏I��");
			dialog.setTitle(actv.getString(R.string.generic_tv_confirm));
	        dialog.setMessage(actv.getString(R.string.main_quit));
	        
	        dialog.setPositiveButton(
	        				actv.getString(R.string.generic_bt_quit),
	        				new DialogListener(actv, dialog, 0));
	        
	        dialog.setNegativeButton(
	        				actv.getString(R.string.generic_bt_cancel),
	        				new DialogListener(actv, dialog, 1));
	        
	        dialog.create();
	        dialog.show();
			
		}//if (keyCode==KeyEvent.KEYCODE_BACK)
		
	}//public static void confirm_quit(Activity actv, int keyCode)

	/****************************************
	 *
	 * 
	 * <Caller> 
	 * 1. TNActv.set_list()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static List<TI> getAllData(Activity actv, String tableName) {
		/****************************
		 * Steps
		 * 0. Table exists?
		 * 1. DB setup
		 * 2. Get data
		 * 		2.1. Get cursor
		 * 		2.2. Add to list
		 * 
		 * 9. Close db
		 * 10. Return value
			****************************/

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "getAllData() => Starts");
		/****************************
		 * 1. DB setup
			****************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/****************************
		 * 0. Table exists?
			****************************/
		boolean res = dbu.tableExists(rdb, tableName);
		
		if (res == false) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "getAllData() => Table doesn't exist: " + tableName);
			
			rdb.close();
			
			return null;
			
		}//if (res == false)
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "rdb.getPath(): " + rdb.getPath());
		
		
		
		/****************************
		 * 2. Get data
		 * 		2.1. Get cursor
		 * 		2.2. Add to list
			****************************/
		//
		String sql = "SELECT * FROM " + tableName;
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
		} catch (Exception e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount() => " + c.getCount());

		/****************************
		 * 2.2. Add to list
			****************************/
		c.moveToNext();
		
		List<TI> tiList = new ArrayList<TI>();
		
		for (int i = 0; i < c.getCount(); i++) {
//		for (int i = 0; i < c.getCount() / 200; i++) {

			TI ti = new TI(
					c.getLong(1),	// file_id
					c.getString(2),	// file_path
					c.getString(3),	// file_name
					c.getLong(4),	// date_added
//					c.getLong(5)		// date_modified
					c.getLong(5),		// date_modified
					c.getString(6),	// memos
					c.getString(7)	// tags
			);
	
			// Add to the list
			tiList.add(ti);
			
			//
			c.moveToNext();
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		
		
		/****************************
		 * 9. Close db
			****************************/
		rdb.close();
		
		/****************************
		 * 10. Return value
			****************************/
		
		
		return tiList;
		
	}//public static List<ThumbnailItem> getAllData

	/*********************************
	 * <Return>
	 * null		=> Pref value not set
	 *********************************/
	public static String convert_path_into_table_name(Activity actv) {
		/*********************************
		 * 1. Get path from pref
		 * 2. Process path into table name
		 * 
		 * 3. Return
		 *********************************/
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Methods.convert_path_into_table_name(Activity actv)");
		
		SharedPreferences prefs_main = 
				actv.getSharedPreferences(CONS.pname_current_path, Activity.MODE_PRIVATE);	

		String value = prefs_main.getString(CONS.pkey_current_path, null);
		
		if (value == null) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "value == null");
			
			return null;
			
		}//if (value == null)
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////				+ "]", "value=" + value);
//				+ "]", "CONS.pkey_current_path=" + value);
		
		/*********************************
		 * 2. Process path into table name
		 *********************************/
		String[] a_path = value.split(File.separator);
		
		// Detect the position of the "dname_base"
		int loc = Methods.get_position_from_array(a_path, CONS.dname_base);
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "loc=" + loc);
		
		// REF=> http://stackoverflow.com/questions/4439595/how-to-create-a-sub-array-from-another-array-in-java
		String[] sub_array = Arrays.copyOfRange(a_path, loc, a_path.length);
		
		String new_value = StringUtils.join(sub_array, "__");
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "new_value=" + new_value);
		
		/*********************************
		 * 3. Return
		 *********************************/
		return new_value;
		
//		String tableName = null;
//		StringBuilder sb = new StringBuilder();
//
////		// Log
////		Log.d("Methods.java" + "["
////				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////				+ "]", "convert_prefs_into_path_label(actv): " + convert_prefs_into_path_label(actv));
//		
////		Log.d("Methods.java" + "["
////				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////				+ "]", "MainActv.dbName: " + MainActv.dbName);
//		
////		if(convert_prefs_into_path_label(actv).equals(MainActv.dbName)) {
//		if(convert_prefs_into_path_label(actv).equals(MainActv.dpath_base)) {
//			
//			tableName = convert_prefs_into_path_label(actv);
//			
//		} else {
//			
//			String[] currentPathArray = convert_prefs_into_path_label(actv).split(File.separator);
//			
//			if (currentPathArray.length > 1) {
//				
//				tableName = StringUtils.join(currentPathArray, "__");
//				
//			} else {//if (currentPathArray.length > 1)
//				
//				sb.append(currentPathArray[0]);
//				
//			}//if (currentPathArray.length > 1)
//			
//		}//if(getCurrentPathLabel(actv).equals(ImageFileManager8Activity.baseDirName))
//		
////		// Log
////		Log.d("Methods.java" + "["
////				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////				+ "]", "tableName => " + tableName);
//		
//		
//		return tableName;
	}//public static String convert_path_into_table_name(Activity actv)

	/*********************************
	 * <Return>
	 * -1	=> Not detected
	 *********************************/
	private static int get_position_from_array(String[] base_array,
			String target_string) {
		/*********************************
		 * memo
		 *********************************/
		int position;
		
		for (position = 0; position < base_array.length; position++) {
			
			if (base_array[position].equals(target_string)) {
				
				return position;
				
			}//if (base_array[i] == condition)
			
		}//for (int position = 0; position < base_array.length; position++)
		
		
		return -1;
	}//private static int get_position_from_array()

	public static String convert_path_into_table_name(Activity actv, String newPath) {
		/****************************
		 * Steps
		 * 1. Get table name => Up to the current path
		 * 2. Add name => Target folder name
			****************************/
		String tableName = null;
		StringBuilder sb = new StringBuilder();

			
//		String[] currentPathArray = convert_prefs_into_path_label(actv).split(File.separator);
		String[] currentPathArray = newPath.split(File.separator);
		
		if (currentPathArray.length > 1) {
			
			tableName = StringUtils.join(currentPathArray, "__");
			
		} else {//if (currentPathArray.length > 1)
			
			sb.append(currentPathArray[0]);
			
			tableName = sb.toString();
			
		}//if (currentPathArray.length > 1)
		
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "tableName => " + tableName);
		
		
		return tableName;
	}//public static String convert_path_into_table_name(Activity actv)

	public static String convert_filePath_into_table_name(Activity actv, String filePath) {
		
		String temp = Methods.convert_prefs_into_path_label(actv, filePath);
		
		return Methods.convert_path_into_table_name(actv, temp);
		
	}//public static String convert_filePath_into_table_name(Activity actv, String filePath)

	public static String convert_filePath_into_path_label(Activity actv, String filePath) {
		
		String temp = Methods.convert_prefs_into_path_label(actv, filePath);
		
		return temp;
		
//		return Methods.convert_path_into_table_name(actv, temp);
		
	}//public static String convert_filePath_into_path_label(Activity actv, String filePath)

	public static String convert_filePath_into_path_label_no_base(Activity actv, String filePath) {
		
		String temp = Methods.convert_prefs_into_path_label(actv, filePath);
		
		String[] a_temp = temp.split(File.separator);
		
//		String[] a_temp2 = Arrays.copyOfRange(a_temp, 1, a_temp.length - 1);
		String[] a_temp2 = Arrays.copyOfRange(a_temp, 1, a_temp.length);
//		String[] a_temp2 = Arrays.copyOfRange(a_temp, 0, a_temp.length);
		
//		return StringUtils.join(a_temp2, MainActv.tableName_separator);
		return StringUtils.join(a_temp2, File.separator);
		
//		return temp;
		
//		return Methods.convert_path_into_table_name(actv, temp);
		
	}//public static String convert_filePath_into_path_label_no_base(Activity actv, String filePath)

	public static List<String> get_table_list(Activity actv) {
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT name FROM " + "sqlite_master"+
						" WHERE type = 'table' ORDER BY name";
		
		Cursor c = null;
		try {
			c = rdb.rawQuery(q, null);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount(): " + c.getCount());

		} catch (Exception e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
		}
		
		// Table names list
		List<String> tableList = new ArrayList<String>();
		
		// Log
		if (c != null) {
			c.moveToFirst();
			
			for (int i = 0; i < c.getCount(); i++) {
				//
				tableList.add(c.getString(0));
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "c.getString(0): " + c.getString(0));
				
				
				// Next
				c.moveToNext();
				
			}//for (int i = 0; i < c.getCount(); i++)

		} else {//if (c != null)
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c => null");
		}//if (c != null)

//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount(): " + c.getCount());
//		
		rdb.close();
		
		return tableList;
	}//public static List<String> get_table_list()

	/****************************************
	 *	refreshMainDB(Activity actv)
	 * 
	 * <Caller> 1. <Desc> 1. <Params> 1.
	 * 
	 * <Return>
	 *  -1		Can't create a table
	 *  0~	Number of items added
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static int refreshMainDB(Activity actv) {
		/****************************
		 * Steps
		 * 1. Set up DB(writable)
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 3. Execute query for image files

		 * 4. Insert data into db
		 * 5. Update table "refresh_log"
		 * 
		 * 9. Close db
		 * 10. Return
			****************************/
		/****************************
		 * 1. Set up DB(writable)
			****************************/
		//
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/****************************
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 		1. baseDirName
		 * 		2. backupTableName
			****************************/
		boolean res = refreshMainDB_1_set_up_table(wdb, dbu);

		if (res == false) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Can't  create table");
			
			wdb.close();
			
			return -1;
			
		}//if (res == false)
		/****************************
		 * 3. Execute query for image files
			****************************/
		Cursor c = refreshMainDB_2_exec_query(actv, wdb, dbu);
		
		/****************************
		 * 4. Insert data into db
			****************************/
		int numOfItemsAdded;
		
		if (c.getCount() < 1) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Query result: 0");
			
			numOfItemsAdded = 0;
			
//			// debug
//			Toast.makeText(actv, "�V�K�̃t�@�C���͂���܂���", 2000).show();
			
		} else {//if (c.getCount() < 1)
			
			numOfItemsAdded = refreshMainDB_3_insert_data(actv, wdb, dbu, c);
			
		}//if (c.getCount() < 1)
		
		/****************************
		 * 9. Close db
			****************************/
		wdb.close();
		
		/****************************
		 * 10. Return
			****************************/
		return numOfItemsAdded;
		
	}//public static int refreshMainDB(Activity actv)

	/*********************************
	 * <Return>
	 * -1	=> Table setup failed
	 * -2	=> "Get file list" failed
	 * -3	=> "Get ai_list" failed
	 * 
	 * 0<	=> Number of items added
	 *********************************/
	public static int refresh_main_db(Activity actv) {
		/*********************************
		 * 1. Set up DB(writable)
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 
		 * 3. Execute query for audio files
		 * 3.1. Get value => Last refreshed date
		 * 3.2. Get a files list
		 * 
		 * 
		 * 4. Insert data into db
		 * 5. Update table "refresh_log"
		 * 
		 * 9. Close db
		 * 10. Return
		 *********************************/
		/****************************
		 * 1. Set up DB(writable)
			****************************/
		//
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/****************************
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 		1. baseDirName
		 * 		2. backupTableName
			****************************/
//		boolean res = Methods.refreshMainDB_1_set_up_table(wdb, dbu, MainActv.tname_item);
		boolean res = Methods.refresh_main_db_1_set_up_table(wdb, dbu, CONS.tname_main);
		
		if (res == false) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "res == false");
			
			wdb.close();
			
			return -1;
			
		} else {//if (res == false)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Done => Methods.refresh_main_db_1_set_up_table()");
		}

		/*********************************
		 * 3.1. Get value => Last refreshed date
		 * 	1. Table exists?
		 * 	2. Get date
		 *********************************/
		/*********************************
		 * 3.1.1. Table exists?
		 *********************************/
		res = Methods.refresh_main_db_2_set_up_table_refresh_history(
								wdb, dbu);

		if (res == false) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "res == false");
			
			wdb.close();
			
			return -1;
			
		} else {//if (res == false)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]",
			"Done => Methods.refresh_main_db_2_set_up_table_refresh_history()");
			
		}

		/*********************************
		 * 3.1.2. Get date
		 *********************************/
		long last_refreshed_date = Methods.get_last_refreshed_date(wdb, dbu);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "last_refreshed_date=" + last_refreshed_date);
		
		/*********************************
		 * 3.2. Get a files list
		 * 	1. Build a path
		 * 	2. If no files in the dir, return
		 * 	3. If new files, then build a list
		 *********************************/
		File[] file_list = Methods.refresh_main_db_3_get_file_list();
		
		if (file_list == null) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "file_list == null");
			
			wdb.close();
			
			return -2;
			
		}//if (file_list == null)
		
//		// Build a path
//		String path = StringUtils.join(
//				(new String[]{
//					MainActv.dpath_storage_internal,
//					MainActv.dname_tt_internal}),
//					File.separator);
//
//		/*********************************
//		 * 3.2.2. If no files in the dir, return
//		 *********************************/
//		// Files list
//		File[] file_list = new File(path).listFiles();
//		
//		if (file_list.length < 1) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "file_list.length < 1");
//			
//			wdb.close();
//			
//			return -2;
//			
//		}//if (file_list.length == condition)
//
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "file_list.length=" + file_list.length);

		/*********************************
		 * 3.3. If new files, then build a list
		 *********************************/
//		List<AI> ai_list = new ArrayList<AI>();
		List<AI> ai_list = Methods.refresh_main_db_4_get_ai_list(
									file_list,
									last_refreshed_date);
		
		if (ai_list == null) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "ai_list == null");
			
			wdb.close();
			
			return -3;
			
		}//if (ai_list == null)
		
//		int num_of_items = 0;
//		
//		for (File f : file_list) {
//			
//			if (f.lastModified() > last_refreshed_date) {
////				String file_name, String title, String memo,
////				long last_played_at,
////				String table_name
//				
//				AI ai = new AI(
//						f.getName(),
//				    	StringUtils.join(
//		    				new String[]{
//		    						MainActv.dpath_storage_internal,
//	//	    						MainActv.dname_source_folder_tt},
//		    						MainActv.dname_tt_internal},
//    						File.separator),
//
//    						"", "", 0,
//    						MainActv.tname_main,
//    						f.lastModified()
//    						);
//				
//				ai_list.add(ai);
//				
//				num_of_items += 1;
//				
//			}//if (f.lastModified() == condition)
//			
//		}//for (File f : file_list)
//		
//		if (ai_list.size() < 1) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "ai_list.size() < 1");
//			
//			wdb.close();
//			
//			return -3;
//			
//		} else {//if (ai_list.size() == condition)
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "ai_list.size()=" + ai_list.size());
//			
//		}//if (ai_list.size() == condition)

		/*********************************
		 * 4. Insert data into db
		 *********************************/
//		int i_res = Methods.insert_data_into_db_ai_list(wdb, dbu, ai_list);
		int i_res = Methods.refresh_main_db_5_insert_data_into_db_ai_list(wdb, dbu, ai_list);

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "i_res=" + i_res);
		
		/*********************************
		 * 5. Update table "refresh_log"
		 * => If data is -1, then the current date is inserted into data
		 *********************************/
		long data = Methods.get_latest_date_from_ai_list(ai_list);
		
		if (data == -1) {
			
			data = Methods.getMillSeconds_now();
			
		}//if (data == -1)
		
		res = Methods.refresh_main_db_6_update_refresh_history(
									wdb, dbu,
									i_res,
//									ai_list.get(ai_list.size() - 1).getCreated_at());
									data);
		
		//debug
		wdb.close();

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "wdb => Closed");
		
		return i_res;
		
//		/****************************
//		 * 3. Execute query for audio files
//		 * 	1. Get value => Last refreshed date
//			****************************/
////		Cursor c = Methods.refreshMainDB_2_exec_query(actv, wdb, dbu);
//		/*********************************
//		 * 3.1. Get value => Last refreshed date
//		 *********************************/
//		long last_refreshed_date = Methods.get_last_refreshed_date(wdb, dbu);
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "last_refreshed_date=" + last_refreshed_date);
//		
//		/*********************************
//		 * memo
//		 *********************************/
//		String path = StringUtils.join(
//					(new String[]{
//						MainActv.dname_storage_internal,
//						MainActv.dname_tt_internal}),
//						File.separator);
//						
//    	File base_dir = new File(path);
//
//		File[] file_list = base_dir.listFiles();
//		
//		List<AI> ai_list = new ArrayList<AI>();
//		
//		if (file_list.length < 1) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "file_list.length < 1");
//			
//			wdb.close();
//			
//			return -2;
//			
//		}//if (file_list.length == condition)
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "file_list.length=" + file_list.length);
//		
//		/*********************************
//		 * If new files, then build a list
//		 *********************************/
//		int num_of_items = 0;
//		
//		for (File f : file_list) {
//			
//			if (f.lastModified() > last_refreshed_date) {
////				String file_name, String title, String memo,
////				long last_played_at,
////				String table_name
//				
//				ai_list.add(new AI(
//						f.getName(),
//				    	StringUtils.join(
//	    				new String[]{
//	    						MainActv.dname_storage_internal,
////	    						MainActv.dname_source_folder_tt},
//	    						MainActv.dname_tt_internal},
//	    						File.separator),
//
//						"",
//						"",
//						0,
//						MainActv.tname_main,
//						f.lastModified()
//						));
//				
//				num_of_items += 1;
//				
//			}//if (f.lastModified() == condition)
//			
//		}//for (File f : file_list)
//		
//		if (ai_list.size() < 1) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "ai_list.size() < 1");
//			
//			wdb.close();
//			
//			return -3;
//			
//		}//if (ai_list.size() == condition)
//		
//		/*********************************
//		 * Insert data => AI, history
//		 *********************************/
//		int i_res = DBUtils.insert_all_data_ai(wdb, dbu, ai_list);
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "i_res=" + i_res);
//		
//		/****************************
//		 * 4. Insert data into db
//			****************************/
////		int numOfItemsAdded;
////		
////		if (c.getCount() < 1) {
////			
////			// Log
////			Log.d("Methods.java" + "["
////					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////					+ "]", "Query result: 0");
////			
////			numOfItemsAdded = 0;
////			
////			// debug
////			Toast.makeText(actv, "�V�K�̃t�@�C���͂���܂���", 2000).show();
////			
////		} else {//if (c.getCount() < 1)
////			
////			numOfItemsAdded = refreshMainDB_3_insert_data(actv, wdb, dbu, c);
////			
////		}//if (c.getCount() < 1)
//		
//		/****************************
//		 * 9. Close db
//			****************************/
//		wdb.close();
//		
//		/****************************
//		 * 10. Return
//			****************************/
//		return i_res;
		
	}//public static boolean refresh_main_db(Activity actv)

	private static long get_latest_date_from_ai_list(List<AI> ai_list) {
		
		long last_one = -1;
		
		for (AI ai : ai_list) {
			
			if (last_one < ai.getCreated_at()) {
				
				last_one = ai.getCreated_at();
				
			}//if (last_one == condition)
			
		}//for (AI ai : ai_list)
		
		return last_one;
		
	}//private static long get_latest_date_from_ai_list(List<AI> ai_list)

	private static boolean refresh_main_db_6_update_refresh_history(
								SQLiteDatabase wdb, DBUtils dbu,
								int i_res,
								long date_of_last_ai_item) {
		/*********************************
		* 1. Table exists?
		* 2. If no, create one
		* 2-2. Create table failed => Return
		* 3. Insert data
		 *********************************/
		/*********************************
		 * 1. Table exists?
		 *********************************/
		String tableName = CONS.tname_refresh_history;
		
		if(!dbu.tableExists(wdb, tableName)) {
		
			Log.e("Methods.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Table doesn't exitst: " + tableName);
		
			/****************************
			* 2. If no, create one
			****************************/
			if(dbu.createTable(wdb, tableName, 
				CONS.cols_refresh_log, CONS.col_types_refresh_log)) {
				
				// Log
				Log.d("Methods.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
				.getLineNumber() + "]", "Table created: " + tableName);
			
			} else {//if
				/****************************
				* 2-2. Create table failed => Return
				****************************/
				//toastAndLog(actv, "Create table failed: " + tableName, Toast.LENGTH_LONG);
				
				// Log
				Log.e("Methods.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
				.getLineNumber() + "]", "Create table failed: " + tableName);
				
				
				return false;
			
			}//if
		
		} else {//if(dbu.tableExists(wdb, ImageFileManager8Activity.refreshLogTableName))
		
			//toastAndLog(actv, "Table exitsts: " + tableName, 2000);
			
			// Log
			Log.d("Methods.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Table exitsts: " + tableName);
		
		
		}//if(dbu.tableExists(wdb, ImageFileManager8Activity.refreshLogTableName))
		
		/****************************
		* 3. Insert data
		****************************/
		boolean res;
		
		try {
			res = dbu.insert_data_refresh_history(
							wdb, 
							tableName, 
							new long[] {date_of_last_ai_item, (long) i_res});

			if (res == true) {
				
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "refresh history => Updated");
				
				wdb.close();
				
				return true;
				
			} else {//if (res == false)

				// Log
				Log.e("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Updated refresh history => Failed");
				
				wdb.close();
				
				return false;
				
			}//if (res == false)
			
		} catch (Exception e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Insert data failed");
			
			return false;
			
		}//try
		
	}//private static boolean refresh_main_db_6_update_refresh_history(int i_res)

	private static List<AI> refresh_main_db_4_get_ai_list(
							File[] file_list, long last_refreshed_date) {
		
		
		
		List<AI> ai_list = new ArrayList<AI>();
		
		int num_of_items = 0;
		
		for (File f : file_list) {

//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "File path=" + f.getAbsolutePath());
			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "f.lastModified()=" + f.lastModified());
			
			if (f.lastModified() > last_refreshed_date) {

				String fileFullPath = StringUtils.join(
	    				new String[]{
	    						CONS.dpath_storage_internal,
//	    						MainActv.dname_source_folder_tt},
	    						CONS.dname_tt_internal},
	    						
	    						File.separator);
				
				AI ai = new AI(
//						f.getName(),
//				    	StringUtils.join(
//		    				new String[]{
//		    						CONS.dpath_storage_internal,
//	//	    						MainActv.dname_source_folder_tt},
//		    						CONS.dname_tt_internal},
//    						File.separator),
//
//    						"", "", 0,
//    						CONS.tname_main,
//    						f.lastModified()
//    						);
						f.getName(),
						fileFullPath,
						

    						"", "", 0,		// title, memo, last_played_at
    						CONS.tname_main,	// table_name
//    						Methods.getFileLength(fileFullPath),	// length
    						Methods.getFileLength(f.getAbsolutePath()),	// length
    						f.lastModified()	// created_at
    						);
				
				ai_list.add(ai);
				
				num_of_items += 1;
				
			}//if (f.lastModified() == condition)
			
		}//for (File f : file_list)
		
		if (ai_list.size() < 1) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "ai_list.size() < 1");
			
			return null;
			
		} else {//if (ai_list.size() == condition)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "ai_list.size()=" + ai_list.size());

			return ai_list;
			
		}//if (ai_list.size() == condition)
		
	}//private static List<AI> refresh_main_db_4_get_ai_list(File[] file_list)

	private static long getFileLength(String fileFullPath) {
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Methods: " + Thread.currentThread().getStackTrace()[2].getMethodName());
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "File path=" + fileFullPath);
		
		MediaPlayer mp = new MediaPlayer();
		
//		int len = 0;
		long len = 0;
		
		try {
			mp.setDataSource(fileFullPath);
			
			mp.prepare();
			
//			len = mp.getDuration() / 1000;
			len = mp.getDuration();
			
			// REF=> http://stackoverflow.com/questions/9609479/android-mediaplayer-went-away-with-unhandled-events
			mp.reset();
			
			// REF=> http://stackoverflow.com/questions/3761305/android-mediaplayer-throwing-prepare-failed-status-0x1-on-2-1-works-on-2-2
			mp.release();
			
		} catch (IllegalArgumentException e) {
			
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "Exception=" + e.toString());
			
		} catch (IllegalStateException e) {
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "Exception=" + e.toString());

		} catch (IOException e) {
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "Exception=" + e.toString());
		}//try
		
		return len;
	}//private static long getFileLength(String fileFullPath)

	private static File[] refresh_main_db_3_get_file_list() {
		// Build a path
		String path = StringUtils.join(
				(new String[]{
					CONS.dpath_storage_internal,
					CONS.dname_tt_internal}),
					File.separator);

		/*********************************
		 * 3.2.2. If no files in the dir, return
		 *********************************/
		// Files list
		File[] file_list = new File(path).listFiles();
		
		if (file_list.length < 1) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "file_list.length < 1");
			
			return null;
			
		}//if (file_list.length == condition)

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "file_list.length=" + file_list.length);

		
		//debug
		for (File f : file_list) {
			
			if (f.getAbsolutePath().contains(".mp3")) {
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber()
						+ ":"
						+ Thread.currentThread().getStackTrace()[2]
								.getMethodName() + "]", "mp3 => " + f.getName());
				
			}//if (f.getAbsolutePath() == condition)
			
		}//for (File f : file_list)
		
		return file_list;
		
	}//private static File[] refresh_main_db_3_get_file_list()

	/*********************************
	 * 20121013_072459
	 * 
	 * <Return>
	 * => Number of items inserted
	 * 
	 *********************************/
	private static int refresh_main_db_5_insert_data_into_db_ai_list(SQLiteDatabase wdb,
			DBUtils dbu, List<AI> ai_list) {
		/*********************************
		 * memo
		 *********************************/
		int i_res = 0;
		
		int failed_items = 0;
		
//		for (AI ai : ai_list) {
		for (int i = 0; i < ai_list.size(); i++) {
			
			boolean res = dbu.insert_data_ai(wdb, ai_list.get(i));
			
			if (res == true) {
				
				i_res += 1;
				
			} else {//if (res == true)
				
				failed_items += 1;
				
			}//if (res == true)
			
			
		}//for (AI ai : ai_list)
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Data insertion => Inserted: " + i_res + "/"
				+ "Failed: " + failed_items);
		
		return i_res;
		
	}//private static boolean refresh_main_db_5_insert_data_into_db_ai_list()

	private static boolean refresh_main_db_2_set_up_table_refresh_history(
			SQLiteDatabase wdb, DBUtils dbu) {
		/*********************************
		 * memo
		 *********************************/
		String tname = CONS.tname_refresh_history;
		
		boolean result = dbu.tableExists(wdb, tname);
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "result=" + result);
		
		// If the table doesn't exist, create one
		if (result == false) {
		
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tname);
			
			result = 
//					dbu.createTable(wdb, tname, DBUtils.cols, DBUtils.col_types);
					dbu.createTable(
							wdb, tname,
							CONS.cols_refresh_history,
							CONS.col_types_refresh_history);
			
			if (result == false) {
			
				Log.e("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Can't create a table: "+ tname);
				
				return false;
				
			} else {//if (result == false)
				
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: "+ tname);
				
				return true;
				
			}//if (result == false)
		
		} else {//if (result == false)
		
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: "+ tname);
			
			return true;
		
		}//if (result == false)
	}//private static boolean refresh_main_db_2_set_up_table_refresh_history

	private static boolean refresh_main_db_1_set_up_table(
			SQLiteDatabase wdb, DBUtils dbu, String tname) {
		/****************************
		* 2-1.1. baseDirName
		****************************/
		
		boolean result = dbu.tableExists(wdb, tname);
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "result=" + result);
		
		// If the table doesn't exist, create one
		if (result == false) {
		
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tname);
			
			result = 
//					dbu.createTable(wdb, tname, DBUtils.cols, DBUtils.col_types);
					dbu.createTable(wdb, tname, CONS.cols_item, CONS.col_types_item);
			
			if (result == false) {
			
				Log.e("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Can't create a table: "+ tname);
				
				return false;
				
			} else {//if (result == false)
				
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: "+ tname);
				
				return true;
				
			}//if (result == false)
		
		} else {//if (result == false)
		
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: "+ tname);
			
			return true;
		
		}//if (result == false)

//		return false;
		
	}//private static boolean refreshMainDB_1_set_up_table(SQLiteDatabase wdb, DBUtils dbu)

	private static long get_last_refreshed_date(SQLiteDatabase wdb, DBUtils dbu) {
		/*********************************
		 * memo
		 *********************************/
		String sql = "SELECT * FROM " + CONS.tname_refresh_history
					+ " ORDER BY " + android.provider.BaseColumns._ID
					+ " DESC";
		
		Cursor c = null;
		
		try {
			
			c = wdb.rawQuery(sql, null);
			
		} catch (Exception e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
//			rdb.close();
			
			return -1;
		}
		
		if (c.getCount() < 1) {
			
			return -2;
			
		} else {//if (condition)
			
			c.moveToFirst();
			
			return c.getLong(3);
			
		}//if (condition)
		
		
//		return 0;
	}//private static long get_last_refreshed_date(SQLiteDatabase wdb, DBUtils dbu)

	public static boolean refreshMainDB(Activity actv, Dialog dlg) {
		/****************************
		 * Steps
		 * 1. Set up DB(writable)
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 3. Execute query for image files

		 * 4. Insert data into db
		 * 5. Update table "refresh_log"
		 * 
		 * 9. Close db
		 * 10. Return
			****************************/
		/****************************
		 * 1. Set up DB(writable)
			****************************/
		//
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/****************************
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 		1. baseDirName
		 * 		2. backupTableName
			****************************/
		boolean res = refreshMainDB_1_set_up_table(wdb, dbu);
		
		/****************************
		 * 3. Execute query for image files
			****************************/
		Cursor c = refreshMainDB_2_exec_query(actv, wdb, dbu);
		
		/****************************
		 * 4. Insert data into db
			****************************/
		int numOfItemsAdded;
		
		if (c.getCount() < 1) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Query result: 0");
			
			numOfItemsAdded = 0;
			
			// debug
			Toast.makeText(actv, "�V�K�̃t�@�C���͂���܂���", 2000).show();
			
		} else {//if (c.getCount() < 1)
			
			numOfItemsAdded = refreshMainDB_3_insert_data(actv, wdb, dbu, c);
			
		}//if (c.getCount() < 1)
		
		/****************************
		 * 9. Close db
			****************************/
		wdb.close();
		
		/****************************
		 * 10. Return
			****************************/
		return true;
		
	}//public static boolean refreshMainDB(Activity actv)

	private static int refreshMainDB_3_insert_data(Activity actv, SQLiteDatabase wdb, DBUtils dbu, Cursor c) {
		/****************************
		 * 4. Insert data into db
			****************************/
		int numOfItemsAdded = insertDataIntoDB(actv, CONS.dpath_base, c);
			
//		int numOfItemsAdded = -1;
		
		/****************************
		 * 5. Update table "refresh_log"
			****************************/
		c.moveToPrevious();
		
		long lastItemDate = c.getLong(3);
		
		updateRefreshLog(actv, wdb, dbu, lastItemDate, numOfItemsAdded);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "c.getLong(3) => " + c.getLong(3));
		

		return numOfItemsAdded;
		
	}//private static int refreshMainDB_3_insert_data(Cursor c)

	private static Cursor refreshMainDB_2_exec_query(
					Activity actv, SQLiteDatabase wdb, DBUtils dbu) {
		/****************************
		 * 3. Execute query for image files
		 * 		1. ContentResolver
		 * 		2. Uri
		 * 		3. proj
		 * 		4. Last refreshed date
		 * 		5. Execute query
			****************************/
		/****************************
		 * 3.1. ContentResolver, Uri, proj
			****************************/
		ContentResolver cr = actv.getContentResolver();
		
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        
		String[] proj = CONS.proj;

		/****************************
		 * 3.4. Last refreshed date
			****************************/
		long lastRefreshedDate = 0;		// Initial value => 0

		boolean result = dbu.tableExists(
							wdb,
//							MainActv.tableName_refreshLog);
							CONS.tname_refresh_history);
		
		if (result != false) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + CONS.tname_refresh_history);
			
			
			// REF=> http://www.accessclub.jp/sql/10.html
			String sql = "SELECT * FROM refresh_log ORDER BY " + android.provider.BaseColumns._ID + " DESC";
			
			Cursor tempC = wdb.rawQuery(sql, null);
			
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "tempC.getCount() => " + tempC.getCount());
	
			if (tempC.getCount() > 0) {
				
				tempC.moveToFirst();
				
				lastRefreshedDate = tempC.getLong(1);
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", 
						"lastRefreshedDate => " + String.valueOf(lastRefreshedDate) +
						" (I will refresh db based on this date!)");
				
			}//if (tempC.getCount() > 0)
		} else {//if (result != false)
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + CONS.tname_refresh_history);
			
			// Create one
			result = dbu.createTable(
											wdb, 
											CONS.tname_refresh_history, 
											CONS.cols_refresh_log, 
											CONS.col_types_refresh_log);
			
			if (result == true) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + CONS.tname_refresh_history);
				
			} else {//if (result == true)
				// Log
				Log.e("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create table failed: " + CONS.tname_refresh_history);
				
			}//if (result == true)
			
		}//if (result != false)
		
		/****************************
		 * 3.5. Execute query
			****************************/
		// REF=> http://blog.csdn.net/uoyevoli/article/details/4970860
		Cursor c = actv.managedQuery(
											uri, 
											proj,
											MediaStore.Images.Media.DATE_ADDED + " > ?",
											new String[] {String.valueOf(lastRefreshedDate)},
											null);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Last refreshed (in sec): " + String.valueOf(lastRefreshedDate / 1000));

        actv.startManagingCursor(c);
        
        // Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "c.getCount() => " + c.getCount());

		return c;
		
	}//private static Cursor refreshMainDB_2_exec_query()

	/****************************************
	 *	refreshMainDB_1_set_up_table(SQLiteDatabase wdb, DBUtils dbu)
	 * 
	 * <Caller> 1. <Desc> 1. <Params> 1.
	 * 
	 * <Return>
	 *  false		=> Can't create table
	 * 	true		=> Either (1) New table created, or, (2) Table exists
	 * 
	 * <Steps> 1.
	 ****************************************/
	private static boolean refreshMainDB_1_set_up_table(SQLiteDatabase wdb, DBUtils dbu) {
		/****************************
		 * 2-1.1. baseDirName
			****************************/
		String tableName = CONS.dpath_base;
		boolean result = dbu.tableExists(wdb, tableName);
		
		// If the table doesn't exist, create one
		if (result == false) {

			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tableName);
			
			result = 
					dbu.createTable(wdb, tableName, CONS.cols, CONS.col_types);
			
			if (result == false) {

				Log.e("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Can't create a table: "+ tableName);
				
				return false;
				
			} else {//if (result == false)
				
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: "+ tableName);
				
				return true;
				
			}//if (result == false)

		} else {//if (result == false)
			
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: "+ tableName);

			return true;
			
		}//if (result == false)
	}//private static boolean refreshMainDB_1_set_up_table(SQLiteDatabase wdb, DBUtils dbu)

	public static boolean refreshMainDB_async(Activity actv, AsyncTask asy) {
		/****************************
		 * Steps
		 * 1. Set up DB(writable)
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 3. Execute query for image files

		 * 4. Insert data into db
		 * 5. Update table "refresh_log"
		 * 
		 * 9. Close db
		 * 10. Return
			****************************/
		/****************************
		 * 1. Set up DB(writable)
			****************************/
		//
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/****************************
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 		1. baseDirName
		 * 		2. backupTableName
			****************************/
		boolean res = refreshMainDB_async_1_set_up_table(wdb, dbu);
		
		/****************************
		 * 3. Execute query for image files
			****************************/
		Cursor c = refreshMainDB_async_2_exec_query(actv, wdb, dbu);
		
		/****************************
		 * 4. Insert data into db
			****************************/
		int numOfItemsAdded;
		
		if (c.getCount() < 1) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Query result: 0");
			
			numOfItemsAdded = 0;
			
		} else {//if (c.getCount() < 1)
			
			numOfItemsAdded = refreshMainDB_async_3_insert_data(actv, wdb, dbu, c, asy);
			
		}//if (c.getCount() < 1)
		
		/****************************
		 * 9. Close db
			****************************/
		wdb.close();
		
		/****************************
		 * 10. Return
			****************************/
		return true;
		
	}//public static boolean refreshMainDB(Activity actv)

	private static int refreshMainDB_async_3_insert_data(
										Activity actv, SQLiteDatabase wdb, DBUtils dbu, Cursor c, AsyncTask asy) {
		/****************************
		 * 4. Insert data into db
			****************************/
		int numOfItemsAdded = insertDataIntoDB_async(actv, CONS.dpath_base, c, asy);

//		int numOfItemsAdded = -1;
		
		/****************************
		 * 5. Update table "refresh_log"
			****************************/
		c.moveToPrevious();
		
		long lastItemDate = c.getLong(3);
		
		updateRefreshLog(actv, wdb, dbu, lastItemDate, numOfItemsAdded);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "c.getLong(3) => " + c.getLong(3));
		

		return numOfItemsAdded;
		
	}//private static int refreshMainDB_3_insert_data(Cursor c)

	private static Cursor refreshMainDB_async_2_exec_query(Activity actv, SQLiteDatabase wdb, DBUtils dbu) {
		/****************************
		 * 3. Execute query for image files
		 * 		1. ContentResolver
		 * 		2. Uri
		 * 		3. proj
		 * 		4. Last refreshed date
		 * 		5. Execute query
			****************************/
		/****************************
		 * 3.1. ContentResolver, Uri, proj
			****************************/
		ContentResolver cr = actv.getContentResolver();
		
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        
		String[] proj = CONS.proj;

		/****************************
		 * 3.4. Last refreshed date
			****************************/
		long lastRefreshedDate = 0;		// Initial value => 0

		boolean result = dbu.tableExists(wdb, CONS.tname_refresh_history);
		
		if (result != false) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + CONS.tname_refresh_history);
			
			
			// REF=> http://www.accessclub.jp/sql/10.html
			String sql = "SELECT * FROM refresh_log ORDER BY " + android.provider.BaseColumns._ID + " DESC";
			
			Cursor tempC = wdb.rawQuery(sql, null);
			
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "tempC.getCount() => " + tempC.getCount());
	
			if (tempC.getCount() > 0) {
				
				tempC.moveToFirst();
				
				lastRefreshedDate = tempC.getLong(1);
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", 
						"lastRefreshedDate => " + String.valueOf(lastRefreshedDate) +
						" (I will refresh db based on this date!)");
				
			}//if (tempC.getCount() > 0)
		} else {//if (result != false)
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + CONS.tname_refresh_history);
			
			// Create one
			result = dbu.createTable(
											wdb, 
											CONS.tname_refresh_history, 
											CONS.cols_refresh_log, 
											CONS.col_types_refresh_log);
			
			if (result == true) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + CONS.tname_refresh_history);
				
			} else {//if (result == true)
				// Log
				Log.e("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create table failed: " + CONS.tname_refresh_history);
				
			}//if (result == true)
			
		}//if (result != false)
		
		/****************************
		 * 3.5. Execute query
			****************************/
		// REF=> http://blog.csdn.net/uoyevoli/article/details/4970860
		Cursor c = actv.managedQuery(
											uri, 
											proj,
											MediaStore.Images.Media.DATE_ADDED + " > ?",
											new String[] {String.valueOf(lastRefreshedDate)},
											null);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Last refreshed (in sec): " + String.valueOf(lastRefreshedDate / 1000));

        actv.startManagingCursor(c);
        
        // Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "c.getCount() => " + c.getCount());

		return c;
		
	}//private static Cursor refreshMainDB_2_exec_query()

	private static boolean refreshMainDB_async_1_set_up_table(SQLiteDatabase wdb, DBUtils dbu) {
		/****************************
		 * 2-1.1. baseDirName
			****************************/
		String tableName = CONS.dpath_base;
		boolean result = dbu.tableExists(wdb, tableName);
		
		// If the table doesn't exist, create one
		if (result == false) {

			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tableName);
			
			result = 
					dbu.createTable(wdb, tableName, CONS.cols, CONS.col_types);
			
			if (result == false) {

				Log.e("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Can't create a table: "+ tableName);
				
				return false;
				
			} else {//if (result == false)
				
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: "+ tableName);
				
				return true;
				
			}//if (result == false)

		} else {//if (result == false)
			
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: "+ tableName);

			return true;
			
		}//if (result == false)
	}//private static boolean refreshMainDB_1_set_up_table(SQLiteDatabase wdb, DBUtils dbu)

	/****************************************
	 *		insertDataIntoDB()
	 * 
	 * <Caller> 
	 * 1. private static boolean refreshMainDB_3_insert_data(Activity actv, Cursor c)
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	private static int insertDataIntoDB(Activity actv, String tableName, Cursor c) {
		/****************************
		 * Steps
		 * 0. Set up db
		 * 1. Move to first
		 * 2. Set variables
		 * 3. Obtain data
		 * 4. Insert data
		 * 5. Close db
		 * 6. Return => counter
			****************************/
		/****************************
		 * 0. Set up db
			****************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/****************************
		 * 1. Move to first
			****************************/
		c.moveToFirst();

		/****************************
		 * 2. Set variables
			****************************/
		int counter = 0;
		int counter_failed = 0;
		
		/****************************
		 * 3. Obtain data
			****************************/
		for (int i = 0; i < c.getCount(); i++) {

			String[] values = {
					String.valueOf(c.getLong(0)),
					c.getString(1),
					c.getString(2),
					String.valueOf(c.getLong(3)),
					String.valueOf(c.getLong(4))
			};

			/****************************
			 * 4. Insert data
			 * 		1. Insert data to tableName
			 * 		2. Record result
			 * 		3. Insert data to backupTableName
			 * 		4. Record result
				****************************/
			boolean blResult = 
						dbu.insertData(wdb, tableName, CONS.cols_for_insert_data, values);
				
			if (blResult == false) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "i => " + i + "/" + "c.getLong(0) => " + c.getLong(0));
				
				counter_failed += 1;
				
			} else {//if (blResult == false)
				counter += 1;
			}

			//
			c.moveToNext();
			
			if (i % 100 == 0) {
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Done up to: " + i);
				
			}//if (i % 100 == 0)
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "All data inserted: " + counter);
		
		/****************************
		 * 5. Close db
			****************************/
		wdb.close();
		
		/****************************
		 * 6. Return => counter
			****************************/
		//debug
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "counter_failed(sum): " + counter_failed);
		
		return counter;
		
	}//private static int insertDataIntoDB(Activity actv, Cursor c)

	static boolean insertDataIntoDB(
			Activity actv, String tableName, String[] types, String[] values) {
		/****************************
		* Steps
		* 1. Set up db
		* 2. Insert data
		* 3. Show message
		* 4. Close db
		****************************/
		/****************************
		* 1. Set up db
		****************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/****************************
		* 2. Insert data
		****************************/
		boolean result = dbu.insertData(wdb, tableName, types, values);
		
		/****************************
		* 3. Show message
		****************************/
		if (result == true) {
		
			// debug
			Toast.makeText(actv, "Data stored", 2000).show();
			
			/****************************
			* 4. Close db
			****************************/
			wdb.close();
			
			return true;
			
		} else {//if (result == true)
		
			// debug
			Toast.makeText(actv, "Store data => Failed", 200).show();
			
			/****************************
			* 4. Close db
			****************************/
			wdb.close();
			
			return false;
		
		}//if (result == true)
		
		/****************************
		* 4. Close db
		****************************/
	
	}//private static int insertDataIntoDB()

	
	/****************************************
	 *		insertDataIntoDB()
	 * 
	 * <Caller> 
	 * 1. private static boolean refreshMainDB_3_insert_data(Activity actv, Cursor c)
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	private static int insertDataIntoDB_async(Activity actv, String tableName, Cursor c, AsyncTask asy) {
		/****************************
		 * Steps
		 * 0. Set up db
		 * 1. Move to first
		 * 2. Set variables
		 * 3. Obtain data
		 * 4. Insert data
		 * 5. Close db
		 * 6. Return => counter
			****************************/
		/****************************
		 * 0. Set up db
			****************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/****************************
		 * 1. Move to first
			****************************/
		c.moveToFirst();

		/****************************
		 * 2. Set variables
			****************************/
		int counter = 0;
		int counter_failed = 0;
		
		/****************************
		 * 3. Obtain data
			****************************/
		for (int i = 0; i < c.getCount(); i++) {

			String[] values = {
					String.valueOf(c.getLong(0)),
					c.getString(1),
					c.getString(2),
					String.valueOf(c.getLong(3)),
					String.valueOf(c.getLong(4))
			};

			/****************************
			 * 4. Insert data
			 * 		1. Insert data to tableName
			 * 		2. Record result
			 * 		3. Insert data to backupTableName
			 * 		4. Record result
				****************************/
			boolean blResult = 
						dbu.insertData(wdb, tableName, CONS.cols_for_insert_data, values);
				
			if (blResult == false) {
				// Log
				Log.e("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "i => " + i + "/" + "c.getLong(0) => " + c.getLong(0));
				
				counter_failed += 1;
				
			} else {//if (blResult == false)
				counter += 1;
			}

			//
			c.moveToNext();
			
			if (i % 100 == 0) {
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Done up to: " + i);
				
			}//if (i % 100 == 0)
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "All data inserted: " + counter);
		
		/****************************
		 * 5. Close db
			****************************/
		wdb.close();
		
		/****************************
		 * 6. Return => counter
			****************************/
		//debug
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "counter_failed(sum): " + counter_failed);
		
		return counter;
		
	}//private static int insertDataIntoDB(Activity actv, Cursor c)

	private static boolean updateRefreshLog(
				Activity actv, SQLiteDatabase wdb, 
				DBUtils dbu, long lastItemDate, int numOfItemsAdded) {
		/****************************
		* Steps
		* 1. Table exists?
		* 2. If no, create one
		* 2-2. Create table failed => Return
		* 3. Insert data
		****************************/
		/****************************
		 * 1. Table exists?
			****************************/
		String tableName = CONS.tname_refresh_history;
		
		if(!dbu.tableExists(wdb, tableName)) {
		
			Log.e("Methods.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Table doesn't exitst: " + tableName);
		
			/****************************
			* 2. If no, create one
			****************************/
			if(dbu.createTable(wdb, tableName, 
				CONS.cols_refresh_log, CONS.col_types_refresh_log)) {
				
				//toastAndLog(actv, "Table created: " + tableName, Toast.LENGTH_LONG);
				
				// Log
				Log.d("Methods.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
				.getLineNumber() + "]", "Table created: " + tableName);
			
			} else {//if
				/****************************
				* 2-2. Create table failed => Return
				****************************/
				//toastAndLog(actv, "Create table failed: " + tableName, Toast.LENGTH_LONG);
				
				// Log
				Log.e("Methods.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
				.getLineNumber() + "]", "Create table failed: " + tableName);
				
				
				return false;
			
			}//if
		
		} else {//if(dbu.tableExists(wdb, ImageFileManager8Activity.refreshLogTableName))
		
			//toastAndLog(actv, "Table exitsts: " + tableName, 2000);
			
			// Log
			Log.d("Methods.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Table exitsts: " + tableName);
		
		
		}//if(dbu.tableExists(wdb, ImageFileManager8Activity.refreshLogTableName))
		
		/****************************
		* 3. Insert data
		****************************/
		try {
			dbu.insertData(
							wdb, 
							tableName, 
							CONS.cols_refresh_log, 
							new long[] {lastItemDate, (long) numOfItemsAdded}
			);
			
			return true;
			
		} catch (Exception e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Insert data failed");
			
			return false;
		}
		
	}//private static boolean updateRefreshLog(SQLiteDatabase wdb, long lastItemDate)

//
	public static void createFolder(Activity actv, Dialog dlg, Dialog dlg2) {
		/****************************
		 * Steps
		 * 1. Get folder name from dlg2
		 * 1-1. CheckBox => Checked?
		 * 1-2. Dismiss dlg2
		 * 2. Get current directory path
		 * 3. Create a file object
		 * 4. Create a dir

		 * 5. If successful, dismiss dialog. Otherwise, toast a message
		 * 6. Create a "list.txt"
		 * 6-2. Create a folder set if checked
		 * 
		 * 7. Refresh list view 
		 * 
		 * 8. Create a new table
			****************************/
		File newDir = Methods.createFolder_1_create_dir(actv, dlg, dlg2);
		
		boolean res = Methods.createFolder_2_create_folder_set(actv, dlg, newDir);
		
//		//debug
//		return;
		
		if (res == true) {
			
			Methods.createFolder_3_create_table(actv, dlg, newDir);
			
		} else {//if (res == true)
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "createFolder_2_create_folder_set() => false");
			
		}//if (res == true)
		
			
		
	}//public static void createFolder(Activity actv, Dialog dlg, Dialog dlg2)

	public static File createFolder_1_create_dir(Activity actv, Dialog dlg, Dialog dlg2) {
		/****************************
		 * Steps
		 * 1. Get folder name from dlg2

		 * 1-2. Dismiss dlg2
		 * 2. Get current directory path
		 * 3. Create a file object
		 * 4. Create a dir

		 * 5. If successful, dismiss dialog. Otherwise, toast a message
		 * 6. Create a "list.txt"
		 * 1-1. CheckBox => Checked?
		 * 6-2. Create a folder set if checked
		 * 
		 * 7. Refresh list view 
		 * 
		 * 8. Create a new table
			****************************/
		//
		TextView tv_folderName = (TextView) dlg2.findViewById(R.id.dlg_confirm_create_folder_tv_table_name);
		String folderName = tv_folderName.getText().toString();
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Folder name => " + tv_folderName.getText().toString());
		
		/****************************
		 * 1-2. Dismiss dlg2
			****************************/
		dlg2.dismiss();
		
		/****************************
		 * 2. Get current directory path
		 * 3. Create a file object
			****************************/
		String currentDirPath = Methods.get_currentPath_from_prefs(actv);
		
		if (currentDirPath == null) {
			
			currentDirPath = StringUtils.join(
						new String[]{
								CONS.dpath_storage_sdcard, CONS.dname_base
						},
						File.separator);
			
		}//if (currentDirPath == null)
		
		File newDir = new File(currentDirPath, folderName);
//		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "currentDirPath: " + currentDirPath + " | " + "newDir: " + newDir.getAbsolutePath());

//		//debug
//		return null;
		
		/****************************
		 * 4. Create a file => Use BufferedWriter
			****************************/
		//
		if (newDir.exists()) {
			// debug
			Toast.makeText(actv, "���̖��O�̃t�H���_�͂��łɂ���܂��I�F " + folderName, Toast.LENGTH_LONG).show();
			
			return null;
			
		} else {//if (newDir.exists())
			//
			try {
				newDir.mkdir();
				
				/****************************
				 * 5. If successful, dismiss dialog. 
					****************************/
				dlg.dismiss();
				
				// debug
				Toast.makeText(actv, "�t�H���_�����܂��� : " + newDir.getAbsolutePath(), Toast.LENGTH_LONG).show();
				
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Folder created => " + newDir.getAbsolutePath());
				
				
			} catch (Exception e) {
				// debug
				Toast.makeText(actv, "�t�H���_�����܂���ł��� : " + newDir.getName(), Toast.LENGTH_LONG).show();
				
				// Log
				Log.e("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "newDir.getName() => " + newDir.getName());
				
				return null;
			}//try
			
		}//if (newDir.exists())
		
		/****************************
		 * 6. Create a "list.txt"
			****************************/
		File listFile = new File(newDir, CONS.fname_list);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "listFile => " + listFile.getAbsolutePath());
		
		if (listFile.exists()) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "listFile => Exists");
			
			// debug
			Toast.makeText(actv, "list.txt => ���łɂ���܂�", Toast.LENGTH_LONG).show();
			
		} else {//if (listFile.exists())
			try {
				BufferedWriter br = new BufferedWriter(new FileWriter(listFile));
				
				br.close();
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "listFile => Created");
				
//				// debug
//				Toast.makeText(actv, "list.txt => �쐬����܂���", Toast.LENGTH_LONG).show();
				
			} catch (IOException e) {
				// Log
				Log.e("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create listFile => Failed: " + listFile.getAbsolutePath());
				// debug
				Toast.makeText(actv, "list.txt => �쐬�ł��܂���ł���", Toast.LENGTH_LONG).show();
				
				return null;
			}
		}//if (listFile.exists())
		
		return newDir;
		
		/****************************
		 * 6-2. Create a folder set if checked
			****************************/
		
	}//public static File createFolder_1_create_dir(Activity actv, Dialog dlg, Dialog dlg2)

	public static boolean createFolder_2_create_folder_set(Activity actv, Dialog dlg, File newDir) {
		/****************************
		 * 6-2. Create a folder set if checked
			****************************/
		CheckBox cb = (CheckBox) dlg.findViewById(R.id.dlg_create_folder_cb_folder_set);
		
		boolean checked = cb.isChecked();
		
		if (checked) {
			
			String[] folder_set = {"DO", "DONE", "LATER", "SENT_TO_PC"};
			
			for (String eachFolder : folder_set) {
				/****************************
				 * 1. Create a folder
				 * 2. list.txt
					****************************/
				/****************************
				 * 1. Create a folder
					****************************/
				File f = new File(newDir, eachFolder);
				
				boolean res = f.mkdir();
				
				/****************************
				 * 2. list.txt
					****************************/
				File listFile;
				
				if (res) {
					// Log
					Log.d("Methods.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "Folder set: " + f.getAbsolutePath());
					
					
					listFile = new File(f, CONS.fname_list);
					
					try {
						BufferedWriter br = new BufferedWriter(new FileWriter(listFile));
						
						br.close();
						
						// Log
						Log.d("Methods.java"
								+ "["
								+ Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]", "listFile => Created");
						
						// debug
						Toast.makeText(actv, "list.txt => �쐬����܂���", Toast.LENGTH_LONG).show();
						
					} catch (IOException e) {
						// Log
						Log.e("Methods.java"
								+ "["
								+ Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]", "Create listFile => Failed: " + listFile.getAbsolutePath());
						// debug
						Toast.makeText(actv, "list.txt => �쐬�ł��܂���ł���", Toast.LENGTH_LONG).show();
					}
					
				}//if (res)
				
				
			}//for (String eachFolder : folder_set)
			
		}//if (checked)
		
		return true;
		
		/****************************
		 * 7. Refresh list viewFile 
			****************************/
		
	}//public static boolean createFolder_2_create_folder_set(Activity actv, Dialog dlg, Dialog dlg2)

	public static boolean createFolder_3_create_table(Activity actv, Dialog dlg, File newDir) {
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starts => createFolder_3_create_table()");
		
		/****************************
		 * 7. Refresh list view
			****************************/
		Methods.refresh_list_view(actv);
		
//		//debug
//		return false;
		
		/****************************
		 * 8. Create a new table
		 * 		8.1. Build a table name
		 * 		8.2. Create a table
			****************************/
		/****************************
		 * 8.1. Build a table name
			****************************/
		String newPath = newDir.getAbsolutePath();

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "newPath=" + newPath);
		
		String convertedPath = Methods.convert_prefs_into_path_label(actv, newPath);
		
		String tableName = Methods.convert_path_into_table_name(actv, convertedPath);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "New table name => " + tableName);

//		//debug
//		return false;


		/****************************
		 * 8.2. Create a table
			****************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		boolean res = dbu.createTable(wdb, tableName, 
//					dbu.get_cols(), dbu.get_col_types());
//							DBUtils.cols, DBUtils.col_types);
					CONS.cols_item, CONS.col_types_item);
		
		wdb.close();
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Methods.createFolder() => Done");
		
		return res;
		
	}//public static boolean createFolder_3_create_table(Activity actv, Dialog dlg, Dialog dlg2)

	public static void removeFolder(Activity actv, Dialog dlg) {
		/****************************
		 * Steps
		 * 1. Get folder name
		 * 2. Validate
		 * 3. Remove
		 * 4. Refresh list
		 * 5. Dismiss dialog
		 * 
		 * 6. Drop table
			****************************/
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "removeFolder()");
		
		/****************************
		 * 1. Get folder name
			****************************/
		TextView tv = (TextView) dlg.findViewById(R.id.dlg_confirm_remove_folder_tv_table_name);
		String folderName = tv.getText().toString();
		
		String current_path = Methods.get_currentPath_from_prefs(actv);
		//
//		File targetDir = new File(Methods.get_currentPath_from_prefs(actv), folderName);
		File targetDir = new File(current_path, folderName);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "current_path=" + current_path);
		
		
		if (!targetDir.exists()) {
			// debug
			Toast.makeText(actv, "���̃A�C�e���́A���݂��܂���", 2000).show();
			
			return;
		}
		
		if (!targetDir.isDirectory()) {
			// debug
			Toast.makeText(actv, "���̃A�C�e���́A�t�H���_�ł͂���܂���", 2000).show();
			
			return;
		}//if (!targetDir.exists() || !targetDir.isDirectory())
		
		/****************************
		 * 3. Remove
			****************************/
		String path = targetDir.getAbsolutePath();
		
		boolean result = deleteDirectory(targetDir);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "result => " + result);

		if (result == true) {
			/****************************
			 * 5. Dismiss dialog
				****************************/
			dlg.dismiss();
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Dir => Removed: " + path);
			
			// debug
			Toast.makeText(actv, "�폜���܂���" + path, Toast.LENGTH_LONG).show();
		} else {//if (result == true)
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Remove dir => Failed: " + path);
			
			// debug
			Toast.makeText(actv, "�폜�ł��܂���ł���: " + path, Toast.LENGTH_LONG).show();
			
			return;
		}//if (result == true)
		
		/****************************
		 * 4. Refresh list
			****************************/
		refresh_list_view(actv);
		
		/****************************
		 * 6. Drop table
			****************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase db = dbu.getWritableDatabase();
		
//		String tableName = Methods.convertPathIntoTableName(actv, targetDir);
//		String tableName = Methods.convert_path_into_table_name(actv, targetDir.getAbsolutePath());
		String tableName = Methods.convert_prefs_into_path_label(actv, targetDir.getAbsolutePath());
		
		tableName = Methods.convert_path_into_table_name(actv, tableName);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "tableName => " + tableName);
		
		dbu.dropTable(actv, db, tableName);

		db.close();
		
		return;
		
	}//public static void removeFolder(Activity actv, Dialog dlg)

	/****************************
	 * deleteDirectory(File target)()
	 * 
	 * 1. REF=> http://www.rgagnon.com/javadetails/java-0483.html
		****************************/
	public static boolean deleteDirectory(File target) {
		
		if(target.exists()) {
			//
			File[] files = target.listFiles();
			
			//
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					
					deleteDirectory(files[i]);
					
				} else {//if (files[i].isDirectory())
					
					String path = files[i].getAbsolutePath();
					
					files[i].delete();
					
					// Log
					Log.d("Methods.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "Removed => " + path);
					
					
				}//if (files[i].isDirectory())
				
			}//for (int i = 0; i < files.length; i++)
			
		}//if(target.exists())
		
		return (target.delete());
	}//public static boolean deleteDirectory(File target)

	public static void sort_tiList(List<TI> tiList) {
		
		Collections.sort(tiList, new Comparator<TI>(){

//			@Override
			public int compare(TI lhs, TI rhs) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				
//				return (int) (lhs.getDate_added() - rhs.getDate_added());
				
				return (int) (lhs.getFile_name().compareToIgnoreCase(rhs.getFile_name()));
			}
			
		});//Collections.sort()

	}//public static void sort_tiList(List<ThumbnailItem> tiList)

	public static void sort_tiList_last_viewed_at(List<TI> tiList) {
		
		Collections.sort(tiList, new Comparator<TI>(){

//			@Override
			public int compare(TI ti_1, TI ti_2) {
				/*********************************
				 * memo
				 *********************************/
				long t1 = ti_1.getLast_viewed_at();
				long t2 = ti_2.getLast_viewed_at();
				
//				if (t1 > 0 && t2 > 0) {
				if (t1 > 0 || t2 > 0) {
					
					// REF=> http://stackoverflow.com/questions/4355303/how-can-i-convert-a-long-to-int-in-java
					return (int)(t1 - t2);
					
				} else {//if (t1 == condition)
					
					return ti_1.getFile_name().compareToIgnoreCase(ti_2.getFile_name());
					
				}
				
//				return (int) (ti_1.getDate_added() - rti.getDate_added());
				
//				return (int) (lti.getFile_name().compareToIgnoreCase(rti.getFile_name()));
			}//public int compare(TI lti, TI rti)
			
		});//Collections.sort()

	}//public static void sort_tiList(List<ThumbnailItem> tiList)

	public static boolean set_pref(Activity actv, String pref_name, String value) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/****************************
		 * 2. Get editor
			****************************/
		SharedPreferences.Editor editor = prefs.edit();

		/****************************
		 * 3. Set value
			****************************/
		editor.putString(pref_name, value);
		
		try {
			editor.commit();
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean set_pref(String pref_name, String value)

	public static String get_pref(
						Activity actv,
						String pref_name,
						String defValue) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/****************************
		 * Return
			****************************/
		return prefs.getString(pref_name, defValue);

	}//public static boolean set_pref(String pref_name, String value)

	public static String get_pref(
					Activity actv,
					String pref_name,
					String pref_key,
					String defValue) {
		SharedPreferences prefs = 
			actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);
		
		/****************************
		* Return
		****************************/
		return prefs.getString(pref_key, defValue);
		
	}//public static boolean set_pref(String pref_name, String value)

	public static boolean set_pref(Activity actv, String pref_name, int value) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/****************************
		 * 2. Get editor
			****************************/
		SharedPreferences.Editor editor = prefs.edit();

		/****************************
		 * 3. Set value
			****************************/
		editor.putInt(pref_name, value);
		
		try {
			editor.commit();
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean set_pref(String pref_name, String value)

	public static boolean
	setPref_long
	(Activity actv, String pref_name, String pref_key, long value) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/****************************
		 * 2. Get editor
			****************************/
		SharedPreferences.Editor editor = prefs.edit();

		/****************************
		 * 3. Set value
			****************************/
		editor.putLong(pref_key, value);
		
		try {
			editor.commit();
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean setPref_long(Activity actv, String pref_name, String pref_key, long value)

	public static boolean set_pref(Activity actv, String pref_name, String pref_key, int value) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/****************************
		 * 2. Get editor
			****************************/
		SharedPreferences.Editor editor = prefs.edit();

		/****************************
		 * 3. Set value
			****************************/
		editor.putInt(pref_key, value);
		
		try {
			editor.commit();
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean set_pref(String pref_name, String value)

	public static boolean set_pref(Activity actv,
								String pref_name, String pref_key,
								String value) {
		
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/****************************
		 * 2. Get editor
			****************************/
		SharedPreferences.Editor editor = prefs.edit();

		/****************************
		 * 3. Set value
			****************************/
		editor.putString(pref_key, value);
		
		try {
			editor.commit();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Pref set => " + pref_key + "/" + value);
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean set_pref(String pref_name, String value)

	public static int get_pref(Activity actv, String pref_name, int defValue) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/****************************
		 * Return
			****************************/
		return prefs.getInt(pref_name, defValue);

	}//public static boolean set_pref(String pref_name, String value)

	public static long
	getPref_long
	(Activity actv, String pref_name, String pref_key, long defValue) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/****************************
		 * Return
			****************************/
		return prefs.getLong(pref_key, defValue);

	}//public static boolean getPref_long(Activity actv, String pref_name, String pref_key, long defValue)

	public static int get_pref(Activity actv, String pref_name, String pref_key, int defValue) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/****************************
		 * Return
			****************************/
		return prefs.getInt(pref_key, defValue);

	}//public static boolean set_pref(String pref_name, String value)

	public static void moveFiles(Activity actv, Dialog dlg1, Dialog dlg2) {
		/****************************
		 * Steps
		 * 1. Move files
		 * 2. Update the list view
		 * 2-2. Update preference for highlighting a chosen item
		 * 3. Dismiss dialogues
			****************************/
		/****************************
		 * 1. Move files
		 * 		1.1. Prepare toMoveFiles
		 * 		1.2. Get target dir path from dlg2
		 * 		1.3. Insert items in toMoveFiles to the new table
		 * 		1.4. Delete the items from the source table
			****************************/
		List<AI> toMoveFiles = Methods.moveFiles_1_get_toMoveFiles();
		
		String dstTableName = Methods.moveFiles__2_getDstTableName(actv, dlg2);

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "dstTableName=" + dstTableName);
		
		/***************************************
		 * Move files
		 ***************************************/
		for (int i = 0; i < toMoveFiles.size(); i++) {
			
			AI ai = toMoveFiles.get(i);
			
			String srcTableName = ai.getTable_name();
			
			Methods.moveFiles__3_db(actv, srcTableName, dstTableName, ai);
			
		}//for (int i = 0; i < toMoveFiles.size(); i++)

		/***************************************
		 * Clear checkedPositions
		 ***************************************/
		for (Integer position : ALActv.checkedPositions) {
			
//			ALActv.ai_list_move.remove(position);
			ALActv.aiList.remove(position);
			
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Removed from ai_list at position=" + position);
			
			
		}//for (Integer position : ThumbnailActivity.checkedPositions)
		
		//
		ALActv.checkedPositions.clear();
		
		//
		ALActv.ail_adp_move.notifyDataSetChanged();

		/***************************************
		 * Update table names in "bm" table
		 ***************************************/
		Methods.moveFiles__4_updateBM(actv, toMoveFiles, dstTableName);
		
		/***************************************
		 * Update: Table names in "history" table
		 ***************************************/
		Methods.moveFiles__5_updateHI(actv, toMoveFiles, dstTableName);
		
		/****************************
		 * 3. Dismiss dialogues
			****************************/
		dlg1.dismiss();
		dlg2.dismiss();
		
	}//public static void moveFiles(Activity actv, Dialog dlg1, Dialog dlg2)
	
	private static void
	moveFiles__5_updateHI
	(Activity actv, List<AI> toMoveFiles, String dstTableName) {
		/***************************************
		 * Setup: DB
		 ***************************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/***************************************
		 * Loop: List<AI>
		 ***************************************/
		for (int i = 0; i < toMoveFiles.size(); i++) {
			
			AI ai = toMoveFiles.get(i);
			
			Cursor c = null;
			
			try {
				// Get bm record where its "ai_id" is a certain value
				c = wdb.query(
						CONS.History.tname_history,
//						CONS.DB.cols_bm,
						CONS.History.cols_history_full,
						"aiId=?",
						new String[]{String.valueOf(ai.getDb_id())},
						null, null, null);
				
				if (c == null) {
					
					// Log
					Log.d("Methods.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber()
							+ ":"
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName() + "]", "c == null");
					
					continue;
					
				}//if (c == null)
				
			} catch (Exception e) {

				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber()
						+ ":"
						+ Thread.currentThread().getStackTrace()[2]
								.getMethodName() + "]",
						e.toString());
				
				continue;
			}
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "c.getCount()=" + c.getCount());
			
			/***************************************
			 * Get: List<HI> (from the cursor)
			 ***************************************/
//			List<BM> bmList = Methods_CM5.getBMList_FromCursor(actv, c);
			List<HI> hiList = Methods_CM5.getHIList_FromCursor(actv, c);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "ai.getFile_name()=" + ai.getFile_name());
			
			//debug
			if (hiList != null) {

				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ ":"
						+ Thread.currentThread().getStackTrace()[2].getMethodName()
						+ "]", "hiList.size()=" + hiList.size());
				
				/***************************************
				 * Loop: List<BM>
				 ***************************************/
//				DBUtils dbu = new DBUtils(actv, CONS.dbName);
				dbu = new DBUtils(actv, CONS.dbName);
				
				for (int j = 0; j < hiList.size(); j++) {
					
					HI hi = hiList.get(j);
				
					boolean res = dbu.updateData_generic(
									actv,
									CONS.History.tname_history,
									hi.getDbId(),
									"aiTableName",
									dstTableName);
					
					// Log
					Log.d("Methods.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber()
							+ ":"
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName() + "]", "res=" + res);
					
				}//for (int j = 0; j < bmList.size(); j++)	// Loop: List<BM>
				
			} else {//if (bmList == condition)
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber()
						+ ":"
						+ Thread.currentThread().getStackTrace()[2]
								.getMethodName() + "]", "hiList => null");
				
				continue;
				
			}//if (bmList == condition)
			
		}//for (int i = 0; i < toMoveFiles.size(); i++) // Loop: List<AI>
		
		/***************************************
		 * Close: DB
		 ***************************************/
		wdb.close();
		
	}//moveFiles__5_updateHI(Activity actv, List<AI> toMoveFiles, String dstTableName)
	

	private static void
	moveFiles__4_updateBM(Activity actv, List<AI> toMoveFiles, String dstTableName) {
		/***************************************
		 * Setup: DB
		 ***************************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/***************************************
		 * Loop: List<AI>
		 ***************************************/
		for (int i = 0; i < toMoveFiles.size(); i++) {
			
			AI ai = toMoveFiles.get(i);
			
			Cursor c = null;
			
			try {
				// Get bm record where its "ai_id" is a certain value
				c = wdb.query(
						CONS.DB.tname_BM,
//						CONS.DB.cols_bm,
						CONS.DB.cols_bm_full,
						"ai_id=?",
						new String[]{String.valueOf(ai.getDb_id())},
						null, null, null);
				
				if (c == null) {
					
					// Log
					Log.d("Methods.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber()
							+ ":"
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName() + "]", "c == null");
					
					continue;
					
				}//if (c == null)
				
			} catch (Exception e) {

				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber()
						+ ":"
						+ Thread.currentThread().getStackTrace()[2]
								.getMethodName() + "]",
						e.toString());
				
				continue;
			}
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "c.getCount()=" + c.getCount());
			
			/***************************************
			 * Get: List<BM> (from the cursor)
			 ***************************************/
			List<BM> bmList = Methods_CM5.getBMList_FromCursor(actv, c);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "ai.getFile_name()=" + ai.getFile_name());
			
			//debug
			if (bmList != null) {

				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ ":"
						+ Thread.currentThread().getStackTrace()[2].getMethodName()
						+ "]", "bmList.size()=" + bmList.size());
				
				/***************************************
				 * Loop: List<BM>
				 ***************************************/
//				DBUtils dbu = new DBUtils(actv, CONS.dbName);
				dbu = new DBUtils(actv, CONS.dbName);
				
				for (int j = 0; j < bmList.size(); j++) {
					
					BM bm = bmList.get(j);
				
					boolean res = dbu.updateData_bm(
									actv,
									bm.getDbId(),
									"aiTableName",
									dstTableName);
					
					// Log
					Log.d("Methods.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber()
							+ ":"
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName() + "]", "res=" + res);
					
				}//for (int j = 0; j < bmList.size(); j++)	// Loop: List<BM>
				
			} else {//if (bmList == condition)
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber()
						+ ":"
						+ Thread.currentThread().getStackTrace()[2]
								.getMethodName() + "]", "bmList => null");
				
				continue;
				
			}//if (bmList == condition)
			
		}//for (int i = 0; i < toMoveFiles.size(); i++) // Loop: List<AI>
		
		/***************************************
		 * Close: DB
		 ***************************************/
		wdb.close();
		
	}//moveFiles__4_updateBM(Activity actv, List<AI> toMoveFiles)
	

	public static void
	moveFiles_search(Activity actv, Dialog dlg1, Dialog dlg2) {
		/****************************
		 * Steps
		 * 1. Move files
		 * 2. Update the list view
		 * 2-2. Update preference for highlighting a chosen item
		 * 3. Dismiss dialogues
			****************************/
		/****************************
		 * 1. Move files
		 * 		1.1. Prepare toMoveFiles
		 * 		1.2. Get target dir path from dlg2
		 * 		1.3. Insert items in toMoveFiles to the new table
		 * 		1.4. Delete the items from the source table
			****************************/
//		List<AI> toMoveFiles = Methods.moveFiles_1_get_toMoveFiles();
//		List<AI> toMoveFiles = CONS.Search.aiList;
		
		CONS.Search.toMoveList = Methods.moveFiles_search__1_get_toMoveFiles();
		
		String dstTableName = Methods.moveFiles__2_getDstTableName(actv, dlg2);

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "dstTableName=" + dstTableName);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]",
				"CONS.Search.toMoveList.size()=" + CONS.Search.toMoveList.size());
		
//		for (AI ai : CONS.Search.toMoveList) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ ":"
//					+ Thread.currentThread().getStackTrace()[2].getMethodName()
//					+ "]", "ai.getFile_name()=" + ai.getFile_name());
//			
//		}//for (Ai ai : CONS.Search.toMoveList)
		
		/***************************************
		 * Move files
		 ***************************************/

		for (int i = 0; i < CONS.Search.toMoveList.size(); i++) {
			
			AI ai = CONS.Search.toMoveList.get(i);
			
			String srcTableName = ai.getTable_name();
			
			Methods.moveFiles__3_db(actv, srcTableName, dstTableName, ai);

		}//for (int i = 0; i < toMoveFiles.size(); i++)

		/***************************************
		 * Clear checkedPositions
		 ***************************************/
		for (Integer position : CONS.Search.checkedPositions) {
			
//			ALActv.ai_list_move.remove(position);
			CONS.Search.aiList.remove(position);
			
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Removed from ai_list at position=" + position);
			
			
		}//for (Integer position : ThumbnailActivity.checkedPositions)
		
		//
		CONS.Search.checkedPositions.clear();
		
		/***************************************
		 * Notify adapters
		 ***************************************/
		SearchActv.ail_adp_move.notifyDataSetChanged();
		SearchActv.ail_adp.notifyDataSetChanged();
		
		/****************************
		 * 1.3. Insert items in toMoveFiles to the new table
		 * 		1.3.1. Insert data to the new table
			****************************/
		/****************************
		 * 1.3.1. Insert data to the new table
		 * 		1. Set up db
		 * 		2. Table exists?
		 * 		2-2. If no, create one
		 * 		3. Get item from toMoveFiles
		 * 
		 * 		4. Insert data into the new table
			****************************/

		/****************************
		 * 3. Dismiss dialogues
			****************************/
		dlg1.dismiss();
		dlg2.dismiss();
		
	}//public static void moveFiles_search(Activity actv, Dialog dlg1, Dialog dlg2)

	private static
	List<AI> moveFiles_search__1_get_toMoveFiles() {
		
		List<AI> toMoveList = new ArrayList<AI>();
		
		for (Integer id : CONS.Search.checkedPositions) {
			
			AI ai = CONS.Search.aiList.get(id.intValue());
			
			toMoveList.add(ai);
			
		}//for (Long id : CONS.Search.checkedPositions)
		
		return toMoveList;
		
	}//List<AI> moveFiles_search__1_get_toMoveFiles()

	private static
	String moveFiles__2_getDstTableName(Activity actv, Dialog dlg2) {
		// TODO Auto-generated method stub
		/****************************
		 * 1.2. Get target dir path from dlg2
			****************************/
		TextView tv = (TextView) dlg2.findViewById(R.id.dlg_confirm_move_files_tv_table_name);
		
		String folderPath = tv.getText().toString();
		
		File f = new File(CONS.dpath_base, folderPath);
		
//		String targetTableName = Methods.convert_path_into_table_name(actv, folderPath);
		String dstTargetTableName = Methods.convert_filePath_into_table_name(actv, f.getAbsolutePath());
		
		return dstTargetTableName;
		
	}//String moveFiles__2_getDstTableName(Activity actv, Dialog dlg2)

	private static void
	moveFiles__3_db
	(Activity actv,
		String sourceTableName,
		String targetTableName,
//		List<AI> toMoveFiles) {
		AI ai) {
		
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/****************************
		 * 1.3.1.2. Table exists?
			****************************/
		boolean res = moveFiles_2_table_exists(actv, wdb, dbu, targetTableName);
		
		if (res == false) {
			
			return;
			
		}//if (res == false)
		
		/****************************
		 * 1.3.1.3. Get item from toMoveFiles
			****************************/
		/****************************
		 * 1.3.4. Insert data into the new table
			****************************/
		// Change the table name
		ai.setTable_name(targetTableName);
		
		// Insert into the target table
		dbu.insertData_ai(wdb, targetTableName, ai);
		
		// Delete from the source table
		Methods.deleteItem_fromTable_ai(actv, sourceTableName, ai);
			
		/****************************
		 * 1.4. Delete the items from the source table
		 * 		1. Delete data from the source table
		 * 		2. Delete the item from tiList
		 * 
		 * 		9. Close db
			****************************/
		/****************************
		 * 1.4.2. Delete the item from tiList
			****************************/
//		for (Integer position : ALActv.checkedPositions) {
//			
////			ALActv.ai_list_move.remove(position);
//			ALActv.ai_list.remove(position);
//			
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Removed from ai_list at position=" + position);
//			
//			
//		}//for (Integer position : ThumbnailActivity.checkedPositions)
//		
//		//
//		ALActv.checkedPositions.clear();
//		
//		//
////		ALActv.ail_adp_move.notifyDataSetChanged();
		
		/****************************
		 * 1.4.9. Close wdb
			****************************/
		wdb.close();

	}//moveFiles__3_db

	private static void moveFiles_4_refresh_list(
						Activity actv, String sourceTableName) {
		
		ALActv.aiList.clear();
		
		ALActv.aiList.addAll(Methods.get_all_data_ai(actv, sourceTableName));
		
		Methods.sort_list_ai_created_at(ALActv.aiList, CONS.SORT_ORDER.DEC);
		
		ALActv.ail_adp_move.notifyDataSetChanged();
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "ALActv.ail_adp_move => Notified");
		
	}//private static void moveFiles_4_refresh_list

	private static void moveFiles_3_db(Activity actv,
			String sourceTableName, String targetTableName, List<AI> toMoveFiles) {
		
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/****************************
		 * 1.3.1.2. Table exists?
			****************************/
		boolean res = moveFiles_2_table_exists(actv, wdb, dbu, targetTableName);
		
		if (res == false) {
			
			return;
			
		}//if (res == false)
		
		/****************************
		 * 1.3.1.3. Get item from toMoveFiles
			****************************/
		for (AI ai : toMoveFiles) {
			
			/****************************
			 * 1.3.4. Insert data into the new table
				****************************/
			// Change the table name
			ai.setTable_name(targetTableName);
			
			// Insert into the target table
			dbu.insertData_ai(wdb, targetTableName, ai);
			
			// Delete from the source table
			Methods.deleteItem_fromTable_ai(actv, sourceTableName, ai);
			
		}//for (ThumbnailItem thumbnailItem : toMoveFiles)
		
		
		/****************************
		 * 1.4. Delete the items from the source table
		 * 		1. Delete data from the source table
		 * 		2. Delete the item from tiList
		 * 
		 * 		9. Close db
			****************************/
		/****************************
		 * 1.4.2. Delete the item from tiList
			****************************/
		for (Integer position : ALActv.checkedPositions) {
			
//			ALActv.ai_list_move.remove(position);
			ALActv.aiList.remove(position);
			
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Removed from ai_list at position=" + position);
			
			
		}//for (Integer position : ThumbnailActivity.checkedPositions)
		
		//
		ALActv.checkedPositions.clear();
		
		//
//		ALActv.ail_adp_move.notifyDataSetChanged();
		
		/****************************
		 * 1.4.9. Close wdb
			****************************/
		wdb.close();

	}//private static void moveFiles_3_db

	private static String[] moveFiles_2_setup_paths(Activity actv, Dialog dlg2) {
		// TODO Auto-generated method stub
		/****************************
		 * 1.2. Get target dir path from dlg2
			****************************/
		TextView tv = (TextView) dlg2.findViewById(R.id.dlg_confirm_move_files_tv_table_name);
		
		String folderPath = tv.getText().toString();
		
		File f = new File(CONS.dpath_base, folderPath);
		
//		String targetTableName = Methods.convert_path_into_table_name(actv, folderPath);
		String targetTableName = Methods.convert_filePath_into_table_name(actv, f.getAbsolutePath());
		
		String sourceTableName = Methods.convert_path_into_table_name(actv);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "folderPath => " + folderPath);

		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "targetTableName => " + targetTableName);
		
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "sourceTableName => " + sourceTableName);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", 
				"prefs_current_path: " 
				+ Methods.get_pref(
						actv,
						CONS.pname_current_path,
						CONS.pkey_current_path,
						"NO DATA"));
		
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "f.getAbsolutePath(): " + f.getAbsolutePath());

		return new String[]{sourceTableName, targetTableName};
		
	}//private static void moveFiles_2_setup_paths

	private static boolean moveFiles_2_table_exists(Activity actv, 
					SQLiteDatabase wdb, DBUtils dbu, String targetTableName) {
		
		boolean result = dbu.tableExists(wdb, targetTableName);
		
		if (result == false) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + targetTableName);
			
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Now I create one");
			
			/****************************
			 * 1.3.2-2. If no, create one
				****************************/
			result = dbu.createTable(
								wdb, targetTableName, 
								dbu.get_cols(), dbu.get_col_types());
			
			if (result == false) {
				
				// Log
				Log.e("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Can't create a table: " + targetTableName);
				
				wdb.close();
				
				return false;
				
			} else {//if (result == false)
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + targetTableName);
				
				return true;
				
			}//if (result == false)
			
		} else {//if (result == true)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + targetTableName);
			
			return true;
			
		}//if (result == true)
	}//private static boolean moveFiles_2_table_exists()

	public static List<AI> moveFiles_1_get_toMoveFiles() {
		/****************************
		 * 1. Move files
		 * 		1.1. Prepare toMoveFiles
		 * 		1.2. Get target dir path from dlg2
		 * 		1.3. Insert items in toMoveFiles to the new table
		 * 		1.4. Delete the items from the source table
			****************************/
		//
		List<AI> toMoveFiles = new ArrayList<AI>();
		
		for (int position : ALActv.checkedPositions) {
			
			toMoveFiles.add(ALActv.aiList.get(position));
			
		}//for (int position : ThumbnailActivity.checkedPositions)
		
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "toMoveFiles.size() => " + toMoveFiles.size());
		
		for (AI ai : toMoveFiles) {
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "ai.getFile_name() => " + ai.getFile_name());
		}
		
		return toMoveFiles;
		
	}//public static List<TI> moveFiles_1_get_toMoveFiles(Activity actv, Dialog dlg1, Dialog dlg2)

	public static void deleteItem_fromTable(Activity actv, String tableName, TI ti) {
		/****************************
		 * 1. db setup
		 * 2. Delete data
		 * 3. Close db
		 * 4. If unsuccesful, toast a message (Not dismiss the dialog)
		 * 4-2. If successful, delete the item from tiList, as well, and,
		 * #4-3. Notify adapter
		 * 5. Dismiss dialog
			****************************/
		
		/****************************
		 * 1. db setup
			****************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/****************************
		 * 2. Delete data
			****************************/
		boolean result = dbu.deleteData(
							actv,
							wdb, 
//							Methods.convertPathIntoTableName(actv),
							tableName,
							ti.getFileId());
		
		/****************************
		 * 3. Close db
			****************************/
		wdb.close();
		
		/****************************
		 * 4. If unsuccesful, toast a message (Not dismiss the dialog)
			****************************/
		if (result == false) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Data wasn't deleted: " + ti.getFile_name());
			
		} else if (result == true) {//if (result == true)
			/****************************
			 * 4-2. If successful, delete the item from tiList, as well
				****************************/
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Data was deleted: " + ti.getFile_name());

//			ThumbnailActivity.tiList.remove(position);
			
//			// Log
//			Log.d("DialogOnItemClickListener.java"
//					+ "["
//					+ Thread.currentThread().getStackTrace()[2]
//							.getLineNumber() + "]", "Data removed from tiList => " + ti.getFile_name());
			
//			/****************************
//			 * 4-3. Notify adapter
//				****************************/
//			ThumbnailActivity.aAdapter.notifyDataSetChanged();
			
		}//if (result == true)
		
//		/****************************
//		 * 5. Notify adapter
//			****************************/
//		ThumbnailActivity.aAdapter.notifyDataSetChanged();
//		
//		/****************************
//		 * 5. Dismiss dialog
//			****************************/
//		dlg.dismiss();

	}//public static void deleteItem_fileId(Activity actv, TI ti, int position)

	public static void deleteItem_fromTable_ai(Activity actv, String tableName, AI ai) {
		/****************************
		 * 1. db setup
		 * 2. Delete data
		 * 3. Close db
		 * 4. If unsuccesful, toast a message (Not dismiss the dialog)
		 * 4-2. If successful, delete the item from tiList, as well, and,
		 * #4-3. Notify adapter
		 * 5. Dismiss dialog
			****************************/
		
		/****************************
		 * 1. db setup
			****************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/****************************
		 * 2. Delete data
			****************************/
		boolean result = dbu.deleteData_ai(
							actv,
							wdb, 
//							Methods.convertPathIntoTableName(actv),
							tableName,
							ai.getDb_id());
		
		/****************************
		 * 3. Close db
			****************************/
		wdb.close();
		
		/****************************
		 * 4. If unsuccesful, toast a message (Not dismiss the dialog)
			****************************/
		if (result == false) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Data wasn't deleted: " + ai.getFile_name());
			
		} else if (result == true) {//if (result == true)
			/****************************
			 * 4-2. If successful, delete the item from tiList, as well
				****************************/
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Data was deleted: " + ai.getFile_name());

//			ThumbnailActivity.tiList.remove(position);
			
//			// Log
//			Log.d("DialogOnItemClickListener.java"
//					+ "["
//					+ Thread.currentThread().getStackTrace()[2]
//							.getLineNumber() + "]", "Data removed from tiList => " + ti.getFile_name());
			
//			/****************************
//			 * 4-3. Notify adapter
//				****************************/
//			ThumbnailActivity.aAdapter.notifyDataSetChanged();
			
		}//if (result == true)
		
//		/****************************
//		 * 5. Notify adapter
//			****************************/
//		ThumbnailActivity.aAdapter.notifyDataSetChanged();
//		
//		/****************************
//		 * 5. Dismiss dialog
//			****************************/
//		dlg.dismiss();

	}//public static void deleteItem_fromTable_ai(Activity actv, String tableName, AI ai)

	public static TI getData(Activity actv, String tableName, long file_id) {

		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		TI ti = dbu.getData(actv, rdb, tableName, file_id);
		
		rdb.close();
		
		return ti;
		
	}//public ThumbnailItem getData(Activity actv, String tableName, long file_id)

	public static void addMemo(Activity actv, Dialog dlg, long file_id, String tableName) {
		/****************************
		 * Steps
		 * 1. Get tuhumbnail item
		 * 1-2. Get text from edit text
		 * 2. Set memo
		 * 3. Update db
		 * 
		 * 4. Refresh thumbnails list
			****************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		TI ti = dbu.getData(actv, rdb, tableName, file_id);
		
		rdb.close();
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "DB => closed");
//		
//		toastAndLog(actv, ti.getFile_name() + "/" + "memo=" + ti.getMemo(), 2000);
		
		/****************************
		 * 1-2. Get text from edit text
			****************************/
		EditText et = (EditText) dlg.findViewById(R.id.dlg_add_memos_et_content);
		
		/****************************
		 * 2. Set memo
			****************************/
//		ti.setMemo("abcdefg");
//		ti.setMemo("123456");
//		ti.setMemo("WHERE����ȗ������ꍇ�̓e�[�u���Ɋ܂܂��S�Ẵf�[�^�̎w��̃J�����̒l���w��̒l�ōX�V����܂��B");
		
		ti.setMemo(et.getText().toString());
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "New memo => " + et.getText().toString());
		
		/****************************
		 * 3. Update db
			****************************/
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		boolean result = dbu.updateData_memos(actv, wdb, tableName, ti);
		
		wdb.close();
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "wdb => Closed");
		
		if (result) {
			
			dlg.dismiss();

			// debug
			Toast.makeText(actv, "Memo => Stored", Toast.LENGTH_LONG).show();

		} else {//if (result)
			
			return;
			
		}//if (result)
		
		/****************************
		 * 4. Refresh thumbnails list
			****************************/
//		refreshTIList(actv);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Calling: Methods.refresh_list_view(actv)");
				+ "]", "Calling: Methods.refresh_tilist(actv)");
		
		
//		Methods.refresh_list_view(actv);
		Methods.refresh_tilist(actv);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "TIList => Refreshed");
		
	}//public static void addMemo()

	private static void refresh_tilist(Activity actv) {
		/****************************
		 * 1. Get table name
		 * 2. Clear tiList
		 * 3. Get data to list
		 * 
		 * 4. Sort list
		 * 5. Notify to adapter
			****************************/
		
		String currentPath = Methods.get_currentPath_from_prefs(actv);
		
		String tableName = Methods.convert_filePath_into_table_name(actv, currentPath);

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "currentPath: " + currentPath);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "tableName: " + tableName);
		
		
		/****************************
		 * 2. Clear tiList
			****************************/
		TNActv.tiList.clear();
		
		/****************************
		 * 3. Get data to list
			****************************/
		if (TNActv.long_searchedItems == null) {

			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "TNActv.long_searchedItems == null");
			
			
//			TNActv.tiList = Methods.getAllData(actv, tableName);
			TNActv.tiList.addAll(Methods.getAllData(actv, tableName));
			
		} else {//if (long_searchedItems == null)

//			tiList = Methods.convert_fileIdArray2tiList(actv, "IFM8", long_searchedItems);
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "TNActv.long_searchedItems != null");
			
		}//if (long_searchedItems == null)

		/****************************
		 * 4. Sort list
			****************************/
		Methods.sort_tiList(TNActv.tiList);
		
		
		/****************************
		 * 5. Notify to adapter
			****************************/
		TNActv.aAdapter.notifyDataSetChanged();
		
	}//private static void refresh_tilist(Activity actv)

	public static void add_pattern_to_text(Dialog dlg, int position, String word) {
		
//		EditText et = (EditText) dlg.findViewById(R.id.dlg_add_memos_et_content);
		EditText et = (EditText) dlg.findViewById(R.id.dlg_edit_title_et_content);
		
		String content = et.getText().toString();
		
		content += word + " ";
		
		et.setText(content);
		
		et.setSelection(et.getText().toString().length());
		
	}//public static void add_pattern_to_text(Dialog dlg, int position, String word)

	public static long getMillSeconds(int year, int month, int date) {
		// Calendar
		Calendar cal = Calendar.getInstance();
		
		// Set time
		cal.set(year, month, date);
		
		// Date
		Date d = cal.getTime();
		
		return d.getTime();
		
	}//private long getMillSeconds(int year, int month, int date)

	/****************************************
	 *	getMillSeconds_now()
	 * 
	 * <Caller> 
	 * 1. ButtonOnClickListener # case main_bt_start
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static long getMillSeconds_now() {
		
		Calendar cal = Calendar.getInstance();
		
		return cal.getTime().getTime();
		
	}//private long getMillSeconds_now(int year, int month, int date)

	public static String get_TimeLabel(long millSec) {
		
		 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
		 
		return sdf1.format(new Date(millSec));
		
	}//public static String get_TimeLabel(long millSec)

	public static void delete_patterns(Activity actv, Dialog dlg, Dialog dlg2,
			Dialog dlg3) {
		/****************************
		 * 0. Get pattern name
		 * 1. Set up db
		 * 2. Query
		 * 3. Dismiss dialogues
			****************************/
		/****************************
		 * 0. Get pattern name
			****************************/
		TextView tv = (TextView) dlg3.findViewById(R.id.dlg_confirm_delete_patterns_tv_pattern_name);
		
		String item = tv.getText().toString();
		
		/****************************
		 * 1. Set up db
			****************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/****************************
		 * 2. Query
			****************************/
		String sql = "DELETE FROM " + CONS.tname_memo_patterns +
							" WHERE word='" + item + "'";
		
		try {
			wdb.execSQL(sql);
		
			
			// debug
			Toast.makeText(actv, "Pattern deleted", Toast.LENGTH_LONG).show();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Pattern deleted: " + item);

			/****************************
			 * 3. Dismiss dialogues
				****************************/
			dlg3.dismiss();
			dlg2.dismiss();
			dlg.dismiss();
			
		} catch (SQLException e) {
			
			// debug
			Toast.makeText(actv, "�p�^�[���폜�@=>�@�ł��܂���ł���", Toast.LENGTH_LONG).show();
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Pattern deletion => Failed:  " + item);
			
		} finally {
			
			wdb.close();
			
		}
		
	}//public static void delete_patterns()

	public static void searchItem(Activity actv, Dialog dlg) {
		/****************************
		 * Steps
		 * 1. Get search words
		 * 2. Format words
		 * 
		 * 2-2. Get table name from current path
		 * 3. Search task
		 * 
		 * 9. Dismiss dialog
			****************************/
		EditText et = (EditText) dlg.findViewById(R.id.dlg_search_et);
		
		String words = et.getText().toString();
		
		if (words.equals("")) {
			
			// debug
//			Toast.makeText(actv, "������ĂȂ���", 2000).show();
			Toast.makeText(actv, "No search word", 2000).show();
			
			return;
			
		}//if (words.equals(""))
		
		/****************************
		 * 2. Format words
			****************************/
//		words = words.replace('�@', ' ');
		words = words.replace('　', ' ');
		
		String[] a_words = words.split(" ");
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "a_words.length => " + a_words.length);
		
		/****************************
		 * 2-2. Get table name from current path
			****************************/
		String tableName = Methods.convert_path_into_table_name(actv);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "tableName=" + tableName);
		
		/****************************
		 * 3. Search task
			****************************/
		CheckBox cb_all_table = (CheckBox) dlg.findViewById(R.id.dlg_search_cb_all_table);
		
		int search_mode = 0;	// 0 => Specific table (default)
		
		if (cb_all_table.isChecked()) {
			
			search_mode = 1;	// 1 => All tables
			
		}//if (condition)
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "search_mode=" + search_mode);
		
		
		/***************************************
		 * Setup search task
		 ***************************************/
//		SearchTask.siList = new ArrayList<SearchedItem>();
		CONS.Search.siList = new ArrayList<SearchedItem>();
		
		SearchTask st = new SearchTask(actv, search_mode);
		
//		SearchTask st = new SearchTask(actv);
		
		
		
//		st.execute(a_words);
//		st.execute(a_words, new String[]{"aaa", "bbb", "ccc"});
		st.execute(a_words, new String[]{tableName});
		
		/****************************
		 * 9. Dismiss dialog
			****************************/
		dlg.dismiss();
		
	}//public static void searchItem(Activity actv, Dialog dlg)

	public static List<TI> convert_fileIdArray2tiList(
			Activity actv, String tableName, long[] long_file_id) {
		/****************************
		* Steps
		* 1. DB setup
		* 2. Get ti list
		* 3. Close db
		* 4. Return
		****************************/
		/****************************
		* 1. DB setup
		****************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/****************************
		* 2. Get ti list
		****************************/
		List<TI> tilist = new ArrayList<TI>();
		
		for (long file_id : long_file_id) {
			
			String sql = "SELECT * FROM " + tableName 
						+ " WHERE file_id = '" + String.valueOf(file_id) + "'";
			
			Cursor c = rdb.rawQuery(sql, null);
			
			if (c.getCount() > 0) {
			
				c.moveToFirst();
				
				tilist.add(Methods.convertCursorToThumbnailItem(c));
				
				c.moveToNext();
				
			}//if (c.getCount() > 0)
		}
		
		// Log
		Log.d("Methods.java" + "["
		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		+ "]", "tilist.size() => " + tilist.size());
		
		/****************************
		* 3. Close db
		****************************/
		rdb.close();
		
		
		/****************************
		* 4. Return
		****************************/
//		return tilist.size() > 0 ? tilist : null;
		return tilist;
	
	}//public static List<ThumbnailItem> convert_fileIdArray2tiList(Activity actv, String tableName, long[] long_file_id)

	public static List<TI> convert_fileIdArray2tiList_all_table(Activity actv,
			long[] long_searchedItems, String[] string_searchedItems_table_names) {
		/*********************************
		 * memo
		 *********************************/
		/****************************
		* Steps
		* 1. DB setup
		* 2. Get ti list
		* 3. Close db
		* 4. Return
		****************************/
		/****************************
		* 1. DB setup
		****************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/****************************
		* 2. Get ti list
		****************************/
		List<TI> tilist = new ArrayList<TI>();
		
		for (int i = 0; i < long_searchedItems.length; i++) {
			
			String sql = "SELECT * FROM " + string_searchedItems_table_names[i] 
						+ " WHERE file_id = '"
						+ String.valueOf(long_searchedItems[i]) + "'";
			
			Cursor c = rdb.rawQuery(sql, null);
			
			if (c.getCount() > 0) {
			
				c.moveToFirst();
				
				tilist.add(Methods.convertCursorToThumbnailItem(c));
				
				c.moveToNext();
				
			}//if (c.getCount() > 0)
			
		}//for (int i = 0; i < long_searchedItems.length; i++)
		
		// Log
		Log.d("Methods.java" + "["
		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		+ "]", "tilist.size() => " + tilist.size());
		
		/****************************
		* 3. Close db
		****************************/
		rdb.close();
		
		
		/****************************
		* 4. Return
		****************************/
		return tilist;
		
	}//public static List<TI> convert_fileIdArray2tiList_all_table()

	/****************************************
	 *
	 * 
	 * <Caller> 1. 
	 * 
	 * <Desc> 
	 * 1. Originally, SearchTask.java was calling this method.
	 * 		But I changed the starategy, ending up not using this method (20120723_145553) 
	 * 
	 * <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static TI convertCursorToThumbnailItem(Cursor c) {
		/****************************
		 * Steps
		 * 1. 
			****************************/
		return new TI(
				c.getLong(1),	// file_id
				c.getString(2),	// file_path
				c.getString(3),	// file_name
				c.getLong(4),	// date_added
//				c.getLong(5)		// date_modified
				c.getLong(5),		// date_modified
				c.getString(6),	// memos
				c.getString(7)	// tags
		);
		
		
	}//public static TI convertCursorToThumbnailItem(Cursor c)

	public static boolean restore_db(Activity actv, String dbName,
				String src, String dst) {
		/*********************************
		 * 1. Setup db
		 * 2. Setup: File paths
		 * 3. Setup: File objects
		 * 4. Copy file
		 * 
		 *********************************/
    	// Setup db
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		wdb.close();

		/*********************************
		 * 2. Setup: File paths

    	/*********************************
		 * 3. Setup: File objects
		 *********************************/

		/*********************************
		 * 4. Copy file
		 *********************************/
		FileChannel iChannel = null;
		FileChannel oChannel = null;
		
		try {
			iChannel = new FileInputStream(src).getChannel();
			oChannel = new FileOutputStream(dst).getChannel();
			iChannel.transferTo(0, iChannel.size(), oChannel);
			
			iChannel.close();
			oChannel.close();
			
			// Log
			Log.d("ThumbnailActivity.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "File copied: " + src);
			
			// debug
			Toast.makeText(actv, "DB restoration => Done", Toast.LENGTH_LONG).show();
			
			return true;

		} catch (FileNotFoundException e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			if (iChannel != null) {
				
				try {
					
					iChannel.close();
					
				} catch (IOException e1) {
					
					// Log
					Log.e("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Exception: " + e.toString());

				}
				
			}
			
			if (iChannel != null) {
				
				try {
					
					iChannel.close();
					
				} catch (IOException e1) {
					
					// Log
					Log.e("Methods.java" + "["
							+ Thread.currentThread().getStackTrace()[2].getLineNumber()
							+ "]", "Exception: " + e.toString());
					
				}
				
			}
			
			if (oChannel != null) {
				
				try {
					oChannel.close();
				} catch (IOException e1) {
					
					// Log
					Log.e("Methods.java" + "["
							+ Thread.currentThread().getStackTrace()[2].getLineNumber()
							+ "]", "Exception: " + e.toString());
					
				}
				
			}

			return false;
			
		} catch (IOException e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
			if (iChannel != null) {
				
				try {
					
					iChannel.close();
					
				} catch (IOException e1) {
					
					// Log
					Log.e("Methods.java" + "["
							+ Thread.currentThread().getStackTrace()[2].getLineNumber()
							+ "]", "Exception: " + e.toString());
					
				}
				
			}
			
			if (oChannel != null) {
				
				try {
					oChannel.close();
				} catch (IOException e1) {
					
					// Log
					Log.e("Methods.java" + "["
							+ Thread.currentThread().getStackTrace()[2].getLineNumber()
							+ "]", "Exception: " + e.toString());
					
				}
				
			}

			
			return false;
			
		}//try
		
	}//private boolean restore_db()

	public static void show_history(Activity actv) {
		/*********************************
		 * 1. Set up db
		 * 2. Table exists?
		 * 3. Get all data
		 * 
		 * 3-2. Set pref value => 1
		 * 
		 * 4. Set data to intent
		 * 4-2. Close db
		 * 5. Start activity
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/*********************************
		 * 2. Table exists?
		 *********************************/
		show_history__1_setupTable(actv, dbu, wdb);
		
		/***************************************
		 * Close db
		 ***************************************/
		wdb.close();
		
//		boolean result = dbu.tableExists(
//							wdb,
////							MainActv.tableName_show_history);
//							CONS.History.tname_show_history);
//		
//		if (result == false) {
//			// Log
//			Log.e("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]",
//					"Table doesn't exist: "
//					+ CONS.History.tname_show_history);
//			
//			// Create one
//			result = dbu.createTable(
//							wdb, 
//							CONS.History.tname_show_history, 
//							CONS.cols_show_history, 
//							CONS.col_types_show_history);
//			
//			if (result == true) {
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"Table created: " + CONS.History.tname_show_history);
//				
//			} else {//if (result == true)
//				// Log
//				Log.e("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Create table failed: " + CONS.History.tname_show_history);
//				
//				// debug
//				Toast.makeText(actv, 
//						"Create table failed: "
//							+ CONS.History.tname_show_history,
//						Toast.LENGTH_SHORT).show();
//
//				wdb.close();
//				
//				return;
//				
//			}//if (result == true)
//		}//if (result == false)
//		
//		/*********************************
//		 * 3. Get all data
//		 *********************************/
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]",
//				"Table exists: " + CONS.History.tname_show_history);
//		
//		
//		// REF=> http://www.accessclub.jp/sql/10.html
//		String sql = "SELECT * FROM " + CONS.History.tname_show_history;
//		
//		Cursor c = wdb.rawQuery(sql, null);
//		
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount() => " + c.getCount());
//
//		if (c.getCount() < 1) {
//			
//			// Log
//			Log.e("Methods.java"
//					+ "["
//					+ Thread.currentThread().getStackTrace()[2]
//							.getLineNumber() + "]", "No history data");
//			
//			// debug
//			Toast.makeText(actv,
//					"No history data", Toast.LENGTH_SHORT).show();
//			
//			wdb.close();
//			
//			return;
//			
//		} else if (c.getCount() > 0) {//if (tempC.getCount() > 0)
//			
//			// debug
//			Toast.makeText(actv, 
//					"Num of history data: " + c.getCount(),
//					Toast.LENGTH_SHORT).show();
//			
//		}//if (tempC.getCount() > 0)
//		
//		/*********************************
//		 * 3-2. Set pref value => 1
//		 *********************************/
////		result = Methods.set_pref(
//		boolean result = Methods.set_pref(
//							actv, 
////							MainActv.prefName_mainActv, 
////							MainActv.prefName_mainActv_history_mode,
//							CONS.pname_mainActv, 
//							CONS.pname_mainActv_history_mode,
//							
//							CONS.HISTORY_MODE_ON);
//
//		if (result == true) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]",
//					"Pref => Set: " + CONS.HISTORY_MODE_ON);
//			
//		} else {//if (result == true)
//			
//			// Log
//			Log.e("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Set pref => Failed");
//			
//		}//if (result == true)
//		
//		
		/*********************************
		 * 4. Set data to intent
		 * 	1. Set up intent
		 * 	2. Get data => File ids
		 * 	3. Get data => Table names
		 * 	4. Put data to intent
		 * 	5. Start activity
		 *********************************/
		Intent i = new Intent();
		
//		i.setClass(actv, TNActv.class);
		i.setClass(actv, HistActv.class);
		
		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		
//		c.moveToFirst();
		
//		/*********************************
//		 * 4.2. Get data => File ids
//		 * 4.3. Get data => Table names
//		 *********************************/
//		int data_length = c.getCount();
//		
//		long[] file_ids = new long[data_length];
//		
//		String[] table_names = new String[data_length];
//		
//		for (int j = 0; j < data_length; j++) {
//			
//			file_ids[j] = c.getLong(3);
//			
//			table_names[j] = c.getString(4);
//			
//			c.moveToNext();
//			
//		}//for (int j = 0; j < data_length; j++)
//		
//		/*********************************
//		 * 4-2. Close db
//		 *********************************/
//		wdb.close();
		
//		/*********************************
//		 * 4.4. Put data to intent
//		 *********************************/
//		i.putExtra(CONS.intent_label_file_ids, file_ids);
//		
//		i.putExtra(CONS.intent_label_table_names, table_names);
		
		/*********************************
		 * 5. Start activity
		 *********************************/
		actv.startActivity(i);

			
//		} else {//if (result != false)
			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Table doesn't exist: " + MainActv.tname_show_history);
//			
//			// Create one
//			result = dbu.createTable(
//											wdb, 
//											MainActv.tname_show_history, 
//											MainActv.cols_show_history, 
//											MainActv.col_types_show_history);
//			
//			if (result == true) {
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Table created: " + MainActv.tname_show_history);
//				
//			} else {//if (result == true)
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Create table failed: " + MainActv.tname_show_history);
//				
//				// debug
//				Toast.makeText(actv, 
//						"Create table failed: " + MainActv.tname_show_history,
//						Toast.LENGTH_SHORT).show();
//
//				return;
//				
//			}//if (result == true)
			
//		}//if (result != false)
		

		
	}//public static void show_history(Activity actv)

	private static void
	show_history__1_setupTable
	(Activity actv, DBUtils dbu, SQLiteDatabase wdb) {
		// TODO Auto-generated method stub
		boolean result = dbu.tableExists(
				wdb,
//				MainActv.tableName_show_history);
				CONS.History.tname_history);

		if (result == false) {
		// Log
		Log.e("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]",
				"Table doesn't exist: "
				+ CONS.History.tname_history);
		
		// Create one
		result = dbu.createTable(
						wdb, 
						CONS.History.tname_history, 
						CONS.cols_show_history, 
						CONS.col_types_show_history);
		
		if (result == true) {
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]",
					"Table created: " + CONS.History.tname_history);
			
		} else {//if (result == true)
			// Log
			Log.e("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "Create table failed: " + CONS.History.tname_history);
			
			// debug
			Toast.makeText(actv, 
					"Create table failed: "
						+ CONS.History.tname_history,
					Toast.LENGTH_SHORT).show();
		
			wdb.close();
			
			return;
			
		}//if (result == true)
		}//if (result == false)

	}//show_history__1_setupTable(DBUtils dbu, SQLiteDatabase wdb)

//	public static void save_history(Activity actv, long fileId,
//			String table_name) {
//		/*********************************
//		 * 1. Build data
//		 * 2. Set up db
//		 * 
//		 * 2-2. Table exists?
//		 * 
//		 * 3. Insert data
//		 * 4. Close db
//		 *********************************/
//		Object[] data = {fileId, table_name};
//		
//		/*********************************
//		 * 2. Set up db
//		 *********************************/
//		DBUtils dbu = new DBUtils(actv, CONS.dbName);
//		
//		//
//		SQLiteDatabase wdb = dbu.getWritableDatabase();
//		
//		/*********************************
//		 * 2-2. Table exists?
//		 *********************************/
//		boolean result = dbu.tableExists(wdb, CONS.History.tname_history);
//		
//		if (result == false) {
//			// Log
//			Log.e("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Table doesn't exist: " + CONS.History.tname_history);
//			
//			// Create one
//			result = dbu.createTable(
//											wdb, 
//											CONS.History.tname_history, 
//											CONS.cols_show_history, 
//											CONS.col_types_show_history);
//			
//			if (result == true) {
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Table created: " + CONS.History.tname_history);
//				
//			} else {//if (result == true)
//				// Log
//				Log.e("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Create table failed: " + CONS.History.tname_history);
//				
//				// debug
//				Toast.makeText(actv, 
//						"Create table failed: " + CONS.History.tname_history,
//						Toast.LENGTH_SHORT).show();
//
//				wdb.close();
//				
//				return;
//				
//			}//if (result == true)
//		}//if (result == false)
//
//		
//		/*********************************
//		 * 3. Insert data
//		 *********************************/
//		boolean res = DBUtils.insertData_history(actv, wdb, data);
//		
////		// Log
////		Log.d("Methods.java" + "["
////				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////				+ "]", "res=" + res);
//		
//		if (res == true) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "History saved: fileId=" + fileId);
//			
//		} else {//if (res == true)
//			
//			// Log
//			Log.e("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Save history => Failed: " + fileId);
//			
//		}//if (res == true)
//		
//		
//		/*********************************
//		 * 4. Close db
//		 *********************************/
//		wdb.close();
//		
//	}//public static void save_history()

	public static List<TI> get_all_data_history(Activity actv,
			long[] history_file_ids, String[] history_table_names) {
		/*********************************
		 * 1. DB setup
		 * 2. Build list
		 *
		 * 2-2. Close db
		 * 
		 * 3. Sort list
		 * 4. Return list
		 *********************************/
		/*********************************
		 * 1. DB setup
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		/*********************************
		 * 2. Build list
		 *********************************/
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Build list");
		
		List<TI> tiList = dbu.get_all_data_history(
									actv,
									rdb,
									history_file_ids,
									history_table_names);
		
		/*********************************
		 * 2-2. Close db
		 *********************************/
		rdb.close();
		
		/*********************************
		 * 3. Sort list
		 *********************************/
		if (tiList != null) {
			
			Methods.sort_tiList(tiList);
			
		}//if (tiList == null)
//		Methods.sort_tiList(tiList);
		
		return tiList;
		
	}//public static List<TI> get_all_data_history()

	public static String[] get_column_list(Activity actv, String dbName, String tableName) {
		/*********************************
		 * 1. Set up db
		 * 2. Cursor null?
		 * 3. Get names
		 * 
		 * 4. Close db
		 * 5. Return
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT * FROM " + tableName;
		
		/*********************************
		 * 2. Cursor null?
		 *********************************/
		Cursor c = null;
		
		try {
			c = rdb.rawQuery(q, null);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount(): " + c.getCount());

		} catch (Exception e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
		/*********************************
		 * 3. Get names
		 *********************************/
		String[] column_names = c.getColumnNames();
		
		/*********************************
		 * 4. Close db
		 *********************************/
		rdb.close();
		
		/*********************************
		 * 5. Return
		 *********************************/
		return column_names;
		
//		return null;
	}//public static String[] get_column_list(Activity actv, String tableName)

    public static void drop_table(Activity actv, String dbName, String tableName) {
    	// Setup db
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		boolean res = 
				dbu.dropTable(actv, wdb, tableName);
		
		if (res == true) {
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table dropped: " + tableName);
		} else {//if (res == true)

			// Log
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Drop table => Failed: " + tableName);
			
		}//if (res == true)
		

		wdb.close();
		
		
	}//private void drop_table(String tableName)

	public static boolean add_column_to_table(Activity actv, String dbName,
			String tableName, String column_name, String data_type) {
		/*********************************
		 * 1. Column already exists?
		 * 2. db setup
		 * 
		 * 3. Build sql
		 * 4. Exec sql
		 * 
		 * 5. Close db
		 *********************************/
		/*********************************
		 * 1. Column already exists?
		 *********************************/
		String[] cols = Methods.get_column_list(actv, dbName, tableName);
		
//		//debug
//		for (String col_name : cols) {
//
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "col: " + col_name);
//			
//		}//for (String col_name : cols)

		
		for (String col_name : cols) {
			
			if (col_name.equals(column_name)) {
				
//				// debug
//				Toast.makeText(actv, "Column exists: " + column_name, Toast.LENGTH_SHORT).show();
				
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Column exists: " + column_name);
				
				return false;
				
			}
			
		}//for (String col_name : cols)
		
		// debug
		Toast.makeText(actv, "Column doesn't exist: " + column_name, Toast.LENGTH_SHORT).show();
		
		/*********************************
		 * 2. db setup
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*********************************
		 * 3. Build sql
		 *********************************/
		// REF[20121001_140817] => http://stackoverflow.com/questions/8291673/how-to-add-new-column-to-android-sqlite-database
		
		String sql = "ALTER TABLE " + tableName + 
					" ADD COLUMN " + column_name + 
					" " + data_type;
		
		/*********************************
		 * 4. Exec sql
		 *********************************/
		try {
//			db.execSQL(sql);
			wdb.execSQL(sql);
			
			// Log
			Log.d(actv.getClass().getName() + 
					"["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Column added => " + column_name);

			/*********************************
			 * 5. Close db
			 *********************************/
			wdb.close();
			
			return true;
			
		} catch (SQLException e) {
			// Log
			Log.d(actv.getClass().getName() + 
					"[" + Thread.currentThread().getStackTrace()[2].getLineNumber() + "]", 
					"Exception => " + e.toString());
			
			/*********************************
			 * 5. Close db
			 *********************************/
			wdb.close();

			return false;
		}//try

		/*********************************
		 * 5. Close db
		 *********************************/


		
	}//public static boolean add_column_to_table()

	public static List<String> get_table_list(Activity actv, String key_word) {
		/*********************************
		 * 1. Set up db
		 * 2. Query
		 * 
		 * 3. Build list
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		/*********************************
		 * 2. Query
		 *********************************/
		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT name FROM " + "sqlite_master"+
//						" WHERE type = 'table' ORDER BY name";
						" WHERE type = 'table' AND name like '"
						+ key_word + "'" + " ORDER BY name";
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "query=" + q);
		
		Cursor c = null;
		try {
			c = rdb.rawQuery(q, null);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount(): " + c.getCount());

		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
		}
		
		// Table names list
		List<String> tableList = new ArrayList<String>();
		
		/*********************************
		 * 3. Build list
		 *********************************/
		// Log
		if (c != null) {
			c.moveToFirst();
			
			for (int i = 0; i < c.getCount(); i++) {
				//
				String t_name = c.getString(0);
				
				tableList.add(c.getString(0));
					
				// Next
				c.moveToNext();
				
			}//for (int i = 0; i < c.getCount(); i++)

		} else {//if (c != null)
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c => null");
		}//if (c != null)

		rdb.close();
		
		return tableList;
	}//public static List<String> get_table_list(Activity actv, String key_word)
	
//	public static boolean record_history(Activity actv, TI ti) {
//		/*********************************
//		 * memo
//		 *********************************/
//		int current_history_mode = Methods.get_pref(
//				actv, 
//				CONS.pname_mainActv, 
////				MainActv.prefName_mainActv_history_mode,
//				CONS.pname_mainActv_history_mode,
//				-1);
//
//		if (current_history_mode == CONS.HISTORY_MODE_OFF) {
//			
//			Methods.save_history(
//					actv,
//					ti.getFileId(),
//					Methods.convert_path_into_table_name(actv));
//			
//			/*********************************
//			 * 2-2-a. Update data
//			 *********************************/
////			// Log
////			Log.d("Methods.java"
////					+ "["
////					+ Thread.currentThread().getStackTrace()[2]
////							.getLineNumber() + "]",
////					"[onListItemClick] Table name=" + Methods.convert_path_into_table_name(actv));
//			
//			DBUtils dbu = new DBUtils(actv, CONS.dbName);
//			
//			//
//			SQLiteDatabase wdb = dbu.getWritableDatabase();
//
//			
//			boolean res = DBUtils.updateData_TI_last_viewed_at(
//								actv,
//								wdb,
//								Methods.convert_path_into_table_name(actv),
//								ti);
//			
//			if (res == true) {
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Data updated: " + ti.getFile_name());
//			} else {//if (res == true)
//				// Log
//				Log.e("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"Update data => Failed: " + ti.getFile_name());
//			}//if (res == true)
//			
//			
//			wdb.close();
//			
//		} else {//if (current_move_mode == MainActv.HISTORY_MODE_OFF)
//			
//			// Log
//			Log.e("Methods.java"
//					+ "["
//					+ Thread.currentThread().getStackTrace()[2]
//							.getLineNumber() + "]", "History not saved");
//			
//		}//if (current_move_mode == MainActv.HISTORY_MODE_OFF)
//
//		
//		
//		return false;
//	}//public static boolean record_history(Activity actv, long fileId)

	/*********************************
	 * <Notes>
	 * 1. File names => Sorted alphabetico-ascendantly
	 * 
	 * <Return>
	 * null		=> File "dpath" doesn't exist
	 *********************************/
	public static List<String> get_file_list(File dpath) {
		/*********************************
		 * 1. Directory exists?
		 * 2. Build list
		 * 2-1. Sort list
		 * 
		 * 3. Return
		 *********************************/
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Methods.get_file_list(File dpath)");
		
		/*********************************
		 * 1. Directory exists?
		 *********************************/
		if (!dpath.exists()) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Dir doesn't exist");
			
			return null;
			
		} else {//if (!dpath.exists() == condition)
			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Dir exists: " + dpath.getAbsolutePath());
			
		}//if (!dpath.exists() == condition)
		
		/*********************************
		 * 2. Build list
		 *********************************/
		List<String> list_dir = new ArrayList<String>();
		
		File[] files_list = dpath.listFiles();
		
		/*********************************
		 * 2-1. Sort list
		 *********************************/
		Methods.sort_list_files(files_list);
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Starts => get_file_list()");
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]",
//				"dpath=" + dpath.getAbsolutePath()
//				+ " // size=" + files_list.length);
		
		
		for (File f : files_list) {
			
			list_dir.add(f.getName());
			
		}//for (File f : files_list)
		
		/*********************************
		 * 3. Return
		 *********************************/
		return list_dir;
		
	}//public static List<String> get_file_list(File dpath)

	/*********************************
	 * <Return>
	 * null		=> Table doesn't exist
	 *********************************/
	public static List<AI> get_all_data_ai(Activity actv, String table_name) {
		/*********************************
		 * 0. Table exists?
		 * 1. DB setup
		 * 2. Get data
		 * 		2.1. Get cursor
		 * 		2.2. Add to list
		 * 
		 * 9. Close db
		 * 10. Return value
		 *********************************/
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "get_all_data_ai(Activity actv, String table_name)");
		
		/*********************************
		 * 1. DB setup
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/****************************
		 * 0. Table exists?
			****************************/
		boolean res = dbu.tableExists(rdb, table_name);
		
		if (res == false) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "getAllData() => Table doesn't exist: " + table_name);
			
			rdb.close();
			
			return null;
			
		}//if (res == false)
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "rdb.getPath(): " + rdb.getPath());
		
		
		
		/****************************
		 * 2. Get data
		 * 		2.1. Get cursor
		 * 		2.2. Add to list
			****************************/
		//
		String sql = "SELECT * FROM " + table_name;
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
		} catch (Exception e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount() => " + c.getCount());

		/****************************
		 * 2.2. Add to list
			****************************/
		c.moveToFirst();
		
		List<AI> ai_list = new ArrayList<AI>();
		
		for (int i = 0; i < c.getCount(); i++) {
//		for (int i = 0; i < c.getCount() / 200; i++) {

//			String file_name, String file_path,
//			String title, String memo,
//			
//			long last_played_at,
//			
//			String table_name,
//			
//			long db_id, long created_at, long modified_at
			
//			{"file_name", 	"file_path",	"title", "memo",
//				"last_played_at",	"table_name"}
			
			AI ai = new AI(
					c.getString(3),	// file_name
					c.getString(4),	// file_path
					
					c.getString(5),	// title
					c.getString(6),	// memo
					
					c.getLong(7),
					
					c.getString(8),	// table_name
					
					c.getLong(9),	// length
					
					c.getLong(0),	// id
					c.getLong(1),	// created_at
					c.getLong(2)	// modified_at
			);
	
			// Add to the list
			ai_list.add(ai);
			
//			// Log
//			String file_full_path = StringUtils.join(
//						new String[]{
//								c.getString(4),
//								c.getString(3)
//						}, File.separator);
//			
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "file_full_path=" + file_full_path);
			
////			File f = new File(file_full_path);
//			
//			MediaPlayer mp = new MediaPlayer();
//			
//			try {
//				mp.setDataSource(file_full_path);
//				
//				mp.prepare();
//				
//				int len = mp.getDuration() / 1000;
//				
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"len=" + len
//						+ "(" + c.getString(3) + ")");
//
//				// REF=> http://stackoverflow.com/questions/9609479/android-mediaplayer-went-away-with-unhandled-events
//				mp.reset();
//				
//				// REF=> http://stackoverflow.com/questions/3761305/android-mediaplayer-throwing-prepare-failed-status-0x1-on-2-1-works-on-2-2
//				mp.release();
//				
//			} catch (IllegalArgumentException e) {
//				
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Exception=" + e.toString());
//				
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "message" + c.getString(3));
//				
//			} catch (IllegalStateException e) {
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Exception=" + e.toString());
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "message" + c.getString(3));
//				
//			} catch (IOException e) {
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Exception=" + e.toString());
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "message" + c.getString(3));
//
//			}
//			
			//
			c.moveToNext();
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		
		
		/****************************
		 * 9. Close db
			****************************/
		rdb.close();
		
		/****************************
		 * 10. Return value
			****************************/
		
		return ai_list;
		
	}//public static List<AI> get_all_data_ai(Activity actv, String table_name)

	
	/*********************************
	 * <Return>
	 * null		=> 
	 *********************************/
	public static AI get_data_ai(Activity actv, long db_id, String table_name) {
		/*********************************
		 * 0. Table exists?
		 * 1. DB setup
		 * 2. Get data
		 * 		2.1. Get cursor
		 * 		2.2. Add to list
		 * 
		 * 9. Close db
		 * 10. Return value
		 *********************************/
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]",
				"Methods.get_data_ai(Activity actv, long db_id, String table_name)");
		
		/*********************************
		 * 1. DB setup
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/****************************
		 * 0. Table exists?
			****************************/
		boolean res = dbu.tableExists(rdb, table_name);
		
		if (res == false) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + table_name);
			
			rdb.close();
			
			return null;
			
		}//if (res == false)
		
		/****************************
		 * 2. Get data
		 * 		2.1. Get cursor
		 * 		2.1-2. Data exists?
		 * 
		 * 		2.2. Add to list
			****************************/
		//
		String sql = "SELECT * FROM " + table_name
					+ " WHERE " + android.provider.BaseColumns._ID
					+ " = '" + db_id + "'";
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
		} catch (Exception e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
		/*********************************
		 * 2.2.1-2. Data exists?
		 *********************************/
		if (c.getCount() < 1) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount() < 1");
			
			rdb.close();
			
			return null;
			
		}//if (c.getCount() == condition)
		
		/****************************
		 * 2.2. Add to list
			****************************/
		c.moveToFirst();
		
		AI ai = new AI(
				c.getString(3),	// file_name
				c.getString(4),	// file_path
				
				c.getString(5),	// title
				c.getString(6),	// memo
				
				c.getLong(7),
				
				c.getString(8),	// table_name
				
				c.getLong(9),	// length
				
				c.getLong(0),	// id
				c.getLong(1),	// created_at
				c.getLong(2)	// modified_at
		);
		
		/****************************
		 * 9. Close db
			****************************/
		rdb.close();
		
		/****************************
		 * 10. Return value
			****************************/
		return ai;
	}//public static AI get_data_ai(Activity actv, long db_id, String table_name)

	
	public static void play_file(Activity actv, AI ai) {
		/*********************************
		 * 1. Media player is playing?
		 * 2. OnCompletionListener
		 * 
		 * 3. Set data source
		 * 4. Prepare mp
		 * 
		 * 5. Start
		 * 
		 *********************************/
		/*********************************
		 * 1. Media player is playing?
		 *********************************/
		if (PlayActv.mp != null && PlayActv.mp.isPlaying()) {

			PlayActv.mp.stop();
			
		}//if (mp.isPlaying())

		/*********************************
		 * 2. OnCompletionListener
		 *********************************/
		PlayActv.mp = new MediaPlayer();
		
		PlayActv.mp.setOnCompletionListener(new MPOnCompletionListener(actv));

		/*********************************
		 * 3. Set data source
		 *********************************/
		String file_full_path = StringUtils.join(
				new String[]{ai.getFile_path(), ai.getFile_name()},
				File.separator);

		try {

			PlayActv.mp.setDataSource(file_full_path);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Data source => Set");
			
		} catch (IllegalArgumentException e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
			
		} catch (IllegalStateException e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());

		} catch (IOException e) {

			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());

		}//try

		/*********************************
		 * 4. Prepare mp
		 *********************************/
		try {

			PlayActv.mp.prepare();
			
		} catch (IllegalStateException e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());

		} catch (IOException e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());

		}//try

		/***************************************
		 * Position set in the preference?
		 ***************************************/
		long prefPosition = 
				Methods.getPref_long(
						actv,
						CONS.Pref.pname_PlayActv,
						CONS.Pref.pkey_PlayActv_position,
						-1);
		
		if (prefPosition >= 0) {
			
			PlayActv.mp.seekTo((int) prefPosition);
			
		}//if (prefPosition == condition)
		
		/***************************************
		 * Prepare: Service
		 ***************************************/
		Intent i = new Intent((Context) actv, Service_ShowProgress.class);

		//
//		i.putExtra("counter", timeLeft);
		
		
		// Log
		Log.d("DialogOnItemClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "Starting service...");
		//
		actv.startService(i);

		
		/*********************************
		 * 5. Start
		 *********************************/
		PlayActv.mp.start();
		
//		if (file_full_path != null) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "file_full_path=" + file_full_path);
//			
//		} else {//if (file_full_path == condition)
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "file_full_path == null");
//			
//		}//if (file_full_path == condition)
		
	}//public static void play_file(Activity actv, AI ai)

	public static void stop_player(Activity actv) {
		/*********************************
		 * memo
		 *********************************/
		if (PlayActv.mp != null && PlayActv.mp.isPlaying()) {

			PlayActv.mp.stop();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Player => Stopped");
			
			/***************************************
			 * Stop: Service
			 ***************************************/
			Intent i = new Intent((Context) actv, Service_ShowProgress.class);

			//
//			i.putExtra("counter", timeLeft);

			// Log
			Log.d("DialogOnItemClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Stopping service...");

			//
//			actv.startService(i);
			actv.stopService(i);

			
		} else if (PlayActv.mp == null) {//if (mp.isPlaying())

			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "PlayActv.mp != null");

		} else if (!PlayActv.mp.isPlaying()) {//if (mp.isPlaying())

			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "PlayActv.mp => Not playing");

		}//if (mp.isPlaying())

	}//public static void stop_player(Activity actv)

	public static void edit_title(Activity actv, Dialog dlg, AI ai) {
		/*********************************
		 * 1. Get view
		 * 2. Set title to the AI object
		 * 
		 * 3. Update data
		 * 
		 * 9. Dismiss dialog
		 *********************************/
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "edit_title(Activity actv, Dialog dlg, AI ai)");
		
		/*********************************
		 * 1. Get view
		 *********************************/
		EditText et = (EditText) dlg.findViewById(R.id.dlg_edit_title_et_content);
		
		/*********************************
		 * 2. Set title to the AI object
		 *********************************/
		ai.setTitle(et.getText().toString());
		
		/*********************************
		 * 3. Update data
		 *********************************/
		boolean res = DBUtils.update_data_ai(
							actv, CONS.dbName, ai.getTable_name(),
							ai.getDb_id(),
							CONS.cols_item[2], ai.getTitle());
//		boolean res = DBUtils.update_data_ai(actv, CONS.dbName, ai.getDb_id(),
//				CONS.cols_item[2], ai.getTitle());
		
		if (res == true) {
			
			// debug
			Toast.makeText(actv, "Data updated", Toast.LENGTH_SHORT).show();
			
		} else {//if (res == true)

			// debug
			Toast.makeText(actv, "Update data => Failed", Toast.LENGTH_SHORT).show();

		}//if (res == true)
		
		/*********************************
		 * 9. Dismiss dialog
		 *********************************/
		dlg.dismiss();
		
		
		/*********************************
		 * 10. Update the text view
		 *********************************/
		TextView tv_title = (TextView) actv.findViewById(R.id.actv_play_tv_title);
		
		tv_title.setText(ai.getTitle());
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "et.getText().toString()=" + et.getText().toString());
//
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "ai.getTitle()=" + ai.getTitle());
		
		
	}//public static void edit_title(Activity actv, Dialog dlg, AI ai)

	public static void sort_list_ai_created_at(List<AI> tiList, final CONS.SORT_ORDER dec) {
		
		Collections.sort(tiList, new Comparator<AI>(){

//			@Override
			public int compare(AI a1, AI a2) {
				
				int res;
				
				switch (dec) {
				
				case ASC:
					
//					res = (int) (a1.getCreated_at() - a2.getCreated_at());
					res = a1.getFile_name().compareTo(a2.getFile_name());
					
					break;
					
				case DEC:
					
//					res = (int) -(a1.getCreated_at() - a2.getCreated_at());
					res = a2.getFile_name().compareTo(a1.getFile_name());
					
					break;
					
				default:
					
					res = 1;
					
					break;
					
				}
				
				return res;
				
//				return (int) (a1.getCreated_at() - a2.getCreated_at());
			}
			
		});//Collections.sort()

	}//public static void sort_tiList(List<ThumbnailItem> tiList)

	public static void db_backup(Activity actv, Dialog dlg) {
		/****************************
		 * 1. Prep => File names
		 * 2. Prep => Files
		 * 2-2. Folder exists?
		 * 
		 * 2-3. Dst folder => Files within the limit?
		 * 3. Copy
			****************************/
		String time_label = Methods.get_TimeLabel(Methods.getMillSeconds_now());
		
		String db_src = StringUtils.join(
					new String[]{
							CONS.dpath_db,
							CONS.fname_db},
					File.separator);
		
		String db_dst_folder = StringUtils.join(
					new String[]{
							CONS.dpath_db_backup,
							CONS.fname_db_backup_trunk},
					File.separator);
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "db_src: " + db_src + " * " + "db_dst: " + db_dst);
//		
		String db_dst = db_dst_folder + "_"
				+ time_label
//				+ MainActv.fileName_db_backup_ext;
				+ CONS.fname_db_backup_ext;
//				+ MainActv.fname_db_backup_trunk;
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "db_src: " + db_src + " * " + "db_dst: " + db_dst);
		
		/****************************
		 * 2. Prep => Files
			****************************/
		File src = new File(db_src);
		File dst = new File(db_dst);
		
		/****************************
		 * 2-2. Folder exists?
			****************************/
		File db_backup = new File(CONS.dpath_db_backup);
		
		if (!db_backup.exists()) {
			
			try {
				db_backup.mkdir();
				
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Folder created: " + db_backup.getAbsolutePath());
			} catch (Exception e) {
				
				// Log
				Log.e("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create folder => Failed");
				
				return;
				
			}
			
		} else {//if (!db_backup.exists())
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Folder exists: ");
			
		}//if (!db_backup.exists())
		
		/*********************************
		 * 2-3. Dst folder => Files within the limit?
		 *********************************/
		File[] files_dst_folder = new File(CONS.dpath_db_backup).listFiles();
		
		int num_of_files = files_dst_folder.length;
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "num_of_files=" + num_of_files);
		
		/****************************
		 * 3. Copy
			****************************/
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
			
			// debug
			Toast.makeText(actv, "DB backup => Done", Toast.LENGTH_LONG).show();

			dlg.dismiss();
			
		} catch (FileNotFoundException e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
		} catch (IOException e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
		}//try

		
	}//public static void db_backup(Activity actv, Dialog dlg, String item)

	public static String convert_intSec2Digits(int t) {
		
		int sec = t % 60;
		
		if (t / 60 < 1) {
			
//			return "00:00:" + String.valueOf(sec);
			return "00:00:" + Methods.convert_sec2digits(sec, 2);
			
		}//if (t / 60 < 1)
		
//		int min = (t - sec) % 60;
		int min = ((t - sec) % (60 * 60)) / 60;
		
		if ((t - sec) / (60 * 60) < 1) {
			
//			return "00:" + String.valueOf(min) + ":" + String.valueOf(sec);
			return "00:"
				+ Methods.convert_sec2digits(min, 2) + ":"
				+ Methods.convert_sec2digits(sec, 2);
			
		}//if (variable == condition)
		
//		int hour = (t - min) / 60;
		int hour = (t - sec) / (60 * 60);
				
//		return String.valueOf(hour) + ":"
//				+ String.valueOf(min) + ":"
//				+ String.valueOf(sec);

		return Methods.convert_sec2digits(min, 2) + ":"
		+ Methods.convert_sec2digits(min, 2) + ":"
		+ Methods.convert_sec2digits(sec, 2);

		
	}//public static String convert_intSec2Digits(int time)

	/***************************************
	 * 20130320_120437<br>
	 * @param t ... Value in seconds, <i>not</i> in mill seconds
	 ***************************************/
	public static String convert_intSec2Digits_lessThanHour(int t) {
		
		int sec = t % 60;
		
		if (t / 60 < 1) {
			
//			return "00:00:" + String.valueOf(sec);
//			return "00:00:" + Methods.convert_sec2digits(sec, 2);
			return "00:" + Methods.convert_sec2digits(sec, 2);
			
		}//if (t / 60 < 1)
		
//		int min = (t - sec) % 60;
		int min = ((t - sec) % (60 * 60)) / 60;
		
		return Methods.convert_sec2digits(min, 2) + ":"
			+ Methods.convert_sec2digits(sec, 2);
			
	}//public static String convert_intSec2Digits(int time)

	private static String convert_sec2digits(int sec, int i) {
		
		int current_len = String.valueOf(sec).length();
		
		if (current_len < i) {
			
			StringBuilder sb = new StringBuilder();
			
			for (int j = 0; j < i - current_len; j++) {
				
				sb.append("0");
			}
			
			sb.append(String.valueOf(sec));
			
			return sb.toString();
			
		}//if (current_len == condition)
		
		return String.valueOf(sec);
		
	}//private static String convert_sec2digits(int sec, int i)

	public static void edit_memo(Activity actv, Dialog dlg, AI ai) {
		/*********************************
		 * 1. Get view
		 * 2. Set title to the AI object
		 * 
		 * 3. Update data
		 * 
		 * 9. Dismiss dialog
		 *********************************/
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "edit_title(Activity actv, Dialog dlg, AI ai)");
		
		/*********************************
		 * 1. Get view
		 *********************************/
		EditText et = (EditText) dlg.findViewById(R.id.dlg_edit_title_et_content);
		
		
		/*********************************
		 * 2. Set memo to the AI object
		 *********************************/
		ai.setMemo(et.getText().toString());
		
		/*********************************
		 * 3. Update data
		 *********************************/
		boolean res = DBUtils.update_data_ai(actv, CONS.dbName, ai.getDb_id(),
							CONS.cols_item[Methods.getArrayIndex(CONS.cols_item, "memo")],
//							ai.getTitle());
							ai.getMemo());
		
		if (res == true) {
			
			// debug
			Toast.makeText(actv, "Data updated", Toast.LENGTH_SHORT).show();
			
		} else {//if (res == true)

			// debug
			Toast.makeText(actv, "Update data => Failed", Toast.LENGTH_SHORT).show();

		}//if (res == true)
		
		/*********************************
		 * 9. Dismiss dialog
		 *********************************/
		dlg.dismiss();
		
		
		/*********************************
		 * 10. Update the text view
		 *********************************/
//		TextView tv_title = (TextView) actv.findViewById(R.id.actv_play_tv_title);
		TextView tvMemo = (TextView) actv.findViewById(R.id.actv_play_tv_memo);
		
		tvMemo.setText(ai.getMemo());
		
	}//public static void edit_memo(Activity actv, Dialog dlg, AI ai)

	public static int getArrayIndex(String[] targetArray, String targetString) {
		int index = -1;
		
		for (int i = 0; i < targetArray.length; i++) {
			
			if (targetArray[i].equals(targetString)) {
				
				index = i;
				
				break;
				
			}//if (targetArray[i] == )
			
		}//for (int i = 0; i < targetArray.length; i++)
		
		return index;
	}//public static int getArrayIndex(String[] targetArray, String targetString)

	public static
	List<AI> selectData_ai(
			Activity actv, String tableName, List<Long> ids) {
		
		/*********************************
		 * 0. Table exists?
		 * 1. DB setup
		 * 2. Get data
		 * 		2.1. Get cursor
		 * 		2.2. Add to list
		 * 
		 * 9. Close db
		 * 10. Return value
		 *********************************/
		/*********************************
		 * 1. DB setup
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/****************************
		 * 0. Table exists?
			****************************/
		boolean res = dbu.tableExists(rdb, tableName);
		
		if (res == false) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "selectData_ai() => Table doesn't exist: " + tableName);
			
			rdb.close();
			
			return null;
			
		}//if (res == false)
		
		/****************************
		 * 2. Get data
		 * 		2.1. Get cursor
		 * 		2.2. Add to list
			****************************/
		List<AI> aiList = new ArrayList<AI>();
		
		//
		for (int i = 0; i < ids.size(); i++) {
			
			String sql = "SELECT * FROM " + tableName
						+ " WHERE " + CONS.cols_with_index[0] + "="
						+ ids.get(i);
			
			Cursor c = null;
			
			try {
				
				c = rdb.rawQuery(sql, null);
				
			} catch (Exception e) {
				// Log
				Log.e("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Exception => " + e.toString());
				
				rdb.close();
				
				return null;
			}
			
			while(c.moveToNext()) {

				AI ai = new AI(
						c.getString(3),	// file_name
						c.getString(4),	// file_path
						
						c.getString(5),	// title
						c.getString(6),	// memo
						
						c.getLong(7),
						
						c.getString(8),	// table_name
						
						c.getLong(9),	// length
						
						c.getLong(0),	// id
						c.getLong(1),	// created_at
						c.getLong(2)	// modified_at
				);
		
				// Add to the list
				aiList.add(ai);

			}//while(c.moveToNext())
			
		}//for (int i = 0; i < ids.size(); i++)
		
//		String sql = "SELECT * FROM " + tableName;
//		
//		Cursor c = null;
//		
//		try {
//			
//			c = rdb.rawQuery(sql, null);
//			
//		} catch (Exception e) {
//			// Log
//			Log.e("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Exception => " + e.toString());
//			
//			rdb.close();
//			
//			return null;
//		}
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount() => " + c.getCount());

//		/****************************
//		 * 2.2. Add to list
//			****************************/
//		c.moveToFirst();
//		
//		List<AI> ai_list = new ArrayList<AI>();
//		
//		for (int i = 0; i < c.getCount(); i++) {
//		for (int i = 0; i < c.getCount() / 200; i++) {

//			String file_name, String file_path,
//			String title, String memo,
//			
//			long last_played_at,
//			
//			String table_name,
//			
//			long db_id, long created_at, long modified_at
			
//			{"file_name", 	"file_path",	"title", "memo",
//				"last_played_at",	"table_name"}
			
//			AI ai = new AI(
//					c.getString(3),	// file_name
//					c.getString(4),	// file_path
//					
//					c.getString(5),	// title
//					c.getString(6),	// memo
//					
//					c.getLong(7),
//					
//					c.getString(8),	// table_name
//					
//					c.getLong(9),	// length
//					
//					c.getLong(0),	// id
//					c.getLong(1),	// created_at
//					c.getLong(2)	// modified_at
//			);
//	
//			// Add to the list
//			ai_list.add(ai);
			
//			// Log
//			String file_full_path = StringUtils.join(
//						new String[]{
//								c.getString(4),
//								c.getString(3)
//						}, File.separator);
//			
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "file_full_path=" + file_full_path);
			
////			File f = new File(file_full_path);
//			
//			MediaPlayer mp = new MediaPlayer();
//			
//			try {
//				mp.setDataSource(file_full_path);
//				
//				mp.prepare();
//				
//				int len = mp.getDuration() / 1000;
//				
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"len=" + len
//						+ "(" + c.getString(3) + ")");
//
//				// REF=> http://stackoverflow.com/questions/9609479/android-mediaplayer-went-away-with-unhandled-events
//				mp.reset();
//				
//				// REF=> http://stackoverflow.com/questions/3761305/android-mediaplayer-throwing-prepare-failed-status-0x1-on-2-1-works-on-2-2
//				mp.release();
//				
//			} catch (IllegalArgumentException e) {
//				
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Exception=" + e.toString());
//				
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "message" + c.getString(3));
//				
//			} catch (IllegalStateException e) {
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Exception=" + e.toString());
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "message" + c.getString(3));
//				
//			} catch (IOException e) {
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Exception=" + e.toString());
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "message" + c.getString(3));
//
//			}
//			
//			//
//			c.moveToNext();
//			
//		}//for (int i = 0; i < c.getCount(); i++)
		
		
		
		/****************************
		 * 9. Close db
			****************************/
		rdb.close();
		
		/****************************
		 * 10. Return value
			****************************/
		
		return aiList;
		
	}//List<AI> selectData_ai

	
	public static void
	sortList_BM(List<BM> bmList, final SortOrder order) {
		// TODO Auto-generated method stub
		Collections.sort(bmList, new Comparator<BM>(){

//			@Override
			public int compare(BM b1, BM b2) {
				
				int res;
				
				switch (order) {
				
				case POSITION:
					
//					res = (int) (a1.getCreated_at() - a2.getCreated_at());
//					res = a1.getFile_name().compareTo(a2.getFile_name());
					res = (int)(b1.getPosition() - b2.getPosition());
					
					break;
					
				default:
					
					res = 1;
					
					break;
					
				}//switch (order)
				
				return res;
				
//				return (int) (a1.getCreated_at() - a2.getCreated_at());
			}//public int compare(BM b1, BM b2)
			
		});//Collections.sort()
		
	}//sortList_BM(List<BM> bmList, SortOrder order)

	/*********************************
	 * get_DirPath(String fullPath)<br>
	 * @return null => fullPath == null<br>
	 * 			String<br>
	 * 	example<br>
	 * 	"/data/data/ifm10.main/files"
	 * 		=> "/data/data/ifm10.main"
	 *********************************/
	public static String get_DirPath(String fullPath) {
		// String top => "/"?
		boolean has_root = false;
		
		if (StringUtils.substring(fullPath, 0, 1).equals(File.separator)) {
			
			has_root = true;
			
		}
		
		// Log
		Log.d("[" + "Methods.java : "
				+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ " : "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "StringUtils.substring(fullPath, 0, 1) => "
				+ StringUtils.substring(fullPath, 0, 1));
		
		// Return
		if (fullPath == null) {
			
			return null;
			
		} else {//if (fullPath == null)
			
			String[] array = StringUtils.split(fullPath, File.separator);
			
			int array_length = array.length;
			
			String[] result_array = Arrays.copyOfRange(array, 0, (array_length - 1));
			
			// Return
			if (has_root == true) {
				
				return File.separator + StringUtils.join(result_array, File.separator);
				
			} else {//if (has_root == true)
				
				return StringUtils.join(result_array, File.separator);
				
			}//if (has_root == true)
			
			
		}//if (fullPath == null)
		
	}//public static String get_DirPath(String fullPath) {

}//public class Methods

