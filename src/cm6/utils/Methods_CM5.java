package cm6.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import cm5.items.AI;
import cm5.items.BM;
import cm5.items.HI;
import cm5.main.ALActv;
import cm5.tasks.Task_UpdateFileLength;
import cm5.utils.CONS.SORT_ORDER;

public class Methods_CM5 {

	public static void updateFileLength(Activity actv) {
		
		Task_UpdateFileLength task = new Task_UpdateFileLength(actv);
		
		task.execute("Start");
		
	}//public static void updateFileLength(Activity actv)

	
	public static
	List<BM> getBMList_FromCursor(Activity actv, Cursor c) {
		
		List<BM> bmList = new ArrayList<BM>();
		
		while(c.moveToNext()) {
			
			BM bm = new BM.Builder()
				.setDbId(c.getLong(c.getColumnIndex(android.provider.BaseColumns._ID)))
				.setPosition(c.getLong(c.getColumnIndex("position")))
				.setTitle(c.getString(c.getColumnIndex("title")))
				.setMemo(c.getString(c.getColumnIndex("memo")))
				.setAiId(c.getLong(c.getColumnIndex("ai_id")))
				.setAiTableName(c.getString(c.getColumnIndex("aiTableName")))
				.build();
					
			if (bm != null) {
				
				bmList.add(bm);
				
			} else {//if (bm != null)
				
				// Log
				Log.d("Methods_CM5.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber()
						+ ":"
						+ Thread.currentThread().getStackTrace()[2]
								.getMethodName() + "]", "bm != null");
				
				continue;
				
			}//if (bm != null)
			
		}//while(c.moveToNext())
		
		return bmList;
		
	}//List<BM> getBMList_FromCursor(Activity actv, Cursor c)

