/**
 * 
 */
package com.shandagames.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.shandagames.android.app.BaseActivity;
import com.shandagames.android.bean.Place;
import com.shandagames.android.http.BetterHttpApiV1;
import com.shandagames.android.local.CacheManager;
import com.shandagames.android.parser.Group;
import com.shandagames.android.parser.Result;
import com.shandagames.android.task.GenericTask;
import com.shandagames.android.task.TaskListener;

/**
 * @file GooglePlacesActivity.java
 * @create 2012-10-9 下午1:44:26
 * @author lilong
 * @description TODO
 */
public class PlacesActivity extends BaseActivity implements TaskListener {

	// KEY Strings
	private static String KEY_REFERENCE = "reference"; // id of the place
	private static String KEY_NAME = "name"; // name of the place
	
	// Progress dialog
	private	ProgressDialog pDialog;
	// Places Listview
	private	ListView lv;
	// Button
	private	Button btnShowOnMap;
	// ListItems data
	private	List<HashMap<String, String>> placesListItems;
	
	private Location location;
	private LoadPlaces loadPlaces;
	private Group<Place> places;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.places_layout);
		// Getting listview
		lv = (ListView) findViewById(R.id.list);
		// button show on map
		btnShowOnMap = (Button) findViewById(R.id.btn_show_map);
		
		location = getIntent().getParcelableExtra("location");
		placesListItems = new ArrayList<HashMap<String,String>>();

		Object object = CacheManager.getCacheValue("location");
		if (object != null) {
			System.out.println("get data for the memory cache!");
			places = (Group<Place>)object;
			ensureUi(places);
		} else {
			// Separeate your place types by PIPE symbol "|"
			// If you want all types places make it as null
			// Check list of types supported by google
			String types = "restaurant"; // Listing places only cafes, restaurants
			// Radius in meters - increase this value if you don't find any places
			double radius = 1000; // 1000 meters 
			// calling background Async task to load Google Places
			// After getting places from Google all the data is shown in listview
			loadPlaces = new LoadPlaces(location.getLatitude(), location.getLongitude(), radius, types, this, "1000");
			loadPlaces.execute();
		}
		
		/** Button click event for shown on map */
		btnShowOnMap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(PlacesActivity.this, PlacesMapActivity.class);
				// Sending user current geo location
				i.putExtra("near_location", location);
				// passing near places to map activity
				i.putExtra("near_places", places);
				// staring activity
				startActivity(i);
			}
		});
	}

	private void ensureUi(List<Place> places) {
		// Successfully got places details
		if (places != null && !places.isEmpty()) {
			// loop through each place
			for (Place p : places) {
				HashMap<String, String> data = new HashMap<String, String>();
				// Place reference won't display in listview - it will be hidden
				// Place reference is used to get "place full details"
				data.put(KEY_REFERENCE, p.getReference());
				// Place name
				data.put(KEY_NAME, p.getName());
				// adding HashMap to ArrayList
				placesListItems.add(data);
			}
			// list adapter
			ListAdapter adapter = new SimpleAdapter(PlacesActivity.this, placesListItems,
	                R.layout.places_list_item,
	                new String[] { KEY_REFERENCE, KEY_NAME}, 
	                new int[] {R.id.reference, R.id.name });
			// Adding data into listview
			lv.setAdapter(adapter);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (loadPlaces!=null) {
			loadPlaces.cancelTask(true);
			loadPlaces = null;
		}
	}

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	public void onTaskStart(String taskId) {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(this);
		pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
		pDialog.setIndeterminate(false);
		pDialog.show();
	}
	
	/**
	 * After completing background task Dismiss the progress dialog and show
	 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
	 * from background thread, otherwise you will get error
	 * **/

	@Override
	public void onTaskFinished(String taskName, Object value) {
		// dismiss the dialog after getting all products
		pDialog.dismiss();
		Result result = (Result) value;
		if (result.getException()!=null) {
			Toast.makeText(PlacesActivity.this, result.getException().getMessage(), Toast.LENGTH_LONG).show();
			return;
		}
		places = (Group<Place>)result.getResult();
		CacheManager.addCache("location", places, null);
		ensureUi(places);
	}
	
	
	class LoadPlaces extends GenericTask<Result> {
		private double _latitude;
		private double _longitude;
		private double _radius;
		private String _types;

		public LoadPlaces(double _latitude, double _longitude, double _radius,
				String _types, TaskListener taskListener, String taskName) {
			super(taskName, taskListener);
			this._latitude = _latitude;
			this._longitude = _longitude;
			this._radius = _radius;
			this._types = _types;
		}

		/**
		 * getting Places JSON
		 * */
		@Override
		protected Result doInBackground(String... params) {
			// TODO Auto-generated method stub
			BetterHttpApiV1 httpApiV1 = BetterHttpApiV1.getInstance();
			Result result = httpApiV1.searchPlaces(_latitude, _longitude, _radius, false, _types);
			return result;
		}
	}
}
