package cm6.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cm6.items.AI;
import cm6.items.BM;
import cm6.items.TI;
import cm6.listeners.dialogues.DialogButtonOnClickListener;
import cm6.listeners.dialogues.DialogButtonOnTouchListener;
import cm6.listeners.dialogues.DialogOnItemClickListener;
import cm6.listeners.dialogues.DialogOnItemLongClickListener;
import cm6.main.ALActv;
import cm6.main.R;
import cm6.main.TNActv;

public class Methods_dlg {

	public static void dlg_moveFiles(Activity actv) {
		/****************************
		 * Steps
		 * 1. Get generic dialog
		 * 2. Get dir list
		 * 2-1. Set list to the adapter
		 * 3. Set adapter to the list view
		 * 4. Set listener to the view
		 * 
		 * 9. Show dialog
			****************************/
		
		Dialog dlg = dlg_template_cancel(
				// Activity, layout, title
				actv, R.layout.dlg_move_files, R.string.thumb_actv_menu_move_files,
				// Ok button, Cancel button
				R.id.dlg_move_files_bt_cancel,
				// Ok tag, Cancel tag
				Tags.DialogTags.dlg_generic_dismiss
							);
		
		/****************************
		 * 2. Get dir list
			****************************/
		File[] files = new File(CONS.dpath_base).listFiles(new FileFilter(){

//			@Override
			public boolean accept(File pathname) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				
				return pathname.isDirectory();
			}
			
		});//File[] files
		
		ALActv.fileNameList = new ArrayList<String>();
		
//		for (String fileName : fileNames) {
		for (File eachFile : files) {
			
//			fileNameList.add(fileName);
			ALActv.fileNameList.add(eachFile.getName());
			
		}//for (String fileName : fileNames)
		
		Collections.sort(ALActv.fileNameList);
		
		/****************************
		 * 2-1. Set list to the adapter
			****************************/
		ALActv.dirListAdapter = new ArrayAdapter<String>(
												actv,
												R.layout.simple_text_view,
												ALActv.fileNameList
											);
		
		/****************************
		 * 3. Set adapter to the list view
			****************************/
		//
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_move_files_lv_list);
		
		lv.setAdapter(ALActv.dirListAdapter);
		
		/****************************
		 * 4. Set listener to the view
		 * 		1. onClick
		 * 		2. onLongClick
			****************************/
		lv.setTag(Tags.DialogItemTags.dlg_move_files);

		lv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg));
		
		/****************************
		 * 4.2. onLongClick
			****************************/
//		lv.setTag(Methods.DialogItemTags.dlg_move_files);
		
		lv.setOnItemLongClickListener(
						new DialogOnItemLongClickListener(
												actv,
												dlg,
												ALActv.dirListAdapter, ALActv.fileNameList));
		
		/****************************
		 * 9. Show dialog
			****************************/
		dlg.show();
		
	}//public static void dlg_moveFiles(Activity actv)

	public static void dlg_moveFiles_search(Activity actv) {
		/****************************
		 * Steps
		 * 1. Get generic dialog
		 * 2. Get dir list
		 * 2-1. Set list to the adapter
		 * 3. Set adapter to the list view
		 * 4. Set listener to the view
		 * 
		 * 9. Show dialog
			****************************/
		
		Dialog dlg = dlg_template_cancel(
				// Activity, layout, title
				actv, R.layout.dlg_move_files, R.string.thumb_actv_menu_move_files,
				// Ok button, Cancel button
				R.id.dlg_move_files_bt_cancel,
				// Ok tag, Cancel tag
				Tags.DialogTags.dlg_generic_dismiss
							);
		
		/****************************
		 * 2. Get dir list
			****************************/
		File[] files = new File(CONS.dpath_base).listFiles(new FileFilter(){

//			@Override
			public boolean accept(File pathname) {
				
				
				return pathname.isDirectory();
			}
			
		});//File[] files
		
//		ALActv.fileNameList = new ArrayList<String>();
		CONS.Search.fileNameList = new ArrayList<String>();
		
//		for (String fileName : fileNames) {
		for (File eachFile : files) {
			
//			fileNameList.add(fileName);
//			ALActv.fileNameList.add(eachFile.getName());
			CONS.Search.fileNameList.add(eachFile.getName());
			
		}//for (String fileName : fileNames)
		
		Collections.sort(CONS.Search.fileNameList);
		
		/****************************
		 * 2-1. Set list to the adapter
			****************************/
//		ALActv.dirListAdapter = new ArrayAdapter<String>(
		CONS.Search.dirListAdapter = new ArrayAdapter<String>(
												actv,
												android.R.layout.simple_list_item_1,
												CONS.Search.fileNameList
											);

		/****************************
		 * 3. Set adapter to the list view
			****************************/
		//
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_move_files_lv_list);
		
		lv.setAdapter(CONS.Search.dirListAdapter);
		
		/****************************
		 * 4. Set listener to the view
		 * 		1. onClick
		 * 		2. onLongClick
			****************************/
		lv.setTag(Tags.DialogItemTags.dlg_move_files_search);

		lv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg));
		
		/****************************
		 * 4.2. onLongClick
			****************************/
//		lv.setTag(Methods.DialogItemTags.dlg_move_files);
		
