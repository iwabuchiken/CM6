package cm6.listeners.dialogues;

import cm6.utils.Methods;
import cm6.utils.Tags;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DialogButtonOnTouchListener implements OnTouchListener {

	/*----------------------------
	 * Fields
		----------------------------*/
	//
	Activity actv;
	Dialog dlg;
	
	public DialogButtonOnTouchListener(Activity actv, Dialog dlg) {
		//
		this.actv = actv;
		this.dlg = dlg;
	}
	
	public DialogButtonOnTouchListener(Activity actv) {
		//
		this.actv = actv;
	}

//	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		Tags.DialogTags tag_name = (Tags.DialogTags) v.getTag();
		
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
				switch (tag_name) {
				
				case dlg_generic_dismiss:
				case dlg_generic_dismiss_third_dialog:
				case dlg_generic_dismiss_second_dialog:
					
				case dlg_create_folder_ok:
				case dlg_create_folder_cancel:
				
				case dlg_input_empty_cancel:
				case dlg_input_empty_reenter:

				case dlg_confirm_create_folder_ok:
				case dlg_confirm_create_folder_cancel:
					
				case dlg_confirm_remove_folder_cancel:
				case dlg_confirm_remove_folder_ok:

				case dlg_confirm_move_files_ok:

				case dlg_search_ok:
					
				case dlg_register_patterns_register:

				case dlg_confirm_delete_patterns_ok:
					
				case dlg_edit_title_bt_ok:
					
				case dlg_edit_item_bt_ok:
					
					//
					v.setBackgroundColor(Color.GRAY);
					
					break;
				}//switch (tag_name)
		
			break;//case MotionEvent.ACTION_DOWN:
			
		case MotionEvent.ACTION_UP:
			switch (tag_name) {

			case dlg_generic_dismiss:
			case dlg_generic_dismiss_second_dialog:
			case dlg_generic_dismiss_third_dialog:

			case dlg_create_folder_ok:
			case dlg_create_folder_cancel:

			case dlg_input_empty_cancel:
			case dlg_input_empty_reenter:

			case dlg_confirm_create_folder_ok:
			case dlg_confirm_create_folder_cancel:

			case dlg_confirm_remove_folder_cancel:
			case dlg_confirm_remove_folder_ok:

			case dlg_confirm_move_files_ok:
				
			case dlg_search_ok:
				
			case dlg_register_patterns_register:
				
			case dlg_confirm_delete_patterns_ok:
				
			case dlg_edit_title_bt_ok:
				
			case dlg_edit_item_bt_ok:
				
				//
					v.setBackgroundColor(Color.WHITE);
					
					break;
				}//switch (tag_name)
		
			break;//case MotionEvent.ACTION_UP:
		
		}//switch (event.getActionMasked())
		return false;
	}

}
