
package com.example.menutranslator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.menutranslator.R;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;


public class CropImage extends MainActivity {
	
	private Cropper mImageView;
	private Bitmap mBitmap;		
	//private String mPath, mDataDir;
	//private final String mFileName = "OCRimage.jpg";
	private Uri srcUri = null;	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);               
        setContentView(R.layout.cropimage);
        
        //Checking if SD card is still available
        if(this.initPath() == true){
        
        	//Getting the Bitmap (a bit complicated, but this way should avoid memory problems)               
        	Intent intent = this.getIntent();
        	String action = intent.getAction();
        	String type = intent.getType();
        
        	//Code is a bit complicated, because other applications can send data
        	if (Intent.ACTION_SEND.equals(action) && type != null) {
        		//Copying the image from gallery to the application path.        		            	
    			srcUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
    			String srcPath = Utilities.getPathFromUri(srcUri, this);
    			try {
					Utilities.copyFile(new File(srcPath), new File(mPath));
				} catch (IOException e) {
					//showDialogFragment(COPY_ERR_DIALOG, "copy_err_dialog");					
					e.printStackTrace();
				}
        	}              	
        
        	mBitmap = Utilities.createBitmapFromPath(mPath, Utilities.calculateSampleSize(mPath));
        	mImageView = (Cropper)findViewById(R.id.cropper);
        	mImageView.setImageBitmap(mBitmap);
        }
        else{
        	//Show error dialog
        	//showDialogFragment(SD_ERR_DIALOG, "sd_err_dialog");
        }        
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {    	
    	super.onConfigurationChanged(newConfig);
    	mImageView.setFirstStart(true);
    }
    
    // Button events
    public void onCancel (View v){
    	cleanMemory();
    	this.finish();
    }
    
    public void onSave (View v){
    	new SaveFullImage().execute("");
    }
    
    public Bitmap getBitmap(){
    	return mBitmap;
    }
    
    public int getOrigWidth(){
    	return mBitmap.getWidth();
    }
    
    public int getOrigHeight(){
    	return mBitmap.getHeight();
    }
    
    private void cleanMemory(){
    	// Unbinding drawable from ImageView
    	if(mImageView.getDrawable() != null)
    		mImageView.getDrawable().setCallback(null);
    	
    	// Telling DVM to free memory
    	if(mBitmap != null){    		
    		mBitmap.recycle();
    		mBitmap = null;
    		System.gc();
    	}   	
    }
    
    private void showImage(){
    	// Showing image for OCR
    	Intent intent = new Intent(this, OCR_result.class);
    	//intent.putExtra("PATH", mPath);
    	//intent.putExtra("DATA", mDataDir);
    	startActivity(intent);    	    	
    	this.finish();
    }
    
  
    /*
    ----------------------------- LIFE CYCLE METHODS -----------------------------
    */
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onPause() {    	
        super.onPause();        
    }
    
    @Override
    protected void onResume() {
        super.onResume();        
    }
    
    @Override
    protected void onStop(){
       super.onStop();       
    }
    
    @Override
    protected void onDestroy(){    	
    	//Log.w("Crop", "onDestroy");
    	cleanMemory();
    	super.onDestroy();
    }
    
    /*---------- Private inner class to save full size image on file ----------*/
    private class SaveFullImage extends AsyncTask<String, Integer, String> {
        
    	private boolean success = false;    	
    	
    	@Override
        protected String doInBackground(String... sUrl) {
    		
    		// Cropping the real image
    		mBitmap = Bitmap.createBitmap(mBitmap, mImageView.getCropX(), mImageView.getCropY(), mImageView.getCropWidth(), mImageView.getCropHeight());    		
    		try{   	    	
    	    	FileOutputStream out = new FileOutputStream(new File(mPath));
    	    	mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);  	    	
				success = true;
			}
			catch (Exception e){				
				success=false;
				e.printStackTrace();
			}           
            return null;
        }
    	
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
           // showDialogFragment(LOADING_DIALOG, "loading_dialog");                    	
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);            
        }
        
        @Override
        protected void onPostExecute(String result) {
        	super.onPostExecute(result);        	
        	if (success){
        	//	removeDialogFragment("loading_dialog");        		  
        		cleanMemory();
        		showImage();
        	}
        	else{
        		//removeDialogFragment("loading_dialog");
        		//showDialogFragment(COPY_ERR_DIALOG, "copy_err_dialog");
        	}			
        }
    }

}
