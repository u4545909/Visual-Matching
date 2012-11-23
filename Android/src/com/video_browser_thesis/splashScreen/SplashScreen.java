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


package com.video_browser_thesis.splashScreen;
/**
 * @author Anshul Saini
 * @version 5
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.video_browser_thesis.activities.MainGalleryActivity;
import com.video_browser_thesis.activities.R;
/**
 * Main Splash Screen when the application is initiated
 * 
 * @param  _active  	Indicates if the splash screen is present
 * @param  _splashTime 	The display time of the splash screen image in milliseconds
 * @return      	
 * @see    Image
 */
public class SplashScreen extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 4000;

	/** 
	 * Called when the activity is first created. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		/** 
		 * Threads for displaying the splash screen
		 */
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while(_active && (waited < _splashTime)) {
						sleep(100);
						if(_active) {
							waited += 100;
						}
					}
				} catch(InterruptedException e) {

				} finally {

					startActivity(new Intent(SplashScreen.this, MainGalleryActivity.class));
					finish();
				}
			}
		};
		splashTread.start();
	}
}
