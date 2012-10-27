package com.abdullahsolutions.solatmalaysia;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SolatDB extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "solatmalaysia";	
	public static final String TABLE_NAME = "waktusolat";
	private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME +
			" ( _id INTEGER PRIMARY KEY, " +
			" kawasan Text," +
			" tarikh Text, " +
			" imsak Text," +
			" subuh Text," +
			" syuruk Text," +
			" zohor Text," +
			" asar Text," +
			" maghrib Text," +
			" isya Text )";

	SolatDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
