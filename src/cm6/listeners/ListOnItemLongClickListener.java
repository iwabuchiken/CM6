package cm6.listeners;

import java.util.ArrayList;
import java.util.List;

import cm6.items.BM;
import cm6.listeners.dialogues.DialogOnItemClickListener;
import cm6.main.R;
import cm6.utils.Methods;
import cm6.utils.Methods_dlg;
import cm6.utils.Tags;
import android.app.Activity;
import android.app.Dialog;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class
ListOnItemLongClickListener implements OnItemLongClickListener {

	Activity actv;
	
	Vibrator vib;
	
	public ListOnItemLongClickListener(Activity actv) {
		// TODO Auto-generated constructor stub
		this.actv = actv;
		
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
		
	}

	public boolean
	onItemLongClick
	(AdapterView<?> parent, View v, int position, long id) {
		
		Tags.ListTags tag = (Tags.ListTags) parent.getTag();
		
		vib.vibrate(Methods.vibLength_click);
		

		
		switch (tag) {
			
		case actv_bm_lv://----------------------------------------------------

			BM bm = (BM) parent.getItemAtPosition(position);
			
			case_actv_bm_lv(bm);
			
			break;// case actv_bm_lv
			
		default:
			break;
		
		}//switch (tag)
		
		return true;
		
	}//onItemLongClick (AdapterView<?> parent, View v, int position, long id)

	private void case_actv_bm_lv(BM bm) {
		/***************************************
		 * Show dialog
		 ***************************************/
		Dialog dlg = Methods_dlg.dlg_template_cancel(
								actv,
								R.layout.dlg_tmpl_cancel_lv,
								R.string.dlg_bmactv_list_long_click_title,
								
								R.id.dlg_tmpl_cancel_lv_bt_cancel,
								Tags.DialogTags.dlg_generic_dismiss);
		
		/****************************
		 * 2. Prep => List
			****************************/
		String[] choices = {
							actv.getString(R.string.generic_tv_delete),
							actv.getString(R.string.generic_tv_edit)
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
				R.layout.simple_text_view,
//				android.R.layout.simple_list_item_1,
				list
				);

		/****************************
		 * 4. Set adapter
			****************************/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_tmpl_cancel_lv_lv);
		
		lv.setAdapter(adapter);
		
		/****************************
		 * 5. Set listener to list
			****************************/
		lv.setTag(Tags.DialogItemTags.dlg_bmactv_list_long_click);
		
		lv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg, bm));

		/***************************************
		 * Modify the list view height
		 ***************************************/
		lv.setLayoutParams(new LinearLayout.LayoutParams(
				300,	//	Width
				LayoutParams.WRAP_CONTENT	//	Height
				));
		
		/***************************************
		 * Modify: Button layout
		 ***************************************/
		LinearLayout llButton =
				(LinearLayout) dlg.findViewById(R.id.dlg_tmpl_cancel_lv_ll_button_cancel);
		
		LinearLayout.LayoutParams params =
						new LinearLayout.LayoutParams(
										LayoutParams.WRAP_CONTENT,
										LayoutParams.WRAP_CONTENT);
		
		params.gravity = Gravity.CENTER_HORIZONTAL;
		
		llButton.setLayoutParams(params);
		

		/****************************
		 * 6. Show dialog
			****************************/
		dlg.show();
		
		/***************************************
		 * Delete bm from: bmList
		 ***************************************/
		
		
//		// Log
//		Log.d("ListOnItemLongClickListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ ":"
//				+ Thread.currentThread().getStackTrace()[2].getMethodName()
//				+ "]",
//				"Position="
//				+ Methods.convert_intSec2Digits_lessThanHour((int)bm.getPosition()/1000));
//		// Log
//		Log.d("ListOnItemLongClickListener.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ ":"
//				+ Thread.currentThread().getStackTrace()[2].getMethodName()
//				+ "]", "bm.getDbId()=" + bm.getDbId());
		
	}//private void case_actv_bm_lv(BM bm)

}//public class ListOnItemLongClickListener implements OnItemLongClickListener
