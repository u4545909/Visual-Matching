package com.video_browser_thesis.processing;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.video_browser_thesis.elements.Video_attributes;
import com.video_browser_thesis.json.JSONParser;

public class MainGalleryProcessing implements Processing{
	private static String url;
	private String category_name;

	private static final String TAG_TITLE = "Title";
	private static final String TAG_AUTHOR = "Author";
	private static final String TAG_THUMBNAIL = "Thumbnail0";
	private static final String TAG_ID = "ID";
	private static final String TAG_VIDEOID = "VideoID";
	private static final String TAG_PLAYER_URL = "PlayerUrl";
	private static final String TAG_LENGTH = "Length";
	private static final String TAG_TOPICS = "Topics";
	private static final String TAG_UPLOADED_TIME = "Uploadtime";
	private static final String TAG_VIEW_COUNT = "ViewCount";
	private static final String TAG_DESCRIPTION = "Description";
	
	Video_attributes[] v_attr;
	
	public MainGalleryProcessing(String category_name){
		url = "http://cantabile.anu.edu.au/memeapp/cgi-bin/metadataJSONOutput.py";
		this.category_name = category_name;
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
	 * @see    Image
	 */
	public ArrayList<HashMap<String, String>> jsonFeed(){
		
		ArrayList<HashMap<String, String>> categoryList = new ArrayList<HashMap<String, String>>();
		JSONArray category = null;
		/** Creating JSON Parser instance */
		JSONParser jParser = new JSONParser();
		JSONObject c;
		String id, title, thumbnail, video_id, player_url, length, author, topics, upload_time,view_count,description;
		HashMap<String, String> map;

		/** Getting JSON string from URL */
		JSONObject json = jParser.getJSONFromUrl(url);

		try {
			/** Getting Array of Contacts */
			category = json.getJSONArray(category_name);

			/** looping through All Contacts */
			for(int i = 0; i < category.length(); i++){
				c = category.getJSONObject(i);

				/** Storing each json item in variable */
				id = c.getString(TAG_ID);
				title = c.getString(TAG_TITLE);
				thumbnail = c.getString(TAG_THUMBNAIL);
				video_id = c.getString(TAG_VIDEOID);
				player_url = c.getString(TAG_PLAYER_URL);
				length = c.getString(TAG_LENGTH);
				author = c.getString(TAG_AUTHOR);
				topics = c.getString(TAG_TOPICS);
				upload_time = c.getString(TAG_UPLOADED_TIME);
				view_count = c.getString(TAG_VIEW_COUNT);		
				description = c.getString(TAG_DESCRIPTION);	
				
				/** Creating new HashMap */
				map = new HashMap<String, String>();

				/** adding each child node to HashMap key => value */
				map.put(TAG_ID, id);
				map.put(TAG_TITLE, title);
				map.put(TAG_THUMBNAIL, thumbnail);
				map.put(TAG_VIDEOID, video_id);
				map.put(TAG_PLAYER_URL, player_url);
				map.put(TAG_LENGTH, length);
				map.put(TAG_AUTHOR, author);
				map.put(TAG_TOPICS, topics);
				map.put(TAG_UPLOADED_TIME, upload_time);
				map.put(TAG_VIEW_COUNT, view_count);
				map.put(TAG_DESCRIPTION, description);

				/** Adding HashList to ArrayList */
				categoryList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return categoryList;
	}
	
	/** 
	 * Segregates and organizes Metadata content
	 * 
	 * @param 
	 * */
	public void setArrayContents(){
		
		ArrayList<HashMap<String, String>> category_list = jsonFeed();
		Video_attributes video_attr[] = new Video_attributes[category_list.size()];
		
		for(int i=0; i<category_list.size();i++){
			
			ImageProcessing ip = new ImageProcessing(category_list.get(i).get(TAG_THUMBNAIL).toString());
			
			video_attr[i] = new Video_attributes(
					category_list.get(i).get(TAG_TITLE).toString(),
					category_list.get(i).get(TAG_AUTHOR).toString(),
					ip.decodeImage(),
					category_list.get(i).get(TAG_ID).toString(),
					category_list.get(i).get(TAG_VIDEOID).toString(),
					category_list.get(i).get(TAG_PLAYER_URL).toString(),
					category_list.get(i).get(TAG_LENGTH).toString(),
					category_list.get(i).get(TAG_TOPICS).toString(),
					category_list.get(i).get(TAG_UPLOADED_TIME).toString(),
					category_list.get(i).get(TAG_VIEW_COUNT).toString(),
					category_list.get(i).get(TAG_DESCRIPTION).toString(),
					category_list.get(i).get(TAG_THUMBNAIL).toString());
		}
		v_attr = video_attr;
	}
	
	public Video_attributes[] getCategoryVideos(){
		return v_attr;
	}
}
