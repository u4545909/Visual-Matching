package com.video_browser_thesis.processing;

/**
 * @author Anshul Saini
 * @version 5
 */
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.video_browser_thesis.elements.KeyFrame_attributes;
import com.video_browser_thesis.json.JSONParser;

public class CB_GalleryProcessing implements Processing{

	private String base_url;
	private String url;

	private static final String TAG_MATCHING_KEYFRAME = "matching_key_frames";
	private static final String TAG_MATCHING_IMAGE_SOURCE= "MatchingFrameURL";

	KeyFrame_attributes[] k_attr;

	public CB_GalleryProcessing(String attach_url){
		base_url = "http://cantabile.anu.edu.au/memeapp/cgi-bin/mKeyFrameJSONOutput.py?q=";
		url = base_url + urlProcessing(attach_url);
		setArrayContents();
	}

	/**
	 * Creates a KeyFrame_attributes object by extracting the relevant data from the HashMap
	 * 
	 * @param  kf_attr 		An instance of KeyFrame_attributes object
	 * @return kf_attr    
	 * @see    
	 */
	@Override
	public void setArrayContents() {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, String>> temp_category_list = jsonFeed();
		KeyFrame_attributes kf_attr[] = new KeyFrame_attributes[temp_category_list.size()];
		
		for(int i=0; i<temp_category_list.size();i++){
			
			ImageProcessing ip = new ImageProcessing(temp_category_list.get(i).get(TAG_MATCHING_IMAGE_SOURCE).toString());
			
			kf_attr[i] = new KeyFrame_attributes(
					temp_category_list.get(i).get(TAG_MATCHING_IMAGE_SOURCE).toString(),
					temp_category_list.get(i).get(TAG_MATCHING_IMAGE_SOURCE).substring(97, 104),
					ip.decodeImage());
		}
		
		k_attr = kf_attr;
	}

	/**
	 * Organizes the data received from the URL in JSON format into a HashMap
	 * 
	 * @param  categoryList  	Hash Map which contains all the segregated metadata under different tag names 
	 * @param  jParser 			An instance of JSONParser which parses data from the provided URL
	 * @param  c				An instance JSONObject which stores all data contained within that JSON Object 
	 * @param  id				A temporary array to store video ids
	 * @param  image_source		A temporary array to store video images
	 * @param  video_url		A temporary array to store video url
	 * @param  video_name		A temporary array to store video name
	 * @return categoryList    
	 * @see    
	 */
	@Override
	public ArrayList<HashMap<String, String>> jsonFeed() {
		// TODO Auto-generated method stub
		JSONArray category = null;
		ArrayList<HashMap<String, String>> keyframe_list = new ArrayList<HashMap<String, String>>();

		/** 
		 * Creating JSON Parser instance 
		 */
		JSONParser jParser = new JSONParser();
		JSONObject c;
		String image_source;
		HashMap<String, String> map;

		/** 
		 * Getting JSON string from URL 
		 */
		JSONObject json = jParser.getJSONFromUrl(url);

		try {
			/** 
			 * Getting Array of Contacts 
			 */
			category = json.getJSONArray(TAG_MATCHING_KEYFRAME);

			/** looping through All Contacts */
			for(int i = 0; i < category.length(); i++){
				c = category.getJSONObject(i);

				/** 
				 * Storing each json item in variable 
				 */
				image_source = c.getString(TAG_MATCHING_IMAGE_SOURCE);

				/** Creating new HashMap */
				map = new HashMap<String, String>();

				/** 
				 * Adding each child node to HashMap key => value 
				 */
				map.put(TAG_MATCHING_IMAGE_SOURCE, image_source);

				/** 
				 * Adding HashList to ArrayList 
				 */
				keyframe_list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return keyframe_list;
	}
	
	public KeyFrame_attributes[] getKeyFrameArray(){
		return k_attr;
	}
	
	/** Returns the URL from which the data needs to be extracted*/
	public String urlProcessing(String url){
		String temp = "http://cantabile.anu.edu.au/memexplore/";
		temp = url.substring(temp.length(),url.length());
		return temp;
	}
	
	public int getArrayLength(){
		return k_attr.length;
	}
}
