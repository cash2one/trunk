/**
 * 
 */
package com.shandagames.android.dao;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.shandagames.android.bean.WebSite;
import com.shandagames.android.db.SQLiteHelper;
import com.shandagames.android.db.SQLiteHelper.RssColumns;
import com.shandagames.android.db.SQLiteHelper.TABLE;

/**
 * @file RssDao.java
 * @create 2012-8-31 下午12:43:17
 * @author lilong
 * @description TODO
 */
public class RssDao {

	private SQLiteDatabase db;

	private SQLiteHelper helper;

	public RssDao(Context context) {
		helper = SQLiteHelper.getInstance(context);
	}

	/**
	 * Adding a new website in websites table Function will check if a site
	 * already existed in database. If existed will update the old one else
	 * creates a new row
	 * */
	public void addSite(WebSite site) {
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(RssColumns.KEY_TITLE, site.getTitle()); // site title
		values.put(RssColumns.KEY_LINK, site.getLink()); // site url
		values.put(RssColumns.KEY_RSS_LINK, site.getRSSLink()); // rss link url
		values.put(RssColumns.KEY_DESCRIPTION, site.getDescription()); // site
																		// description
		// Check if row already existed in database
		if (!isSiteExists(db, site.getRSSLink())) {
			// site not existed, create a new row
			db.insert(TABLE.RSS, null, values);
			db.close();
		} else {
			// site already existed update the row
			updateSite(site);
			db.close();
		}
	}

	/**
	 * Reading all rows from database
	 * */
	public List<WebSite> getAllSites() {
		List<WebSite> siteList = new ArrayList<WebSite>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE.RSS + " ORDER BY "
				+ RssColumns.KEY_ID + " DESC";
		db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				WebSite site = new WebSite();
				site.setId(Integer.parseInt(cursor.getString(0)));
				site.setTitle(cursor.getString(1));
				site.setLink(cursor.getString(2));
				site.setRSSLink(cursor.getString(3));
				site.setDescription(cursor.getString(4));
				// Adding contact to list
				siteList.add(site);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return contact list
		return siteList;
	}

	/**
	 * Updating a single row row will be identified by rss link
	 * */
	public int updateSite(WebSite site) {
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(RssColumns.KEY_TITLE, site.getTitle());
		values.put(RssColumns.KEY_LINK, site.getLink());
		values.put(RssColumns.KEY_RSS_LINK, site.getRSSLink());
		values.put(RssColumns.KEY_DESCRIPTION, site.getDescription());

		// updating row return
		int update = db.update(TABLE.RSS, values, RssColumns.KEY_RSS_LINK
				+ " = ?", new String[] { String.valueOf(site.getRSSLink()) });
		db.close();
		return update;

	}

	/**
	 * Reading a row (website) row is identified by row id
	 * */
	public WebSite getSite(int id) {
		db = helper.getReadableDatabase();
		Cursor cursor = db.query(TABLE.RSS, new String[] { RssColumns.KEY_ID,
				RssColumns.KEY_TITLE, RssColumns.KEY_LINK,
				RssColumns.KEY_RSS_LINK, RssColumns.KEY_DESCRIPTION },
				RssColumns.KEY_ID + "=?", new String[] { String.valueOf(id) },
				null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		WebSite site = new WebSite(cursor.getString(1), cursor.getString(2),
				cursor.getString(3), cursor.getString(4));
		site.setId(Integer.parseInt(cursor.getString(0)));
		site.setTitle(cursor.getString(1));
		site.setLink(cursor.getString(2));
		site.setRSSLink(cursor.getString(3));
		site.setDescription(cursor.getString(4));
		cursor.close();
		db.close();
		return site;
	}

	/**
	 * Deleting single row
	 * */
	public void deleteSite(WebSite site) {
		db = helper.getWritableDatabase();
		db.delete(TABLE.RSS, RssColumns.KEY_ID + " = ?",
				new String[] { String.valueOf(site.getId()) });
		db.close();
	}

	/**
	 * Checking whether a site is already existed check is done by matching rss
	 * link
	 * */
	public boolean isSiteExists(SQLiteDatabase db, String rss_link) {
		Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE.RSS + " WHERE "
				+ RssColumns.KEY_RSS_LINK + " = '" + rss_link + "'",
				new String[] {});
		boolean exists = (cursor.getCount() > 0);
		return exists;
	}

}
