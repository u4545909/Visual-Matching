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
import android.graphics.Color;

/**
 * KeyFrame attributes 
 * @author Anshul Saini
 * @version 5
 * @param image_source
 * @param frame_point
 * @param video_img
 *
 */
public class KeyFrame_attributes{
	public String image_source;
	public String frame_point;
	public Bitmap video_img;
	public int background_color = Color.TRANSPARENT;
	
	public KeyFrame_attributes(String image_source,String frame_point, Bitmap video_img){
		this.image_source = image_source;
		this.frame_point = frame_point;
		this.video_img = video_img;
	}
	
	public void setBackgroundColor(){
		background_color = Color.YELLOW;
	}
}
