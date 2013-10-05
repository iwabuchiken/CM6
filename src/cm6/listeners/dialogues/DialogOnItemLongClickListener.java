package cm6.listeners.dialogues;


import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cm5.main.MainActv;
import cm5.main.R;
import cm5.utils.CONS;
import cm5.utils.Methods;
import cm5.utils.Tags;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class DialogOnItemLongClickListener implements OnItemLongClickListener {

	Activity actv;
	static Vibrator vib;

	//
	static Tags.ItemTags itemTag = null;
	
	//
	ArrayAdapter<String> dirListAdapter;	// Used in => case dir_list_move_files
	
	//
	Dialog dlg;	// Used in => case dir_list_move_files
	
	//
	List<String> fileNameList;	// Used in => case dir_list_move_files
	
	/****************************************
	 * Constructor
	 ****************************************/
	public DialogOnItemLongClickListener(Activity actv) {
		// 
		this.actv = actv;
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	/*----------------------------
	 * Used in => case dir_list_move_files
		----------------------------*/
	public DialogOnItemLongClickListener(Activity actv,
			Dialog dlg, ArrayAdapter<String> dirListAdapter, List<String> fileNameList) {
		// 
		this.actv = actv;
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
		this.dlg = dlg;
		this.fileNameList = fileNameList;
		this.dirListAdapter = dirListAdapter;
		
	}//public CustomOnItemLongClickListener

	/****************************************
	 * Methods
	 ****************************************/
//	@Override
	public boolean onItemLongClick(
										AdapterView<?> parent, View v,
										int position, long id) {
		/*----------------------------
		 * Steps
		 * 1. Get item
		 * 2. Get tag
		 * 3. Vibrate
		 * 
		 * 4. Is the tag null?
		 * 
		 * 5. If no, the switch
			----------------------------*/
		
		//
		Tags.DialogItemTags tag = (Tags.DialogItemTags) parent.getTag();
		
		// Log
		Log.d("DialogOnItemLongClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Tag => " + tag.name());

		//
//		vib.vibrate(400);
		vib.vibrate(40);
		
		switch (tag) {
		
		case dlg_move_files:

			/*----------------------------
			 * 0. Get folder name
				----------------------------*/
			String folderName = (String) parent.getItemAtPosition(position);

			case_dlg_move_files(folderName);
			


			
			break;
		
		}//switch (tag)
		
		
//		return false;
		return true;
		
	}//public boolean onItemLongClick()

	private void case_dlg_move_files(String folderName) {
		// TODO Auto-generated method stub
		// Log
		Log.d("DialogOnItemLongClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "folderName: " + folderName);
		
		/***************************************
		 * 5.1.1. Is a directory?
		 ***************************************/
//		String curPath = Methods.get_currentPath_from_prefs(actv);
		
//		File targetFile = new File(curPath, folderName);
//		File targetFile = new File(MainActv.dirPath_base, folderName);
		File targetFile = new File(CONS.dpath_base, folderName);

		/***************************************
		 * File exists?
		 ***************************************/
		if (!targetFile.exists()) {
			
			// Log
			Log.d("DialogOnItemLongClickListener.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "Item doesn't exist: " + targetFile.getAbsolutePath());
			
//			return true;
			return;
			
		}//if (!targetFile.exists())

		/***************************************
		 * If the file is a file, returns
		 ***************************************/
		if (targetFile.exists() && targetFile.isFile()) {
			// debug
			Toast.makeText(actv, "This is a file", 2000).show();
			
//			return false;
//			return true;		//=> "false" => Then, onClick process starts
			return;
			
		}//if (targetFile.exists() && targetFile.isFile())

		// Log
		Log.d("DialogOnItemLongClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "targetFile.getAbsolutePath(): " + targetFile.getAbsolutePath());

		/***************************************
		 * Build: List of directories
		 ***************************************/
		File[] files = new File(targetFile.getAbsolutePath()).listFiles(new FileFilter(){

//			@Override
			public boolean accept(File pathname) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				
				return pathname.isDirectory();
			}
			
		});//File[] files
		
		/***************************************
		 * Any subdirectories?
		 ***************************************/
		if (files.length < 1) {
			
			AlertDialog.Builder dialog=new AlertDialog.Builder(actv);
			
	        dialog.setTitle(actv.getString(R.string.generic_notice));
	        dialog.setMessage("No sub folders");
	        
	        dialog.setNegativeButton("OK",new DialogListener(actv, dialog, 1));
	        
	        dialog.create();
	        dialog.show();

			
		} else {//if (files.length < 1)
			
			// Log
			Log.d("DialogOnItemLongClickListener.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "files.length: " + files.length);
			
			fileNameList.clear();
			
			for (File eachFile : files) {
				
//				fileNameList.add(fileName);
//				fileNameList.add(eachFile.getName());
//				fileNameList.add(eachFile.getAbsolutePath());
//				fileNameList.add(Methods.convert_filePath_into_table_name(actv, eachFile.getAbsolutePath()));
//				fileNameList.add(Methods.convert_filePath_into_path_label(actv, eachFile.getAbsolutePath()));
				
				fileNameList.add(Methods.convert_filePath_into_path_label_no_base(actv, eachFile.getAbsolutePath()));

				// Log
				Log.d("DialogOnItemLongClickListener.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber()
						+ ":"
						+ Thread.currentThread().getStackTrace()[2]
								.getMethodName() + "]", "Sub folder=" + Methods.convert_filePath_into_path_label_no_base(actv, eachFile.getAbsolutePath()));
				
			}//for (String fileName : fileNames)
		
			Collections.sort(fileNameList);

			dirListAdapter.notifyDataSetChanged();

		}//if (files.length < 1)
		
//		return true;
		return;
		
		/*----------------------------
		 * 5.1.2. If yes, call a method
			----------------------------*/
//		Methods.dlg_removeFolder(actv, folderName);
		
	}//private void case_dlg_move_files(String folderName)

}//public class DialogOnItemLongClickListener implements OnItemLongClickListener
