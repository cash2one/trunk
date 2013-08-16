package com.shandagames.android.common;

import org.apache.http.NameValuePair;

public class NameValuePairImpl implements NameValuePair {

	private final String name, value;

	public NameValuePairImpl(final String name, final Object value) {
		this.name = name;
		this.value = String.valueOf(value);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

}
