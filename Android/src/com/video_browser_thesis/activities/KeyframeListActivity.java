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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.video_browser_thesis.adapters.Keyframe_ListAdapter;
import com.video_browser_thesis.elements.Video_attributes;
import com.video_browser_thesis.processing.ImageProcessing;
import com.video_browser_thesis.processing.KeyFrameList_Processing;

/**
 * Displays all the key frames of a video
 * @author Anshul Saini
 * @version 5
 */
public class KeyframeListActivity extends Activity{
	
	private Bundle b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keyframe);
		
		/**
		 * Setting the parameters for the reference frame
		 */
		b = getIntent().getExtras();
		TextView video_title = (TextView)findViewById(R.id.video_title);
		video_title.setText(b.getString("video_title"));
		
		TextView video_description = (TextView)findViewById(R.id.video_description);
		video_description.setText(b.getString("video_description"));
		
		TextView video_upload_time = (TextView)findViewById(R.id.video_uploaded_timestamp);
		video_upload_time.setText(b.getString("video_upload_time"));
		
		TextView view_count = (TextView)findViewById(R.id.view_count);
		view_count.setText(b.getString("video_view_count"));
		
		TextView video_author = (TextView)findViewById(R.id.author);
		video_author.setText(b.getString("video_author"));
		
		TextView video_length = (TextView)findViewById(R.id.video_length);
		video_length.setText(b.getString("video_length"));
		
		ImageView video_thumbnail = (ImageView)findViewById(R.id.video_keyframe_image);
		ImageProcessing ip = new ImageProcessing(b.getString("video_img"));
		video_thumbnail.setImageBitmap(ip.decodeImage());
		
		KeyFrameList_Processing kf_processing = new KeyFrameList_Processing(b.getString("video_id"));
		final Video_attributes[] matching_videoList = kf_processing.getMatchingVideos();

		ListView keyframe_list = (ListView) findViewById(R.id.matching_keyframes_list);
		keyframe_list.setAdapter(new Keyframe_ListAdapter(this,matching_videoList));
		keyframe_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String temp_base_url = "http://www.youtube.com/watch?v=";
				String full_url = temp_base_url + b.getString("video_id");
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(full_url)));
			}
		});
	}
	
	/**
	 * Ends the current activity and returns to the previous activity
	 */
	public void onCrossBrowser_btn_click(View v){
		Intent cb_intent = new Intent(this,com.video_browser_thesis.activities.CrossBrowserActivity.class);
		cb_intent.putExtras(b);
		startActivity(cb_intent);
	}
	
	/** When the activity is resumed */
	@SuppressWarnings("static-access")
	public void onResume(){
		super.onResume();
		CrossBrowserActivity cb_i = new CrossBrowserActivity();
		if (cb_i.i == true){
			cb_i.i = false;
			finish();
		}
	}
	
	/**
	 * Ends the current activity and returns to the main activity
	 */
	public void onHomeButtonClick(View v){
		finish();
	}
}
