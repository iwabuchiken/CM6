package cm6.utils;



import java.util.ArrayList;
import java.util.List;

import cm6.items.AI;
import cm6.items.BM;
import cm6.items.HI;
import cm6.items.TI;
import cm6.main.MainActv;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
//import android.view
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/****************************************
 * Copy & pasted from => C:\WORKS\WORKSPACES_ANDROID\ShoppingList\src\shoppinglist\main\DBUtils.java
 ****************************************/
public class DBUtils extends SQLiteOpenHelper{

	/*****************************************************************
	 * Class fields
	 *****************************************************************/
	 // DB name
	static String dbName = null;
	
	// Activity
	Activity activity;
	
	//
	Context context;

	/*********************************
	 * DB
	 *********************************/
	// Database
	SQLiteDatabase db = null;

	//
//	String[] cols_with_index = 
//				{android.provider.BaseColumns._ID, 
//					"file_id", 		"file_path", "file_name", "date_added",
//					"date_modified", "memos", "tags"};
//	
//	String[] col_types_with_index =
//				{	"INTEGER", "TEXT", 	"TEXT",		"INTEGER",
//					"INTEGER",		"TEXT",	"TEXT"};
//
//	// Main data
//	public static String[] cols = 
//		{"file_id", "file_path", "file_name", 	"date_added",
//		"date_modified",	"memos", "tags", 	"last_viewed_at"};
////	"date_modified", "memos", "tags"};
//
//	public static String[] col_types =
//		{"INTEGER", "TEXT", 	"TEXT",			"INTEGER",
//		"INTEGER",			"TEXT",	"TEXT",		"INTEGER"};
//
//	static String[] cols_for_insert_data = 
//		{"file_id", 		"file_path", "file_name", "date_added", "date_modified"};
//
//	// Proj
//	static String[] proj = {
//		MediaStore.Images.Media._ID, 
//		MediaStore.Images.Media.DATA,
//		MediaStore.Images.Media.DISPLAY_NAME,
//		MediaStore.Images.Media.DATE_ADDED,
//		MediaStore.Images.Media.DATE_MODIFIED,
//		};
//
//	static String[] proj_for_get_data = {
//		MediaStore.Images.Media._ID, 
//		MediaStore.Images.Media.DATA,
//		MediaStore.Images.Media.DISPLAY_NAME,
//		MediaStore.Images.Media.DATE_ADDED,
//		MediaStore.Images.Media.DATE_MODIFIED,
//		"memos",
//		"tags"
//		};
//
//	static String[] cols_refresh_log = {
//		"last_refreshed", "num_of_items_added"
//	};
//	
//	static String[] col_types_refresh_log = {
//		"INTEGER", 			"INTEGER"
//	};
//
//	static String[] cols_memo_patterns = {"word", "table_name"};
//	static String[] col_types_memo_patterns = {"TEXT", "TEXT"};
//	
//	static String table_name_memo_patterns = "memo_patterns";
	
	/*****************************************************************
	 * Constructor
	 *****************************************************************/
	public DBUtils(Context context, String dbName) {
		super(context, dbName, null, 1);
		
		// Initialize activity
		this.activity = (Activity) context;
		
		this.context = context;
		
		this.dbName = dbName;
		
	}//public DBUtils(Context context)

//	public DBUtils() {
//		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
//	}

	/*******************************************************
	 * Methods
	 *******************************************************/
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}//public void onCreate(SQLiteDatabase db)

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}

	/****************************************
	 * createTable_generic()
	 * 
	 * <Caller> 
	 * 1. 
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public boolean createTable(
					SQLiteDatabase db, String tableName, String[] columns, String[] types) {
		/*----------------------------
		 * Steps
		 * 1. Table exists?
		 * 2. Build sql
		 * 3. Exec sql
			----------------------------*/
		
		//
