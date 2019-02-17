

package com.example.menutranslator;

import java.io.File;

import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity {
	
	protected final int ABOUT_ID = 0;
	protected String mPath, mDataDir;
	protected final String mFileName = "OCRimage.jpg";
	
  
    
    //Methods to correctly initialize paths
    public boolean initPath(){
    	boolean allOk = false;
    	// The application needs to work with SD card. The code below checks if it is available.        
        if (Utilities.checkExternalStorage() == true && this.getExternalFilesDir(null) != null){
        	mDataDir = this.getExternalFilesDir("data").getAbsolutePath();        	
        	mPath = this.getExternalFilesDir("images").getAbsolutePath() + File.separator + mFileName;
        	allOk = true;
        	//Log.i("Utilities.java", mDataDir);
        	//Log.i("This", mDataDir);
        	//Toast.makeText(this, "Picture Directory: " + mPath, Toast.LENGTH_LONG).show();
        }    	
    	return allOk;
    }

}
