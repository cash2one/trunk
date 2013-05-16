package com.shandagames.android.provider;

import com.shandagames.android.db.SQLiteHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

/**
 * ContentProvider(内容提供者)统一了数据的访问方式
 * 
 * @author selience
 * 
 */
public class ContentProviderHelper extends ContentProvider {

	// 数据集的MIME类型字符串则应该以vnd.android.cursor.dir/开头
	public static final String PERSONS_TYPE = "vnd.android.cursor.dir/person";
	// 单一数据的MIME类型字符串应该以vnd.android.cursor.item/开头
	public static final String PERSONS_ITEM_TYPE = "vnd.android.cursor.item/person";
	public static final String AUTHORITY = "com.itcast.provider.personprovider";// 主机名
	/* 自定义匹配码 */
	public static final int PERSONS = 1;
	/* 自定义匹配码 */
	public static final int PERSON = 2;
	public static final Uri PERSONS_URI = Uri.parse("content://" + AUTHORITY + "/person");

	/* 这里UriMatcher是用来匹配Uri的类，使用match()方法匹配路径时返回匹配码 */
	private static final UriMatcher sMatcher;
	
	static {
		sMatcher = new UriMatcher(UriMatcher.NO_MATCH);// 常量UriMatcher.NO_MATCH表示不匹配任何路径的返回码
		// 如果match()方法匹配content://com.itcast.provider.personprovider/person路径，返回匹配码为PERSONS
		sMatcher.addURI(AUTHORITY, "person", PERSONS);
		// 如果match()方法匹配content://com.itcast.provider.personprovider/person/230路径，返回匹配码为PERSON
		sMatcher.addURI(AUTHORITY, "person/#", PERSON);
	}
	
	private SQLiteHelper databaseHelper;

	/** 系统启动时调用  */
	@Override
	public boolean onCreate() {
		databaseHelper = new SQLiteHelper(this.getContext());
		
		return true;
	}

	/** 供外部应用向ContentProvider添加数据 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		if (sMatcher.match(uri) != PERSONS) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		long rowId = db.insert("person", "personid", values);// 往person表添加一条记录
		db.close();
		if (rowId > 0) {// 如果添加成功
			//发送通知
			getContext().getContentResolver().notifyChange(uri, null);
			// ContentUris是content URI的一个辅助类。下面方法负责把rowId和PERSONS_URI连接成一个新的Uri，
			// 生成的Uri如：content://com.itcast.provider.personprovider/person/10
			return ContentUris.withAppendedId(PERSONS_URI, rowId);
		}
		throw new SQLException("Failed to insert row into " + uri);// 抛出添加失败信息
	}

	/** 用于供外部应用从ContentProvider删除数据  */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		int count = 0;
		switch (sMatcher.match(uri)) {
			case PERSONS:
				count = db.delete("person", selection, selectionArgs);
				break;
			case PERSON:
				// 下面的方法用于从URI中解析出id，对这样的路径content://com.itcast.provider.personprovider/person/10
				// 进行解析，返回值为10
				long personid = ContentUris.parseId(uri);
				String where = "personid=" + personid;// 删除指定id的记录
				where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")"
						: "";// 把其它条件附加上
				count = db.delete("person", where, selectionArgs);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		db.close();
		// 通知监视者更新数据
		getContext().getContentResolver().notifyChange(uri, null);
		
		return count;
	}

	/** 用于供外部应用更新ContentProvider中的数据 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		int count = 0;
		switch (sMatcher.match(uri)) {
			case PERSONS:
				count = db.update("person", values, selection, selectionArgs);
				break;
			case PERSON:
				// 下面的方法用于从URI中解析出id，对这样的路径content://com.itcast.provider.personprovider/person/10
				// 进行解析，返回值为10
				long personid = ContentUris.parseId(uri);
				String where = "personid=" + personid;// 删除指定id的记录
				where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")"
						: "";// 把其它条件附加上
				count = db.update("person", values, where, selectionArgs);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		db.close();
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	/** 用于供外部应用从ContentProvider中获取数据  */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cursor cursor;

		switch (sMatcher.match(uri)) {
			case PERSONS:
				cursor = db.query("person", projection, selection, selectionArgs,
						null, null, sortOrder);
				break;
			case PERSON:
				// 下面的方法用于从URI中解析出id，对这样的路径content://com.itcast.provider.personprovider/person/10
				// 进行解析，返回值为10
				long personid = ContentUris.parseId(uri);
				String where = "personid=" + personid;// 删除指定id的记录
				where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")"
						: "";// 把其它条件附加上
				cursor = db.query("person", projection, where, selectionArgs, null,
						null, sortOrder);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		return cursor;
	}

	/**
	 * 该方法用于返回当前Url所代表数据的MIME类型。如果操作的数据属于集合类型，那么MIME类型字符串应该以vnd.android.cursor.dir/开头
	 */
	@Override
	public String getType(Uri uri) {
		switch (sMatcher.match(uri)) {
			case PERSONS:
				return PERSONS_TYPE;
			case PERSON:
				return PERSONS_ITEM_TYPE;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

}