//		if (!tableExists(db, tableName)) {
		if (tableExists(db, tableName)) {
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists => " + tableName);
			
			return false;
		}//if (!tableExists(SQLiteDatabase db, String tableName))
		
		/*----------------------------
		 * 2. Build sql
			----------------------------*/
		//
		StringBuilder sb = new StringBuilder();
		
		sb.append("CREATE TABLE " + tableName + " (");
		sb.append(android.provider.BaseColumns._ID +
							" INTEGER PRIMARY KEY AUTOINCREMENT, ");
		
		// created_at, modified_at
		sb.append("created_at INTEGER, modified_at INTEGER, ");
		
		int i = 0;
		for (i = 0; i < columns.length - 1; i++) {
			sb.append(columns[i] + " " + types[i] + ", ");
		}//for (int i = 0; i < columns.length - 1; i++)
		
		sb.append(columns[i] + " " + types[i]);
		
		sb.append(");");
		
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "sql => " + sb.toString());
		
		/*----------------------------
		 * 3. Exec sql
			----------------------------*/
		//
		try {
//			db.execSQL(sql);
			db.execSQL(sb.toString());
			
			// Log
			Log.d(this.getClass().getName() + 
					"["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table created => " + tableName);
			
			
			return true;
		} catch (SQLException e) {
			// Log
			Log.e(this.getClass().getName() + 
					"[" + Thread.currentThread().getStackTrace()[2].getLineNumber() + "]", 
					"Exception => " + e.toString());
			
			return false;
		}//try
		
	}//public boolean createTable(SQLiteDatabase db, String tableName)

	public boolean
	createTable(String tableName, String[] columns, String[] types) {
		/*----------------------------
		 * Steps
		 * 1. Table exists?
		 * 2. Build sql
		 * 3. Exec sql
			----------------------------*/
		SQLiteDatabase wdb = this.getWritableDatabase();
		
		//
		//if (!tableExists(db, tableName)) {
		if (tableExists(wdb, tableName)) {
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists => " + tableName);
			
			return false;
		}//if (!tableExists(SQLiteDatabase db, String tableName))
		
		/*----------------------------
		 * 2. Build sql
			----------------------------*/
		//
		StringBuilder sb = new StringBuilder();
		
		sb.append("CREATE TABLE " + tableName + " (");
		sb.append(android.provider.BaseColumns._ID +
							" INTEGER PRIMARY KEY AUTOINCREMENT, ");
		
		// created_at, modified_at
		sb.append("created_at INTEGER, modified_at INTEGER, ");
		
		int i = 0;
		for (i = 0; i < columns.length - 1; i++) {
			sb.append(columns[i] + " " + types[i] + ", ");
		}//for (int i = 0; i < columns.length - 1; i++)
		
		sb.append(columns[i] + " " + types[i]);
		
		sb.append(");");
		
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "sql => " + sb.toString());
		
		/*----------------------------
		 * 3. Exec sql
			----------------------------*/
		//
		try {
		//	db.execSQL(sql);
			wdb.execSQL(sb.toString());
			
			// Log
			Log.d(this.getClass().getName() + 
					"["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table created => " + tableName);
			
			wdb.close();
			
			return true;
			
		} catch (SQLException e) {
			
			// Log
			Log.e(this.getClass().getName() + 
					"[" + Thread.currentThread().getStackTrace()[2].getLineNumber() + "]", 
					"Exception => " + e.toString());
			
			wdb.close();
			
			return false;
			
		}//try

	}//public boolean createTable(SQLiteDatabase db, String tableName)

	public boolean tableExists(SQLiteDatabase db, String tableName) {
		// The table exists?
		Cursor cursor = db.rawQuery(
									"SELECT * FROM sqlite_master WHERE tbl_name = '" + 
									tableName + "'", null);
		
		((Activity) context).startManagingCursor(cursor);
//		actv.startManagingCursor(cursor);
		
		// Judge
		if (cursor.getCount() > 0) {
			return true;
		} else {//if (cursor.getCount() > 0)
			return false;
		}//if (cursor.getCount() > 0)
	}//public boolean tableExists(String tableName)

	public String[] get_cols_with_index() {
		return CONS.cols_with_index;
	}
	
	public String[] get_col_types_with_index() {
		return CONS.col_types_with_index;
	}

	public String[] get_cols() {
		return CONS.cols;
	}
	
	public String[] get_col_types() {
		return CONS.col_types;
	}

	public boolean dropTable(Activity actv, SQLiteDatabase db, String tableName) {
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starting: dropTable()");
		
		/*------------------------------
		 * The table exists?
		 *------------------------------*/
		// The table exists?
		boolean tempBool = tableExists(db, tableName);
		
		if (tempBool == true) {
		
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + tableName);

		} else {//if (tempBool == true)
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tableName);

			return false;
		}//if (tempBool == true)

		/*------------------------------
		 * Drop the table
		 *------------------------------*/
		// Define the sql
        String sql 
             = "DROP TABLE " + tableName;
        
        // Execute
        try {
			db.execSQL(sql);
			
			// Vacuum
			db.execSQL("VACUUM");
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "The table dropped => " + tableName);
			
			// Return
			return true;
			
		} catch (SQLException e) {
			// TODO �����������ꂽ catch �u���b�N
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "DROP TABLE => failed (table=" + tableName + "): " + e.toString());
			
			// debug
			Toast.makeText(actv, 
						"DROP TABLE => failed(table=" + tableName, 
						3000).show();
			
			// Return
			return false;
		}//try

	}//public boolean dropTable(String tableName) 

	public boolean drop_table(Activity actv, SQLiteDatabase db, String tableName) {
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starting: dropTable()");
		
		/*------------------------------
		 * The table exists?
		 *------------------------------*/
		// The table exists?
		boolean tempBool = tableExists(db, tableName);
		
		if (tempBool == true) {
		
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + tableName);

		} else {//if (tempBool == true)
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tableName);

			return false;
		}//if (tempBool == true)

		/*------------------------------
		 * Drop the table
		 *------------------------------*/
		// Define the sql
        String sql 
             = "DROP TABLE " + tableName;
        
        // Execute
        try {
			db.execSQL(sql);
			
			// Vacuum
			db.execSQL("VACUUM");
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "The table dropped => " + tableName);
			
			// Return
			return true;
			
		} catch (SQLException e) {
			// TODO �����������ꂽ catch �u���b�N
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "DROP TABLE => failed (table=" + tableName + "): " + e.toString());
			
			// debug
			Toast.makeText(actv, 
						"DROP TABLE => failed(table=" + tableName, 
						3000).show();
			
			// Return
			return false;
		}//try

	}//public boolean dropTable(String tableName) 

	public boolean insertData(SQLiteDatabase db, String tableName, 
												String[] columnNames, String[] values) {
		
////		String sql = "SELECT * FROM TABLE " + DBUtils.table_name_memo_patterns;
//		String sql = "SELECT * FROM " + DBUtils.table_name_memo_patterns;
//		
//		Cursor c = db.rawQuery(sql, null);
//		
//		
//		
//		// Log
//		Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount() => " + c.getCount() + " / " +
//				"c.getColumnCount() => " + c.getColumnCount());
//		
//		c.close();
		
		
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		try {
			// Start transaction
			db.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
			// Put values
			for (int i = 0; i < columnNames.length; i++) {
				val.put(columnNames[i], values[i]);
			}//for (int i = 0; i < columnNames.length; i++)
			
			// Insert data
			db.insert(tableName, null, val);
			
			// Set as successful
			db.setTransactionSuccessful();
			
			// End transaction
			db.endTransaction();
			
			// Log
//			Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Data inserted => " + "(" + columnNames[0] + " => " + values[0] + 
//				" / " + columnNames[3] + " => " + values[3] + ")");
			
			return true;
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
		
//		//debug
//		return false;
		
	}//public insertData(String tableName, String[] columnNames, String[] values)

	public boolean dropTable(Activity actv, String tableName) {
		/***************************************
		 * Setup: DB
		 ***************************************/
		SQLiteDatabase wdb = this.getWritableDatabase();
		
		/*------------------------------
		 * The table exists?
		 *------------------------------*/
		// The table exists?
		boolean tempBool = tableExists(wdb, tableName);
		
		if (tempBool == true) {
		
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + tableName);

		} else {//if (tempBool == true)
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tableName);

			return false;
		}//if (tempBool == true)

		/*------------------------------
		 * Drop the table
		 *------------------------------*/
		// Define the sql
        String sql 
             = "DROP TABLE " + tableName;
        
        // Execute
        try {
			wdb.execSQL(sql);
			
			// Vacuum
			wdb.execSQL("VACUUM");
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "The table dropped => " + tableName);
			
			wdb.close();
			
			// Return
			return true;
			
		} catch (SQLException e) {
			// TODO �����������ꂽ catch �u���b�N
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "DROP TABLE => failed (table=" + tableName + "): " + e.toString());
			
			// debug
			Toast.makeText(actv, 
						"DROP TABLE => failed(table=" + tableName, 
						Toast.LENGTH_LONG).show();
			
			wdb.close();
			
			// Return
			return false;
		}//try

	}//public boolean dropTable(String tableName) 

	public boolean insertData(SQLiteDatabase db, String tableName, 
											String[] columnNames, long[] values) {
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		try {
			// Start transaction
			db.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
			// Put values
			for (int i = 0; i < columnNames.length; i++) {
				val.put(columnNames[i], values[i]);
			}//for (int i = 0; i < columnNames.length; i++)
			
			// Insert data
			db.insert(tableName, null, val);
			
			// Set as successful
			db.setTransactionSuccessful();
			
			// End transaction
			db.endTransaction();
			
			// Log
			Log.d("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Data inserted => " + "(" + columnNames[0] + " => " + values[0] + "), and others");
			
			return true;
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
	}//public insertData(String tableName, String[] columnNames, String[] values)

	public boolean insertData(SQLiteDatabase db, String tableName, TI ti) {
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		try {
			// Start transaction
			db.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
//			{"file_id", 		"file_path", "file_name", "date_added",
//				"date_modified", "memos", "tags"};
			
//			// Put values
//			for (int i = 0; i < columnNames.length; i++) {
//				val.put(columnNames[i], values[i]);
//			}//for (int i = 0; i < columnNames.length; i++)

			val.put("file_id", ti.getFileId());
			
			val.put("file_path", ti.getFile_path());
			val.put("file_name", ti.getFile_name());
			
			val.put("date_added", ti.getDate_added());
			val.put("date_modified", ti.getDate_modified());
			
			val.put("memos", ti.getMemo());
			val.put("tags", ti.getTags());
			
			// Insert data
			db.insert(tableName, null, val);
			
			// Set as successful
			db.setTransactionSuccessful();
			
			// End transaction
			db.endTransaction();
			
			// Log
			Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Data inserted => " + "(file_name => " + val.getAsString("file_name") + "), and others");
			
			return true;
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
	}//public insertData(SQLiteDatabase db, String tableName, ThumbnailItem ti)

	public boolean insertData_ai(SQLiteDatabase db, String tableName, AI ai) {
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		try {
			// Start transaction
			db.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
//			{"file_id", 		"file_path", "file_name", "date_added",
//				"date_modified", "memos", "tags"};
			
//			// Put values
//			for (int i = 0; i < columnNames.length; i++) {
//				val.put(columnNames[i], values[i]);
//			}//for (int i = 0; i < columnNames.length; i++)

			val.put(android.provider.BaseColumns._ID, ai.getDb_id());
			
			val.put("created_at", ai.getCreated_at());
			val.put("modified_at", ai.getModified_at());
			
			val.put("file_path", ai.getFile_path());
			val.put("file_name", ai.getFile_name());
			
			val.put("title", ai.getTitle());
			val.put("memo", ai.getMemo());
			
			val.put("last_played_at", ai.getLast_played_at());
			val.put("table_name", ai.getTable_name());
			
			val.put("length", ai.getLength());
			
			// Insert data
			db.insert(tableName, null, val);
			
			// Set as successful
			db.setTransactionSuccessful();
			
			// End transaction
			db.endTransaction();
			
			// Log
			Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Data inserted => " + "(file_name => " + val.getAsString("file_name") + "), and others");
			
			return true;
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
	}//public boolean insertData_ai(SQLiteDatabase db, String tableName, AI ai)

	
	public static boolean insertData_history(
							Activity actv, 
							SQLiteDatabase wdb, 
							HI hi) {
		/*********************************
		 * memo
		 *********************************/
		
		
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		try {
			// Start transaction
			wdb.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();

			val.put("created_at", Methods.getMillSeconds_now());
			val.put("modified_at", Methods.getMillSeconds_now());

//			cols_history = {"aiId", 	"aiTableName"}]
			
			val.put("aiId", hi.getAiId());
			
			val.put("aiTableName", hi.getAiTableName());

			// Insert data
//			wdb.insert(MainActv.tableName_show_history, null, val);
//			wdb.insert(CONS.tname_show_history, null, val);
			long res = wdb.insert(CONS.History.tname_history, null, val);
			
			if (res > 0) {
				
				// Set as successful
				wdb.setTransactionSuccessful();
				
				// End transaction
				wdb.endTransaction();
				
				// Log
				Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Data inserted => " + "(file_name => " + val.getAsString("file_name") + "), and others");

				wdb.close();
				
				return true;
				
			} else {//if (res > 0)
				
				// Log
				Log.d("DBUtils.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber()
						+ ":"
						+ Thread.currentThread().getStackTrace()[2]
								.getMethodName() + "]",
						"Data insertion => Failed: AI id=" + hi.getAiId());
				
				wdb.close();
				
				return false;
				
			}//if (res > 0)
			
			
//			// Set as successful
//			wdb.setTransactionSuccessful();
//			
//			// End transaction
//			wdb.endTransaction();
//			
//			// Log
//			Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Data inserted => " + "(file_name => " + val.getAsString("file_name") + "), and others");
			
//			return true;
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception! => " + e.toString());
			
			wdb.close();
			
			return false;
		}//try
	}//public static boolean insertData_history(Activity actv, SQLiteDatabase wdb, Object[] data)

	
	public TI getData(Activity actv, SQLiteDatabase rdb, String tableName, long file_id) {
		/*----------------------------
		 * Steps
		 * 1. 
			----------------------------*/
		String sql = "SELECT * FROM " + tableName + " WHERE file_id = '" + String.valueOf(file_id) + "'";
		
		Cursor c = rdb.rawQuery(sql, null);
		
		actv.startManagingCursor(c);
		
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "c.getCount() => " + c.getCount());
		
		
		c.moveToFirst();
		
//		ThumbnailItem ti = new ThumbnailItem(
		return new TI(
				c.getLong(1),	// file_id
				c.getString(2),	// file_path
				c.getString(3),	// file_name
				c.getLong(4),	// date_added
				c.getLong(5),		// date_modified
				c.getString(6),		// memos
				c.getString(7)		// tags
		);
		
		
//		ThumbnailItem ti = new ThumbnailItem(
//							c.getLong(1),
//							c.getString(2),
//							c.getString(3),
//							c.getLong(4),
//							c.getLong(4),
//							);

		
		
	}//public void getData(SQLiteDatabase rdb, String tableName, long file_id)

	public boolean updateData_memos(Activity actv, SQLiteDatabase wdb, 
								String tableName, TI ti) {
		/*----------------------------
		 * Steps
		 * 1. 
			----------------------------*/
		String sql = "UPDATE " + tableName + " SET " + 
						"file_id='" + String.valueOf(ti.getFileId()) + "', " + 
						"file_path='" + ti.getFile_path() + "', " +
						"file_name='" + ti.getFile_name() + "', " +
						"date_added='" + String.valueOf(ti.getDate_added()) + "', " +
						"date_modified='" + String.valueOf(ti.getDate_modified()) + "', " +
						"memos='" + ti.getMemo() + "', " +
						"tags='" + ti.getTags() + "'" +
						
						" WHERE file_id = '" + String.valueOf(ti.getFileId()) + "'";
		
						
//								"file_id", 		"file_path", "file_name", "date_added", "date_modified"
//		static String[] cols = 
//			{"file_id", 		"file_path", "file_name", "date_added",
//				"date_modified", "memos", "tags"};

		
		try {
			
			wdb.execSQL(sql);
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "sql => Done: " + sql);
			
//			Methods.toastAndLog(actv, "Data updated", 2000);
			
			return true;
			
			
		} catch (SQLException e) {
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
			
			return false;
		}
//		
//		actv.startManagingCursor(c);
//		
//		// Log
//		Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount() => " + c.getCount());
//		
//		
//		c.moveToFirst();
		
		
		
	}//public void updateData_memos

	public static boolean updateData_TI_last_viewed_at(Activity actv, SQLiteDatabase wdb, 
			String tableName, TI ti) {
		/*----------------------------
		* Steps
		* 1. 
		----------------------------*/
		String sql = "UPDATE " + tableName + " SET " + 
//			"file_id='" + String.valueOf(ti.getFileId()) + "', " + 
//			"last_viewed_at='" + Methods.getMillSeconds_now() + "', " +
			"last_viewed_at='" + Methods.getMillSeconds_now() + "' " +
			
			" WHERE file_id = '" + String.valueOf(ti.getFileId()) + "'";
		
			
		//			"file_id", 		"file_path", "file_name", "date_added", "date_modified"
		//static String[] cols = 
		//{"file_id", 		"file_path", "file_name", "date_added",
		//"date_modified", "memos", "tags"};
		
		
		try {
		
			wdb.execSQL(sql);
			
			// Log
			Log.d("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "sql => Done: " + sql);
			
			//Methods.toastAndLog(actv, "Data updated", 2000);
			
			return true;
			
			
		} catch (SQLException e) {
			// Log
			Log.e("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
			
			return false;
		}
		//
		//actv.startManagingCursor(c);
		//
		//// Log
		//Log.d("DBUtils.java" + "["
		//+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		//+ "]", "c.getCount() => " + c.getCount());
		//
		//
		//c.moveToFirst();
		
		
	
	}//public void updateData_memos

	public boolean deleteData(Activity actv, SQLiteDatabase db, String tableName, long file_id) {
		/*----------------------------
		 * Steps
		 * 1. Item exists in db?
		 * 2. If yes, delete it
			----------------------------*/
		/*----------------------------
		 * 1. Item exists in db?
			----------------------------*/
		boolean result = isInDB_long(db, tableName, file_id);
		
		if (result == false) {		// Result is false ==> Meaning the target data doesn't exist
											//							in db; Hence, not executing delete op
			
			// debug
			Toast.makeText(actv, 
					"Data doesn't exist in db: " + String.valueOf(file_id), 
					2000).show();
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"Data doesn't exist in db => Delete the data (file_id = " + String.valueOf(file_id) + ")");
			
			return false;
			
		} else {//if (result == false)
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"Data exists in db" + String.valueOf(file_id) + ")");
			
		}//if (result == false)
		
		
		String sql = 
						"DELETE FROM " + tableName + 
						" WHERE file_id = '" + String.valueOf(file_id) + "'";
		
		try {
			db.execSQL(sql);
			
//			// Log
//			Log.d("DBUtils.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Data deleted => file id = " + String.valueOf(file_id));
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Sql executed: " + sql);
			
			return true;
			
		} catch (SQLException e) {
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			return false;
			
		}//try
		
	}//public boolean deleteData(SQLiteDatabase db, String tableName, long file_id)

	public boolean deleteData_ai(Activity actv,
						SQLiteDatabase db, String tableName, long db_id) {
		/*----------------------------
		 * Steps
		 * 1. Item exists in db?
		 * 2. If yes, delete it
			----------------------------*/
		/*----------------------------
		 * 1. Item exists in db?
			----------------------------*/
		boolean result = DBUtils.isInDB_long_ai(db, tableName, db_id);
		
		if (result == false) {		// Result is false ==> Meaning the target data doesn't exist
											//							in db; Hence, not executing delete op
			
			// debug
			Toast.makeText(actv, 
					"Data doesn't exist in db: " + String.valueOf(db_id), 
					2000).show();
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"Data doesn't exist in db => Delete the data (db_id = " + String.valueOf(db_id) + ")");
			
			return false;
			
		} else {//if (result == false)
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"Data exists in db" + String.valueOf(db_id) + ")");
			
		}//if (result == false)
		
		
		String sql = 
						"DELETE FROM " + tableName
						+ " WHERE " + android.provider.BaseColumns._ID
						+ " = "
						+ String.valueOf(db_id);
		
		try {
			db.execSQL(sql);
			
//			// Log
//			Log.d("DBUtils.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Data deleted => file id = " + String.valueOf(db_id));
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Sql executed: " + sql);
			
			return true;
			
		} catch (SQLException e) {
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "sql=" + sql);
			
			return false;
			
		}//try
		
	}//public boolean deleteData_ai(Activity actv, SQLiteDatabase db, String tableName, long db_id)

	/****************************************
	 *
	 * 
	 * <Caller> 
	 * 1. deleteData(Activity actv, SQLiteDatabase db, String tableName, long file_id)
	 * 
	 * <Desc> 
	 * 1. REF=> http://stackoverflow.com/questions/3369888/android-sqlite-insert-unique
	 * 
	 * <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static boolean isInDB_long(SQLiteDatabase db, String tableName, long file_id) {
		
		String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE file_id = '" +
						String.valueOf(file_id) + "'";
		
		long result = DatabaseUtils.longForQuery(db, sql, null);
		
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "result => " + String.valueOf(result));
		
		if (result > 0) {

			return true;
			
		} else {//if (result > 0)
			
			return false;
			
		}//if (result > 0)
		
//		return false;
		
	}//public boolean isInDB_long(SQLiteDatabase db, String tableName, long file_id)

	public static boolean isInDB_long_ai(
						SQLiteDatabase db, String tableName, long db_id) {
		
		String sql = "SELECT COUNT(*) FROM " + tableName
					+ " WHERE " + android.provider.BaseColumns._ID + " = "
					+ String.valueOf(db_id);
		
		long result = DatabaseUtils.longForQuery(db, sql, null);
		
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "result => " + String.valueOf(result));
		
		if (result > 0) {

			return true;
			
		} else {//if (result > 0)
			
			return false;
			
		}//if (result > 0)
		
//		return false;
		
	}//public static boolean isInDB_long_ai

	public List<TI> get_all_data_history(Activity actv,
			SQLiteDatabase rdb, long[] history_file_ids, String[] history_table_names) {
		/*********************************
		 * 1. Declare tiList
		 * 2. Query
		 * 
		 * 2-1. Record exists?
		 * 2-2. Create a TI object
		 * 3. Add to list
		 * 
		 * 4. Sort list
		 * 5. Return list
		 *********************************/
		List<TI> tiList = new ArrayList<TI>();
		
