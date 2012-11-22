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


package com.video_browser_thesis.elements;

import android.graphics.Bitmap;
/**
 * Video_attributes 
 * @author Anshul Saini
 * @version 5
 */
public class Video_attributes{
	public String video_title;
	public String video_author;
	public Bitmap video_image;
	public String thumbnail;
	public String tag_id;
	public String video_id;
	public String player_url;
	public String length;
	public String topics;
	public String uploaded_time;
	public String view_count;
	public String description;
	
	public Video_attributes(){
		super();
	}
	
	public Video_attributes(
			String video_title,
			String video_author,
			Bitmap video_image, 
			String tag_id, 
			String video_id,
			String player_url,
			String length,
			String topics,
			String uploaded_time,
			String view_count,
			String description,
			String thumbnail){
		super();
		this.tag_id = tag_id;
		this.video_title = video_title;
		this.video_author = video_author;
		this.video_image = video_image;
		this.video_id = video_id;
		this.player_url = player_url;
		this.length = length;
		this.topics = topics;
		this.uploaded_time = uploaded_time;
		this.view_count = view_count;
		this.description = description;
		this.thumbnail = thumbnail;
	}
}
