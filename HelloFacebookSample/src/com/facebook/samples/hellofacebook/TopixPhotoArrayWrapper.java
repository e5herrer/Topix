package com.facebook.samples.hellofacebook;

import java.io.Serializable;

public class TopixPhotoArrayWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TopixPhoto[] topPhotos;

	   public TopixPhotoArrayWrapper(TopixPhoto [] t) {
	      this.topPhotos = t; 
	   }

	   public TopixPhoto[] getTopPhotos(){
		   return this.topPhotos;
	   }

	
}
