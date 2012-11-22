package com.video_browser_thesis.processing;

/**
 * @author Anshul Saini
 * @version 5
 */

import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageProcessing {

	Bitmap bitmap_image;
	String imageUrl;

	public ImageProcessing(String imageUrl) {
		this.imageUrl = imageUrl;
		fetchImage();
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		System.out.println("Height " + height);
		System.out.println("width " + width);
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	/** Decode bitmap with inSampleSize set
	 * @param reqWidth
	 * @param reqHeight
	 * @param is
	 * @return Bitmap
	 * */
	public static Bitmap decodeSampledBitmapFromResource(
			 int reqWidth, int reqHeight, InputStream is) {

		
		final BitmapFactory.Options options = new BitmapFactory.Options();
		
	    options.inJustDecodeBounds = false;
	    options.inPurgeable = true;
	    return 	BitmapFactory.decodeStream(is, null, options);
	
	}
	
	/**
	 * Fetches the image content from the provided URL
	 * 
	 * @param bitmap
	 *            Stores the decoded image retrieved from the provided url
	 * @return bitmap
	 * @see Bitmap Image
	 * */
	private void fetchImage() {
		Bitmap bitmap = null;
		try {
			InputStream lol = (InputStream) new URL(imageUrl)
			.getContent();
			System.out.println("LOL " + lol);
			bitmap =decodeSampledBitmapFromResource(100,100,lol);
			bitmap_image = bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		bitmap_image = bitmap;
	}

	/** 
	 * Returns the decoded Image 
	 * @param 
	 * @return	Bitmap
	 * @see Bitmap image*/
	public Bitmap decodeImage() {
		return bitmap_image;
	}
}
