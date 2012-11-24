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
import com.video_browser_thesis.elements.KeyFrame_attributes;

/**
 * Constructs a CrossBrowserGallery Video Adapter which extends a Base Adapter
 * @author Anshul Saini 
 * @version 5
 */
public class CrossBrowser_GalleryAdapter extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater=null;
	KeyFrame_attributes keyframe_attr[] = null;

	public CrossBrowser_GalleryAdapter(Activity a,KeyFrame_attributes[] keyframe_attr) {
		activity = a;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.keyframe_attr = keyframe_attr;
	}

	public int getCount() {
		return keyframe_attr.length;
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
			vi = inflater.inflate(R.layout.cross_browser_galleryitem, null);
			holder=new ViewHolder();
			holder.video_image=(ImageView)vi.findViewById(R.id.cross_browsers_gallery_img);
			holder.video_length=(TextView)vi.findViewById(R.id.cb_gallery_video_length);
			
			vi.setTag(holder);
		}
		else{
			holder=(ViewHolder)vi.getTag();
		}

		KeyFrame_attributes v_attr = keyframe_attr[position];
		holder.video_length.setText(v_attr.frame_point);
		
		if(v_attr.video_img != null){
			holder.video_image.setImageBitmap(v_attr.video_img);
		}

		return vi;
	}

	/**
	 * Holds all the elements of the view created in its super class
	 * @param video_length	Video length in TextView format
	 * @param video_image	Video image in ImageView format
	 */
	public static class ViewHolder{
		public ImageView video_image;
		public TextView video_length;
	}
}