//		lv.setOnItemLongClickListener(
//						new DialogOnItemLongClickListener(
//												actv,
//												dlg,
//												ALActv.dirListAdapter, ALActv.fileNameList));
		
		/****************************
		 * 9. Show dialog
			****************************/
		dlg.show();
		
	}//public static void dlg_moveFiles_search(Activity actv)

	public static Dialog dlg_template_cancel(Activity actv, int layoutId, int titleStringId,
			int cancelButtonId, Tags.DialogTags cancelTag) {
		/****************************
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		****************************/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(layoutId);
		
		// Title
		dlg.setTitle(titleStringId);
		
		/****************************
		* 2. Add listeners => OnTouch
		****************************/
		//
		Button btn_cancel = (Button) dlg.findViewById(cancelButtonId);
		
		//
		btn_cancel.setTag(cancelTag);
		
		//
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/****************************
		* 3. Add listeners => OnClick
		****************************/
		//
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		//
		//dlg.show();
		
		return dlg;
	
	}//public static Dialog dlg_template_okCancel()

	public static Dialog dlg_template_okCancel(Activity actv, int layoutId, int titleStringId,
			int okButtonId, int cancelButtonId, Tags.DialogTags okTag, Tags.DialogTags cancelTag) {
		/****************************
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		****************************/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(layoutId);
		
		// Title
		dlg.setTitle(titleStringId);
		
		/****************************
		* 2. Add listeners => OnTouch
		****************************/
		//
		Button btn_ok = (Button) dlg.findViewById(okButtonId);
		Button btn_cancel = (Button) dlg.findViewById(cancelButtonId);
		
		//
		btn_ok.setTag(okTag);
		btn_cancel.setTag(cancelTag);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/****************************
		* 3. Add listeners => OnClick
		****************************/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		//
		//dlg.show();
		
		return dlg;
	
	}//public static Dialog dlg_template_okCancel()

	public static
	Dialog dlg_template_okCancel
		(Activity actv, int layoutId, int titleStringId,
			int okButtonId, int cancelButtonId,
			Tags.DialogTags okTag, Tags.DialogTags cancelTag,
			BM bm) {
		/****************************
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		****************************/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(layoutId);
		
		// Title
		dlg.setTitle(titleStringId);
		
		/****************************
		* 2. Add listeners => OnTouch
		****************************/
		//
		Button btn_ok = (Button) dlg.findViewById(okButtonId);
		Button btn_cancel = (Button) dlg.findViewById(cancelButtonId);
		
		//
		btn_ok.setTag(okTag);
		btn_cancel.setTag(cancelTag);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/****************************
		* 3. Add listeners => OnClick
		****************************/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, bm));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		//
		//dlg.show();
		
		return dlg;
	
	}//public static Dialog dlg_template_okCancel()

	public static
	Dialog dlg_template_okCancel_SecondDialog
		(Activity actv, int layoutId, int titleStringId,
			int okButtonId, int cancelButtonId,
			Tags.DialogTags okTag, Tags.DialogTags cancelTag,
			
			Dialog dlg1, BM bm) {
		/****************************
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		****************************/
		
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(layoutId);
		
		// Title
		dlg2.setTitle(titleStringId);
		
		/****************************
		* 2. Add listeners => OnTouch
		****************************/
		//
		Button btn_ok = (Button) dlg2.findViewById(okButtonId);
		Button btn_cancel = (Button) dlg2.findViewById(cancelButtonId);
		
		//
		btn_ok.setTag(okTag);
		btn_cancel.setTag(cancelTag);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		
		/****************************
		* 3. Add listeners => OnClick
		****************************/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg1, dlg2, bm));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg2));
		
		//
		//dlg2.show();
		
		return dlg2;
	
	}//public static Dialog dlg_template_okCancel_SecondDialog()

	public static void dlg_confirm_moveFiles(Activity actv, Dialog dlg, String folderPath) {
		/****************************
		 * Steps
		 * 1. Get a confirm dialog
		 * 2. Set a chosen folder name to the view
		 * 9. Show dialog
			****************************/
		/****************************
		* 1. Get a confirm dialog
			* 1. Set up
			* 2. Add listeners => OnTouch
			* 3. Add listeners => OnClick
		****************************/
		
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_confirm_move_files);
		
		// Title
		dlg2.setTitle(R.string.generic_tv_confirm);
		
		/****************************
		* 2. Add listeners => OnTouch
		****************************/
		//
		Button btn_ok = (Button) dlg2.findViewById(R.id.dlg_confirm_move_files_btn_ok);
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_confirm_move_files_btn_cancel);
		
		//
		btn_ok.setTag(Tags.DialogTags.dlg_confirm_move_files_ok);
		btn_cancel.setTag(Tags.DialogTags.dlg_generic_dismiss_second_dialog);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		
		/****************************
		* 3. Add listeners => OnClick
		****************************/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
				
		/****************************
		 * 2. Set a chosen folder name to the view
			****************************/
		TextView tv_folder_name = (TextView) dlg2.findViewById(R.id.dlg_confirm_move_files_tv_table_name);
		
		tv_folder_name.setText(folderPath);
		
		/****************************
		 * 9. Show dialog
			****************************/
		dlg2.show();
		
	}//public static void dlg_confirm_moveFiles(Activity actv, Dialog dlg)

	public static void
	dlg_confirm_moveFiles_search
	(Activity actv, Dialog dlg, String folderPath) {
		/****************************
		 * Steps
		 * 1. Get a confirm dialog
		 * 2. Set a chosen folder name to the view
		 * 9. Show dialog
			****************************/
		/****************************
		* 1. Get a confirm dialog
			* 1. Set up
			* 2. Add listeners => OnTouch
			* 3. Add listeners => OnClick
		****************************/
		
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_confirm_move_files);
		
		// Title
		dlg2.setTitle(R.string.generic_tv_confirm);
		
		/****************************
		* 2. Add listeners => OnTouch
		****************************/
		//
		Button btn_ok = (Button) dlg2.findViewById(R.id.dlg_confirm_move_files_btn_ok);
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_confirm_move_files_btn_cancel);
		
		//
//		btn_ok.setTag(Tags.DialogTags.dlg_confirm_move_files_ok);
		btn_ok.setTag(Tags.DialogTags.dlg_confirm_move_files_search_ok);
		btn_cancel.setTag(Tags.DialogTags.dlg_generic_dismiss_second_dialog);

		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		
		/****************************
		* 3. Add listeners => OnClick
		****************************/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
				
		/****************************
		 * 2. Set a chosen folder name to the view
			****************************/
		TextView tv_folder_name = (TextView) dlg2.findViewById(R.id.dlg_confirm_move_files_tv_table_name);
		
		tv_folder_name.setText(folderPath);
		
		/****************************
		 * 9. Show dialog
			****************************/
		dlg2.show();
		
	}//public static void dlg_confirm_moveFiles_search()

	public static void dlg_addMemo(Activity actv, long file_id, String tableName) {
		/****************************
		 * Steps
		 * 1. Set up
		 * 1-2. Set text to edit text
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
		 * 
		 * 4. GridView
		 * 
		 * 8. Close db
		 * 9. Show dialog
			****************************/
		Dialog dlg = Methods_dlg.dlg_addMemo_1_get_dialog(actv, file_id, tableName);

		/****************************
		 * 4. GridView
		 * 	1. Set up db
		 * 	2. Get cursor
		 * 	3. Get list
		 * 	4. Adapter
		 * 	5. Set adapter to view
		 * 6. Set listener
			****************************/
		dlg = dlg_addMemo_2_set_gridview(actv, dlg);
		
		dlg.show();
		
	}//public static void dlg_addMemo(Activity actv, long file_id, String tableName)

	
	
	public static Dialog dlg_addMemo_1_get_dialog(Activity actv, long file_id, String tableName) {
		/****************************
		 * Steps
		 * 1. Set up
		 * 1-2. Set text to edit text
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
		 * 
		 * 4. GridView
		 * 
		 * 8. Close db
		 * 9. Show dialog
			****************************/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(R.layout.dlg_add_memos);
		
		// Title
		dlg.setTitle(R.string.dlg_add_memos_tv_title);
		
		/****************************
		 * 1-2. Set text to edit text
			****************************/
		TI ti = Methods.getData(actv, tableName, file_id);
		
		EditText et = (EditText) dlg.findViewById(R.id.dlg_add_memos_et_content);
		
		if (ti.getMemo() != null) {
			
			et.setText(ti.getMemo());
			
			et.setSelection(ti.getMemo().length());
			
		} else {//if (ti.getMemo() != null)
			
			et.setSelection(0);
			
		}//if (ti.getMemo() != null)
		
		/****************************
		 * 2. Add listeners => OnTouch
			****************************/
		//
		Button btn_add = (Button) dlg.findViewById(R.id.dlg_add_memos_bt_add);
		Button btn_cancel = (Button) dlg.findViewById(R.id.dlg_add_memos_cancel);
		
		Button btn_patterns = (Button) dlg.findViewById(R.id.dlg_add_memos_bt_patterns);
		
		// Tags
		btn_add.setTag(Tags.DialogTags.dlg_add_memos_bt_add);
		btn_cancel.setTag(Tags.DialogTags.dlg_generic_dismiss);
		
		btn_patterns.setTag(Tags.DialogTags.dlg_add_memos_bt_patterns);
		
		//
		btn_add.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		btn_patterns.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/****************************
		 * 3. Add listeners => OnClick
			****************************/
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "file_id => " + file_id);
		
		
		//
