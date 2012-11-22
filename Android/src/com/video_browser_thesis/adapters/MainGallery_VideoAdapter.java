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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.video_browser_thesis.activities.R;
import com.video_browser_thesis.elements.Video_attributes;

/**
 * Constructs a MainGallery Video Adapter which extends a Base Adapter
 * @author Anshul Saini 
 * @version 5
 */
public class MainGallery_VideoAdapter extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater=null;
	Video_attributes video_attr[] = null;

	public MainGallery_VideoAdapter(Activity a,Video_attributes[] v_attr) {
		activity = a;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.video_attr = v_attr;
	}

	public int getCount() {
		return video_attr.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * Inflates the gallery item with different types of views. 
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		ViewHolder holder;
		if(convertView==null){
			vi = inflater.inflate(R.layout.categorized_gallery_list, null);
			holder=new ViewHolder();
			holder.video_name=(TextView)vi.findViewById(R.id.video_name);
			holder.video_author=(TextView)vi.findViewById(R.id.video_author);
			holder.video_image=(ImageView)vi.findViewById(R.id.video_image);
			vi.setTag(holder);
		}
		else{
			holder=(ViewHolder)vi.getTag();
		}

		Video_attributes v_attr = video_attr[position];
		holder.video_name.setText(v_attr.video_title);
		holder.video_author.setText(v_attr.video_author);
		holder.video_image.setImageBitmap(v_attr.video_image);

		return vi;
	}
	
	/**
	 * Holds all the elements of the view created in its super class
	 * @param video_name	Video Frame point in TextView format
	 * @param video_author	Video author in TextView format
	 * @param video_author	Video image in ImageView format
	 */
	public static class ViewHolder{
		public TextView video_name;
		public TextView video_author;
		public ImageView video_image;
	}
}