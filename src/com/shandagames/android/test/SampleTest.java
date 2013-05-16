package com.shandagames.android.test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import com.shandagames.android.bean.Person;
import com.shandagames.android.dao.PersonDao;
import com.shandagames.android.preferences.ObscuredSharedPreferences;
import com.shandagames.android.util.DateHelper;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.SystemClock;
import android.test.AndroidTestCase;
import android.util.Xml;

/**
 * @author lilong
 * @version 2012-6-25 下午5:59:23
 * 
 */
public class SampleTest extends AndroidTestCase {

	public void testSavePerson() throws Throwable {
		Person person = new Person();
		person.setId(1000);
		person.setName("selience");
		person.setPhone("12345678");
		person.setTel("12345678");
		person.setEmail("selienceblog@sina.com");
		person.setAddress("中国上海");
		person.setBackContent("测试数据");
	
		PersonDao personDao = new PersonDao(getContext());
		long rowId = personDao.add(person);
		System.out.println("sqlite status:"+rowId);
	}
	
	
	public void testSaveFile() throws Throwable {
		String str = "android save file";
		OutputStream outStream = getContext().openFileOutput(System.currentTimeMillis()+".txt", Context.MODE_PRIVATE);
		outStream.write(str.getBytes());
		outStream.close();
	}
	
	public void testSimple() throws Throwable {
		String date = DateHelper.millisToString(System.currentTimeMillis());
		long lasttime = SystemClock.currentThreadTimeMillis();
		SystemClock.sleep(5000);
		long time = SystemClock.uptimeMillis() - lasttime;
		System.out.println("currenttime:"+date+","+lasttime+","+time);
	}
	
	public void encrypt() throws Throwable {
		SharedPreferences preference = new ObscuredSharedPreferences(getContext(), "selience");
		Editor editor = preference.edit();
		editor.putString("a", "qqqqq");
		editor.commit();
		
		String str = preference.getString("a", null);
		System.out.println("DES:"+str);
	}
	
	public void parseXml() throws XmlPullParserException, IOException {
		Reader reader = new StringReader("<?xml version='1.0'?>" + "<menu>"
				+ "	<item>Waffles</item>" + "	<item>Coffee</item>" + "</menu>");
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(reader);
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null, "menu");
		while (parser.nextTag() == XmlPullParser.START_TAG) {
			parser.require(XmlPullParser.START_TAG, null, "item");
			String itemText = parser.nextText();
			if (parser.getEventType() != XmlPullParser.END_TAG) {
				parser.nextTag(); // this call shouldn’t be necessary!
			}
			parser.require(XmlPullParser.END_TAG, null, "item");
			System.out.println("menu option: " + itemText);
		}
		parser.require(XmlPullParser.END_TAG, null, "menu");
	}

	public String safeNextText(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		String result = parser.nextText();
		if (parser.getEventType() != XmlPullParser.END_TAG) {
			parser.nextTag();
		}
		return result;
	}
}