//		btn_add.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		btn_add.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, file_id, tableName));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		btn_patterns.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));

		
		return dlg;
		
	}//public static Dialog dlg_addMemo(Activity actv, long file_id, String tableName)

	public static Dialog
	dlg_addMemo_2_set_gridview(Activity actv, Dialog dlg) {
		/****************************
		 * 4.1. Set up db
			****************************/
		GridView gv = (GridView) dlg.findViewById(R.id.dlg_add_memos_gv);
		
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		/****************************
		 * 4.2. Get cursor
		 * 		1. Table exists?
		 * 		2. Get cursor
			****************************/
		/****************************
		 * 4.2.1. Table exists?
			****************************/
//		String tableName = MainActv.tableName_memo_patterns;
		String tableName = CONS.tname_memo_patterns;
		
		boolean res = dbu.tableExists(rdb, tableName);
		
		if (res == true) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + tableName);
			
			rdb.close();
			
//			return;
			
		} else {//if (res == false)
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tableName);
			
			rdb.close();
			
			SQLiteDatabase wdb = dbu.getWritableDatabase();
			
			res = dbu.createTable(wdb, tableName, CONS.cols_memo_patterns, CONS.col_types_memo_patterns);
			
			if (res == true) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + tableName);
				
				wdb.close();
				
			} else {//if (res == true)
				// Log
				Log.e("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create table failed: " + tableName);
				
				wdb.close();
				
				return dlg;
				
			}//if (res == true)

			
		}//if (res == false)
		
		
		/****************************
		 * 4.2.2. Get cursor
			****************************/
		rdb = dbu.getReadableDatabase();
		
		String sql = "SELECT * FROM " + tableName + " ORDER BY word ASC";
		
		Cursor c = rdb.rawQuery(sql, null);
		
		actv.startManagingCursor(c);
		
		c.moveToFirst();
		
		/****************************
		 * 4.3. Get list
			****************************/
		List<String> patternList = new ArrayList<String>();
		
		if (c.getCount() > 0) {
			
			for (int i = 0; i < c.getCount(); i++) {
				
				patternList.add(c.getString(1));
				
				c.moveToNext();
				
			}//for (int i = 0; i < patternList.size(); i++)
			
		} else {//if (c.getCount() > 0)
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "!c.getCount() > 0");
			
		}//if (c.getCount() > 0)
		
		
		Collections.sort(patternList);

		/****************************
		 * 4.4. Adapter
			****************************/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
										actv,
										R.layout.add_memo_grid_view,
										patternList
										);
		
		/****************************
		 * 4.5. Set adapter to view
			****************************/
		gv.setAdapter(adapter);
		
		/****************************
		 * 4.6. Set listener
			****************************/
//		gv.setTag(DialogTags.dlg_add_memos_gv);
		gv.setTag(Tags.DialogItemTags.dlg_add_memos_gv);
		
		gv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg));
		
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "GridView setup => Done");
		
		/****************************
		 * 8. Close db
			****************************/
		rdb.close();
		
		/****************************
		 * 9. Show dialog
			****************************/