	/***************************************
	 * @return null ... Table doesn't exist and can't create a table<br>
	 * 
	 ***************************************/
	public static List<HI> getAllData_HI(Activity actv) {
		// TODO Auto-generated method stub
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
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/****************************
		 * 0. Table exists?
			****************************/
		boolean res = dbu.tableExists(wdb, CONS.History.tname_history);
		
		if (res == false) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]",
					"getAllData() => Table doesn't exist: "
					+ CONS.History.tname_history);
			
			res = dbu.createTable(
							CONS.History.tname_history,
							CONS.History.cols_history,
							CONS.History.col_types_history);
			if (res == true) {
				
				// Log
				Log.d("Methods_CM5.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber()
						+ ":"
						+ Thread.currentThread().getStackTrace()[2]
								.getMethodName() + "]",
						"Table created: " + CONS.History.tname_history);
				
				wdb.close();
				
//				return null;
				
			} else {//if (res == true)
				
				// Log
				Log.d("Methods_CM5.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber()
						+ ":"
						+ Thread.currentThread().getStackTrace()[2]
								.getMethodName() + "]",
						"Create table => Failed: " + CONS.History.tname_history);

				wdb.close();
				
				return null;
				
			}//if (res == true)
			
		} else {//if (res == false)
			
			// Log
			Log.d("Methods_CM5.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Table exists: " + CONS.History.tname_history);
			
			wdb.close();
			
		}//if (res == false)
		
		/****************************
		 * 2. Get data
		 * 		2.1. Get cursor
		 * 		2.2. Add to list
			****************************/
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		String sql = "SELECT * FROM " + CONS.History.tname_history;
		
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

		/****************************
		 * 2.2. Add to list
			****************************/
		c.moveToFirst();
		
		List<HI> hiList = new ArrayList<HI>();
		
		for (int i = 0; i < c.getCount(); i++) {
			
//			"aiId", 	"aiTableName"
			HI hi = new HI.Builder()
//					.setDbId(0)
					.setDbId(c.getLong(0))
					.setCreatedAt(c.getLong(1))
					.setModifiedAt(c.getLong(2))
					.setAiId(c.getLong(c.getColumnIndex("aiId")))
					.setAiTableName(c.getString(c.getColumnIndex("aiTableName")))
					.build();
			
			// Add to the list
			hiList.add(hi);
			
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
		
		return hiList;
		
	}//public static List<HI> getAllData_HI(Activity actv)


	public static void
	sortList_HI_createdAt(List<HI> hiList, final SORT_ORDER sortOrder) {
		// TODO Auto-generated method stub
		Collections.sort(hiList, new Comparator<HI>(){

//			@Override
			public int compare(HI h1, HI h2) {
				
				int res;
				
				switch (sortOrder) {
				
				case CREATED_AT://-----------------------------
					
//					res = (int) (h1.getCreated_at() - h2.getCreated_at());
					res = (int) (h1.getCreatedAt() - h2.getCreatedAt());
					
					break;// case CREATED_AT
					
				default:
					
					res = 1;
					
					break;
					
				}
				
				return res;
				
//				return (int) (h1.getCreated_at() - h2.getCreated_at());
			}
			
		});//Collections.sort()

	}//sortList_HI_createdAt(List<HI> hiList, SORT_ORDER sortOrder)


	
	public static int saveHistory(Activity actv, AI ai) {
		// TODO Auto-generated method stub
		/***************************************
		 * Build a HI instance
		 ***************************************/
		/*********************************
		 * 1. Build data
		 * 2. Set up db
		 * 
		 * 2-2. Table exists?
		 * 
		 * 3. Insert data
		 * 4. Close db
		 *********************************/
		HI hi = new HI.Builder()
					.setAiId(ai.getDb_id())
					.setAiTableName(ai.getTable_name())
					.build();
		
		/*********************************
		 * 2. Set up db
		 *********************************/
		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*********************************
		 * 2-2. Table exists?
		 *********************************/
		boolean result = dbu.tableExists(wdb, CONS.History.tname_history);
		
		if (result == false) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + CONS.History.tname_history);
			
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
								.getLineNumber() + "]", "Table created: " + CONS.History.tname_history);
				
			} else {//if (result == true)
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create table failed: " + CONS.History.tname_history);
				
				// debug
				Toast.makeText(actv, 
						"Create table failed: " + CONS.History.tname_history,
						Toast.LENGTH_SHORT).show();

				wdb.close();
				
				return CONS.History.SAVE_HISTORY_CREATE_TABLE_FAILED;
				
			}//if (result == true)
			
		}//if (result == false)
		
		/*********************************
		 * 3. Insert data
		 *********************************/
		boolean res = DBUtils.insertData_history(actv, wdb, hi);
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "res=" + res);
		
		if (res == true) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "History saved: AI id=" + ai.getDb_id());
			
			wdb.close();
			
			return CONS.History.SAVE_HISTORY_SUCCESSFUL;
			
		} else {//if (res == true)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Save history => Failed: AI id=" + ai.getDb_id());

			wdb.close();
			
			return CONS.History.SAVE_HISTORY_FAILED;

		}//if (res == true)
		
//		/*********************************
//		 * 4. Close db
//		 *********************************/
//		wdb.close();
		
	}//public static void saveHistory(Activity actv, AI ai)


	
	public static List<HI>
	getHIList_FromCursor(Activity actv, Cursor c) {
		List<HI> hiList = new ArrayList<HI>();
		
		while(c.moveToNext()) {
			
			HI hi = new HI.Builder()
		//			.setDbId(0)
					.setDbId(c.getLong(0))
					.setCreatedAt(c.getLong(1))
					.setModifiedAt(c.getLong(2))
					.setAiId(c.getLong(c.getColumnIndex("aiId")))
					.setAiTableName(c.getString(c.getColumnIndex("aiTableName")))
					.build();
					
			if (hi != null) {
				
				hiList.add(hi);
				
			} else {//if (hi != null)
				
				// Log
				Log.d("Methods_CM5.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber()
						+ ":"
						+ Thread.currentThread().getStackTrace()[2]
								.getMethodName() + "]", "hi != null");
				
				continue;
				
			}//if (hi != null)
			
		}//while(c.moveToNext())
		
		return hiList;
		
	}//getHIList_FromCursor(Activity actv, Cursor c)
	
}//public class Methods_CM5
