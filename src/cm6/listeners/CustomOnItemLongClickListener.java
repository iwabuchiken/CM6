package cm6.listeners;


import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cm6.utils.Methods;
import cm6.utils.Methods_dlg;
import cm6.utils.Tags;

import android.app.Activity;
import android.app.Dialog;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class CustomOnItemLongClickListener implements OnItemLongClickListener {

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
	public CustomOnItemLongClickListener(Activity actv) {
		// 
		this.actv = actv;
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	/*----------------------------
	 * Used in => case dir_list_move_files
		----------------------------*/
	public CustomOnItemLongClickListener(Activity actv,
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
		Tags.ListTags tag = (Tags.ListTags) parent.getTag();
		
		// Log
		Log.d("CustomOnItemLongClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Tag => " + tag.name());

		//
//		vib.vibrate(400);
		vib.vibrate(40);
		
		switch (tag) {
		
		case actv_main_lv:
			
			int result = case_actv_main_lv(parent, position);
			
			if (result == 2) {
				
				return true;
				
			} else {//if (result == 2)
				
				break;
				
			}//if (result == 2)
			
		case dir_list_thumb_actv:
			
			break;
		
		}//switch (tag)
		
//		return false;
		return true;
		
	}//public boolean onItemLongClick()

	/*********************************
	 * @param position 
	 * @param parent 
	 * @return 2 => The item is a "list.txt" file<br>
	 * 			1 => Dialog process launched
	 *********************************/
	private int case_actv_main_lv(AdapterView<?> parent, int position) {
		// TODO Auto-generated method stub
		/*----------------------------
		 * 0. Get folder name
			----------------------------*/
		String folderName = (String) parent.getItemAtPosition(position);
		
		// Log
		Log.d("CustomOnItemLongClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "folderName: " + folderName);
		
		/*----------------------------
		 * 5.1.1. Is a directory?
			----------------------------*/
		String curPath = Methods.get_currentPath_from_prefs(actv);
		
		File targetFile = new File(curPath, folderName);
		
		if (targetFile.exists() && targetFile.isFile()) {
			// debug
			Toast.makeText(actv, "list.txt", Toast.LENGTH_SHORT).show();
			
			//			return false;
			return 2;		//=> "false" => Then, onClick process starts
			
		}//if (targetFile.exists() && targetFile.isFile())
		
		
		/*----------------------------
		 * 5.1.2. If yes, call a method
			----------------------------*/
		Methods_dlg.dlg_removeFolder(actv, folderName);
		
		/*********************************
		 * Return
		 *********************************/
		return 1;
	}//private void case_actv_main_lv()

}