//		dlg.show();
		
		return dlg;
		
	}//public static Dialog dlg_addMemo(Activity actv, long file_id, String tableName)

	public static void dlg_createFolder(Activity actv) {
		/****************************
		 * Steps
		 * 1. Set up
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
		 * 
		 * 4. CheckBox
			****************************/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(R.layout.dlg_create_folder);
		
		// Title
		dlg.setTitle(R.string.dlg_create_folder_title);
		
		//
		
		
		/****************************
		 * 2. Add listeners => OnTouch
			****************************/
		//
		Button btn_ok = (Button) dlg.findViewById(R.id.dlg_create_folder_bt_ok);
		Button btn_cancel = (Button) dlg.findViewById(R.id.dlg_create_folder_cancel);
		
		//
		btn_ok.setTag(Tags.DialogTags.dlg_create_folder_ok);
		btn_cancel.setTag(Tags.DialogTags.dlg_create_folder_cancel);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/****************************
		 * 3. Add listeners => OnClick
			****************************/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		/****************************
		 * 4. CheckBox
			****************************/
//		CheckBox cb = (CheckBox) dlg.findViewById(R.id.dlg_create_folder_cb_folder_set);
		
		//
		dlg.show();
		
	}//public static void dlg_createFolder(Activity actv)

	/****************************************
	 * <Caller> DialogButtonOnClickListener.onClick()
	 *  1. 
	 *  
	 *  <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static void dlg_isEmpty(Activity actv, Dialog dlg) {
		/****************************
		 * 1. Check if the input exists. If not, show a dialog
		 * 2. If yes, go to Methods.createFolder()
			****************************/
		/****************************
		 * 1. Check if the input exists. If not, show a dialog
			****************************/
		//
		EditText et = (EditText) dlg.findViewById(R.id.dlg_create_folder_et);
		String folderName = et.getText().toString();
		
		//
		if (!folderName.equals("")) {
			/****************************
			 * 2. If yes, go to Methods.createFolder()
				****************************/
			Methods_dlg.dlg_confirm_createFolder(actv, dlg);
			
			return;
			
		}//if (!folderName.equals(""))
		
		/****************************
		 * If not, show a dialog
			****************************/
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_input_empty);
		
		// Title
		dlg2.setTitle(R.string.dlg_input_empty_title);
		
		//
		
		
		/****************************
		 * 2. Add listeners => OnTouch
			****************************/
		//
		Button btn_ok = (Button) dlg2.findViewById(R.id.dlg_input_empty_btn_reenter);
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_input_empty_btn_cancel);
		
		//
		btn_ok.setTag(Tags.DialogTags.dlg_input_empty_reenter);
		btn_cancel.setTag(Tags.DialogTags.dlg_input_empty_cancel);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		
		/****************************
		 * 3. Add listeners => OnClick
			****************************/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		
		//
		dlg2.show();
		
	}//public static void dlg_isEmpty(Activity actv, Dialog dlg)

	private static void dlg_confirm_createFolder(Activity actv, Dialog dlg) {
		/****************************
		 * Steps
		 * 1. Set up
		 * 2. Set folder name to text view
			****************************/
		
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_confirm_create_folder);
		
		// Title
		dlg2.setTitle(R.string.generic_tv_confirm);
		
		/****************************
		 * 2. Set folder name to text view
			****************************/
		EditText et = (EditText) dlg.findViewById(R.id.dlg_create_folder_et);
		
		TextView tv = (TextView) dlg2.findViewById(R.id.dlg_confirm_create_folder_tv_table_name);
		
		tv.setText(et.getText().toString());
		
		/****************************
		 * 3. Add listeners => OnTouch
			****************************/
		//
		Button btn_ok = (Button) dlg2.findViewById(R.id.dlg_confirm_create_folder_btn_ok);
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_confirm_create_folder_btn_cancel);
		
		//
		btn_ok.setTag(Tags.DialogTags.dlg_confirm_create_folder_ok);
		btn_cancel.setTag(Tags.DialogTags.dlg_confirm_create_folder_cancel);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		
		/****************************
		 * 4. Add listeners => OnClick
			****************************/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		
		/****************************
		 * 5. Show dialog
			****************************/
		dlg2.show();
		
	}//private static void dlg_confirm_createFolder

	public static void dlg_confirm_delete_patterns(Activity actv, Dialog dlg,
			Dialog dlg2, String item) {
		/****************************
		 * 1. Set up dialog
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
		 * 
		 * 4. Set pattern name
		 * 5. Show dialog
			****************************/
		/****************************
		 * 1. Set up dialog
			****************************/
		Dialog dlg3 = new Dialog(actv);
		
		//
		dlg3.setContentView(R.layout.dlg_confirm_delete_patterns);
		
		// Title
		dlg3.setTitle(R.string.generic_tv_confirm);
		
		/****************************
		* 2. Add listeners => OnTouch
		****************************/
		//
		Button btn_ok = (Button) dlg3.findViewById(R.id.dlg_confirm_delete_patterns_btn_ok);
		Button btn_cancel = (Button) dlg3.findViewById(R.id.dlg_confirm_delete_patterns_btn_cancel);
		
		//
		btn_ok.setTag(Tags.DialogTags.dlg_confirm_delete_patterns_ok);
		btn_cancel.setTag(Tags.DialogTags.dlg_generic_dismiss_third_dialog);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg3));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg3));
		
		/****************************
		* 3. Add listeners => OnClick
		****************************/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2, dlg3));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2, dlg3));
		
		/****************************
		 * 4. Set pattern name
			****************************/
		TextView tv = (TextView) dlg3.findViewById(R.id.dlg_confirm_delete_patterns_tv_pattern_name);
		
		tv.setText(item);
		
		/****************************
		 * 5. Show dialog
			****************************/
		dlg3.show();
		
	}//public static void dlg_confirm_delete_patterns()

	public static void dlg_db_activity(Activity actv) {
		/****************************
		 * 1. Dialog
		 * 2. Prep => List
		 * 3. Adapter
		 * 4. Set adapter
		 * 
		 * 5. Set listener to list
		 * 6. Show dialog
			****************************/
		Dialog dlg = Methods_dlg.dlg_template_cancel(
									actv, R.layout.dlg_db_admin, 
									R.string.dlg_db_admin_title, 
									R.id.dlg_db_admin_bt_cancel, 
									Tags.DialogTags.dlg_generic_dismiss);
		
		/****************************
		 * 2. Prep => List
			****************************/
		String[] choices = {
										actv.getString(R.string.dlg_db_admin_item_backup_db),
										actv.getString(R.string.dlg_db_admin_item_refresh_db)
										};
		
		List<String> list = new ArrayList<String>();
		
		for (String item : choices) {
			
			list.add(item);
			
		}
		
		/****************************
		 * 3. Adapter
			****************************/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				actv,
//				R.layout.dlg_db_admin,
//				android.R.layout.simple_list_item_1,
				R.layout.simple_text_view,
				list
				);

		/****************************
		 * 4. Set adapter
			****************************/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_db_admin_lv);
		
		lv.setAdapter(adapter);
		
		/****************************
		 * 5. Set listener to list
			****************************/
		lv.setTag(Tags.DialogItemTags.dlg_db_admin_lv);
		
		lv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg));
		
		/****************************
		 * 6. Show dialog
			****************************/
		dlg.show();
		
		
	}//public static void dlg_db_activity(Activity actv)

