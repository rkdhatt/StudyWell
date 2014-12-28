package com.calc.gpacalculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Calculator {

	private float gpa;
	private float course_mark;
	private float task_mark;
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * @param grade
	 */
	
	public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
	
	public Float taskGrade (float average, float total, float weight) {
		return (average/total)*(weight/100);
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	
	public float calculate_total(){
		return 0;
		
	}
	
	public float calculate_course(){
		/*	
		
		tds1.open();
		List<Task> tasks1 = tds1.getAllTasks();
		
		List<String> ls1 = new ArrayList();
		int i = tasks1.size();
		for (int j = 0; j<i;j++){
			ls1.add(tasks1.get(j).getName());
			//ls1.add(tasks1.get(j).getID());
			Log.d("database", tasks1.get(j).getName());
			Log.d("database", "ID " + String.valueOf(ls1.get(j)));
		}
		testView = (ListView) findViewById(R.id.abcdef);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ls1);
		
		adapter.toString();
		
		
		testView.setAdapter(adapter);
		tds1.close();
	*/
		return 0;
	}
}
