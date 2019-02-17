package com.example.menutranslator;



import com.example.menutranslator.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


public class about extends Activity{

	ListView listv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		ActionBar ab=getActionBar();
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setIcon(android.R.drawable.ic_dialog_info);
		
		//List View
		listv=(ListView)findViewById(R.id.listView1);
		
		
		//Data
		String[] data=new String[]{"About Us",
								"Info",
								"Rate it"
				
					};
		//Adapter
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,android.R.id.text1,data);
		listv.setAdapter(adapter);
		listv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					Toast.makeText(getBaseContext(), (String)listv.getItemAtPosition(arg2), Toast.LENGTH_SHORT).show();
					aboutusdialog();
					break;
				case 1:
					Toast.makeText(getBaseContext(), (String)listv.getItemAtPosition(arg2), Toast.LENGTH_SHORT).show();
					infoDialog();
					break;
				case 2:
					
					break;

				default:
					break;
				}
			}
	
		});
		
		//Spinner country
		String[] countryName ={"Pakistan", "India","China","Japan","Malaisia","Austrailia","Canada","Uk","USA"};
		Spinner spinnerCountry=(Spinner) findViewById(R.id.SpinnerCountry);
		ArrayAdapter<String> adopterSpinnnerC=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,countryName);
		adopterSpinnnerC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinnerCountry.setAdapter(adopterSpinnnerC);
		//Spinner Language
		 String[] mLangArray = {"English","Chinese","URDU","Japanese","German"}; 
		Spinner spinnerLang=(Spinner) findViewById(R.id.spinnerLanguage);
		ArrayAdapter<String> adopterLang=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mLangArray);
		adopterLang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinnerLang.setAdapter(adopterLang);
	}
	
	
	protected void infoDialog() {
		
		final AlertDialog alertDialog = new AlertDialog.Builder(about.this).create();
		alertDialog.setTitle("Info");
		alertDialog.setIcon(android.R.drawable.ic_dialog_info);
		alertDialog.setMessage("App use karo or chup kar k khana khao :p ");
		alertDialog.setButton(-1, "OK",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}


	private void aboutusdialog() {
		
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("About US");
		alertDialog.setMessage("This App is Created by :" +
				"\n"+ " Jahanzeb Jabbar," +
				"\n"+" Iqra Urooj," +
				"\n"+" Tahira," +
				"\n"+" Shiza" +"\n"+".........." +
						"\n"+"Students of Computer Systems Engineering ,MUET Jamshoro");
		alertDialog.setButton(-1, "OK",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		
		alertDialog.show();
		
		
	}
	
	
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	
	
}