//	public static void db_backup(Activity actv, Dialog dlg) {
//		/****************************
//		 * 1. Prep => File names
//		 * 2. Prep => Files
//		 * 2-2. Folder exists?
//		 * 
//		 * 2-3. Dst folder => Files within the limit?
//		 * 3. Copy
//			****************************/
//		String time_label = Methods.get_TimeLabel(Methods.getMillSeconds_now());
//		
//		String db_src = StringUtils.join(
//					new String[]{
//							CONS.dpath_db,
//							CONS.fname_db},
//					File.separator);
//		
//		String db_dst_folder = StringUtils.join(
//					new String[]{
//							CONS.dpath_db_backup,
//							CONS.fname_db_backup_trunk},
//					File.separator);
//		
////		// Log
////		Log.d("Methods.java" + "["
////				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////				+ "]", "db_src: " + db_src + " * " + "db_dst: " + db_dst);
////		
//		String db_dst = db_dst_folder + "_"
//				+ time_label
////				+ MainActv.fileName_db_backup_ext;
//				+ CONS.fname_db_backup_ext;
////				+ MainActv.fname_db_backup_trunk;
//		
////		// Log
////		Log.d("Methods.java" + "["
////				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////				+ "]", "db_src: " + db_src + " * " + "db_dst: " + db_dst);
//		
//		/****************************
//		 * 2. Prep => Files
//			****************************/
//		File src = new File(db_src);
//		File dst = new File(db_dst);
//		
//		/****************************
//		 * 2-2. Folder exists?
//			****************************/
//		File db_backup = new File(CONS.dpath_db_backup);
//		
//		if (!db_backup.exists()) {
//			
//			try {
//				db_backup.mkdir();
//				
//				// Log
//				Log.d("Methods.java" + "["
//						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//						+ "]", "Folder created: " + db_backup.getAbsolutePath());
//			} catch (Exception e) {
//				
//				// Log
//				Log.e("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Create folder => Failed");
//				
//				return;
//				
//			}
//			
//		} else {//if (!db_backup.exists())
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Folder exists: ");
//			
//		}//if (!db_backup.exists())
//		
//		/*********************************
//		 * 2-3. Dst folder => Files within the limit?
//		 *********************************/
//		File[] files_dst_folder = new File(CONS.dpath_db_backup).listFiles();
//		
//		int num_of_files = files_dst_folder.length;
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "num_of_files=" + num_of_files);
//		
//		/****************************
//		 * 3. Copy
//			****************************/
//		try {
//			FileChannel iChannel = new FileInputStream(src).getChannel();
//			FileChannel oChannel = new FileOutputStream(dst).getChannel();
//			iChannel.transferTo(0, iChannel.size(), oChannel);
//			iChannel.close();
//			oChannel.close();
//			
//			// Log
//			Log.d("ThumbnailActivity.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "File copied");
//			
//			// debug
//			Toast.makeText(actv, "DB backup => Done", 3000).show();
//
//			dlg.dismiss();
//			
//		} catch (FileNotFoundException e) {
//			// Log
//			Log.e("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Exception: " + e.toString());
//			
//		} catch (IOException e) {
//			// Log
//			Log.e("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Exception: " + e.toString());
//		}//try
//
//		
//	}//public static void db_backup(Activity actv, Dialog dlg, String item)

	public static void dlg_seratchItem(Activity actv) {
		/****************************
		 * Steps
		 * 1. Dialog
		 * 9. Show
			****************************/
		Dialog dlg = dlg_template_okCancel(
								actv, R.layout.dlg_search, R.string.dlg_search_title,
								
								R.id.dlg_search_bt_ok,
								R.id.dlg_search_cancel,
								
								Tags.DialogTags.dlg_search_ok,
								Tags.DialogTags.dlg_generic_dismiss);
		
		/****************************
		 * 9. Show
			****************************/
		dlg.show();
		
	}//public static void dlg_seratchItem(Activity actv)

	public static void dlg_delete_patterns(Activity actv, Dialog dlg) {
		/****************************
		 * 1. Set up
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
		 * 
		 * 4. Prep => List
		 * 5. Prep => Adapter
		 * 6. Set adapter
		 * 
		 * 7. Show dialog
			****************************/
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_delete_patterns);
		
		// Title
		dlg2.setTitle(R.string.dlg_delete_patterns_title);
		
		/****************************
		* 2. Add listeners => OnTouch
		****************************/
		//
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_delete_patterns_bt_cancel);
		
		//
		btn_cancel.setTag(Tags.DialogTags.dlg_generic_dismiss_second_dialog);
		
		//
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		
		/****************************
		* 3. Add listeners => OnClick
		****************************/
		//
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		
		/****************************
		 * 4. Prep => List
		 * 5. Prep => Adapter
		 * 6. Set adapter
			****************************/
		GridView gv = dlg_delete_patterns_2_grid_view(actv, dlg, dlg2);

		/****************************
		 * 7. Show dialog
			****************************/
		dlg2.show();
		
	}//public static void dlg_delete_patterns(Activity actv, Dialog dlg)

	private static GridView dlg_delete_patterns_2_grid_view(Activity actv, Dialog dlg, Dialog dlg2) {
		/****************************
		 * 1. Set up db
		 * 1-1. Get grid view
		 * 2. Table exists?
		 * 3. Get cursor
		 * 
		 * 4. Get list
		 * 5. Prep => Adapter
		 * 6. Set adapter to view
		 * 
		 * 7. Set listener
			****************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		/****************************
		 * 1-1. Get grid view
			****************************/
		GridView gv = (GridView) dlg2.findViewById(R.id.dlg_delete_patterns_gv);
		
		/****************************
		 * 2. Table exists?
			****************************/
		String tableName = CONS.tname_memo_patterns;
		
		boolean res = dbu.tableExists(rdb, tableName);
		
		if (res == true) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + tableName);
			
			rdb.close();
			
//			return;
			
		} else {//if (res == false)
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tableName);
			
			rdb.close();
			
			SQLiteDatabase wdb = dbu.getWritableDatabase();
			
			res = dbu.createTable(wdb, tableName, CONS.cols_memo_patterns, CONS.col_types_memo_patterns);
			
			if (res == true) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + tableName);
				
				wdb.close();
				
			} else {//if (res == true)
				// Log
				Log.e("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create table failed: " + tableName);
				
				wdb.close();
				
				return gv;
				
			}//if (res == true)

			
		}//if (res == false)
		
		/****************************
		 * 3. Get cursor
			****************************/
		rdb = dbu.getReadableDatabase();
		
		String sql = "SELECT * FROM " + tableName + " ORDER BY word ASC";
		
		Cursor c = rdb.rawQuery(sql, null);
		
		actv.startManagingCursor(c);
		
		c.moveToFirst();
		
		/****************************
		 * 4. Get list
			****************************/
		List<String> patternList = new ArrayList<String>();
		
		if (c.getCount() > 0) {
			
			for (int i = 0; i < c.getCount(); i++) {
				
//				patternList.add(c.getString(1));
				patternList.add(c.getString(3));
				
				c.moveToNext();
				
			}//for (int i = 0; i < patternList.size(); i++)
			
		} else {//if (c.getCount() > 0)
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "!c.getCount() > 0");
			
		}//if (c.getCount() > 0)
		
		
		Collections.sort(patternList);

		/****************************
		 * 5. Prep => Adapter
			****************************/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
										actv,
										R.layout.add_memo_grid_view,
										patternList
										);
		
		/****************************
		 * 6. Set adapter to view
			****************************/
		gv.setAdapter(adapter);
		
		/****************************
		 * 7. Set listener
			****************************/
