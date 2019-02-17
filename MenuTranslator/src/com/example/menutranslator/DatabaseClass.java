package com.example.menutranslator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseClass {
	SQLiteDatabase db;
	public static String pathDish;
	
	public void CreateDB()
	{
		db=SQLiteDatabase.openOrCreateDatabase("DishesPak", null);
		db.execSQL("create Table  if not exists Dishes (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,path TEXT)");
		
		String insetQ="insert into Dishes (name,path) values ('Chicken Tikka','chicken_tikka.jpg'),"
				+"('Biryani','Biryani.jpg'),('Chicken Roll','Chicken_r.jpg'),('Biryani','Biryani.jpg')"+
				"('Chicheken Rice','B.jpg'),('Malai Tikka','Biharii.jpg')"; 
		db.execSQL(insetQ);
		
	}
	
	public void getimage(String name){
		
		String query="select path from Dishes where name='"+name+"'";
		Cursor c=	db.rawQuery(query, null);
		if(c.moveToNext()){
			
			pathDish=c.getString(0);
			
		}
			
		}
	
	
}
