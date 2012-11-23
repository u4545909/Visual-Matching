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


package com.video_browser_thesis.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.video_browser_thesis.activities.R;
import com.video_browser_thesis.elements.Video_attributes;

/**
 * Constructs a Keyframe Video Adapter which extends a Custom List Adapter
 * @author Anshul Saini 
 * @version 5
 */
public class Keyframe_ListAdapter extends ArrayAdapter<Video_attributes>{

	Context context;
	static int layoutResourceId = R.layout.keyframe_list_layout;    
	Video_attributes video_attr[] = null;

	public Keyframe_ListAdapter(Context context, Video_attributes[] video_attr) {
		// TODO Auto-generated constructor stub
		super(context, layoutResourceId, video_attr);
		this.context = context;
		this.video_attr = video_attr;
	}

	/**
	 * Inflates the gallery item with different types of views. 
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		VideoListHolder holder = null;

		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new VideoListHolder();
			holder.video_title = (TextView)row.findViewById(R.id.video_title);
			holder.video_description = (TextView)row.findViewById(R.id.video_description);
			holder.uploaded_time = (TextView)row.findViewById(R.id.video_uploaded_timestamp);
			holder.view_count = (TextView)row.findViewById(R.id.view_count);
			holder.video_author =  (TextView)row.findViewById(R.id.video_author);
			holder.video_length = (TextView)row.findViewById(R.id.video_length);
			holder.video_img = (ImageView)row.findViewById(R.id.video_keyframe_image);

			row.setTag(holder);
		}
		else{
			holder = (VideoListHolder)row.getTag();
		}

		Video_attributes video_attributes = video_attr[position];
		holder.video_title.setText(video_attributes.video_title);
		holder.video_author.setText(video_attributes.video_author);
		holder.uploaded_time.setText(video_attributes.uploaded_time);
		holder.view_count.setText(video_attributes.view_count);
		holder.view_count.setText(video_attributes.view_count);
		holder.video_length.setText(video_attributes.length);
		holder.video_description.setText(video_attributes.description);

//		if(video_attributes.video_image!=null){
			holder.video_img.setImageBitmap(video_attributes.video_image);
//		}

		return row;
	}

	/**
	 * Holds all the elements of the view created in its super class
	 * @param video_title		Video title in TextView format
	 * @param video_description	Video description in TextView format
	 * @param uploaded_time		Video image in TextView format
	 * @param view_count		Video image in TextView format
	 * @param video_author		Video image in TextView format
	 * @param video_length		Video image in TextView format
	 * @param video_img			Video image in ImageView format
	 */
	static class VideoListHolder
	{
		TextView video_title;
		TextView video_description;
		TextView uploaded_time;
		TextView view_count;
		TextView video_author;
		TextView video_length;
		ImageView video_img;
	}
}