//		gv.setTag(DialogTags.dlg_add_memos_gv);
		gv.setTag(Tags.DialogItemTags.dlg_delete_patterns_gv);
		
		gv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg, dlg2));
		
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "GridView setup => Done");
		
		/****************************
		 * 8. Close db
			****************************/
		rdb.close();

		return gv;
	}//private static GridView dlg_delete_patterns_2_grid_view(Activity actv, Dialog dlg, Dialog dlg2)

	public static void dlg_edit_title(Activity actv, AI ai) {
		/*********************************
		 * 1. Build dialog
		 * 2. Add listeners
		 * 		1. OnTouch
		 * 		2. OnClick
		 * 
		 * 3. Set text
		 * 
		 * 
		 * 
		 * 9. Show dialog
		 *********************************/
		/*********************************
		 * 1. Build dialog
		 *********************************/
//		Dialog dlg = Methods.dlg_template_okCancel(
//							actv, R.layout.dlg_edit_title,
//							R.string.dlg_edit_title_title,
//							R.id.dlg_edit_title_bt_add,
//							R.id.dlg_edit_title_bt_cancel,
//							Methods.DialogTags.dlg_edit_title_bt_ok,
//							Methods.DialogTags.dlg_generic_dismiss);

		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(R.layout.dlg_edit_title);
		
		// Title
		dlg.setTitle(R.string.dlg_edit_title_title);
		
		/****************************
		* 2. Add listeners
		* 2.1. OnTouch
		****************************/
		//
		Button btn_ok = (Button) dlg.findViewById(R.id.dlg_edit_title_bt_add);
		Button btn_cancel = (Button) dlg.findViewById(R.id.dlg_edit_title_bt_cancel);
		
		//
		btn_ok.setTag(Tags.DialogTags.dlg_edit_title_bt_ok);
		btn_cancel.setTag(Tags.DialogTags.dlg_generic_dismiss);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/****************************
		* 2.2. OnClick
		****************************/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, ai));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));

		
		EditText et = (EditText) dlg.findViewById(R.id.dlg_edit_title_et_content);
		
		/*********************************
		 * Grid view
		 *********************************/
		dlg = Methods_dlg.dlg_edit_title_2_grid_view(actv, dlg, ai);
		
		/*********************************
		 * 3. Set text
		 *********************************/
		String title = ai.getTitle();
		
		
//		if (ai.getTitle().equals("")) {
		if (title.equals("")) {

			title = "";
			
//			et.setText("Title");
			et.setText(title);
			
		} else {//if (ai.getTitle().equals(""))
			
//			et.setText(ai.getTitle());
			et.setText(title);
			
		}//if (ai.getTitle().equals(""))
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "title.length()=" + title.length());
		
		et.setSelection(title.length());
		
		/*********************************
		 * 9. Show dialog
		 *********************************/
		dlg.show();
		
	}//public static void dlg_edit_title(Activity actv, AI ai)

	private static Dialog dlg_edit_title_2_grid_view(Activity actv, Dialog dlg,
			AI ai) {
		// TODO Auto-generated method stub
		/****************************
		 * 4.1. Set up db
			****************************/
		GridView gv = (GridView) dlg.findViewById(R.id.dlg_edit_title_gv);
		
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		/****************************
		 * 4.2. Get cursor
		 * 		1. Table exists?
		 * 		2. Get cursor
			****************************/
		/****************************
		 * 4.2.1. Table exists?
			****************************/
//		String tableName = MainActv.tableName_memo_patterns;
		String tableName = CONS.tname_memo_patterns;
		
		boolean res = dbu.tableExists(rdb, tableName);
		
		if (res == true) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + tableName);
			
			rdb.close();
			
//			return;
			
		} else {//if (res == false)
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tableName);
			
			rdb.close();
			
			SQLiteDatabase wdb = dbu.getWritableDatabase();
			
			res = dbu.createTable(
							wdb,
							tableName,
							CONS.cols_memo_patterns,
							CONS.col_types_memo_patterns);
			
			if (res == true) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + tableName);
				
				wdb.close();
				
			} else {//if (res == true)
				// Log
				Log.e("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create table failed: " + tableName);
				
				wdb.close();
				
				return dlg;
				
			}//if (res == true)

			
		}//if (res == false)
		
		
		/****************************
		 * 4.2.2. Get cursor
			****************************/
		rdb = dbu.getReadableDatabase();
		
		String sql = "SELECT * FROM " + tableName + " ORDER BY word ASC";
		
		Cursor c = rdb.rawQuery(sql, null);
		
		actv.startManagingCursor(c);
		
		c.moveToFirst();
		
		/****************************
		 * 4.3. Get list
			****************************/
		List<String> patternList = new ArrayList<String>();
		
		if (c.getCount() > 0) {
			
			for (int i = 0; i < c.getCount(); i++) {
				
				patternList.add(c.getString(3));	// "word"
				
				c.moveToNext();
				
			}//for (int i = 0; i < patternList.size(); i++)
			
		} else {//if (c.getCount() > 0)
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "!c.getCount() > 0");
			
		}//if (c.getCount() > 0)
		
		
		Collections.sort(patternList);

		/****************************
		 * 4.4. Adapter
			****************************/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
										actv,
										R.layout.add_memo_grid_view,
										patternList
										);
		
		/****************************
		 * 4.5. Set adapter to view
			****************************/
		gv.setAdapter(adapter);
		
		/****************************
		 * 4.6. Set listener
			****************************/
//		gv.setTag(DialogTags.dlg_add_memos_gv);
		gv.setTag(Tags.DialogItemTags.dlg_add_memos_gv);
		
		gv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg));
		
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "GridView setup => Done");
		
		/****************************
		 * 8. Close db
			****************************/
		rdb.close();
		
		/****************************
		 * 9. Show dialog
			****************************/