//		for (String name : history_table_names) {
//			
//			// Log
//			Log.d("DBUtils.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "history: name=" + name);
//			
//		}//for (String name : history_table_names)
		
		for (int i = 0; i < history_file_ids.length; i++) {
			
			/*********************************
			 * 2. Query
			 *********************************/
			String sql = "SELECT * FROM " + history_table_names[i]
						+ " WHERE file_id='" + history_file_ids[i] + "'";
			
			Cursor c = null;
			
			try {
				
				c = rdb.rawQuery(sql, null);
				
//				// Log
//				Log.d("DBUtils.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Query => Done");
				
			} catch (Exception e) {
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Exception => " + e.toString()
						+ "(i=" + i + ")");
				
				continue;
				
//				rdb.close();
//				
//				return null;
			}
			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "c.getCount() => " + c.getCount());

			/*********************************
			 * 2-1. Record exists?
			 *********************************/
			if (c.getCount() < 1) {
				
				// Log
				Log.d("DBUtils.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "c.getCount() < 1");
				
				continue;
				
			}
			
			
			/*********************************
			 * 2-2. Create a TI object
			 *********************************/
			c.moveToFirst();
			
			TI ti = new TI(
					c.getLong(1),	// file_id
					c.getString(2),	// file_path
					c.getString(3),	// file_name
					
					c.getLong(4),	// date_added
//					c.getLong(5)		// date_modified
					c.getLong(5),		// date_modified
					
					c.getString(6),	// memos
					c.getString(7),	// tags
					
					c.getLong(8)	// last_viewed_at
					);	

			/*********************************
			 * 3. Add to list
			 *********************************/
			tiList.add(ti);
					
			/*----------------------------
			 * 2.2. Add to list
				----------------------------*/
//			c.moveToNext();

			
		}//for (int i = 0; i < history_file_ids.length; i++)
		
		
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "tiList.size()=" + tiList.size());
		
		/*********************************
		 * 4. Sort list
		 *********************************/
