package com.shandagames.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * @file SQLiteHelper.java
 * @create 2012-9-6 下午3:35:18
 * @author lilong
 * @description SQLite使用辅助类
 * 		优化： 1. 减少String "+"操作，使用StringBuilder代替
 * 			  2. 循环插入多条语句时采用compileStatement进行复用，循环体外仅编译一次
 */
public class SQLiteHelper extends SQLiteAssetHelper {

	// 类没有实例化,是不能用作父类构造器的参数,必须声明为静态
	private final static String DATABASE_NAME = "northwind"; // 数据库名称
	private final static int DATABASE_VERSION = 1; // 数据库版本
	private static SQLiteHelper INSTANCE;

	public SQLiteHelper(Context context) {
		// 第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static synchronized SQLiteHelper getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new SQLiteHelper(context);
		}
		return INSTANCE;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PERSON_SQL);
		//db.execSQL(CREATE_RSS_SQL);
	}

	private static final String CREATE_PERSON_SQL = 
		"CREATE TABLE " + TABLE.PERSON + "(" + 
				PersonColumns.ID + " INTEGER PRIMARY KEY,"+ 
				PersonColumns.NAME + " TEXT NOT NULL,"+ 
				PersonColumns.PHONE +" phone TEXT NOT NULL DEFAULT '',"+ 
				PersonColumns.EMAIL +" TEXT NOT NULL DEFAULT '',"+ 
				PersonColumns.TEL +" TEXT NOT NULL DEFAULT '',"+ 
				PersonColumns.ADDRESS + " TEXT,"+
				PersonColumns.BACK_CONTENT+" TEXT," + 
				PersonColumns.CREATED_DATE +" INTEGER NOT NULL DEFAULT (strftime('%s','now') * 1000)" + 
		 ")";
	
	private static final String CREATE_RSS_SQL = 
		"CREATE TABLE " + TABLE.RSS + "(" + 
				RssColumns.KEY_ID + " INTEGER PRIMARY KEY,"+ 
				RssColumns.KEY_TITLE + " TEXT NOT NULL,"+ 
				RssColumns.KEY_LINK+" phone TEXT NOT NULL,"+ 
				RssColumns.KEY_RSS_LINK+" TEXT NOT NULL,"+ 
				RssColumns.KEY_DESCRIPTION+" TEXT "+ 
		")";

	

	public interface TABLE {

		static final String PERSON = "t_person";

		static final String RSS = "t_websites";
	}

	public interface RssColumns {
		// Contacts Table Columns names
		static final String KEY_ID = "id";

		static final String KEY_TITLE = "title";

		static final String KEY_LINK = "link";

		static final String KEY_RSS_LINK = "rss_link";

		static final String KEY_DESCRIPTION = "description";
	}

	public interface PersonColumns {

		static final String ID = "id";

		static final String NAME = "name";

		static final String PHONE = "phone";

		static final String EMAIL = "email";

		static final String TEL = "tel";
		
		static final String ADDRESS = "address";

		static final String BACK_CONTENT = "back_content";

		static final String CREATED_DATE = "created_date";
	}
}