//		dlg.show();
		
		return dlg;
		
	}//private static Dialog dlg_edit_title_2_grid_view

	public static void dlg_patterns(Activity actv) {
		/****************************
		 * memo
			****************************/
		Dialog dlg = Methods_dlg.dlg_template_cancel(
													actv, R.layout.dlg_admin_patterns, 
													R.string.dlg_memo_patterns_title, 
													R.id.dlg_admin_patterns_bt_cancel, 
													Tags.DialogTags.dlg_generic_dismiss);
		
		/****************************
		 * 2. Prep => List
			****************************/
		String[] choices = {
				actv.getString(R.string.generic_tv_register),
				actv.getString(R.string.generic_tv_edit),
				actv.getString(R.string.generic_tv_delete)
		};
		
		List<String> list = new ArrayList<String>();
		
		for (String item : choices) {
			
			list.add(item);
			
		}
		
		/****************************
		 * 3. Adapter
			****************************/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				actv,
//				R.layout.dlg_db_admin,
				android.R.layout.simple_list_item_1,
				list
				);

		/****************************
		 * 4. Set adapter
			****************************/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_admin_patterns_lv);
		
		lv.setAdapter(adapter);

		/****************************
		 * 5. Set listener to list
			****************************/
		lv.setTag(Tags.DialogItemTags.dlg_admin_patterns_lv);
		
		lv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg));
		
		/****************************
		 * 6. Show dialog
			****************************/
		dlg.show();
		
	}//public static void dlg_patterns(Activity actv)

	public static void dlg_register_patterns(Activity actv) {
		/****************************
		 * Steps
		 * 1. Dialog
		 * 9. Show
			****************************/
		Dialog dlg = dlg_template_okCancel(
					actv, R.layout.dlg_register_patterns, R.string.dlg_register_patterns_title,
				R.id.dlg_register_patterns_btn_create, R.id.dlg_register_patterns_btn_cancel, 
				Tags.DialogTags.dlg_register_patterns_register, Tags.DialogTags.dlg_generic_dismiss);
		
		
		/****************************
		 * 9. Show
			****************************/
		dlg.show();
	}//public static void dlg_register_patterns(Activity actv)

	public static void dlg_register_patterns(Activity actv, Dialog dlg) {
		/****************************
		 * Steps
		 * 1. Dialog
		 * 9. Show
			****************************/
//		Dialog dlg2 = dlg_template_okCancel(
//					actv, , ,
//				, , 
//				, );
		
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_register_patterns);
		
		// Title
		dlg2.setTitle(R.string.dlg_register_patterns_title);
		
		/****************************
		* 2. Add listeners => OnTouch
		****************************/
		//
		Button btn_ok = (Button) dlg2.findViewById(R.id.dlg_register_patterns_btn_create);
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_register_patterns_btn_cancel);
		
		//
		btn_ok.setTag(Tags.DialogTags.dlg_register_patterns_register);
		btn_cancel.setTag(Tags.DialogTags.dlg_generic_dismiss_second_dialog);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		
		/****************************
		* 3. Add listeners => OnClick
		****************************/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		
		/****************************
		 * 9. Show
			****************************/
		dlg2.show();
		
	}//public static void dlg_register_patterns(Activity actv)

	public static void dlg_register_patterns_isInputEmpty(Activity actv, Dialog dlg) {
		/****************************
		 * Steps
		 * 1. Get views
		 * 2. Prepare data
		 * 3. Register data
		 * 4. Dismiss dialog
			****************************/
		// Get views
		EditText et_word = (EditText) dlg.findViewById(R.id.dlg_register_patterns_et_word);
		EditText et_table_name = 
					(EditText) dlg.findViewById(R.id.dlg_register_patterns_et_table_name);
		
		if (et_word.getText().length() == 0) {
			// debug
			Toast.makeText(actv, "������Ă�������", 3000).show();
			
			return;
		}// else {//if (et_column_name.getText().length() == 0)
		
		/****************************
		 * 2. Prepare data
			****************************/
		//
		String word = et_word.getText().toString();
		String table_name = et_table_name.getText().toString();
		
		/****************************
		 * 3. Register data
			****************************/
		boolean result = Methods.insertDataIntoDB(actv, CONS.table_name_memo_patterns, 
								CONS.cols_memo_patterns, new String[]{word, table_name});
		
		/****************************
		 * 4. Dismiss dialog
			****************************/
		if (result == true) {
		
			dlg.dismiss();
			
		} else {//if (result == true)

			// debug
			Toast.makeText(actv, "������ۊǂł��܂���ł���", 3000).show();

		}//if (result == true)
		
		
	}//public static void dlg_register_patterns_isInputEmpty(Activity actv, Dialog dlg)

	public static void dlg_register_patterns_isInputEmpty(
							Activity actv, Dialog dlg, Dialog dlg2) {
		/****************************
		 * Steps
		 * 1. Get views
		 * 2. Prepare data
		 * 3. Register data
		 * 4. Dismiss dialog
			****************************/
		// Get views
		EditText et_word = (EditText) dlg2.findViewById(R.id.dlg_register_patterns_et_word);
		EditText et_table_name = 
					(EditText) dlg2.findViewById(R.id.dlg_register_patterns_et_table_name);
		
		if (et_word.getText().length() == 0) {
			// debug
			Toast.makeText(actv, "������Ă�������", 3000).show();
			
			return;
		}// else {//if (et_column_name.getText().length() == 0)
		
		/****************************
		 * 2. Prepare data
			****************************/
		//
		String word = et_word.getText().toString();
		String table_name = et_table_name.getText().toString();
		
		/****************************
		 * 3. Register data
			****************************/
		boolean result = Methods.insertDataIntoDB(actv, CONS.table_name_memo_patterns, 
								CONS.cols_memo_patterns, new String[]{word, table_name});
		
		/****************************
		 * 4. Dismiss dialog
			****************************/
		if (result == true) {
		
			dlg.dismiss();
			dlg2.dismiss();
			
			// debug
			Toast.makeText(actv, "��^���ۊǂ��܂���", 3000).show();
			
		} else {//if (result == true)

			// debug
			Toast.makeText(actv, "��^���ۊǂł��܂���ł���", 3000).show();

		}//if (result == true)
		
		
	}//public static void dlg_register_patterns_isInputEmpty(Activity actv, Dialog dlg)

	public static void dlg_removeFolder(Activity actv, String folderName) {
		/****************************
		 * Steps
		 * 1. Set up
		 * 2. Set folder name to text view
			****************************/
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(R.layout.dlg_confirm_remove_folder);
		
		// Title
		dlg.setTitle(R.string.generic_tv_confirm);
		
		/****************************
		 * 2. Set folder name to text view
			****************************/
		TextView tv = (TextView) dlg.findViewById(R.id.dlg_confirm_remove_folder_tv_table_name);
		
		tv.setText(folderName);
		
		/****************************
		 * 3. Add listeners => OnTouch
			****************************/
		//
		Button btn_ok = (Button) dlg.findViewById(R.id.dlg_confirm_remove_folder_btn_ok);
		Button btn_cancel = (Button) dlg.findViewById(R.id.dlg_confirm_remove_folder_btn_cancel);
		
		//
		btn_ok.setTag(Tags.DialogTags.dlg_confirm_remove_folder_ok);
		btn_cancel.setTag(Tags.DialogTags.dlg_confirm_remove_folder_cancel);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/****************************
		 * 4. Add listeners => OnClick
			****************************/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		/****************************
		 * 5. Show dialog
			****************************/
		dlg.show();

		
	}//public static void dlg_removeFolder(Activity actv)
	
	public static void dlg_removeAiFile(Activity actv, AI ai, Dialog dlg1) {
		/****************************
		 * Steps
		 * 1. Set up
		 * 2. Set folder name to text view
		 ****************************/
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_confirm_remove_folder);
		
		// Title
		dlg2.setTitle(R.string.generic_tv_confirm);
		
		/****************************
		 * 2. Set folder name to text view
		 ****************************/
		TextView tvFileName = (TextView) dlg2.findViewById(
									R.id.dlg_confirm_remove_folder_tv_table_name);
		
		tvFileName.setText(ai.getFile_name());
		
		/****************************
		 * 2. Set message
		 ****************************/
		TextView tvMessage = (TextView) dlg2.findViewById(
				R.id.dlg_confirm_remove_folder_tv_message);
		
		tvMessage.setText(actv.getString(R.string.generic_confirm_remove_file));
		
		/****************************
		 * 3. Add listeners => OnTouch
		 ****************************/
		//
		Button btn_ok = (Button) dlg2.findViewById(R.id.dlg_confirm_remove_folder_btn_ok);
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_confirm_remove_folder_btn_cancel);
		
		//
		btn_ok.setTag(Tags.DialogTags.dlg_confirm_remove_file_ok);
		btn_cancel.setTag(Tags.DialogTags.dlg_confirm_remove_file_cancel);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg1));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg1));
		
		/****************************
		 * 4. Add listeners => OnClick
		 ****************************/
		//
		btn_ok.setOnClickListener(
						new DialogButtonOnClickListener(actv, dlg1, dlg2, ai));
		
		btn_cancel.setOnClickListener(
						new DialogButtonOnClickListener(actv, dlg1, dlg2));
		
		/****************************
		 * 5. Show dialog
		 ****************************/
		dlg2.show();
		
		
	}//public static void dlg_removeFolder(Activity actv)

	public static void dlg_edit_memo(Activity actv, AI ai) {
		// TODO Auto-generated method stub
		/*********************************
		 * 1. Build dialog
		 * 2. Add listeners
		 * 		1. OnTouch
		 * 		2. OnClick
		 * 
		 * 3. Set text
		 * 
		 * 
		 * 
		 * 9. Show dialog
		 *********************************/
		/*********************************
		 * 1. Build dialog
		 *********************************/
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(R.layout.dlg_edit_title);
		
		// Title
		dlg.setTitle(R.string.dlg_edit_memo_title);
		
		/****************************
		* 2. Add listeners
		* 2.1. OnTouch
		****************************/
		//
		Button btn_ok = (Button) dlg.findViewById(R.id.dlg_edit_title_bt_add);
		Button btn_cancel = (Button) dlg.findViewById(R.id.dlg_edit_title_bt_cancel);
		
		//
		btn_ok.setTag(Tags.DialogTags.dlg_edit_memo_bt_ok);
		btn_cancel.setTag(Tags.DialogTags.dlg_generic_dismiss);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/****************************
		* 2.2. OnClick
		****************************/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, ai));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));

		
		EditText et = (EditText) dlg.findViewById(R.id.dlg_edit_title_et_content);
		
		/*********************************
		 * Grid view
		 *********************************/
		dlg = Methods_dlg.dlg_edit_title_2_grid_view(actv, dlg, ai);
		
		/*********************************
		 * 3. Set text
		 *********************************/
		String title = ai.getMemo();
		
		
