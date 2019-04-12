package com.example.databaseexample;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends Activity {

	DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDB=new DatabaseHelper(this, "app");
        setContentView(R.layout.activity_main);
    }
        
    public void doList(View v){
    	LinearLayout l = (LinearLayout)findViewById(R.id.mainView);
    	TextView t = new TextView(this);
    	t.setText("Some text");
    	l.addView(t);
    }
    
    public void doInsert(View v){
    	String[] vals = {"Steve","23"};
    	myDB.doUpdate("Insert into students(name, age) values (?,?);", vals);
    }

	public void doQuery(View v){
		Cursor c = myDB.doQuery("SELECT name, age from students");
		while (c.moveToNext()){
				System.out.println(c.getString(c.getColumnIndex("name"))+","+c.getLong(c.getColumnIndex("age")));
		}
		c.close();
	}
}
