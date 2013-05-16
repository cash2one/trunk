/*
 * Copyright 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shandagames.android.zing;

/**
 * <p>Encapsulates the result of a barcode scan invoked through {@link IntentIntegrator}.</p>
 *		http://code.google.com/p/zxing/wiki/ScanningViaIntent
 *
 *	1.	IntentIntegrator integrator = new IntentIntegrator(yourActivity);
 *		integrator.initiateScan();
 *
 *	2.	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
 *		  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
 *		  if (scanResult != null) { }
 *		}
 * @author Jacky Lee
 */
public final class IntentResult {

	private final String contents;
	private final String formatName;

	IntentResult(String contents, String formatName) {
		this.contents = contents;
		this.formatName = formatName;
	}

	/**
	 * @return raw content of barcode
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * @return name of format, like "QR_CODE", "UPC_A". See
	 *         <code>BarcodeFormat</code> for more format names.
	 */
	public String getFormatName() {
		return formatName;
	}

	@Override
	public String toString() {
		return "IntentResult [contents=" + contents + ", formatName="
				+ formatName + "]";
	}

}