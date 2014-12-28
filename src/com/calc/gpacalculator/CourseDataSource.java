package com.calc.gpacalculator;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * TODO: fix algorithm for calculating GPA from tasks something to do with
 * weights? 0.75/100 or 75/100 getting weird numbers e.g 125%
 * 
 * e.g 50% weight grade 1/1 & 50% weight grade 1/2 SHOULD equal 75% == 3.0 GPA
 * ... we are getting 4.0
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * @author Jasmine
 * 
 */

public class CourseDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_COURSE_NAME,
			MySQLiteHelper.COLUMN_COURSE_ID,
			MySQLiteHelper.COLUMN_SEM2COURSE_ID,
			MySQLiteHelper.COLUMN_COURSE_GRADE };

	public CourseDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Course createCourse(int ID, String cName, int s2c_ID, float marks) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_COURSE_NAME, cName);
		values.put(MySQLiteHelper.COLUMN_COURSE_ID, ID);
		values.put(MySQLiteHelper.COLUMN_SEM2COURSE_ID, s2c_ID);
		values.put(MySQLiteHelper.COLUMN_COURSE_GRADE, marks);

		long insertId = database.insert(MySQLiteHelper.TABLE_COURSES, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_COURSES,
				allColumns, MySQLiteHelper.COLUMN_COURSE_ID + " = " + insertId,
				null, null, null, null);
		
		if(cursor == null){
			cursor.close();
			Course noData = new Course("", 0, getNewID(), -1);
			return noData;
			
		}
		
		
		
		cursor.moveToFirst();
		Course newComment = cursorToCourse(cursor);
		cursor.close();
		return newComment;
	}

	private Course cursorToCourse(Cursor cursor) {
		Course course = new Course("", 0, 0, 0);
		course.setName(cursor.getString(0));
		course.setID(cursor.getInt(1));
		course.setSem2course(cursor.getInt(2));
		course.setMark(cursor.getFloat(3));

		return course;
	}

	public void deleteCourse(int id) {
		System.out.println("Course deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_COURSES,
				MySQLiteHelper.COLUMN_COURSE_ID + " = " + id, null);
	}

	public List<Course> getAllCourses() {
		List<Course> list_of_courses = new ArrayList<Course>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_COURSES,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Course c = cursorToCourse(cursor);
			list_of_courses.add(c);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return list_of_courses;
	}

	public int getNewID() {

		int last_courseID;
		String countQuery = "SELECT "+ MySQLiteHelper.COLUMN_COURSE_ID + " FROM " + MySQLiteHelper.TABLE_COURSES + " ORDER BY "
				+ MySQLiteHelper.COLUMN_COURSE_ID+ " DESC LIMIT 1";
		Cursor cursor = database.rawQuery(countQuery, null);
		
		if(cursor.getCount() == 0){
			return 0;
		}
		cursor.moveToFirst();
		last_courseID = cursor.getInt(0);

		Log.d("newID", Integer.toString(last_courseID));
		cursor.close();
		return last_courseID + 1;

	}

	public List<Course> getCoursesfromSem(int semID) {

		List<Course> course_list = new ArrayList<Course>();

		String query = "SELECT c.coursename, c.courseID, c.sem2courseID, c.coursegrade FROM "
				+ MySQLiteHelper.TABLE_SEMESTERS
				+ " s, "
				+ MySQLiteHelper.TABLE_COURSES
				+ " c"
				+ " WHERE "
				+ "s."
				+ MySQLiteHelper.COLUMN_SEM_ID
				+ " = "
				+ " c."
				+ MySQLiteHelper.COLUMN_SEM2COURSE_ID
				+ " AND "
				+ "s."
				+ MySQLiteHelper.COLUMN_SEM_ID + " = " + semID;

		Log.d("database", "QUERY = " + query);

		Cursor cursor = database.rawQuery(query, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Course c = cursorToCourse(cursor);
			course_list.add(c);
			cursor.moveToNext();
		}
		cursor.close();
		return course_list;
	}

	public float getGradefromcID(int cID) {

		float grade = 0;

		String query = "SELECT " + MySQLiteHelper.COLUMN_COURSE_GRADE
				+ " FROM " + MySQLiteHelper.TABLE_COURSES + " WHERE " + cID
				+ " = " + MySQLiteHelper.COLUMN_COURSE_ID;

		Cursor cursor = database.rawQuery(query, null);

		cursor.moveToFirst();

		grade = cursor.getFloat(0);
		cursor.close();
		return grade;
	}
	
	/**
	 * sql return from tasks, column id, average, total, weight for a particular task
	 * to calculate (running sum of average /  running sum of total) * task weight = X
	 * X / sum of weights
	 * 
	 * 
	 * 
	 * (9/10)* 20 = X = 18 --> calculated by getTaskGrade()
	 * (1/10)* 20 = Y = 2  --> calculated by getTaskGrade()
	 * X+Y = Z = 20		   --> "course_grade"	
	 * 
	 * Z/(20+20) = grade  --> "course_grade" / totalWeights
	 * 
	 * convert to GPA
	 * save to DB
	 * 
	 * Methods:
	 * 1) 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param courseID
	 * @param tds
	 * @return
	 */
	

	public float getCourseGradefromTasks(int courseID, TaskDataSource tds) {
		float course_grade = 0;
		float GPA_course_grade = 0;

		String query = "SELECT " + MySQLiteHelper.COLUMN_ID + " FROM "
				+ MySQLiteHelper.TABLE_TASKS + " WHERE " + courseID + " = "
				+ MySQLiteHelper.COLUMN_COURSE2TASK_ID;
		
		String getTotalWeight_fromCourse = "SELECT " + MySQLiteHelper.COLUMN_WEIGHT + " FROM "
				+ MySQLiteHelper.TABLE_TASKS + " WHERE " + courseID + " = "
				+ MySQLiteHelper.COLUMN_COURSE2TASK_ID;
		
		float totalWeights = 0;
		
		Cursor weightCursor = database.rawQuery(getTotalWeight_fromCourse, null);
		weightCursor.moveToFirst();
		while(!weightCursor.isAfterLast()){ // CALCUATES SUM OF WEIGHTS
			
			totalWeights += weightCursor.getFloat(0);
			weightCursor.moveToNext();
		}
		
		weightCursor.close();
		

		Cursor cursor = database.rawQuery(query, null);

		
		
		// When there are no tasks in a course, set course grade to 0
		if (cursor.getCount() == 0) {
			updateGrade(GPA_course_grade, courseID);
			return 0;
		}

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) { // CALULATES Z
			int taskID = cursor.getInt(0);
			float weighted_task_grade = tds.getTaskGrade(taskID, courseID);
			Log.d("course GPA",
					"value of weighted task grade = "
							+ Float.toString(weighted_task_grade));
			course_grade += weighted_task_grade; // X

//			float totalWeight = tds.getTotalWeights(courseID);
//
//			Log.d("course GPA",
//					"value of weighted_grade % = "
//							+ Float.toString(totalWeight));
//
//			course_grade = 100 * (course_grade / totalWeight);
//
//			Log.d("course GPA",
//					"value of course_grade % = " + Float.toString(course_grade));
//			GPA_course_grade = Course.courseGrade2GPA(course_grade);
//			Log.d("course GPA",
//					"value of course_grade GPA = "
//							+ Float.toString(GPA_course_grade));
//
//			updateGrade(GPA_course_grade, courseID);

			cursor.moveToNext();
		}

		// CALCULATE TOTAL WEIGHT
		
		
		course_grade = 100 * (course_grade / totalWeights);
		
		
		// CONVERT TO GPA
		
		GPA_course_grade = Course.courseGrade2GPA(course_grade);
		
		
		
		// UPDATE DB
		
		updateGrade(GPA_course_grade, courseID);
		
		

		Log.d("marks",
				"value of course_grade = " + Float.toString(course_grade));

		// String query1 = "SELECT " + MySQLiteHelper.COLUMN_COURSE_GRADE +
		// " FROM " + MySQLiteHelper.TABLE_COURSES
		// + " WHERE " + courseID + " = " + MySQLiteHelper.COLUMN_COURSE_ID;
		//
		// Cursor cursor1 = database.rawQuery(query1, null);
		//
		// cursor1.moveToFirst();
		// Log.d("update", "course_grade after update = " +
		// Float.toString(cursor1.getFloat(0)));

		cursor.close();

		return GPA_course_grade;
	}

	public boolean updateGrade(float GPA_course_grade, int courseID) {

		ContentValues cv = new ContentValues();
		cv.put(MySQLiteHelper.COLUMN_COURSE_GRADE, GPA_course_grade);

		int r = database.update(MySQLiteHelper.TABLE_COURSES, cv,
				MySQLiteHelper.COLUMN_COURSE_ID + " = " + courseID, null);

		if (r < 1) {

			Log.d("update", "update failed");
			return false;
		}
		
		
		return true;

	}

	public List<Course> mostRecent() {

		List<Course> cList = new ArrayList<Course>();



		String query = "SELECT " + MySQLiteHelper.COLUMN_COURSE_NAME + " , "
				+ MySQLiteHelper.COLUMN_COURSE_ID + " , "
				+ MySQLiteHelper.COLUMN_SEM2COURSE_ID + " , "
				+ MySQLiteHelper.COLUMN_COURSE_GRADE  + " FROM " + MySQLiteHelper.TABLE_COURSES
				+ " ORDER BY " + MySQLiteHelper.COLUMN_COURSE_ID
				+ " DESC LIMIT 5 ";

		
		Cursor cursor = database.rawQuery(query, null);

		Log.d("listmain", "size of cursor " + Integer.toString(cursor.getCount()));

		
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Course c = cursorToCourse(cursor);
			cList.add(c);
			cursor.moveToNext();
		}
		cursor.close();
		return cList;

	}

	public boolean updateName(String newName, int cID) {

		ContentValues cv = new ContentValues();
		cv.put(MySQLiteHelper.COLUMN_COURSE_NAME, newName);

		int r = database.update(MySQLiteHelper.TABLE_COURSES, cv,
				MySQLiteHelper.COLUMN_COURSE_ID + " = " + cID, null);

		if (r < 1) {

			Log.d("update", "update failed");
			return false;
		}
		return true;

	}
	
	public boolean resetGrade(int cID) {

		float reset = 0;
		
		ContentValues cv = new ContentValues();
		cv.put(MySQLiteHelper.COLUMN_COURSE_GRADE, reset);

		int r = database.update(MySQLiteHelper.TABLE_COURSES, cv,
				MySQLiteHelper.COLUMN_COURSE_ID + " = " + cID, null);

		if (r < 1) {

			Log.d("update", "update failed");
			return false;
		}
		return true;

	}
	
	public int getSemFromCourse(int c2tID){
		
		String query = "SELECT " + MySQLiteHelper.COLUMN_SEM2COURSE_ID + " FROM "
					+ MySQLiteHelper.TABLE_COURSES + " WHERE " +
					c2tID + " = " + MySQLiteHelper.COLUMN_COURSE_ID;
		
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
		
		int SID_from_CID = cursor.getInt(0);
		
		return SID_from_CID;
		
		
		
		
	}
	

}
