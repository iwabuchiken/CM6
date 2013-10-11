package cm6.listeners.dialogues;

import cm6.items.AI;
import cm6.items.BM;
import cm6.main.R;
import cm6.utils.CONS;
import cm6.utils.DBUtils;
import cm6.utils.Methods;
import cm6.utils.Methods_dlg;
import cm6.utils.Tags;
import android.app.Activity;
import android.app.Dialog;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class DialogButtonOnClickListener implements OnClickListener {
	/*----------------------------
	 * Fields
		----------------------------*/
	//
	Activity actv;
	Dialog dlg1;
	Dialog dlg2;		//=> Used in dlg_input_empty_btn_XXX
	Dialog dlg3;

	AI ai;
	BM bm;
	
	//
	Vibrator vib;
	
	// Used in => Methods.dlg_addMemo(Activity actv, long file_id, String tableName)
	long file_id;
	String tableName;
	
	public DialogButtonOnClickListener(Activity actv, Dialog dlg1) {
		//
		this.actv = actv;
		this.dlg1 = dlg1;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	public DialogButtonOnClickListener(Activity actv, Dialog dlg1,
			Dialog dlg2) {
		//
		this.actv = actv;
		this.dlg1 = dlg1;
		this.dlg2 = dlg2;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	public DialogButtonOnClickListener(Activity actv, Dialog dlg1,
			Dialog dlg2, Dialog dlg3) {
		//
		this.actv = actv;
		this.dlg1 = dlg1;
		this.dlg2 = dlg2;
		this.dlg3 = dlg3;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	public DialogButtonOnClickListener(Activity actv, Dialog dlg1, long file_id, String tableName) {
		// 
		this.actv = actv;
		this.dlg1 = dlg1;
		
		this.tableName = tableName;
		
		this.file_id = file_id;
		
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
		
	}//public DialogButtonOnClickListener(Activity actv, Dialog dlg1, long file_id, String tableName)

//	@Override
	public DialogButtonOnClickListener(Activity actv, Dialog dlg1, AI ai) {

		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
		
		this.actv = actv;
		this.dlg1 = dlg1;
		
		this.ai = ai;

	}//public DialogButtonOnClickListener(Activity actv2, Dialog dlg4, AI ai)

	public DialogButtonOnClickListener(Activity actv, Dialog dlg1, BM bm) {
		// TODO Auto-generated constructor stub
		this.actv = actv;
		this.dlg1 = dlg1;
		this.bm = bm;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);

	}

	public DialogButtonOnClickListener(Activity actv, Dialog dlg1,
			Dialog dlg2, BM bm) {
		// TODO Auto-generated constructor stub
		this.actv = actv;
		this.dlg1 = dlg1;
		this.dlg2 = dlg2;
		
		this.bm = bm;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);

	}

	public DialogButtonOnClickListener(Activity actv, Dialog dlg1,
			Dialog dlg2, AI ai) {
		// TODO Auto-generated constructor stub
		this.actv	= actv;
		this.dlg1	= dlg1;
		this.dlg2	= dlg2;

		this.ai		= ai;
	}

	public void onClick(View v) {
		//
		Tags.DialogTags tag_name = (Tags.DialogTags) v.getTag();

		// Log
		Log.d("DialogButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "tag_name.name()=" + tag_name.name());
		
		// Log
		Log.d("DialogButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "Switching...");
		//
		switch (tag_name) {
		
		case dlg_generic_dismiss://------------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
			dlg1.dismiss();
			
			break;

		case dlg_generic_dismiss_second_dialog: // ----------------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
			dlg2.dismiss();
			
			break;// case dlg_generic_dismiss_second_dialog

		case dlg_generic_dismiss_third_dialog://------------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
			dlg3.dismiss();
			
			break;

		case dlg_create_folder_cancel://---------------------------------------------
			
			dlg1.dismiss();
			
			break;// case dlg_create_folder_cancel

		case dlg_create_folder_ok://--------------------------------------------------
			
			Methods_dlg.dlg_isEmpty(actv, dlg1);
			
			break;// case dlg_create_folder_ok

		case dlg_input_empty_reenter://----------------------------------------------
			
			dlg2.dismiss();
			
			break;// case dlg_input_empty_reenter

		case dlg_input_empty_cancel://---------------------------------------------
			
			dlg2.dismiss();
			dlg1.dismiss();
			
			break;// case dlg_input_empty_cancel

		case dlg_confirm_create_folder_cancel://---------------------------------------------
			
			dlg2.dismiss();
			
			break;

		// dlg_confirm_create_folder.xml
		case dlg_confirm_create_folder_ok://---------------------------------------------
			
			Methods.createFolder(actv, dlg1, dlg2);
			
			// debug
//			Toast.makeText(actv, "Create folder", Toast.LENGTH_SHORT).show();
			
			break;

		case dlg_confirm_remove_folder_cancel://---------------------------------------------
			
			dlg1.dismiss();
			
			break;// case dlg_confirm_remove_folder_cancel

		case dlg_confirm_remove_file_cancel://---------------------------------------------
			
			dlg2.dismiss();
			
			break;// case dlg_confirm_remove_folder_cancel
			
		// dlg_confirm_remove_folder.xml
		case dlg_confirm_remove_folder_ok://---------------------------------------------
			
			Methods.removeFolder(actv, dlg1);
			
			break;// case dlg_confirm_remove_folder_ok


		case dlg_confirm_move_files_ok: // ----------------------------------------------------
			
			case_dlg_confirm_move_files_ok();
//			Methods.moveFiles(actv, dlg1, dlg2);
			
			break;

		case dlg_add_memos_bt_add: // ----------------------------------------------------
			
			// Log
			Log.d("DialogButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "file_id => " + file_id);
			
			
			// Log
			Log.d("DialogButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Calling => Methods.addMemo()");
			
			vib.vibrate(Methods.vibLength_click);
			
			Methods.addMemo(actv, dlg1, file_id, tableName);
			
			break;// case dlg_add_memos_bt_add

		case dlg_register_patterns_register:// ----------------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
//			Methods.dlg_register_patterns_isInputEmpty(actv, dlg1);
			Methods_dlg.dlg_register_patterns_isInputEmpty(actv, dlg1, dlg2);
			
			break;

		case dlg_confirm_delete_patterns_ok:// ----------------------------------------------------
			
			Methods.delete_patterns(actv, dlg1, dlg2, dlg3);
			
			break;// case dlg_confirm_delete_patterns_ok
			
		case dlg_search_ok:// ---------------------------------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
			Methods.searchItem(actv, dlg1);
			
			break;// case dlg_search_ok

		case dlg_edit_title_bt_ok://----------------------------------------
			
			Methods.edit_title(actv, dlg1, ai);
			
			break;// case dlg_edit_title_bt_ok
			
		case dlg_edit_memo_bt_ok://----------------------------------------
			
			Methods.edit_memo(actv, dlg1, ai);
			
			break;// case dlg_edit_memo_bt_ok
			
		case dlg_confirm_move_files_search_ok: // ----------------------------------------------------
			
			case_dlg_confirm_move_files_search_ok();
//			Methods.moveFiles(actv, dlg1, dlg2);
			
			break;

		case dlg_edit_item_bt_ok:// ----------------------------------------------------
			
			
			// Log
			Log.d("DialogButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "case dlg_edit_item_bt_ok");
			
			
			// debugging....
			
			case_dlg_edit_item_bt_ok();
			
			break;// case dlg_edit_item_bt_ok
			
		default: // ----------------------------------------------------
			break;
		}//switch (tag_name)
	}//public void onClick(View v)

	private void case_dlg_edit_item_bt_ok() {
		// TODO Auto-generated method stub
		/***************************************
		 * Get data
		 ***************************************/
		EditText etTitle = (EditText) dlg2.findViewById(R.id.dlg_edit_item_et_title);
		EditText etMemo = (EditText) dlg2.findViewById(R.id.dlg_edit_item_et_memo);

		/***************************************
		 * Set: New data to bm instance
		 ***************************************/
		bm.setTitle(etTitle.getText().toString());
		bm.setMemo(etMemo.getText().toString());
		
		
		// Log
		Log.d("DialogButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]",
				"etTitle.getText().toString()=" + etTitle.getText().toString());
		
		/***************************************
		 * Store data to db
		 ***************************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
////		boolean res = dbu.deleteData_bm(actv, bm.getDbId());
		boolean res = dbu.updateData_BM_TitleAndMemo(
							actv,
							bm.getDbId(),
							bm);
		
//		// Log
//		Log.d("DialogButtonOnClickListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ ":"
//				+ Thread.currentThread().getStackTrace()[2].getMethodName()
//				+ "]", "bm.getDbId()=" + bm.getDbId());
		
		/***************************************
		 * Update: bmList
		 * 1. Remove the original bm element, using bm.getDbId()
		 * 2. Add to the list the new bm element
		 ***************************************/
		/***************************************
		 * 1. Remove the original bm element, using bm.getDbId()
		 ***************************************/
		for (int i = 0; i < CONS.BMActv.bmList.size(); i++) {
			
			BM b = CONS.BMActv.bmList.get(i);
			
			if (b.getDbId() == bm.getDbId()) {
				
//				// Log
//				Log.d("DialogButtonOnClickListener.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber()
//						+ ":"
//						+ Thread.currentThread().getStackTrace()[2]
//								.getMethodName() + "]",
//						"FOUND!:  b=" + b.getDbId()
//						+ "/"
//						+ "bm=" + bm.getDbId());
				
				CONS.BMActv.bmList.remove(b);
				
				CONS.BMActv.bmList.add(bm);

				Methods.sortList_BM(CONS.BMActv.bmList, CONS.BMActv.SortOrder.POSITION);
				
				break;
				
			}//if (b.getDbId() == bm.getDbId())
			
		}//for (int i = 0; i < CONS.BMActv.bmList.size(); i++)
		
		CONS.BMActv.adpBML.notifyDataSetChanged();
		
		/***************************************
		 * If successful, close dialog
		 ***************************************/
		dlg2.dismiss();
		dlg1.dismiss();
		
	}//private void case_dlg_edit_item_bt_ok()

	private void case_dlg_confirm_move_files_ok() {
		// TODO Auto-generated method stub
		Methods.moveFiles(actv, dlg1, dlg2);
		
	}

	private void case_dlg_confirm_move_files_search_ok() {
		// TODO Auto-generated method stub
		
//		// debug
//		Toast.makeText(actv, "Move files", Toast.LENGTH_LONG).show();
		
		Methods.moveFiles_search(actv, dlg1, dlg2);
	}

}//DialogButtonOnClickListener
