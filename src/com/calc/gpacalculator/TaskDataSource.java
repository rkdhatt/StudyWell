package com.calc.gpacalculator;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TaskDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_AVG,
			MySQLiteHelper.COLUMN_TOTAL, MySQLiteHelper.COLUMN_COURSE2TASK_ID,
			MySQLiteHelper.COLUMN_WEIGHT, MySQLiteHelper.COLUMN_GRADE };

	public TaskDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Task createTask(int ID, String taskname, float average, float total,
			int course2taskID, float weight, float grade) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ID, ID);
		values.put(MySQLiteHelper.COLUMN_NAME, taskname);
		values.put(MySQLiteHelper.COLUMN_AVG, average);
		values.put(MySQLiteHelper.COLUMN_TOTAL, total);
		values.put(MySQLiteHelper.COLUMN_COURSE2TASK_ID, course2taskID);
		values.put(MySQLiteHelper.COLUMN_WEIGHT, weight);
		values.put(MySQLiteHelper.COLUMN_GRADE, grade);

		long insertId = database.insert(MySQLiteHelper.TABLE_TASKS, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TASKS, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		Log.d("database", Integer.toString(cursor.getColumnCount()));
		cursor.moveToFirst();
		Task newComment = cursorToTask(cursor);
		cursor.close();
		return newComment;
	}

	public void deleteTask(int tID) {

		System.out.println("Task deleted with id: " + tID);
		database.delete(MySQLiteHelper.TABLE_TASKS, MySQLiteHelper.COLUMN_ID
				+ " = " + tID, null);
	}

	public List<Task> getAllTasks() {
		List<Task> list_of_tasks = new ArrayList<Task>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_TASKS, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Task task = cursorToTask(cursor);
			list_of_tasks.add(task);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return list_of_tasks;
	}

	private Task cursorToTask(Cursor cursor) {
		Task task = new Task(0, "", 0, 0, 0, 0, 0);
		task.setID(cursor.getInt(0));
		task.setName(cursor.getString(1));
		task.setAverage(cursor.getFloat(2));
		task.setTotal_marks(cursor.getFloat(3));
		task.setSemester_ID(cursor.getInt(4));
		task.setWeight(cursor.getFloat(5));
		task.setGrade(cursor.getFloat(6));
		return task;
	}

	public int getNewID() {

		int last_taskID;
		String countQuery = "SELECT "+ MySQLiteHelper.COLUMN_ID + " FROM " + MySQLiteHelper.TABLE_TASKS + " ORDER BY "
				+ MySQLiteHelper.COLUMN_ID + " DESC LIMIT 1";
		Cursor cursor = database.rawQuery(countQuery, null);
		
		if(cursor.getCount() == 0){
			return 0;
		}
		cursor.moveToFirst();
		last_taskID = cursor.getInt(0);

		Log.d("newID", Integer.toString(last_taskID));
		cursor.close();
		return last_taskID + 1;
	}

	public List<Task> getTasksfromCourse(int cID) {

		List<Task> task_list = new ArrayList<Task>();

		String query = "SELECT * FROM " + MySQLiteHelper.TABLE_TASKS
				+ " WHERE " + MySQLiteHelper.COLUMN_COURSE2TASK_ID + " = "
				+ cID;

		Cursor cursor = database.rawQuery(query, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Task t = cursorToTask(cursor);
			task_list.add(t);
			cursor.moveToNext();
		}
		cursor.close();
		return task_list;
	}

	public Float getTaskGrade(int taskID, int cID) {
		float TaskGrade = 0;
		Calculator calc = new Calculator();
		String countQuery = "SELECT " + MySQLiteHelper.COLUMN_AVG + ", "
				+ MySQLiteHelper.COLUMN_TOTAL + " , "
				+ MySQLiteHelper.COLUMN_WEIGHT + " FROM "
				+ MySQLiteHelper.TABLE_TASKS + " WHERE "
				+ MySQLiteHelper.COLUMN_ID + " = " + taskID;

		Cursor cursor = database.rawQuery(countQuery, null);

		cursor.moveToFirst();
		float average = cursor.getFloat(0);
		float total = cursor.getFloat(1);
		float weight = cursor.getFloat(2);

		//if total is 0
		
		Log.d("calc",
				"value of weight in getTaskGrade = " + Float.toString(weight));

		TaskGrade = (average / total) * (weight);


		// calc.taskGrade(average, total, weight);

		cursor.close();
		return TaskGrade;
	}

	public float getTotalWeights(int courseID) {
		float totalWeight = 0;

		String weightQuery = "SELECT " + MySQLiteHelper.COLUMN_WEIGHT
				+ " FROM " + MySQLiteHelper.TABLE_TASKS + " WHERE "
				+ MySQLiteHelper.COLUMN_COURSE2TASK_ID + " = " + courseID;

		Cursor c1 = database.rawQuery(weightQuery, null);

		c1.moveToFirst();
		while (!c1.isAfterLast()) {

			totalWeight += c1.getFloat(0);

			c1.moveToNext();
		}

		c1.close();

		return totalWeight;

	}

	public boolean updateFromDialog(String newName, float newAvg,
			float newTotes, float newWeight, int tID) {
		ContentValues cv = new ContentValues();
		cv.put(MySQLiteHelper.COLUMN_NAME, newName);
		cv.put(MySQLiteHelper.COLUMN_AVG, newAvg);
		cv.put(MySQLiteHelper.COLUMN_TOTAL, newTotes);
		cv.put(MySQLiteHelper.COLUMN_WEIGHT, newWeight);

		int r = database.update(MySQLiteHelper.TABLE_TASKS, cv,
				MySQLiteHelper.COLUMN_ID + " = " + tID, null);

		if (r < 1) {

			Log.d("update", "update failed");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * S: c.course_ID, s.sem_id F: task_table t, course_table s, table_sem s W:
	 * t.course2task_id == c.course_id and c.course_id == s.sem_id
	 * 
	 * 
	 * 
	 * 
	 */

	public int getCid_Sid(int c2tID) {

//		String query = "SELECT c.courseID, s.semesterID FROM "
//				+ MySQLiteHelper.TABLE_TASKS + " t, "
//				+ MySQLiteHelper.TABLE_COURSES + " c, "
//				+ MySQLiteHelper.TABLE_SEMESTERS + " s WHERE " + c2tID
//				+ " = c." + MySQLiteHelper.COLUMN_COURSE_ID + " AND c."
//				+ MySQLiteHelper.COLUMN_SEM2COURSE_ID + " = s."
//				+ MySQLiteHelper.COLUMN_SEM_ID;
		
		
		String query = "SELECT s.semesterID FROM "
				+ MySQLiteHelper.TABLE_TASKS + " t, "
				+ MySQLiteHelper.TABLE_COURSES + " c, "
				+ MySQLiteHelper.TABLE_SEMESTERS + " s WHERE " + c2tID
				+ " = c." + MySQLiteHelper.COLUMN_COURSE_ID + " AND c."
				+ MySQLiteHelper.COLUMN_SEM2COURSE_ID + " = s."
				+ MySQLiteHelper.COLUMN_SEM_ID;
		

		int sID;
//		int cID;

		Cursor cursor = database.rawQuery(query, null);
		

		
		cursor.moveToFirst();

//		cID = cursor.getInt(0);
		sID = cursor.getInt(0);
		
//		List<Integer> id = new ArrayList();
//		
//		id.add(sID);
//		id.add(cID);
//		
		cursor.close();
		
		return sID;
		
		
		

	}

}
