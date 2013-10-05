package cm6.tasks;


import java.util.ArrayList;
import java.util.List;

import cm6.items.SearchedItem;
import cm6.main.MainActv;
import cm6.main.SearchActv;
import cm6.main.TNActv;
import cm6.utils.CONS;
import cm6.utils.DBUtils;
import cm6.utils.Methods;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SearchTask extends AsyncTask<String[], Integer, Integer>{

	//
	Activity actv;
	
	//
	String[] search_words;

	//
	static long[] long_searchedItems;
	
//	public static List<SearchedItem> siList;
	
	static String[] string_searchedItems_table_names;
	
	int search_mode;
	
	public SearchTask(Activity actv, String[] search_words) {
		
		this.actv = actv;
		this.search_words = search_words;
		
	}//public SearchTask(Activity actv, String[] search_words)


	public SearchTask(Activity actv) {
		
		this.actv = actv;
		
		// Log
		Log.d("SearchTask.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starts => SearchTask(Activity actv)");
		
	}//public SearchTask(Activity actv)


	public SearchTask(Activity actv, int search_mode) {
		
		this.actv = actv;
		
		this.search_mode = search_mode;
		
		string_searchedItems_table_names = null;

		// Log
		Log.d("SearchTask.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starts => SearchTask(Activity actv, int search_mode)");

	}//public SearchTask(Activity actv2, int search_mode)
	


	@Override
	protected Integer doInBackground(String[]... sw) {
		
//		// Log
//		if (string_searchedItems_table_names != null) {
//			
//			Log.d("SearchTask.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", 
//					"string_searchedItems_table_names.length="
//						+ string_searchedItems_table_names.length);
//			
//		} else {//if (string_searchedItems_table_names != null)
//
//			Log.d("SearchTask.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", 
//					"string_searchedItems_table_names => Null");
//
//		}//if (string_searchedItems_table_names != null)

		/***************************************
		 * Hub
		 ***************************************/
		if(search_mode == 0) {
			
//			// Log
//			Log.d("SearchTask.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Calling => doInBackground_specific_table(sw)");
			
//			// Log
//			Log.d("SearchTask.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ ":"
//					+ Thread.currentThread().getStackTrace()[2].getMethodName()
//					+ "]", "sw[0][0]=" + sw[0][0]);
//
//			// Log
//			Log.d("SearchTask.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ ":"
//					+ Thread.currentThread().getStackTrace()[2].getMethodName()
//					+ "]", "sw[1][0]=" + sw[1][0]);

			return Integer.valueOf(this.doInBackground_specific_table(sw));
//			return this.doInBackground_specific_table(sw);
//			return null;
			
		} else {

			// Log
			Log.d("SearchTask.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Calling => doInBackground_all_table(sw)");

//			return this.doInBackground_all_table(sw);
			return null;
			
		}//if(search_mode == 0)
		
	}//protected String doInBackground(String[]... sw)

	private String doInBackground_all_table(String[][] sw) {
		/*----------------------------
		 * Steps
		 * 1. Get table names list
		 * 1-2. Get => Table names list
		 * 
		 * 2. Construct data			##Add ThumbnailItem to tiLIst
		 * 3. Close db
		 * 4. Set up intent
		 * 5. Return
			----------------------------*/
		
		/*----------------------------
		 * 1. Get table names list
			----------------------------*/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/*********************************
		 * 1-2. Get => Table names list
		 *********************************/
//		List<String> table_names = Methods.get_table_list(actv);
		List<String> table_names = Methods.get_table_list(actv, "IFM9");
		
		/*----------------------------
		 * 2. Construct data
		 * 		1. Table name
		 * 		1-2. Declare => List<Long> searchedItems
		 * 		2. Exec query
		 * 		3. Search
		 * 		4. List<Long> searchedItems => file id
		 * 		
		 * 		5. List<Long> searchedItems => to array
		 * 
		 * 		6. List<String> string_searchedItems_table_names => to array
		 * 
			----------------------------*/
//		String targetTable = sw[1][0];
		
		List<Long> searchedItems = new ArrayList<Long>();
		
		List<String> searchedItems_table_names = new ArrayList<String>();
		
		/*----------------------------
		 * 2.2. Exec query
			----------------------------*/
		for (String targetTable : table_names) {
			
			String sql = "SELECT * FROM " + targetTable;
			
			// Log
			Log.d("SearchTask.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "targetTable: " + targetTable);
			
			
			Cursor c = rdb.rawQuery(sql, null);
			
			actv.startManagingCursor(c);
			
			c.moveToFirst();
	
			
			/*----------------------------
			 * 2.3. Search
				----------------------------*/
			doInBackground_all_table_search(
						c, sw[0], 
						searchedItems, searchedItems_table_names,
						targetTable);
			
		}//for (String targetTable : table_names)
		
		/*********************************
		 * 2.5. List<Long> searchedItems => to array
		 * 2.6. String[] searchedItems_table_names => to array
		 *********************************/
		int len = searchedItems.size();
		
//		long[] long_searchedItems = new long[len];
		long_searchedItems = new long[len];
		
		string_searchedItems_table_names = new String[len];
		
		for (int i = 0; i < len; i++) {
			
			long_searchedItems[i] = searchedItems.get(i);
			
			string_searchedItems_table_names[i] = searchedItems_table_names.get(i);
			
		}//for (int i = 0; i < len; i++)
		
		/*----------------------------
		 * 3. Close db
			----------------------------*/
		rdb.close();
		
		/*----------------------------
		 * 5. Return
			----------------------------*/
		return "Search done";
		
	}//private String doInBackground_all_table(String[][] sw)


	private void doInBackground_all_table_search(
					Cursor c, String[] key_words,
					List<Long> searchedItems,
					List<String> searchedItems_table_names,
					String targetTable) {
		/*********************************
		 * 1. No memo in the item => Next item
		 * 2. If it matches, add to searchedItems and table_names
		 *********************************/
		
		
		for (int i = 0; i < c.getCount(); i++) {
			
			String memo = c.getString(6);
			
			/*********************************
			 * 1. No memo in the item => Next item
			 *********************************/
			if (memo == null) {

				c.moveToNext();
				
				continue;
				
			}//if (memo == null)
			
			for (String string : key_words) {
				
				
				/*********************************
				 * 2. If it matches, add to searchedItems and table_names
				 *********************************/
				if (memo.matches(".*" + string + ".*")) {
					
					// Log
					Log.d("SearchTask.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "memo => " + memo);
					
				
					/*----------------------------
					 * 2.4. List<Long> searchedItems => file id
						----------------------------*/
					searchedItems.add(c.getLong(1));
					
					/*********************************
					 * Table name
					 *********************************/
					searchedItems_table_names.add(targetTable);
					
					break;
					
				}//if (memo.matches(".*" + ))
				
			}//for (String string : sw[0])
			
			c.moveToNext();
			
		}//for (int i = 0; i < c.getCount(); i++)
		
	}//private void doInBackground_all_table_search()

	/***************************************
	 * 20130319_072737
	 * @param sw (1)sw[0] ... Keywords
	 * 			(2)sw[1] ... Table names
	 * @return Message concerning the result
	 ***************************************/
	private int doInBackground_specific_table(String[][] sw) {
		/*----------------------------
		 * Steps
		 * 1. Get table names list
		 * 2. Construct data			##Add ThumbnailItem to tiLIst
		 * 3. Close db
		 * 4. Set up intent
		 * 5. Return
			----------------------------*/
		/*----------------------------
		 * 1. Get table names list
			----------------------------*/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		
		String targetTable = sw[1][0];
		
		List<Long> searchedItems = new ArrayList<Long>();
		

		/***************************************
		 * SearchedItem object<br>
		 * 1. SearchedItem si ... Composite of a table name and a list of ai instances<br>
		 * 		=> Used for: The list in the UI of SearchActv
		 * 		=> Deprecated as of: B25 v-2.2
		 * 2. List<Long> searchedItems ... A list of the ids of the matching ai instances
		 * 		=> Used for:
		 * 		=> Deprecated as of: B25 v-2.2 
		 ***************************************/
		SearchedItem si = new SearchedItem();

//		siList = new ArrayList<SearchedItem>();
		
		si.setTableName(targetTable);
		
		/***************************************
		 * 2.2. Exec query
		 ***************************************/
		String sql = "SELECT * FROM " + targetTable;
		
//		// Log
//		Log.d("SearchTask.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "targetTable: " + targetTable);
		
		
		Cursor c = rdb.rawQuery(sql, null);
		
		while(c.moveToNext()) {
			
			String title = c.getString(c.getColumnIndex("title"));

			if (title == null) continue;
			
			for (String string : sw[0]) {
				
				
				
				if (title.matches(".*" + string + ".*")) {
					
					// Log
					Log.d("SearchTask.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "title=" + title);
					
					/*----------------------------
					 * 2.4. List<Long> searchedItems => file id
						----------------------------*/
//					searchedItems.add(c.getLong(1));
					searchedItems.add(c.getLong(0));
					
					si.setId(c.getLong(0));
					
					break;
					
				}//if (memo.matches(".*" + ))
				
			}//for (String string : sw[0])
				
		}//while(c.moveToNext())
		
		/***************************************
		 * 2.5. List<Long> searchedItems => to array
		 ***************************************/
		int len = searchedItems.size();
		
//		long[] long_searchedItems = new long[len];
		long_searchedItems = new long[len];
		
		for (int i = 0; i < len; i++) {
			
			long_searchedItems[i] = searchedItems.get(i);
			
		}//for (int i = 0; i < len; i++)
		
		// Log
		Log.d("SearchTask.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]",
				"long_searchedItems.length=" + long_searchedItems.length);

		/***************************************
		 * Validate: Found any?
		 ***************************************/
		if (si.getIds() == null) {
			
			// Log
			Log.d("SearchTask.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "si.getIds() == null");
			
			return CONS.Search.SEARCH_FAILED;
			
		}//if (si.getIds() == null)
		
		if (si.getIds().size() < 1) {
			
			// Log
			Log.d("SearchTask.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "si.getIds().size() < 1");
			
			return CONS.Search.SEARCH_NOT_FOUND;
			
		}//if (si.getIds() == null)
		
		
		/***************************************
		 * Add si object to si list
		 ***************************************/
		
		
		
//		siList.add(si);
		CONS.Search.siList.add(si);
		
		// Log
		Log.d("SearchTask.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "si.getTableName()=" + si.getTableName());
		
		// Log
		Log.d("SearchTask.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "si.getIds().size()=" + si.getIds().size());
		
		/*----------------------------
		 * 3. Close db
			----------------------------*/
		rdb.close();
		
		/***************************************
		 * 4. Set up intent
		 ***************************************/
//		// Log
//		Log.d("SearchTask.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "long_searchedItems.length => " + long_searchedItems.length);
//		
//		Log.d("SearchTask.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "long_searchedItems[0] => " + long_searchedItems[0]);
		
		Intent i = new Intent();
		
		i.setClass(actv, SearchActv.class);
		
		i.putExtra("long_searchedItems", long_searchedItems);
		
		actv.startActivity(i);
		
		/*----------------------------
		 * 5. Return
			----------------------------*/
//		return "Search done";
		return CONS.Search.SEARCH_SUCCESSFUL;
		
	}//private String doInBackground_specific_table(String[][] sw)
	


	@Override
	protected void onPostExecute(Integer result) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onPostExecute(result);

		// debug
//		Toast.makeText(actv, result, 2000).show();
		// Log
		Log.d("SearchTask.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "] ", "result=" + result);

		/***************************************
		 * Message
		 ***************************************/
		if (result.intValue() == CONS.Search.SEARCH_SUCCESSFUL) {
		
			// debug
			Toast.makeText(actv, "Search => Successful", Toast.LENGTH_LONG).show();
			
		} else if (result.intValue() == CONS.Search.SEARCH_NOT_FOUND) {//if (result.intValue() == CONS.Search.SEARCH_SUCCESSFUL)
			
			// debug
//			Toast.makeText(actv, "Search => Failed", Toast.LENGTH_LONG).show();
			Toast.makeText(actv, "Search => Not found", Toast.LENGTH_LONG).show();

		} else if (result.intValue() == CONS.Search.SEARCH_FAILED) {//if (result.intValue() == CONS.Search.SEARCH_SUCCESSFUL)

			// debug
			Toast.makeText(actv, "Search => Failed", Toast.LENGTH_LONG).show();

		}//if (result.intValue() == CONS.Search.SEARCH_SUCCESSFUL)
		

//		/*----------------------------
//		 * 1. Set up intent
//			----------------------------*/
//		// Log
//		Log.d("SearchTask.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "long_searchedItems.length => " + long_searchedItems.length);
//		
//		if(long_searchedItems.length > 0) {
//			
//			Log.d("SearchTask.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "long_searchedItems[0] => " + long_searchedItems[0]);
//			
//			Intent i = new Intent();
//			
//			i.setClass(actv, TNActv.class);
//			
//			i.putExtra("long_searchedItems", long_searchedItems);
//			
//			if (string_searchedItems_table_names != null &&
//					string_searchedItems_table_names.length > 0) {	
//				
//				i.putExtra(
////						"string_searchedItems_table_names",
//						CONS.intent_label_searchedItems_table_names,
//						string_searchedItems_table_names);
//				
//			}//if (variable == condition)
////				i.putExtra("string_searchedItems_table_names", string_searchedItems_table_names);
//
//			// Log
//			if (string_searchedItems_table_names != null) {
//				
//				Log.d("SearchTask.java" + "["
//						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//						+ "]", 
//						"string_searchedItems_table_names.length="
//							+ string_searchedItems_table_names.length);
//				
//			} else {//if (string_searchedItems_table_names != null)
//
//				Log.d("SearchTask.java" + "["
//						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//						+ "]", 
//						"string_searchedItems_table_names => Null");
//
//			}//if (string_searchedItems_table_names != null)
//			
////			Log.d("SearchTask.java" + "["
////					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////					+ "]", 
////					"string_searchedItems_table_names.length="
////						+ string_searchedItems_table_names.length);
//			
//			/*----------------------------
//			 * 2. Start activity
//				----------------------------*/
//			actv.startActivity(i);
//			
//		} else {
//			
//			// debug
//			Toast.makeText(actv, "������܂���ł���", 2000).show();
//		}

	}//protected void onPostExecute(String result)
	
	
}//public class SearchTask
