package cm6.utils;

public class Tags {

	public static enum DialogTags {
		// Generics
		dlg_generic_dismiss, dlg_generic_dismiss_second_dialog, dlg_generic_dismiss_third_dialog,
		
		
		// dlg_create_folder.xml
		dlg_create_folder_ok, dlg_create_folder_cancel,

		// dlg_input_empty.xml
		dlg_input_empty_reenter, dlg_input_empty_cancel,
		
		// dlg_confirm_create_folder.xml
		dlg_confirm_create_folder_ok, dlg_confirm_create_folder_cancel,

		// dlg_confirm_remove_folder.xml
		dlg_confirm_remove_folder_ok, dlg_confirm_remove_folder_cancel,

		// dlg_drop_table.xml
		dlg_drop_table_btn_cancel, dlg_drop_table,
		
		// dlg_confirm_drop.xml
		dlg_confirm_drop_table_btn_ok, dlg_confirm_drop_table_btn_cancel,
		
		// dlg_add_memos.xml
		dlg_add_memos_bt_add, dlg_add_memos_bt_cancel, dlg_add_memos_bt_patterns,
		dlg_add_memos_gv,

		// dlg_move_files.xml
		dlg_move_files_move, dlg_move_files,
		
		// dlg_confirm_move_files.xml	=> ok, cancel, dlg tag
		dlg_confirm_move_files_ok, dlg_confirm_move_files_cancel, dlg_confirm_move_files,

		// dlg_item_menu.xml
		dlg_item_menu_bt_cancel, dlg_item_menu,

		// dlg_create_table.xml
		dlg_create_table_bt_create,

		// dlg_memo_patterns.xml
		dlg_memo_patterns,
		
		// dlg_register_patterns.xml
		dlg_register_patterns_register,

		// dlg_search.xml
		dlg_search, dlg_search_ok,

		// dlg_admin_patterns.xml

		// dlg_confirm_delete_patterns.xml
		dlg_confirm_delete_patterns_ok,
		
		// dlg_edit_title.xml
		dlg_edit_title_bt_ok,
		
		// dlg_edit_title.xml(Dialog: egit memo)
		dlg_edit_memo_bt_ok,

		// dlg_confirm_move_files_search	=> ok, cancel, dlg tag
		dlg_confirm_move_files_search_ok,
		
		// dlg_edit_item.xml
		dlg_edit_item_bt_ok,
		
		
	}//public static enum DialogTags
	
	public static enum DialogItemTags {
		// dlg_moveFiles(Activity actv)
		dlg_move_files,
		
		// dlg_add_memos.xml
		dlg_add_memos_gv,

		// dlg_db_admin.xml
		dlg_db_admin_lv,

		// dlg_admin_patterns.xml
		dlg_admin_patterns_lv,

		// dlg_delete_patterns.xml
		dlg_delete_patterns_gv,

		// dlg_moveFiles_search(Activity actv)
		dlg_move_files_search,

		// main_opt_menu_admin
		main_opt_menu_admin,
		
		// dlg_bmactv_list_long_click
		dlg_bmactv_list_long_click,
		
	}//public static enum DialogItemTags
	
	
	public static enum ButtonTags {
		// MainActivity.java
		ib_up,
		
		// DBAdminActivity.java
		db_manager_activity_create_table, db_manager_activity_drop_table, 
		db_manager_activity_register_patterns,
		
		// thumb_activity.xml
		thumb_activity_ib_back, thumb_activity_ib_bottom, thumb_activity_ib_top,
		
		// image_activity.xml
		image_activity_back, image_activity_prev, image_activity_next,
		
		// AILAdapter.java
		ailist_cb,
		
		// actv_play.xml
		actv_play_bt_play, actv_play_bt_stop, actv_play_bt_back,
		actv_play_tv_title, actv_play_tv_memo,
		
		actv_play_bt_see_bm, actv_play_bt_add_bm,
		
		// SearchActv.java
		actv_search_ib_bottom, actv_search__ib_top,

		// AILAdapter_move.java
		ailist_cb_search,
		
		// actv_bm.xml
		actv_bm_bt_back,
		
		// actv_hist.xml
		actv_hist_ib_back, actv_hist_ib_bottom, actv_hist_ib_top,

	}//public static enum ButtonTags
	
	public static enum ItemTags {
		
		// MainActivity.java
		dir_list,
		
		// ThumbnailActivity.java
		dir_list_thumb_actv,
		
		// Methods.java
		dir_list_move_files,
		
		// TIListAdapter.java
		tilist_checkbox,
		
		// SearchActv.java
		dir_list_actv_search,
		
		// actv_hist.xml
		dir_list_actv_hist,
		
	}//public static enum ItemTags

//	public static enum MoveMode {
//		// TIListAdapter.java
//		ON, OFF,
//		
//	}//public static enum MoveMode

	public static enum PrefenceLabels {
		
		CURRENT_PATH,
		
		thumb_actv,
		
		chosen_list_item,
		
	}//public static enum PrefenceLabels

	public static enum ListTags {
		// MainActivity.java
		actv_main_lv,
		
		// BMActv.java
		actv_bm_lv,
		
		// ALActv
		dir_list_thumb_actv,
		
	}//public static enum ListTags


}//public class Tags
