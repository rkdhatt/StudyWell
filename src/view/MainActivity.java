package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.calc.gpacalculator.Course;
import com.calc.gpacalculator.CourseActivity;
import com.calc.gpacalculator.CourseDataSource;
import com.calc.gpacalculator.R;
import com.calc.gpacalculator.R.id;
import com.calc.gpacalculator.R.layout;
import com.calc.gpacalculator.R.menu;
import com.calc.gpacalculator.Semester;
import com.calc.gpacalculator.SemesterActivity;
import com.calc.gpacalculator.SemesterDataSource;
import com.calc.gpacalculator.Task;
import com.calc.gpacalculator.TaskActivity;
import com.calc.gpacalculator.TaskDataSource;

import controller.Course_ListAdapter;
import controller.Gpa_ListAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;


import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.Activity.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * todo hide actionbar (main)
 * todo colour actionbar in others
 * todo bottom bar icons (semester) | home | set goal
 * 
 * 
 * 
 * @author Jasmine
 *
 */



// Test

public class MainActivity extends ActionBarActivity {

	//private TextView gpa_view;

	private ListView testView;
	private TextView semGrade;
	public TaskDataSource tds;
	private ImageButton folder;

	public List<Task> values;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.hide();
		

		//this.deleteDatabase("tasks.db");
		tds = new TaskDataSource(this);

		folder = (ImageButton) findViewById(R.id.cute_folder_main);
		folder.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
				Intent sem_intent = new Intent(getApplicationContext(), SemesterActivity.class);

				startActivity(sem_intent);
		    }
		});
		
		tds.open();

		main_page();
		setup_adapter();
		
	}

	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    setup_adapter();
	   main_page();
	}
	
	public List<Task> getValues(){
		return values;
	}
	
	
	private void main_page() {
		TaskDataSource tds1 = new TaskDataSource(getApplicationContext());
		SemesterDataSource sds1 = new SemesterDataSource(getApplicationContext());
		CourseDataSource cds1 = new CourseDataSource(getApplicationContext());
		
		tds1.open();
		sds1.open();
		cds1.open();
		
		List<Task> tasks1 = tds1.getAllTasks();
		List<Course> courses1 = cds1.getAllCourses();
		List<Semester> semesters1 = sds1.getAllSemesters();
		
		
		//GETS COUNTS
		int i = tasks1.size();
		int j = courses1.size();
		int k = semesters1.size();
		
		TextView semCount = (TextView) findViewById(R.id.semCount);
		semCount.setText(Integer.toString(k));
		
		TextView courseCount = (TextView) findViewById(R.id.courseCount);
		courseCount.setText(Integer.toString(j));

		TextView taskCount = (TextView) findViewById(R.id.taskCount);
		taskCount.setText(Integer.toString(i));
	
		
		
		//GETS OVERALL GPA
		float GPA_grade = sds1.getOverallGPA();
		
		
		semGrade = (TextView) findViewById(R.id.main_GPA);
		semGrade.setText(Float.toString(GPA_grade));
		
		tds1.close();
		
		sds1.close();
		cds1.close();
	}
	
	private void setup_adapter(){

		
		CourseDataSource cds = new CourseDataSource(getApplicationContext());
		cds.open();

		List<Course> courses = cds.mostRecent();

		Course_ListAdapter adapter = new Course_ListAdapter(this, R.layout.course_entity,
						courses);
		testView = (ListView) findViewById(R.id.abcdef);
		testView.setAdapter(adapter);

		testView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Course c = (Course) testView
						.getItemAtPosition(position);
				Intent i = new Intent(getApplicationContext(),
						TaskActivity.class);
				i.putExtra("cID", c.getID());

				startActivity(i);

			}

		});

		
		cds.close();
		
		

	}
	
}
