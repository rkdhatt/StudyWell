package com.calc.gpacalculator;

import java.util.ArrayList;
import java.util.List;

import view.MainActivity;
import controller.Gpa_ListAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import controller.*;
import com.calc.gpacalculator.*;

public class TaskActivity extends ActionBarActivity {

	// private Button add_task;
	private ListView task_list;
	private Gpa_ListAdapter adapter;
	final Context context = this;

	private int c2t_ID;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_activity);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// getActionBar().setIcon(android.R.color.transparent);

		// TextView myTextView=(TextView)findViewById(R.id.task_nameedit);
		// Typeface
		// typeFace=Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed-Light.ttf");
		// myTextView.setTypeface(typeFace);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			c2t_ID = extras.getInt("cID");
			Log.d("onclick", "oncreate task id " + Integer.toString(c2t_ID));
		}

		task_list = (ListView) findViewById(R.id.view_tasklist);
		setup_adapter();

	}

	void setup_add() {

		LayoutInflater li = LayoutInflater.from(context);

		// Get XML file to view
		View promptsView = li.inflate(R.layout.add_task_dialog, null);

		final EditText taskname_edit = (EditText) promptsView
				.findViewById(R.id.taskname_dialog);
		final EditText average_edit = (EditText) promptsView
				.findViewById(R.id.average_dialog);
		final EditText total_edit = (EditText) promptsView
				.findViewById(R.id.total_dialog);
		final EditText task_weight = (EditText) promptsView
				.findViewById(R.id.weight_dialog);

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

								// Add new task to the DB
								TaskDataSource tds1 = new TaskDataSource(
										getApplicationContext());
								tds1.open();
								int newID = tds1.getNewID();

								// If a field is left empty show error message
								// close dialog, otherwise add to DB
								if (!taskname_edit.getText().toString()
										.isEmpty()
										& !average_edit.getText().toString()
												.isEmpty()
										& !total_edit.getText().toString()
												.isEmpty()
										& !task_weight.getText().toString()
												.isEmpty()) {

									String new_taskName = "";
									Float float_avg_edit = (float) 0;

									Float float_total_edit = (float) 0;

									Float float_weight = (float) 0;

									new_taskName = taskname_edit.getText()
											.toString();
									float_avg_edit = Float
											.parseFloat(average_edit.getText()
													.toString());
									float_total_edit = Float
											.parseFloat(total_edit.getText()
													.toString());
									float_weight = Float.parseFloat(task_weight
											.getText().toString());

									if (
											(
													//Both are wrong
													((float_weight > 100) || (float_weight < 1)) 
													&& (float_total_edit < 1) 
											) ||
													// Either one are wrong
													((float_weight > 100) || (float_weight < 1)) 
													|| (float_total_edit < 1) 
											) {

										AlertDialog.Builder dialog1 = new AlertDialog.Builder(
												context);
										dialog1.setTitle("Whoops!");
										dialog1.setMessage("Input a Weight from 1% to 100% and a Total not less than 1.");
										dialog1.setPositiveButton(
												"Okay",
												new DialogInterface.OnClickListener() {

													public void onClick(
															DialogInterface dialog,
															int id) {

														dialog.cancel();
														setup_add();

													}
												});

										dialog1.show();

									}
									
//									if (float_total_edit == 0) {
//											AlertDialog.Builder dialog1 = new AlertDialog.Builder(
//													context);
//											dialog1.setTitle("Whoops!");
//											dialog1.setMessage("Total cannot be 0.");
//											dialog1.setPositiveButton(
//													"Okay",
//													new DialogInterface.OnClickListener() {
//
//														public void onClick(
//																DialogInterface dialog,
//																int id) {
//															setup_add();
//															dialog.cancel();
//															
//
//														}
//													});
//
//											dialog1.show();
//
//										
//									}
//									
//									
//									if ((float_weight > 100) || (float_weight < 1)) {
//
//										AlertDialog.Builder dialog1 = new AlertDialog.Builder(
//												context);
//										dialog1.setTitle("Whoops!");
//										dialog1.setMessage("Input a weight from 1% to 100%");
//										dialog1.setPositiveButton(
//												"Okay",
//												new DialogInterface.OnClickListener() {
//
//													public void onClick(
//															DialogInterface dialog,
//															int id) {
//
//														dialog.cancel();
//														setup_add();
//
//													}
//												});
//
//										dialog1.show();
//
//									}
//									
									else {

										Log.d("weight",
												Float.toString(float_weight));

										tds1.createTask(newID, new_taskName,
												float_avg_edit,
												float_total_edit, c2t_ID,
												float_weight, -1);
									}
								} else {
									showInValidInputMessage();
								}
								tds1.close();
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

	private void setup_adapter() {

		updateAll();

		TaskDataSource tds1 = new TaskDataSource(getApplicationContext());

		tds1.open();
		List<Task> tasks = tds1.getTasksfromCourse(c2t_ID);

		tds1.close();
		adapter = new Gpa_ListAdapter(this, R.layout.task_entity, tasks);
		final ListView activity_taskview = task_list;
		activity_taskview.setAdapter(adapter);

		activity_taskview
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> parent,
							View v, int position, long id) {

						Task t = (Task) activity_taskview
								.getItemAtPosition(position);

						String tName = t.getName();
						float tAvg = t.getAverage();
						float tTotes = t.getTotal_marks();
						float tWeight = t.getWeight();
						int tID = t.getID();

						setup_edit(tName, tAvg, tTotes, tWeight, tID);

						// Toast.makeText(getApplicationContext(), "click",
						// Toast.LENGTH_SHORT).show();

						return false;

					}
				});

		tds1.close();
	}

	public void updateAll() {

		TaskDataSource tds = new TaskDataSource(context);
		CourseDataSource cds = new CourseDataSource(context);
		SemesterDataSource sds = new SemesterDataSource(context);

		tds.open();
		cds.open();
		sds.open();

		List<Task> check = tds.getTasksfromCourse(c2t_ID);

		if (!check.isEmpty()) {

			// List<Integer> sid_cid = tds.getCid_Sid(c2t_ID);
			// int courseID = sid_cid.get(0);
			int semID = tds.getCid_Sid(c2t_ID);

			float GPA_course_grade = cds.getCourseGradefromTasks(c2t_ID, tds);

			Log.d("2ndcoursebug",
					"GPA_course_grade = " + Float.toString(GPA_course_grade));

			float GPA_from_sem = sds.getGPAfromCourses(semID, cds);
			
			Log.d("2ndcoursebug",
					"GPA_semester_grade = " + Float.toString(GPA_course_grade));
			
			
		} else {

			cds.resetGrade(c2t_ID);
			int sid_from_cid = cds.getSemFromCourse(c2t_ID);
			float GPA_from_sem = sds.getGPAfromCourses(sid_from_cid, cds);

		}

		tds.close();
		cds.close();
		sds.close();

	}

	protected void setup_edit(final String tName, float tAvg, float tTotes,
			float tWeight, final int tID) {

		LayoutInflater li = LayoutInflater.from(context);

		// Get XML file to view
		View promptsView = li.inflate(R.layout.edit_task_dialog, null);

		final EditText taskname_edit = (EditText) promptsView
				.findViewById(R.id.taskname_dialog_edit);
		final EditText average_edit = (EditText) promptsView
				.findViewById(R.id.average_dialog_edit);
		final EditText total_edit = (EditText) promptsView
				.findViewById(R.id.total_dialog_edit);
		final EditText task_weight = (EditText) promptsView
				.findViewById(R.id.weight_dialog_edit);

		taskname_edit.setText(tName);
		average_edit.setText(Float.toString(tAvg));
		total_edit.setText(Float.toString(tTotes));
		task_weight.setText(Float.toString(tWeight));

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

				// Add new task to the DB
				TaskDataSource tds1 = new TaskDataSource(
						getApplicationContext());
				tds1.open();
				// If a field is left empty show error message close dialog,
				// otherwise add to DB
				if (!taskname_edit.getText().toString().isEmpty()
						& !average_edit.getText().toString().isEmpty()
						& !total_edit.getText().toString().isEmpty()
						& !task_weight.getText().toString().isEmpty()) {

					String new_taskName = taskname_edit.getText().toString();
					final Float float_avg_edit = Float.parseFloat(average_edit
							.getText().toString());
					final Float float_total_edit = Float.parseFloat(total_edit
							.getText().toString());
					final Float float_weight = Float.parseFloat(task_weight.getText()
							.toString());

					
					if (
							(
									//Both are wrong
									((float_weight > 100) || (float_weight < 1)) 
									&& (float_total_edit < 1) 
							) ||
									// Either one are wrong
									((float_weight > 100) || (float_weight < 1)) 
									|| (float_total_edit < 1) 
							) {
						AlertDialog.Builder dialog1 = new AlertDialog.Builder(
								context);
						dialog1.setTitle("Whoops!");
						dialog1.setMessage("Input a Weight from 1% to 100% and a Total not less than 1.");
						dialog1.setPositiveButton("Okay",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int id) {

										dialog.cancel();
										
										// WE FIXED EDITING BY MAKING THESE ARGS FINAL
										
										setup_edit(tName, float_avg_edit, float_total_edit, float_weight, tID);

									}
								});

						dialog1.show();
						
						
					}
					
