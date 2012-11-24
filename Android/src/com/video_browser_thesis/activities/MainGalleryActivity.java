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
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import com.video_browser_thesis.adapters.MainGallery_VideoAdapter;
import com.video_browser_thesis.elements.Video_attributes;
import com.video_browser_thesis.processing.MainGalleryProcessing;

/**
 * Displays all the videos in various categories
 * @author Anshul Saini
 * @version 5
 */
public class MainGalleryActivity extends Activity {
	private  Gallery galleryView1,galleryView2,galleryView3, galleryView4,galleryView5;
	private DisplayMetrics metrics = new DisplayMetrics();

	private static final String TAG_CATEGORY01 = "ray_william_johnson";
	private static final String TAG_CATEGORY02 = "golden_voice";
	private static final String TAG_CATEGORY03 = "zinedine_zidane";
	private static final String TAG_CATEGORY04 = "charlie_bit_my_finger";	
	private static final String TAG_CATEGORY05 = "samsung_galaxy_s3_ad";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isConnected(this)){
			setContentView(R.layout.activity_categorized_video_gallery);

			getWindowManager().getDefaultDisplay().getMetrics(metrics);

			galleryView1 = (Gallery)findViewById(R.id.galleryid1);
			galleryView2 = (Gallery)findViewById(R.id.galleryid2);	
			galleryView3 = (Gallery)findViewById(R.id.galleryid3);
			galleryView4 = (Gallery)findViewById(R.id.galleryid4);
			galleryView5 = (Gallery)findViewById(R.id.galleryid5);

			TextView c1 = (TextView)findViewById(R.id.category01);
			TextView c2 = (TextView)findViewById(R.id.category02);
			TextView c3 = (TextView)findViewById(R.id.category03);
			TextView c4 = (TextView)findViewById(R.id.category04);
			TextView c5 = (TextView)findViewById(R.id.category05);

			/** Processing information that is retrieved from different URL for different categories of videos*/
			MainGalleryProcessing mg_p1 = new MainGalleryProcessing(TAG_CATEGORY01);
			MainGalleryProcessing mg_p2 = new MainGalleryProcessing(TAG_CATEGORY02);
			MainGalleryProcessing mg_p3 = new MainGalleryProcessing(TAG_CATEGORY03);
			MainGalleryProcessing mg_p4 = new MainGalleryProcessing(TAG_CATEGORY04);
			MainGalleryProcessing mg_p5 = new MainGalleryProcessing(TAG_CATEGORY05);
			
			/** Instantiating different objects for populating it with different items for different categories in different galleries*/
			final Video_attributes[] v_attr_01 = mg_p1.getCategoryVideos();
			final Video_attributes[] v_attr_02 = mg_p2.getCategoryVideos();
			final Video_attributes[] v_attr_03 = mg_p3.getCategoryVideos();
			final Video_attributes[] v_attr_04 = mg_p4.getCategoryVideos();
			final Video_attributes[] v_attr_05 = mg_p5.getCategoryVideos();

			/** Setting the list items of the gallery for each category*/
			galleryView1.setAdapter(new MainGallery_VideoAdapter(this,v_attr_01));
			galleryView2.setAdapter(new MainGallery_VideoAdapter(this,v_attr_02));
			galleryView3.setAdapter(new MainGallery_VideoAdapter(this,v_attr_03));
			galleryView4.setAdapter(new MainGallery_VideoAdapter(this,v_attr_04));
			galleryView5.setAdapter(new MainGallery_VideoAdapter(this,v_attr_05));

			/** Setting the default selection item*/
			galleryView1.setSelection(2);
			galleryView2.setSelection(2);
			galleryView3.setSelection(2);
			galleryView4.setSelection(2);
			galleryView5.setSelection(2);

			/** Rendering text for UI purposes*/
			c1.setText(TAG_CATEGORY01.replace("_"," ").toUpperCase());
			c2.setText(TAG_CATEGORY02.replace("_"," ").toUpperCase());
			c3.setText(TAG_CATEGORY03.replace("_"," ").toUpperCase());
			c4.setText(TAG_CATEGORY04.replace("_"," ").toUpperCase());
			c5.setText(TAG_CATEGORY05.replace("_"," ").toUpperCase());

			galleryView1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1, int position,
						long arg3) {
					// TODO Auto-generated method stub

					passingBundles(v_attr_01, position);
				}
			});

			galleryView2.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1, int position,
						long arg3) {
					// TODO Auto-generated method stub

					passingBundles(v_attr_02, position);
				}
			});

			galleryView3.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1, int position,
						long arg3) {
					// TODO Auto-generated method stub

					passingBundles(v_attr_03, position);
				}
			});

			galleryView4.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1, int position,
						long arg3) {
					// TODO Auto-generated method stub

					passingBundles(v_attr_04, position);
				}
			});

			galleryView5.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1, int position,
						long arg3) {
					// TODO Auto-generated method stub

					passingBundles(v_attr_05, position);
				}
			});
		}else{
			Toast.makeText(this, "Please ensure that the internet is switched on!", Toast.LENGTH_LONG).show();
			try{
				Thread.sleep(2000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			finish();
		}
	}

	/** 
	 * Creates a bundle of data to pass it to the another activity
	 * 
	 * @param galleryIntent New Intent
	 * @param extras		The bundle which carries all data from the galleryIntent
	 * @return
	 * */
	private void passingBundles(Video_attributes[] v_arr,int position){
		Intent galleryIntent = new Intent(getApplicationContext(),com.video_browser_thesis.activities.KeyframeListActivity.class);
		Bundle extras = new Bundle();

		extras.putString("video_title", v_arr[position].video_title);
		extras.putString("video_description", v_arr[position].description);
		extras.putString("video_upload_time", v_arr[position].uploaded_time);
		extras.putString("video_view_count", v_arr[position].view_count);
		extras.putString("video_author", v_arr[position].video_author);
		extras.putString("video_img", v_arr[position].thumbnail);
		extras.putString("video_length", v_arr[position].length);
		extras.putString("video_id", v_arr[position].video_id);
		extras.putString("video_url",v_arr[position].player_url);
		galleryIntent.putExtras(extras);
		startActivity(galleryIntent);
	}

	/**
	 * Does nothing onHomeButton click as it is already in the home activity
	 * @param v
	 */
	public void onHomeButtonClick(View v){
		//Do Nothing
	}

	/** Checks for Internet connection availability*/
	public boolean isConnected(Context context) {

		ConnectivityManager cm = (ConnectivityManager)
		context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork =
			cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null &&  wifiNetwork.isConnectedOrConnecting()) {
			return true;
		}

		NetworkInfo mobileNetwork =
			cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnectedOrConnecting()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
			return true;
		}

		return false;
	}
}