//		if (ai.getTitle().equals("")) {
		if (title.equals("")) {

			title = "";
			
//			et.setText("Title");
			et.setText(title);
			
		} else {//if (ai.getTitle().equals(""))
			
//			et.setText(ai.getTitle());
			et.setText(title);
			
		}//if (ai.getTitle().equals(""))
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "title.length()=" + title.length());
		
		et.setSelection(title.length());
		
		/*********************************
		 * 9. Show dialog
		 *********************************/
		dlg.show();
		
	}//public static void dlg_edit_memo(Activity actv, AI ai)

	
	public static void dlg_admin(Activity actv) {
		// TODO Auto-generated method stub
		Dialog dlg = Methods_dlg.dlg_template_cancel(
				actv, R.layout.dlg_db_admin, 
				R.string.main_opt_menu_admin, 
				R.id.dlg_db_admin_bt_cancel, 
				Tags.DialogTags.dlg_generic_dismiss);

		/****************************
		* 2. Prep => List
		****************************/
		String[] choices = {
							actv.getString(R.string.admin_reset_table_bm),
							actv.getString(R.string.admin_reset_table_history),
							actv.getString(R.string.admin_start_service_player),
							actv.getString(R.string.admin_stop_service_player),
							
							};
		
		List<String> list = new ArrayList<String>();
		
		for (String item : choices) {
		
			list.add(item);
		
		}
		
		/****************************
		* 3. Adapter
		****************************/
		ArrayAdapter<String> adapter =
					new ArrayAdapter<String>(
						actv,
						//R.layout.dlg_db_admin,
						android.R.layout.simple_list_item_1,
						list
		);
		
		/****************************
		* 4. Set adapter
		****************************/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_db_admin_lv);
		
		lv.setAdapter(adapter);
		
		/****************************
		* 5. Set listener to list
		****************************/
		lv.setTag(Tags.DialogItemTags.main_opt_menu_admin);
		
		lv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg));
		
		/****************************
		* 6. Show dialog
		****************************/
		dlg.show();
		
	}//public static void dlg_admin(Activity actv)

	public static void dlg_AIList(Activity actv, AI ai) {
		// TODO Auto-generated method stub
		Dialog dlg = Methods_dlg.dlg_template_cancel(
				actv, R.layout.dlg_db_admin, 
				R.string.generic_tv_menu, 
				R.id.dlg_db_admin_bt_cancel, 
				Tags.DialogTags.dlg_generic_dismiss);

		/*----------------------------
		* 2. Prep => List
		----------------------------*/
		String[] choices = {
				actv.getString(R.string.generic_tv_edit),
				actv.getString(R.string.generic_tv_delete),
		};
		
		List<String> list = new ArrayList<String>();
		
		for (String item : choices) {
		
			list.add(item);
		
		}
		
		/*----------------------------
		* 3. Adapter
		----------------------------*/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						actv,
						//R.layout.dlg_db_admin,
						R.layout.simple_text_view,
						list
		);
		
		/*----------------------------
		* 4. Set adapter
		----------------------------*/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_db_admin_lv);
		
		lv.setAdapter(adapter);
		
		/*----------------------------
		* 5. Set listener to list
		----------------------------*/
		lv.setTag(Tags.DialogItemTags.dlg_ai_list);
		
		lv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg, ai));
		
		/*----------------------------
		* 6. Show dialog
		----------------------------*/
		dlg.show();

	}//public static void dlg_TNList(Activity actv, TI ti)

}//public class Methods_dialog