//					if (float_total_edit == 0) {
//						AlertDialog.Builder dialog1 = new AlertDialog.Builder(
//								context);
//						dialog1.setTitle("Whoops!");
//						dialog1.setMessage("Total cannot be 0.");
//						dialog1.setPositiveButton("Okay",
//								new DialogInterface.OnClickListener() {
//
//									public void onClick(DialogInterface dialog,
//											int id) {
//
//										dialog.cancel();
//										
//										// WE FIXED EDITING BY MAKING THESE ARGS FINAL
//										
//										setup_edit(tName, float_avg_edit, float_total_edit, float_weight, tID);
//
//									}
//								});
//
//						dialog1.show();
//
//						
//					}
//					
//					if ((float_weight > 100) || (float_weight < 1)) {
//
//						AlertDialog.Builder dialog1 = new AlertDialog.Builder(
//								context);
//						dialog1.setTitle("Whoops!");
//						dialog1.setMessage("Input a weight from 1% to 100%.");
//						dialog1.setPositiveButton("Okay",
//								new DialogInterface.OnClickListener() {
//
//									public void onClick(DialogInterface dialog,
//											int id) {
//
//										dialog.cancel();
//										
//										// WE FIXED EDITING BY MAKING THESE ARGS FINAL
//										
//										setup_edit(tName, float_avg_edit, float_total_edit, float_weight, tID);
//
//									}
//								});
//
//						dialog1.show();
//
//					} 
					
					else {

						tds1.updateFromDialog(new_taskName, float_avg_edit,
								float_total_edit, float_weight, tID);

					}
				} else {
					showInValidInputMessage();
				}
				tds1.close();
				// Call to update the list view

				setup_adapter();

			}
		})

		.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				AlertDialog.Builder dialog1 = new AlertDialog.Builder(context);
				dialog1.setTitle("Are you sure?");
				dialog1.setMessage("This Task will be permanently deleted");
				dialog1

				.setPositiveButton("Delete",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								TaskDataSource tds = new TaskDataSource(context);
								tds.open();

								tds.deleteTask(tID);

								tds.close();

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
		// updateAll();
		alertDialog.show();

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.task_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.addtask_menu:
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

	@Override
	public void onPause() {
		super.onPause(); // Always call the superclass method first

	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first

	}

	public void showInValidInputMessage() {
		Context context = getApplicationContext();
		CharSequence text = "Invalid Input";
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

}
