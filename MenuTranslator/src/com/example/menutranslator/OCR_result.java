package com.example.menutranslator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import com.example.menutranslator.R;
import com.googlecode.tesseract.android.TessBaseAPI;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

public class OCR_result extends MainActivity {

	public ImageView IMGshow,dish;
   //EditText _field;
	AutoCompleteTextView _field;
	Button btn;
	private Bitmap mBitmap;
	ProgressDialog mProgressDialog;
	String a,lang="eng";
	 public static final String DATA_PATH = Environment
				.getExternalStorageDirectory().toString() + "/RestaurantMenuExpert/";
	 String dishName;
	 
	 //Database
	 DatabaseClass db;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ocr_work);
		//set home
		ActionBar ab=getActionBar();
		ab.setHomeButtonEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
		
		
		//set images and data
		IMGshow=(ImageView)findViewById(R.id.imageView_show);
		dish=(ImageView)findViewById(R.id.imageView_dish);
		//btn=(Button) findViewById(R.id.button1);
		//_field=(TextView)findViewById(R.id.textView1);
	   _field=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        
        //copy lang file 
		setTessData();
		//
		 if(this.initPath() == true){
	        	
	                
	        	//Log.i(TAG, mTrainedDataPath);
	        
	        	// Creating a bitmap from the image path 
	        	Log.w("checkingdata", "Sample Size: " + Utilities.calculateSampleSize(mPath));
	        	mBitmap = Utilities.createBitmapFromPath(mPath, Utilities.calculateSampleSize(mPath));
	        
		
		 }
		IMGshow.setImageBitmap(mBitmap);
		
		//imageProcess(mBitmap);
		 doOCR(mBitmap);
		 
		 
		 
		 
		 _field.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				showImage();
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		 

		    dish.setOnClickListener(new OnClickListener() {
			    
			      
		   	  public void onClick(View arg0){
		   		     		
		 
		   		String a= "https://www.google.com.pk/search?ie=ISO-8859-1&q="+dishName+"&btnG=Search";

				  Uri uri = Uri.parse(a);
				  Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				  startActivity(intent);
		   
		   	  }});
		    
		    
		    String[] list ={"Biryani","Tikka","Chicken Roll","Chicken Soup","Chicken Chaow Mein","Chicken Behari Boti","Chicken Tikka","Chinese Rice","Malai Tikaa","Seekh kabab","Zinger Burger","Chicken karhai","Vegetable Soup","handi" };  
		    
		    
		    ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,list);
		    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		    
		   // text1.setThreshold(1);
		    _field.setAdapter(adapter);
		    
	}
	
	

	private void doOCR(final Bitmap bitmap) {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(this, "Processing",
					"Doing OCR...", true);
		}
		else {
			mProgressDialog.show();
		}
		
		new Thread(new Runnable() {
			public void run() {

				 TessBaseAPI tessap=new TessBaseAPI();
				 tessap.setDebug(true);
				 tessap.init(DATA_PATH, lang);
				 tessap.setImage(bitmap);//BitmapFactory.decodeFile(imagepath));
				 final String result = tessap.getUTF8Text();
					
					tessap.end();
					

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (result != null && !result.equals("")) {
							_field.setText(result);
							showImage();
						}

						mProgressDialog.dismiss();
					}

				});

			};
		}).start(); 
	}
	
	//another ocr method
	 public void imageProcess(Bitmap bmp )
	 {
		 //copy lang file 
		//setTessData();
		//use Tessrect library
		 TessBaseAPI tessap=new TessBaseAPI();
		 tessap.setDebug(true);
		 tessap.init(DATA_PATH, lang);
		tessap.setImage(bmp);//BitmapFactory.decodeFile(imagepath));
		 String recognizedText = tessap.getUTF8Text();
			
			tessap.end();
			_field.setText(recognizedText);
	 }
	
	
	//COpy lang files
	 private void setTessData(){
	    	
		 String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

			for (String path : paths) {
				File dir = new File(path);
				if (!dir.exists()) {
					if (!dir.mkdirs()) {
						Log.e("", "ERROR: Creation of directory " + path + " on sdcard failed");
						return;
					} else {
						Log.e("", "Created directory " + path + " on sdcard");
					}
				}

			}
			
			// lang.traineddata file with the app (in assets folder)
			// You can get them at:
			// http://code.google.com/p/tesseract-ocr/downloads/list
			// This area needs work and optimization
			if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
				try {

					AssetManager assetManager = getAssets();
					InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
					//GZIPInputStream gin = new GZIPInputStream(in);
					OutputStream out = new FileOutputStream(DATA_PATH
							+ "tessdata/" + lang + ".traineddata");

					// Transfer bytes from in to out
					byte[] buf = new byte[1024];
					int len;
					//while ((lenf = gin.read(buff)) > 0) {
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					in.close();
					//gin.close();
					out.close();
					
					Log.v("", "Copied " + lang + " traineddata");
				} catch (IOException e) {
					Log.e("", "Was unable to copy " + lang + " traineddata " + e.toString());
				}
			}
	    }
	
	 public void showImage()
	 {
		 
	        dishName=_field.getText().toString();
	         
	        
	        // Toast.makeText(getApplicationContext(), dishName,	Toast.LENGTH_SHORT).show();
	         
		       if(dishName.equalsIgnoreCase("Biryani")){
		    
		   dish.setImageResource(R.drawable.biryani);
		    	
		  
		       }
		       
		       else if(dishName.equalsIgnoreCase("Chicken chaow mein")){
		    	
		   dish.setImageResource(R.drawable.chicken_chaow_mein);
		    	   
		    	   
		    	   
		    	   
		       }
		       
		       
		       else  if(dishName.equalsIgnoreCase("Chicken Roll")){
		    	  	
		   dish.setImageResource(R.drawable.chicken_roll);
			   
		       }
		       
		       
		       else  if(dishName.equalsIgnoreCase("Chicken Behari Boti")){
		   	  	
		    	   dish.setImageResource(R.drawable.chicken_behari_boti);
		    		   
		    	       } 
		       
		       else   if(dishName.equalsIgnoreCase("Chicken Soup")){
		      	  	
		    	   dish.setImageResource(R.drawable.chicken_corn_soup);
		    		   
		    	       } 
		       
		       
		       
		       
		       else    if(dishName.equalsIgnoreCase("Chicken Tikka")){
		      	  	
		    	   dish.setImageResource(R.drawable.chicken_tikka);
		    		   
		    	       } 
		       
		       else   if(dishName.equalsIgnoreCase("Chinese Rice")){
		      	  	
		    	   dish.setImageResource(R.drawable.chinese_fried_rice);
		    		   
		    	       } 
		       
		       
		       
		       else   if(dishName.equalsIgnoreCase("Malai Tikka")){
		     	  	
		    	   dish.setImageResource(R.drawable.malai_tikka1);
		    		   
		    	       } 
		       
		       else  if(dishName.equalsIgnoreCase("Seekh kabab")){
		     	  	
		    	   dish.setImageResource(R.drawable.seekh_kabab);
		    		   
		    	       } 
		       
		       else  if(dishName.equalsIgnoreCase("Tikka")){
		    	  	
		    	   dish.setImageResource(R.drawable.tikka);
		    		   
		    	       } 
		       
		       else  if(dishName.equalsIgnoreCase("Vegetable Soup")){
		   	  	
		    	   dish.setImageResource(R.drawable.vegetable_soup);
		    		   
		    	       } 
		       
		       else  if(dishName.equalsIgnoreCase("Zinger Burger")){
		      	  	
		    	   dish.setImageResource(R.drawable.zinger_burger);
		    		   
		    	       } 
		       
		       else  if(dishName.equalsIgnoreCase("Chicken karhai")){
		     	  	
		    	   dish.setImageResource(R.drawable.chiken_karhai);
		    		   
		    	       } 
		       
		       else  if(dishName.equalsIgnoreCase("Handi")){
		     	  	
		    	   dish.setImageResource(R.drawable.handi);
		    		   
		    	       } 
		       else
		       {
		    	   dish.setImageResource(R.drawable.cancel);
		    	  // Toast.makeText(getApplicationContext(), "NOt Found..", Toast.LENGTH_SHORT).show();
		       }
	       
	 }
	 @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 
		 switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			
			break;

		default:
			break;
		}
		 
		 
		 
		return super.onOptionsItemSelected(item);
	}
	 	 
}
