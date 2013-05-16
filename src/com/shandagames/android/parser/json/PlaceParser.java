/**
 * 
 */
package com.shandagames.android.parser.json;

import org.json.JSONException;
import org.json.JSONObject;
import com.shandagames.android.bean.Place;
import com.shandagames.android.bean.Place.Geometry;
import com.shandagames.android.bean.Place.Location;
import com.shandagames.android.parser.AbstractParser;

/**
 * @file PlaceParser.java
 * @create 2012-10-11 下午4:00:46
 * @author lilong
 * @description TODO
 */
public class PlaceParser extends AbstractParser<Place> {

	@Override
	public Place parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		Place place = new Place();

		if (json.has("id")) {
			place.setId(json.optString("id"));
		}
		if (json.has("name")) {
			place.setName(json.optString("name"));
		}
		if (json.has("reference")) {
			place.setReference(json.optString("reference"));
		}
		if (json.has("icon")) {
			place.setIcon(json.optString("icon"));
		}
		if (json.has("vicinity")) {
			place.setVicinity(json.optString("vicinity"));
		}
		if (json.has("geometry")) {
			place.setGeometry(new GeometryParser().parse(json.getJSONObject("geometry")));
		}
		if (json.has("formatted_address")) {
			place.setFormatted_address("formatted_address");
		}
		if (json.has("formatted_phone_number")) {
			place.setFormatted_phone_number("formatted_address");
		}
		return place;
	}

	public class GeometryParser extends AbstractParser<Geometry> {

		@Override
		public Geometry parse(JSONObject json) throws JSONException {
			// TODO Auto-generated method stub
			Geometry geometry = new Geometry();
			
			if (json.has("location")) {
				geometry.setLocation(new LocationParser().parse(json.getJSONObject("location")));
			}
			return geometry;
		}
	}
	
	public class LocationParser extends AbstractParser<Location> {

		@Override
		public Location parse(JSONObject json) throws JSONException {
			// TODO Auto-generated method stub
			Location location = new Location();
			
			if (json.has("lat")) {
				location.setLat(json.getDouble("lat"));
			}
			if (json.has("lng")) {
				location.setLat(json.getDouble("lng"));
			}
			return location;
		}
	}
}
