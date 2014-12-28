package com.calc.gpacalculator;

import java.util.List;

import view.MainActivity;

import controller.Course_ListAdapter;
import controller.Semester_ListAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

//TODO Fix bug when adding, for each sem,c,t pass in a list of floats representing grades to calculator method
// 		in calculator return grade 
/**
 * 
 * for loop each grade in list sql: select c.grade from semester s, course c
 * where s.cid = c.cid see public List<Course> getCoursesfromSem (int semID){
 * from COURSEDATASOURCE
 * 
 * sem1 > c1
 * 
 * @author Jasmine
 * 
 */

public class SemesterActivity extends ActionBarActivity {

	private String semester_name;
	private Semester_ListAdapter adapter;
	private ListView semester_listview;
	final Context context = this;

	private Float semMark;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.semester_activity);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		//getActionBar().setIcon(android.R.color.transparent);
		
		
		semester_listview = (ListView) findViewById(R.id.view_semesterlist);
		setup_adapter();
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first

		setup_adapter();

	}

	private void setup_add() {

		LayoutInflater li = LayoutInflater.from(context);
		final SemesterDataSource sds = new SemesterDataSource(context);

		// Get XML file to view
		View promptsView = li.inflate(R.layout.add_semester_dialog, null);

		final EditText coursename_edit = (EditText) promptsView
				.findViewById(R.id.semestername_dialog);

		// Create a new AlertDialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// Link the alertdialog to the XML
		alertDialogBuilder.setView(promptsView);

		// Create the buttons
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Save",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								// If a field is left empty show error message
								// close dialog, otherwise add to DB
								if (!coursename_edit.getText().toString()
										.isEmpty()) {
									semester_name = coursename_edit.getText()
											.toString();
									sds.open();
									int sID = sds.getNewID();
									Log.d("onclick", "checking new id of sem "
											+ Integer.toString(sID));

									sds.createSemester(semester_name, sID, 0);

									sds.close();

								} else {
									showInValidInputMessage();
								}

								// Call to update the list view
								setup_adapter();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Do nothing
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.semester_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.add_semester_menu:
			setup_add();

			break;

		case android.R.id.home:
			Intent home_intent = new Intent(this, MainActivity.class);

			startActivity(home_intent);
			
			break;

		default:
			break;
		}
		return true;
	}

	public void showInValidInputMessage() {
		Context context = getApplicationContext();
		CharSequence text = "Invalid Input";
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	private void setup_adapter() {

		SemesterDataSource sds = new SemesterDataSource(context);
		CourseDataSource cds = new CourseDataSource(context);

		cds.open();
		sds.open();
		List<Semester> semesters_fromDB = sds.getAllSemesters(); // Change to
																	// get
																	// courses
																	// from a
																	// semester?]

		int i = semesters_fromDB.size();
//		for (int j = 0; j < i; j++) {
//
//			semMark = sds.getGPAfromCourses(semesters_fromDB.get(j).getID(),
//					cds);
//			
//			if(semMark == Float.NaN){
//				semMark = (float) 0;
//			}
//			
//
//			Log.d("marks", "semMark = " + Float.toString(semMark));
//
//			semesters_fromDB.get(j).setMark_value(semMark);
//
//		}

		adapter = new Semester_ListAdapter(this, R.layout.semester_entity,
				semesters_fromDB);

		final ListView activity_taskview = semester_listview;

		activity_taskview.setAdapter(adapter);

		cds.close();
		sds.close();

		activity_taskview
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> parent,
							View v, int position, long id) {

						Semester s1 = (Semester) activity_taskview
								.getItemAtPosition(position);

						String sName = s1.getName();
						final int sid = s1.getID();

						setup_edit(sName, sid);

						// Toast.makeText(getApplicationContext(), "click",
						// Toast.LENGTH_SHORT).show();

						return true;

					}
				});

		activity_taskview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Log.d("onclick",
						"Position clicked " + Integer.toString(position));
				Semester s = (Semester) activity_taskview
						.getItemAtPosition(position);
				Log.d("onclick", "SEM Name from list: " + s.getName());
				Log.d("onclick",
						"SEM ID from list: " + Integer.toString(s.getID()));

				Intent i = new Intent(getApplicationContext(),
						CourseActivity.class);
				i.putExtra("sID", s.getID());

				startActivity(i);

			}

		});

		sds.close();
	}

	protected void setup_edit(String sname, final int sid) {
		LayoutInflater li = LayoutInflater.from(context);
		final SemesterDataSource sds = new SemesterDataSource(context);

		// Get XML file to view
		View promptsView = li.inflate(R.layout.edit_semester_dialog, null);

		final EditText coursename_edit = (EditText) promptsView
				.findViewById(R.id.edit_semestername_dialog);

		coursename_edit.setText(sname);

		// Create a new AlertDialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// Link the alertdialog to the XML
		alertDialogBuilder.setView(promptsView);

		// Create the buttons
		alertDialogBuilder.setCancelable(false)

		.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Do nothing

			}
		})

		.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				// If a field is left empty show error message
				// close dialog, otherwise add to DB
				if (!coursename_edit.getText().toString().isEmpty()) {

					String newName = coursename_edit.getText().toString();

					Log.d("update", newName);

					sds.open();

					sds.updateName(newName, sid);

					sds.close();

				} else {
					showInValidInputMessage();
				}

				// Call to update the list view
				setup_adapter();
			}
		}).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				final SemesterDataSource sds1 = new SemesterDataSource(context);
				sds1.open();

				AlertDialog.Builder dialog1 = new AlertDialog.Builder(context);
				dialog1.setTitle("Are you sure?");
				dialog1.setMessage("Deleting this Term will erase all associated Courses & Tasks");
				
				//setcolor
				dialog1

				.setPositiveButton("Delete",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								TaskDataSource tds = new TaskDataSource(context);
								tds.open();

								CourseDataSource cds = new CourseDataSource(
										context);
								cds.open();

								List<Course> cDelete = cds
										.getCoursesfromSem(sid);
								int cSize = cDelete.size();

								List<Task> tDelete;
								int tsize;

								for (int i = 0; i < cSize; i++) {

									// Delete courses within semester

									tDelete = tds.getTasksfromCourse(cDelete
											.get(i).getID());
									tsize = tDelete.size();

									for (int j = 0; j < tsize; j++) {

										// Delete tasks within courses

										tds.deleteTask(tDelete.get(j).getID());

									}
									cds.deleteCourse(cDelete.get(i).getID());
								}

								// Delete semester
								sds1.deleteSemester(sid);

								cds.close();
								tds.close();
								sds1.close();
								setup_adapter();

							}
						}).setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});

				dialog1.show();
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

}
