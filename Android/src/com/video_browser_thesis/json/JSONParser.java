/*
 * Copyright (C) 2012 Anshul Saini
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


package com.video_browser_thesis.json;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Creates a JSON object from the data retrieved from the provided url
 * @author Anshul Saini
 * @version 5
 *
 * @param  is  		
 * @param  jObj 	JSON Object which is finally created from the JSON string 
 * @param  json 	JSON String which is used to create JSON Object 	
 * @return      	
 * @see         	
 */
public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	/**
	 * Makes the HTTP request, parses the JSON String, creates and JSON Object 
	 * @param httpClient	
	 * @param httpPost
	 * @param httpResponse
	 * @param httpEntity
	 * @param reader
	 * @param sb
	 * @param line
	 * @return jObj
	 * @see JSONObject
	 */
	public JSONObject getJSONFromUrl(String url) {

		DefaultHttpClient httpClient;
		HttpPost httpPost;
		HttpResponse httpResponse;
		HttpEntity httpEntity;
		BufferedReader reader;
		StringBuilder sb;
		String line;

		try {
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost(url);

			httpResponse = httpClient.execute(httpPost);
			httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();			

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			sb = new StringBuilder();
			line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		return jObj;
	}
}
