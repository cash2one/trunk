package com.shandagames.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


public abstract class ContentProviderDB<T> implements ISQLiteDatabase {
	private final Context context;

	public ContentProviderDB(Context context) {
		this.context = context;
	}

	public abstract Uri getUriFromTable(String paramString);

	@Override
	public Cursor query(String paramString1, String[] paramArrayOfString1,
			String paramString2, String[] paramArrayOfString2,
			String paramString3, String paramString4, String paramString5) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor rawQuery(String paramString, String[] paramArrayOfString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean execSQL(String paramString1, String paramString2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long insert(String paramString1, String paramString2,
			ContentValues paramContentValues) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(String paramString1, ContentValues paramContentValues,
			String paramString2, String[] paramArrayOfString) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long replace(String paramString1, String paramString2,
			ContentValues paramContentValues) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(String paramString1, String paramString2,
			String[] paramArrayOfString) {
		// TODO Auto-generated method stub
		return 0;
	}

}