//		Methods.sort_tiList_last_viewed_at(tiList);
		
		return tiList;
		
	}//public List<TI> get_all_data_history()


	public boolean insert_data_ai(SQLiteDatabase wdb, AI ai) {
		/*********************************
		 * memo
		 *********************************/
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		try {
			// Start transaction
			wdb.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();

//			Table =
//			"created_at INTEGER, modified_at INTEGER, "
//			{"file_name", 	"file_path",	"title", "memo",
//				"last_played_at",	"table_name"};

//			AI =
//			String file_name, String file_path,
//			String title, String memo,
//			
//			long last_played_at,
//			
//			String table_name, long created_at
			
//			val.put("file_id", ti.getFileId());
			val.put("created_at", ai.getCreated_at());
			
			val.put("modified_at", ai.getModified_at());
			
			val.put("file_name", ai.getFile_name());
			
			val.put("file_path", ai.getFile_path());
			
			val.put("title", ai.getTitle());
			
			val.put("memo", ai.getMemo());
			
			val.put("last_played_at", ai.getLast_played_at());
			
			val.put("table_name", ai.getTable_name());
			
			val.put("length", ai.getLength());
			
			// Insert data
			wdb.insert(CONS.tname_main, null, val);
			
			// Set as successful
			wdb.setTransactionSuccessful();
			
			// End transaction
			wdb.endTransaction();
			
			// Log
			Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Data inserted => " + "(file_name => " + val.getAsString("file_name") + "), and others");
			
			return true;
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try		
	}//public void insert_data_ai(SQLiteDatabase wdb, AI ai)

	public boolean insert_data_refresh_history(SQLiteDatabase wdb,
			String tableName, long[] data) {
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		try {
			// Start transaction
			wdb.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
//			// Put values
//			for (int i = 0; i < columnNames.length; i++) {
//				val.put(columnNames[i], values[i]);
//			}//for (int i = 0; i < columnNames.length; i++)
			
//			"last_refreshed", "num_of_items_added"
			
			val.put("last_refreshed", data[0]);
			
			val.put("num_of_items_added", data[1]);
			
			// Insert data
			wdb.insert(tableName, null, val);
			
			// Set as successful
			wdb.setTransactionSuccessful();
			
			// End transaction
			wdb.endTransaction();
			
			// Log
//			Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Data inserted => " + "(" + columnNames[0] + " => " + values[0] + 
//				" / " + columnNames[3] + " => " + values[3] + ")");
			
			return true;
			
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception! => " + e.toString());
			
			return false;
			
		}//try
		
	}//public boolean insert_data_refresh_history

	
	public static void update_data_ai(Activity actv,
								String dbName, String table_name, AI ai) {
//		/*********************************
//		 * memo
//		 *********************************/
//		DBUtils dbu = new DBUtils(actv, dbName);
//		
//		//
//		SQLiteDatabase wdb = dbu.getWritableDatabase();
//
//		
//		String sql = "UPDATE " + table_name + " SET " + 
//				"last_viewed_at='" + Methods.getMillSeconds_now() + "' " +
//				
//				" WHERE file_id = '" + String.valueOf(ti.getFileId()) + "'";
//			
//				
//			//			"file_id", 		"file_path", "file_name", "date_added", "date_modified"
//			//static String[] cols = 
//			//{"file_id", 		"file_path", "file_name", "date_added",
//			//"date_modified", "memos", "tags"};
//			
//			
//			try {
//			
//				wdb.execSQL(sql);
//				
//				// Log
//				Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "sql => Done: " + sql);
//				
//				//Methods.toastAndLog(actv, "Data updated", 2000);
//				
//				return true;
//				
//				
//			} catch (SQLException e) {
//				// Log
//				Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
//				
//				return false;
//			}
	}//public static void update_data_ai(String dbName, String table_name, AI ai)

	public static boolean update_data_ai(Activity actv, String dbName,
			long db_id, String col_name, String value) {
		/*********************************
		 * memo
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		
		String sql = "UPDATE " + CONS.tname_main + " SET " + 
//				"last_viewed_at='" + Methods.getMillSeconds_now() + "' " +
				
				col_name + " = '" + value + "' "
				+ " WHERE " + android.provider.BaseColumns._ID + " = '"
				+ db_id + "'";
			
				
			//			"file_id", 		"file_path", "file_name", "date_added", "date_modified"
			//static String[] cols = 
			//{"file_id", 		"file_path", "file_name", "date_added",
			//"date_modified", "memos", "tags"};
			
			
			try {
			
				wdb.execSQL(sql);
				
				// Log
				Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "sql => Done: " + sql);
				
				//Methods.toastAndLog(actv, "Data updated", 2000);
				
				return true;
				
				
			} catch (SQLException e) {
				// Log
				Log.e("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
				
				return false;
			}
		
	}//public static boolean update_data_ai()

	public static boolean
	update_data_ai
	(Activity actv, String dbName, String tableName,
			long db_id, String col_name, String value) {
		/*********************************
		 * memo
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		
//		String sql = "UPDATE " + CONS.tname_main + " SET " + 
	
		String sql = "UPDATE " + tableName + " SET " + 
//				"last_viewed_at='" + Methods.getMillSeconds_now() + "' " +

				col_name + " = '" + value + "' "
				+ " WHERE " + android.provider.BaseColumns._ID + " = '"
				+ db_id + "'";
		
		
		//			"file_id", 		"file_path", "file_name", "date_added", "date_modified"
		//static String[] cols = 
		//{"file_id", 		"file_path", "file_name", "date_added",
		//"date_modified", "memos", "tags"};
		
		
		try {
			
			wdb.execSQL(sql);
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "sql => Done: " + sql);
			
			//Methods.toastAndLog(actv, "Data updated", 2000);
			
			return true;
			
			
		} catch (SQLException e) {
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
			
			return false;
		}
		
	}//public static boolean update_data_ai()

	
	public void updateData_aiLength(Activity actv, String table_name,
			long db_id, int length) {
		
		DBUtils dbu = new DBUtils(actv, dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		
		String sql = "UPDATE " + table_name + " SET " + 
				"length" + " = " + length + " "
				+ " WHERE " + android.provider.BaseColumns._ID + " = '"
				+ db_id + "'";
				
		// Log
		Log.d("DBUtils.java" + "["
		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		+ "]", "sql=" + sql);

		try {
			
			wdb.execSQL(sql);
			
			// Log
			Log.d("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exec sql => Done");
			
		} catch (SQLException e) {

			// Log
			Log.e("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exception => " + e.toString());

		}//try
		
		// Close
		wdb.close();
		
	}//public void updateData_aiLength

	public boolean insertData_bm(Activity actv, BM bm) {
		// TODO Auto-generated method stub
		SQLiteDatabase wdb = this.getWritableDatabase();
		
		try {
			// Start transaction
			wdb.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();

//			"ai_id", "position", "title", "memo", "aiTableName"
//			val.put(android.provider.BaseColumns._ID, ai.getDb_id());
			
			val.put("created_at", Methods.getMillSeconds_now());
			val.put("modified_at", Methods.getMillSeconds_now());
			
			val.put("ai_id", bm.getAiId());
			val.put("position", bm.getPosition());
			
			val.put("title", bm.getTitle());
			val.put("memo", bm.getMemo());
			
			val.put("aiTableName", bm.getAiTableName());
			
			// Insert data
			wdb.insert(CONS.DB.tname_BM, null, val);
			
			// Set as successful
			wdb.setTransactionSuccessful();
			
			// End transaction
			wdb.endTransaction();
			
			// Log
			Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]",
				"Data inserted => "
				+ "(file_name => "
				+ val.getAsString("title") + "), and others");
			
			return true;
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
		
	}//public boolean insertData_bm(Activity actv, BM bm)

	
	public List<BM> getBMList(Activity actv, long aiDbId) {
		
		SQLiteDatabase rdb = this.getReadableDatabase();

		Cursor c = null;
		
		try {
			
			c = rdb.query(
							CONS.DB.tname_BM,
//							CONS.DBAdmin.col_purchaseSchedule,
//							CONS.DB.cols_bm,
							CONS.DB.cols_bm_full,
//							CONS.DB.cols_bm[0], new String[]{String.valueOf(aiDbId)},
							CONS.DB.cols_bm[0] + " = ?", new String[]{String.valueOf(aiDbId)},
							null, null, null);
			
		} catch (Exception e) {

			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", e.toString());
			
			rdb.close();
			
			return null;
			
		}//try
		
		/***************************************
		 * Validate
		 * 	Cursor => Null?
		 * 	Entry => 0?
		 ***************************************/
		if (c == null) {
			
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Query failed");
			
			rdb.close();
			
			return null;
			
		} else if (c.getCount() < 1) {//if (c == null)
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "No entry in the table");
			
			rdb.close();
			
			return null;
			
		}//if (c == null)
		
		/***************************************
		 * Build list
		 ***************************************/
		c.moveToFirst();
		
		List<BM> bmList = new ArrayList<BM>();
		
		for (int i = 0; i < c.getCount(); i++) {
//			"ai_id", "position", "title", "memo", "aiTableName"
			BM bm = new BM.Builder()
				.setPosition(c.getLong(c.getColumnIndex("position")))
				.setTitle(c.getString(c.getColumnIndex("title")))
				.setMemo(c.getString(c.getColumnIndex("memo")))
				.setAiId(c.getLong(c.getColumnIndex("ai_id")))
				.setAiTableName(c.getString(c.getColumnIndex("aiTableName")))
				.setDbId(c.getLong(c.getColumnIndex(CONS.DB.cols_bm_full[0])))
				.build();

			bmList.add(bm);
			
			c.moveToNext();
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		
		rdb.close();
		
		return bmList;
		
	}//public List<BM> getBMList(Activity actv)

	public boolean
	updateData_bm
	(Activity actv, long dbId, String colName, String colValue) {

		/***************************************
		 * Setup: DB
		 ***************************************/
		SQLiteDatabase wdb = this.getWritableDatabase();
		
		/***************************************
		 * Build SQL
		 ***************************************/
		String sql = "UPDATE " + CONS.DB.tname_BM + " SET "
//				+ colName + "='" + colValue + "', "
				+ colName + "='" + colValue + "'"
				+ " WHERE " + android.provider.BaseColumns._ID + " = '" + dbId + "'";
				
		/***************************************
		 * Exec: Query
		 ***************************************/
		try {
			
			wdb.execSQL(sql);
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "sql => Done: " + sql);
			
		//	Methods.toastAndLog(actv, "Data updated", 2000);

			wdb.close();
			
			return true;
			
			
		} catch (SQLException e) {
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
			
			wdb.close();
			
			return false;
		}

	}//updateData_bm()

	public boolean
	updateData_generic
	(Activity actv, String tableName, long dbId, String colName, String colValue) {

		/***************************************
		 * Setup: DB
		 ***************************************/
		SQLiteDatabase wdb = this.getWritableDatabase();
		
		/***************************************
		 * Build SQL
		 ***************************************/
		String sql = "UPDATE " + tableName + " SET "
//				+ colName + "='" + colValue + "', "
				+ colName + "='" + colValue + "'"
				+ " WHERE " + android.provider.BaseColumns._ID + " = '" + dbId + "'";
				
		/***************************************
		 * Exec: Query
		 ***************************************/
		try {
			
			wdb.execSQL(sql);
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "sql => Done: " + sql);
			
		//	Methods.toastAndLog(actv, "Data updated", 2000);

			wdb.close();
			
			return true;
			
			
		} catch (SQLException e) {
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
			
			wdb.close();
			
			return false;
		}

	}//updateData_generic()

	public int
	getNumOfEntries(Activity actv, String table_name) {
		/*********************************
		 * memo
		 *********************************/
//		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = this.getReadableDatabase();

		String sql = "SELECT * FROM " + table_name;
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return -1;
		}
		
		int num_of_entries = c.getCount();
		
		rdb.close();

		return num_of_entries;
		
	}//public int getNumOfEntries(Activity actv, String table_name)

	public int
	getNumOfEntries_BM(Activity actv, String table_name, long aiDbId) {
		/*********************************
		 * memo
		 *********************************/
//		DBUtils dbu = new DBUtils(actv, CONS.dbName);
		
		SQLiteDatabase rdb = this.getReadableDatabase();

		String sql = "SELECT * FROM " + table_name
					+ " WHERE "
					+ "ai_id = "
					+ aiDbId;
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return -1;
		}
		
		int num_of_entries = c.getCount();
		
		rdb.close();

		return num_of_entries;
		
	}//public int getNumOfEntries_BM(Activity actv, String table_name, long aiDbId)

	
	public boolean deleteData_bm(Activity actv, long dbId) {
		/*----------------------------
		 * Steps
		 * 1. Item exists in db?
		 * 2. If yes, delete it
			----------------------------*/
		/***************************************
		 * 1. Item exists in db?
		 ***************************************/
		SQLiteDatabase wdb = this.getWritableDatabase();
		
//		boolean result = DBUtils.isInDB_long_ai(db, tableName, db_id);
		boolean result = this.isInDB_bm(wdb, dbId);
		
		if (result == false) {		// Result is false ==> Meaning the target data doesn't exist
											//							in db; Hence, not executing delete op
			
			// debug
			Toast.makeText(actv, 
					"Data doesn't exist in db: " + String.valueOf(dbId), 
					Toast.LENGTH_LONG).show();
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"Data doesn't exist in db => " + String.valueOf(dbId));
			
			return false;
			
		} else {//if (result == false)
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"Data exists in db" + String.valueOf(dbId) + ")");
			
		}//if (result == false)
		

		/***************************************
		 * Delete data
		 ***************************************/
		String sql = 
						"DELETE FROM " + CONS.DB.tname_BM
						+ " WHERE "
						+ CONS.DB.cols_bm_full[0] + " = '"
						+ String.valueOf(dbId) + "'";
		
		try {
			wdb.execSQL(sql);
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Sql executed: " + sql);

			wdb.close();
			
			return true;
			
		} catch (SQLException e) {
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "sql=" + sql);
			
			wdb.close();
			
			return false;
			
		}//try
		
	}//public boolean deleteData_bm(Activity actv, long dbId)

	private boolean
	isInDB_bm(SQLiteDatabase wdb, long dbId) {
		String sql = "SELECT COUNT(*) FROM "
					+ CONS.DB.tname_BM
					+ " WHERE "
					+ CONS.DB.cols_bm_full[0] + " = '"
					+ String.valueOf(dbId) + "'";

//		long result = DatabaseUtils.longForQuery(db, sql, null);
		long result = DatabaseUtils.longForQuery(wdb, sql, null);
		
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "result => " + String.valueOf(result));
		
		if (result > 0) {
		
			return true;
			
		} else {//if (result > 0)
			
			return false;
			
		}//if (result > 0)
		
	}//isInDB_bm(SQLiteDatabase wdb, long dbId)

	
	public boolean
	updateData_BM_TitleAndMemo(Activity actv, long dbId, BM bm) {
		/***************************************
		 * Setup: DB
		 ***************************************/
		SQLiteDatabase wdb = this.getWritableDatabase();
		
		/***************************************
		 * Build SQL
		 ***************************************/
//			0		1			2		3		4
//		"ai_id", "position", "title", "memo", "aiTableName"
		
		String sql =
				"UPDATE " + CONS.DB.tname_BM + " SET " + 
				
				CONS.DB.cols_bm[2] + "='" + bm.getTitle() + "', " + 
				CONS.DB.cols_bm[3] + "='" + bm.getMemo() + "' " +
				
				" WHERE " + CONS.DB.cols_bm_full[0] + " = '"
				+ String.valueOf(bm.getDbId()) + "'";
		
		/***************************************
		 * Exec: Query
		 ***************************************/
		try {
			
			wdb.execSQL(sql);
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "sql => Done: " + sql);
			
		//	Methods.toastAndLog(actv, "Data updated", 2000);

			wdb.close();
			
			return true;
			
			
		} catch (SQLException e) {
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
			
			wdb.close();
			
			return false;
			
		}//try
		
	}//updateData_bm_full(Activity actv, long dbId, BM bm)

}//public class DBUtils

