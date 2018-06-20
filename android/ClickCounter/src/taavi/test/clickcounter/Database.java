package taavi.test.clickcounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database {
	private static final String TAG = "Database";
	
	public static final class DatabaseHelper extends SQLiteOpenHelper {
		public static final String DATABASE_NAME = "counterDb.db";
		private static final int DATABASE_VERSION = 1;

		/**
		 * Constructor
		 * 
		 * @param context
		 * */
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/**
		 * Creates database table(s)
		 * 
		 * @param db - database where to create table
		 * */
		@Override
		public void onCreate(SQLiteDatabase db) {
			onCreateConfig(db);
		}

		/**
		 * Upgrades database (at the moment deletes all data from tables
		 * 
		 * @param db - database to upgrade
		 * @param oldVersion - old database version number
		 * @param newVersion - new database version number
		 * */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
			Log.w(TAG, "Upgrading database from version " + oldVer + " to "
					+ newVer + ", which will destroy all old data");
			
			onUpgradeConfig(db);
		}
		
		/**
		 * Creates config table
		 * 
		 * @param db - database where the table is located in
		 */
		private void onCreateConfig(SQLiteDatabase db) {
			String sql = "CREATE TABLE " + Conf.TABLE_NAME + "("
					+ Conf.COL_ID + " INTEGER PRIMARY KEY, "
					+ Conf.COL_IP + " TEXT"
					+ ");";
			
			db.execSQL(sql);
		}
		
		/**
		 * Upgrades config table
		 * 
		 * @param db - database where the table is located in
		 * */
		private void onUpgradeConfig(SQLiteDatabase db) {
			String sql = "DELETE TABLE IF EXSTS " + Conf.TABLE_NAME + ";";
			db.execSQL(sql);
			onCreateConfig(db);
		}
	}
	
	/**
	 * Holds the table and column names for config table
	 */
	public class Conf {
		public static final String TABLE_NAME = "config";
		public static final String COL_ID = "_id";
		public static final String COL_IP = "ip";
	}
}
