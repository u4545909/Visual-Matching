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

public class CB_Processing implements Processing{
	
	private String base_url;
	private String url;
	
	private static final String TAG_KEYFRAME = "key_frame";
	private static final String TAG_IMAGE_SOURCE= "ImageSource";
	private static final String TAG_NAME = "Name";
	
	KeyFrame_attributes[] k_attr;
	
	public CB_Processing(String attach_url){
		base_url = "http://cantabile.anu.edu.au/memeapp/cgi-bin/keyFramesJSONOutput.py?q=";
		url = base_url + attach_url;
		setArrayContents();
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
	public ArrayList<HashMap<String, String>> jsonFeed(){
		
		JSONArray category = null;
		ArrayList<HashMap<String, String>> keyframe_list = new ArrayList<HashMap<String, String>>();
		
		/** 
		 * Creating JSON Parser instance 
		 */
		JSONParser jParser = new JSONParser();
		JSONObject c;
		String image_source,video_name;
		HashMap<String, String> map;

		/** 
		 * Getting JSON string from URL 
		 */
		JSONObject json = jParser.getJSONFromUrl(url);

		try {
			/** 
			 * Getting Array of Contacts 
			 */
			category = json.getJSONArray(TAG_KEYFRAME);

			/** looping through All Contacts */
			for(int i = 0; i < category.length(); i++){
				c = category.getJSONObject(i);

				/** 
				 * Storing each json item in variable 
				 */
				image_source = c.getString(TAG_IMAGE_SOURCE);
				video_name = c.getString(TAG_NAME);
				
				/** Creating new HashMap */
				map = new HashMap<String, String>();

				/** 
				 * Adding each child node to HashMap key => value 
				 */
				map.put(TAG_IMAGE_SOURCE, image_source);
				map.put(TAG_NAME, video_name);

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
	
	/**
	 * Creates a KeyFrame_attributes object by extracting the relevant data from the HashMap
	 * 
	 * @param  kf_attr 		An instance of KeyFrame_attributes object
	 * @return kf_attr    
	 * @see    
	 */
	public void setArrayContents(){ 
		
		ArrayList<HashMap<String, String>> temp_category_list = jsonFeed();
		KeyFrame_attributes kf_attr[] = new KeyFrame_attributes[temp_category_list.size()];
		
		for(int i=0; i<temp_category_list.size();i++){
			
			ImageProcessing ip = new ImageProcessing(temp_category_list.get(i).get(TAG_IMAGE_SOURCE).toString());
			
			kf_attr[i] = new KeyFrame_attributes(
					temp_category_list.get(i).get(TAG_IMAGE_SOURCE).toString(),
					temp_category_list.get(i).get(TAG_NAME).substring(32, 38),
					ip.decodeImage());
		}
		
		k_attr = kf_attr;
	}	
	
	public KeyFrame_attributes[] getKeyFrameArray(){
		return k_attr;
	}
}
