

package com.example.menutranslator;

import java.io.File;
import java.io.IOException;

import com.example.menutranslator.R;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.Toast;

public class HomeActivity extends MainActivity{
	
	//private String mPath, mDataDir;
	//private final String mFileName = "OCRimage.jpg";
	private final int FROM_CAMERA = 0, FROM_GALLERY = 1;
	//private Uri srcUri = null;
	private ImageSwitcher fromCamera, fromGallery;
	//private final String TAG = "AndrOCR.java";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        
        
        fromCamera = (ImageSwitcher)findViewById(R.id.imageSwitcher1camera);
        fromGallery = (ImageSwitcher)findViewById(R.id.ImageSwitcher01Gallery);
        
        if(this.initPath() == false){
        	Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        	//If SD not available, disable buttons        	
        	fromCamera.setEnabled(false);        	
        	fromCamera.setBackgroundResource(R.drawable.holo_background_dark);
        	fromGallery.setEnabled(false);
        	fromGallery.setBackgroundResource(R.drawable.holo_background_dark);  
        }
        
       
        // Checking for camera
        PackageManager pm = this.getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
        	fromCamera.setEnabled(false);
        	fromCamera.setBackgroundResource(R.drawable.holo_background_dark);
        }

        
    }  
	public void settingsclick(View v)
	{
		Toast.makeText(getApplicationContext(), "Info",Toast.LENGTH_SHORT).show();
		 openOptionsMenu();
	}
	
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
}
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	//Toast.makeText(getApplicationContext(), "option1",Toast.LENGTH_SHORT).show();
	Intent i=new Intent(this,about.class);
	startActivity(i);
	return super.onOptionsItemSelected(item);
}
    
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        boolean cam = fromCamera.isEnabled();
        boolean gal = fromGallery.isEnabled();
    	super.onConfigurationChanged(newConfig);        
        setContentView(R.layout.activity_main);
        fromCamera = (ImageSwitcher)findViewById(R.id.imageSwitcher1camera);
        fromGallery = (ImageSwitcher)findViewById(R.id.ImageSwitcher01Gallery);
        if (cam == false){
        	fromCamera.setEnabled(false);        	
        	fromCamera.setBackgroundResource(R.drawable.holo_background_dark);
        }
        if (gal == false){
        	fromGallery.setEnabled(false);
        	fromGallery.setBackgroundResource(R.drawable.holo_background_dark);
        }
    }
    
    //Method to be called when "From Camera" button is clicked
    public void cameraaction(View v){
    	Uri targetUri = Uri.fromFile(new File(mPath));
    	Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);    	
    	intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
    	startActivityForResult(intent, FROM_CAMERA);
    }
    
    //Method to be called when "From Gallery" button is clicked
    public void galleryaction(View v){
    	Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK);		
		startActivityForResult(Intent.createChooser(intent, ""), FROM_GALLERY);
    }
    
    //This method runs when the Camera Activity or the Gallery Activity are ended. 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	
    	//Log.i(TAG, "Request Code: "+requestCode);
    	//Log.i(TAG, "Result Code: "+resultCode);   	
    	
    	if (requestCode == FROM_CAMERA){
    		
        	switch (resultCode){
        	
        	case 0:
            	Toast.makeText(this, "close", Toast.LENGTH_SHORT).show();
            	break;
            	
        	case -1:
        		//Image from camera is already stored in AndrOCR path
        		Intent intent = new Intent (this, CropImage.class);
        		//intent.putExtra("PATH", mPath);
            	//intent.putExtra("DATA", mDataDir);
            	startActivity(intent);  		
                                       		
        	}
    	} 
    	
    	if (requestCode == FROM_GALLERY) {
    		
    		//Result from gallery
    		switch (resultCode){
    		
        	case 0:
            	Toast.makeText(this, "close", Toast.LENGTH_SHORT).show();
            	break;
            	
        	case -1:        		
        		//Copying the image from gallery to the application path.        		            	
        		Uri srcUri = data.getData();
        		String srcPath = Utilities.getPathFromUri(srcUri, this);
        		try {
					Utilities.copyFile(new File(srcPath), new File(mPath));
				} catch (IOException e) {
								
					e.printStackTrace();
				}
        		Intent intent = new Intent (this, CropImage.class);
        		//intent.putExtra("PATH", mPath);
            	//intent.putExtra("DATA", mDataDir);
            	startActivity(intent);        		
        	}
    	}   	
    	
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
    
}