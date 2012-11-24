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
import com.video_browser_thesis.elements.KeyFrame_attributes;

/**
 * Constructs a Keyframe KeyFrame Adapter which extends a Custom List Adapter
 * @author Anshul Saini 
 * @version 5
 */
public class CrossBrowser_ListAdapter extends ArrayAdapter<KeyFrame_attributes>{

	Context context;
	static int layoutResourceId = R.layout.cross_browser_listitem;    
	KeyFrame_attributes keyframe_attr[] = null;

	public CrossBrowser_ListAdapter(Context context, KeyFrame_attributes[] keyframe_attr) {
		super(context, layoutResourceId,keyframe_attr);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.keyframe_attr = keyframe_attr;
	}

	/**
	 * Inflates the gallery item with different types of views. 
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		CrossBrowserListHolder holder = null;

		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new CrossBrowserListHolder();
			holder.video_img = (ImageView)row.findViewById(R.id.cross_browsers_img);
			holder.video_frame_point = (TextView)row.findViewById(R.id.cb_video_length);
			holder.video_background_color = (View) row.findViewById(R.id.cb_list);
			
			row.setTag(holder);
		}
		else{
			holder = (CrossBrowserListHolder)row.getTag();
		}

		KeyFrame_attributes kf_attributes = keyframe_attr[position];
		holder.video_img.setImageBitmap(kf_attributes.video_img);
		holder.video_frame_point.setText(kf_attributes.frame_point);
		holder.video_background_color.setBackgroundColor(kf_attributes.background_color);
		
		return row;
	}

	/**
	 * Holds all the elements of the view created in its super class
	 * @param video_frame_point	Video frame point in TextView format
	 * @param video_img			Video image in ImageView format
	 */
	static class CrossBrowserListHolder{
		ImageView video_img;
		TextView video_frame_point;
		View video_background_color;
	}
}