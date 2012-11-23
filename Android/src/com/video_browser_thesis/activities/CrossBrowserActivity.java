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

package com.video_browser_thesis.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;

import com.video_browser_thesis.adapters.CircularListAdapter;
import com.video_browser_thesis.adapters.CrossBrowser_GalleryAdapter;
import com.video_browser_thesis.adapters.CrossBrowser_ListAdapter;
import com.video_browser_thesis.elements.KeyFrame_attributes;
import com.video_browser_thesis.processing.CB_GalleryProcessing;
import com.video_browser_thesis.processing.CB_Processing;
import com.video_browser_thesis.processing.ImageProcessing;

/**
 * Displays all the matching key frames of a video
 * @author Anshul Saini
 * @version 5
 */

public class CrossBrowserActivity extends Activity{

	private Bundle cb_bundle;
	public static Boolean i = false;

	private  Gallery crossbrowser_gallery;
	private DisplayMetrics metrics = new DisplayMetrics();
	private KeyFrame_attributes[] keyframe_attr;
	private KeyFrame_attributes[] keyframe_gall;
	protected boolean _active = true;
	protected int _splashTime = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cross_browser);

		/**
		 *  Retrieves data sent by the previous activity
		 */
		cb_bundle = getIntent().getExtras();
		keyframe_attr = null;

		/** 
		 * Setting up the gallery view positioning
		 * */
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		crossbrowser_gallery = (Gallery)findViewById(R.id.cross_browser_gallery);

		/** Retrieving processing information from URL*/
		CB_Processing cb_p = new CB_Processing(cb_bundle.getString("video_id"));
		
		keyframe_attr = cb_p.getKeyFrameArray();

		final ListView crossbrowser_list = (ListView) findViewById(R.id.cross_browser_listview);
		final ImageView selected_img = (ImageView)findViewById(R.id.selected_matching_keyframe);

		/** Initialising the list adapter with the contents retrieved */
		CrossBrowser_ListAdapter cb_listAdapter = new CrossBrowser_ListAdapter(this, keyframe_attr);
		final CircularListAdapter circularListAdapter = new CircularListAdapter(cb_listAdapter);
		crossbrowser_list.setAdapter(circularListAdapter);
		CB_GalleryProcessing cb_gall1;
		int arr_len = 0;

		/** Identifying if there are any matching key frames for all reference key frames*/
		for(int i=0;i<keyframe_attr.length;i++){
			cb_gall1 = new CB_GalleryProcessing(keyframe_attr[i].image_source);
			arr_len = 0;
			arr_len = cb_gall1.getArrayLength();

			if (arr_len > 0){
				keyframe_attr[i].setBackgroundColor();
			}
		}


		crossbrowser_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				int tempPos = position;
				if (position == keyframe_attr.length) position = 0;
				if (position > keyframe_attr.length) position = position%keyframe_attr.length;			

				/** This is repositioning the list item which is clicked to fit into the frame*/
				CB_GalleryProcessing cb_gall = new CB_GalleryProcessing(keyframe_attr[position].image_source);
				keyframe_gall = cb_gall.getKeyFrameArray();			
				int positionsToMove = 0;
				if (tempPos > crossbrowser_list.getFirstVisiblePosition()){
					positionsToMove = tempPos - crossbrowser_list.getFirstVisiblePosition()-2;
					crossbrowser_list.setSelection(crossbrowser_list.getFirstVisiblePosition() + positionsToMove);
				}
				else if (tempPos < crossbrowser_list.getFirstVisiblePosition()){
					positionsToMove = tempPos - crossbrowser_list.getFirstVisiblePosition()-2;
					crossbrowser_list.setSelection(crossbrowser_list.getFirstVisiblePosition() + positionsToMove);
				}else if(tempPos == 1){
					positionsToMove = keyframe_gall.length-2;
					crossbrowser_list.setSelection(positionsToMove);
				}

				/** As this is a circular list adapter, the items should be re-referenced*/
				if(keyframe_gall.length != 0){
					crossbrowser_gallery.setAdapter(new CrossBrowser_GalleryAdapter(CrossBrowserActivity.this,keyframe_gall));
				}else{
					KeyFrame_attributes[] defaultList = new KeyFrame_attributes[5];
					for(int i=0;i<5;i++){
						defaultList[i] = new KeyFrame_attributes(null,"",null);
					}
					crossbrowser_gallery.setAdapter(new CrossBrowser_GalleryAdapter(CrossBrowserActivity.this, defaultList));
					crossbrowser_gallery.setSelection(1);
				}

				crossbrowser_gallery.setSelection(1);
				
				crossbrowser_gallery.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position_gall,
							long arg3) {
						// TODO Auto-generated method stub
						
						String temp = keyframe_gall[position_gall].image_source.substring(53, keyframe_gall[position_gall].image_source.length());
						String temp_video_id = temp.substring(0, temp.indexOf("/"));
						String temp_base_url = "http://www.youtube.com/watch?v=";
						String full_url = temp_base_url + temp_video_id;
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(full_url)));
					}
				});
			

				/**
				 * Setting Selected Video 
				 */
				ImageProcessing processing_selected_img = new ImageProcessing(keyframe_attr[position].image_source);
				selected_img.setImageBitmap(processing_selected_img.decodeImage());
			}
		});	
		

		/** 
		 * Sets the default content of the gallery
		 */
		KeyFrame_attributes[] defaultList = new KeyFrame_attributes[5];
		for(int i=0;i<5;i++){
			defaultList[i] = new KeyFrame_attributes(null,"",null);
		}
		crossbrowser_gallery.setAdapter(new CrossBrowser_GalleryAdapter(this, defaultList));
		crossbrowser_gallery.setSelection(1);
	}

	/**
	 * Ends the current activity and returns to the previous activity
	 */
	public void onListView_btn_click(View v){
		finish();
	}
	
	/**
	 * Ends the current activity and returns to the main activity
	 */
	public void onHomeButtonClick(View v){
		i = true;
		finish();
	}

	/** 
	 * Retrieve value from the bundle
	 * @param
	 * @return String*/
	public String getValueFromBundle(){
		return cb_bundle.getString("video_id");
	}
}
