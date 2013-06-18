package com.shandagames.android.dao;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.shandagames.android.bean.Person;
import com.shandagames.android.database.SQLiteHelper;
import com.shandagames.android.database.SQLiteHelper.PersonColumns;
import com.shandagames.android.database.SQLiteHelper.TABLE;

/**
 * @file PersonDAO.java
 * @create 2012-8-31 下午12:36:56
 * @author lilong
 * @description TODO
 */
public class PersonDao {

	private Context context;
	private SQLiteDatabase db;
	private SQLiteHelper helper;

	public PersonDao(Context context) {
		this.context = context;
		helper = SQLiteHelper.getInstance(context);
	}

	// 新建联系人
	public long add(Person person) {
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(PersonColumns.NAME, person.getName());
		values.put(PersonColumns.PHONE, person.getPhone());
		values.put(PersonColumns.EMAIL, person.getEmail());
		values.put(PersonColumns.TEL, person.getTel());
		values.put(PersonColumns.ADDRESS, person.getAddress());
		values.put(PersonColumns.BACK_CONTENT, person.getBackContent());
		// 返回新添加的记录行号,与主键无关
		long rowId = db.insert(TABLE.PERSON, PersonColumns.ID, values);
		db.close();
		//通知观察者
		context.getContentResolver().notify();
		return rowId;
	}

	// 批量插入数据
	public long addAll(List<Person> persons) {
		db = helper.getWritableDatabase();
		db.beginTransaction();
		String sql = "insert into "+TABLE.PERSON+" values(?,?,?,?,?,?)";
		SQLiteStatement statement = null;
		try {
			statement = db.compileStatement(sql);
			for (Person person : persons) {
				int index = 1;
				
				statement.bindString(index++, person.getName());
				statement.bindString(index++, person.getPhone());
				statement.bindString(index++, person.getEmail());
				statement.bindString(index++, person.getTel());
				statement.bindString(index++, person.getAddress());
				statement.bindString(index++, person.getBackContent());
			}
			long rowId = statement.executeInsert();
			db.setTransactionSuccessful();
			return rowId;
		} finally {
			if (statement != null) {
				statement.close();
			}
			db.endTransaction();
			db.close();
		}
	}
	
	// 删除联系人
	public boolean delete(Integer... ids) {
		if (ids.length == 0)
			return false;
		db = helper.getWritableDatabase();
		StringBuffer sb = new StringBuffer();
		String[] idstr = new String[ids.length];
		for (int i = 0; i < ids.length; i++) {
			sb.append("?").append(",");
			idstr[i] = String.valueOf(ids[i]);

		}
		sb.deleteCharAt(ids.length);
		db.delete(TABLE.PERSON, PersonColumns.ID + " in (" + sb + ")", idstr);
		db.close();
		return true;
	}

	// 更新联系人
	public boolean update(Person person) {
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(PersonColumns.NAME, person.getName());
		values.put(PersonColumns.PHONE, person.getPhone());
		values.put(PersonColumns.EMAIL, person.getEmail());
		values.put(PersonColumns.TEL, person.getTel());
		values.put(PersonColumns.ADDRESS, person.getAddress());
		values.put(PersonColumns.BACK_CONTENT, person.getBackContent());
		db.update(TABLE.PERSON, values, PersonColumns.ID + "=?",
				new String[] { String.valueOf(person.getId()) });
		db.close();
		return true;
	}

	// 根据id查询联系人
	public Person find(int id) {
		Person person = null;
		db = helper.getWritableDatabase();
		String[] columns = { PersonColumns.ID, PersonColumns.NAME,
				PersonColumns.PHONE, PersonColumns.TEL, PersonColumns.EMAIL,
				PersonColumns.ADDRESS, PersonColumns.BACK_CONTENT };
		Cursor cursor = db.query(TABLE.PERSON, columns,
				PersonColumns.ID + "=?", new String[] { String.valueOf(id) },
				null, null, null);
		if (cursor.moveToNext()) {
			person = new Person();
			person.setId(cursor.getInt(0));
			person.setName(cursor.getString(1));
			person.setPhone(cursor.getString(2));
			person.setTel(cursor.getString(3));
			person.setEmail(cursor.getString(4));
			person.setAddress(cursor.getString(5));
			person.setBackContent(cursor.getString(6));
		}
		cursor.close();
		db.close();
		return person;
	}

	// 查询所有联系人
	public List<Person> findAll() {
		db = helper.getWritableDatabase();
		String[] columns = { PersonColumns.ID, PersonColumns.NAME,
				PersonColumns.PHONE, PersonColumns.TEL, PersonColumns.EMAIL,
				PersonColumns.ADDRESS, PersonColumns.BACK_CONTENT };
		Cursor cursor = db.query(TABLE.PERSON, columns, null, null, null, null,
				PersonColumns.NAME + " ASC");
		List<Person> list = new ArrayList<Person>();
		while (cursor.moveToNext()) {
			Person person = new Person();
			person.setId(cursor.getInt(0));
			person.setName(cursor.getString(1));
			person.setPhone(cursor.getString(2));
			person.setTel(cursor.getString(3));
			person.setEmail(cursor.getString(4));
			person.setAddress(cursor.getString(5));
			person.setBackContent(cursor.getString(6));
			list.add(person);
		}
		//感知数据的变化
		//cursor.setNotificationUri(context.getContentResolver(), null);
		cursor.close();
		db.close();
		return list;
	}

	// 名称查询所有联系人
	public List<Person> findByName(String name) {
		db = helper.getWritableDatabase();
		String[] columns = { PersonColumns.ID, PersonColumns.NAME,
				PersonColumns.PHONE, PersonColumns.TEL, PersonColumns.EMAIL,
				PersonColumns.ADDRESS, PersonColumns.BACK_CONTENT };
		Cursor cursor = db.query(TABLE.PERSON, columns, PersonColumns.NAME
				+ " LIKE '" + name + "%'", null, null, null, PersonColumns.NAME
				+ " ASC");
		List<Person> list = new ArrayList<Person>();
		while (cursor.moveToNext()) {
			Person person = new Person();
			person.setId(cursor.getInt(0));
			person.setName(cursor.getString(1));
			person.setPhone(cursor.getString(2));
			person.setTel(cursor.getString(3));
			person.setEmail(cursor.getString(4));
			person.setAddress(cursor.getString(5));
			person.setBackContent(cursor.getString(6));
			list.add(person);
		}
		cursor.close();
		db.close();
		return list;
	}

	// 所有联系人总得条数
	public int count() {
		int count = 0;
		db = helper.getWritableDatabase();
		String[] columns = { "count(1)" };
		Cursor cursor = db.query(TABLE.PERSON, columns, null, null, null, null,
				null);
		if (cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return count;
	}
}
