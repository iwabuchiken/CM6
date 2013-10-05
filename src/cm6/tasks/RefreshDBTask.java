package cm6.tasks;

import cm6.utils.Methods;
import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class RefreshDBTask extends AsyncTask<String, Integer, String> {

	//
	Activity actv;
	Dialog dlg;
	
	long refreshed_date;
	
	int num_of_new_items;
	
	public RefreshDBTask(Activity actv) {
		
		this.actv = actv;
		
		this.refreshed_date = Methods.getMillSeconds_now();
		
	}//public RefreshDBTask(Activity actv)
	
	public RefreshDBTask(Activity actv, Dialog dlg) {
		// 
		this.actv = actv;
		this.dlg = dlg;
		
		this.refreshed_date = Methods.getMillSeconds_now();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
//		int result = Methods.refreshMainDB((ListActivity) actv);
		int result = Methods.refresh_main_db((ListActivity) actv);
//		boolean result = Methods.refreshMainDB((ListActivity) actv);
//		boolean result = Methods.refreshMainDB_async((ListActivity) actv);
//		boolean result = Methods.refreshMainDB_async((ListActivity) actv, this);
		
		// Log
		Log.d("RefreshDBTask.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "result => " + result);
		
		
		if (result > 0) {
			
			num_of_new_items = result;
			
			return "DB refreshed";
			
		} else if (result == -1){//if (result == true)

			return "�e�[�u�����Ȃ��A�܂��A��邱�Ƃ��ł��܂���ł���";
			
		} else if (result == -2){//if (result == true)
			
			return "�t�@�C��������܂���";

		} else if (result == -3){//if (result == true)
			
			return "No new entries";

		} else {//if (result == true)
			
			return "�Ȃɂ��s���̌��ʂ������܂���";
			
		}//if (result == true)
		
//		return null;
	}//protected String doInBackground(String... params)

	@Override
	protected void onPostExecute(String result) {
		/*********************************
		 * 1. super
		 * 2. Save history
		 * 3. Toast
		 *********************************/
		super.onPostExecute(result);

//		/*********************************
//		 * 2. Save history
//		 *********************************/
//		Methods.save_refresh_history(actv,
//					MainActv.dbName, MainActv.tname_refresh_history,
//					refreshed_date, num_of_new_items);
		
		
		// debug
		Toast.makeText(actv, result, 2000).show();
		
//		dlg.dismiss();
		
	}//protected void onPostExecute(String result)

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
//		super.onProgressUpdate(values);
		
		// Log
		Log.d("RefreshDBTask.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Progress: " + values[0]);
		
		
	}//protected void onProgressUpdate(Integer... values)

}
