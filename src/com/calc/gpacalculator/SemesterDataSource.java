package com.calc.gpacalculator;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SemesterDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_SEM_NAME,
			MySQLiteHelper.COLUMN_SEM_ID, MySQLiteHelper.COLUMN_SEM_GRADE };

	public SemesterDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Semester createSemester(String sName, int sID, float marks) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_SEM_NAME, sName);
		values.put(MySQLiteHelper.COLUMN_SEM_ID, sID);
		values.put(MySQLiteHelper.COLUMN_SEM_GRADE, marks);

		long insertId = database.insert(MySQLiteHelper.TABLE_SEMESTERS, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_SEMESTERS,
				allColumns, MySQLiteHelper.COLUMN_SEM_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		Semester newComment = cursorToSemester(cursor);
		cursor.close();
		return newComment;
	}

	private Semester cursorToSemester(Cursor cursor) {
		Semester semester = new Semester("", 0, 0);
		semester.setName(cursor.getString(0));
		semester.setID(cursor.getInt(1));
		semester.setMark_value(cursor.getFloat(2));

		return semester;
	}

	public void deleteSemester(int sID) {
		System.out.println("Semester deleted with id: " + sID);
		database.delete(MySQLiteHelper.TABLE_SEMESTERS,
				MySQLiteHelper.COLUMN_SEM_ID + " = " + sID, null);
	}

	public List<Semester> getAllSemesters() {
		List<Semester> list_of_semesters = new ArrayList<Semester>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_SEMESTERS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Semester c = cursorToSemester(cursor);
			list_of_semesters.add(c);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return list_of_semesters;
	}

	public int getNewID() {

		int last_semID;
		String countQuery = "SELECT "+ MySQLiteHelper.COLUMN_SEM_ID + " FROM " + MySQLiteHelper.TABLE_SEMESTERS + " ORDER BY "
				+ MySQLiteHelper.COLUMN_SEM_ID+ " DESC LIMIT 1";
		Cursor cursor = database.rawQuery(countQuery, null);
		
		if(cursor.getCount() == 0){
			return 0;
		}
		cursor.moveToFirst();
		last_semID = cursor.getInt(0);

		Log.d("newID", Integer.toString(last_semID));
		cursor.close();
		return last_semID + 1;
	}

	/**
	 * 
	 * TODO: fix bug with not saving a course grade properly --> cannot
	 * calculate sem grade correctly --> sql update statment? TODO: make weights
	 * as percentages TODO: GPA goals @ course and semester levels TODO: home
	 * screen display GPA TODO: table headers for tasks TODO: design
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param semId
	 * @param cds
	 * @return
	 */

	public Float getGPAfromCourses(int semId, CourseDataSource cds) {
		float semester_grade = 0;

		Log.d("marks", "SDS.java. semID = " + Integer.toString(semId));

//		String query = "SELECT " + MySQLiteHelper.COLUMN_COURSE_GRADE
//				+ " FROM " + MySQLiteHelper.TABLE_COURSES + " WHERE " + semId
//				+ " = " + MySQLiteHelper.COLUMN_SEM2COURSE_ID;
		
//		String query = "SELECT " + MySQLiteHelper.COLUMN_COURSE_GRADE
//				+ " FROM " + MySQLiteHelper.TABLE_COURSES + " , " + MySQLiteHelper.TABLE_TASKS + " , " + MySQLiteHelper.TABLE_SEMESTERS + " , " +
//				" WHERE " + semId + " = " + MySQLiteHelper.COLUMN_SEM2COURSE_ID + " and " +
//				MySQLiteHelper.COLUMN_COURSE_ID + " = " + MySQLiteHelper.COLUMN_COURSE2TASK_ID + " and " + 		
//				;
		
		
		String query = "SELECT DISTINCT " + MySQLiteHelper.COLUMN_COURSE_GRADE
				+ " FROM " + MySQLiteHelper.TABLE_COURSES + " , " + MySQLiteHelper.TABLE_TASKS + " , " + MySQLiteHelper.TABLE_SEMESTERS +
				" WHERE " + semId + " = " + MySQLiteHelper.COLUMN_SEM2COURSE_ID + " and " +
				MySQLiteHelper.COLUMN_COURSE_ID + " = " + MySQLiteHelper.COLUMN_COURSE2TASK_ID	
				;
		
		

		Cursor cursor = database.rawQuery(query, null);

		int courseCount = cursor.getCount();
		
		if(courseCount == 0){
			updateGrade(semester_grade, semId);
			return semester_grade;
		}
		
		
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			
			Log.d("marks", "value of cursor.getFloat(0) " + Float.toString(cursor.getFloat(0)));
			semester_grade += cursor.getFloat(0);

			cursor.moveToNext();
		}
		
		Log.d("2ndcoursebug", "value of courseCount " + Float.toString(courseCount));

		semester_grade = semester_grade/courseCount;
		
		Log.d("2ndcoursebug", "value of semester_grade " + Float.toString(semester_grade));

		semester_grade = Calculator.round(semester_grade, 1);
		
		Log.d("2ndcoursebug", "value of semester_grade AFTER ROUND " + Float.toString(semester_grade));
		
		updateGrade(semester_grade, semId);

		// Log.d("marks", "semester_grade = " + Float.toString(semester_grade));

		// String updateCourseGrade = "UPDATE " + MySQLiteHelper.TABLE_SEMESTERS
		// + " SET " + MySQLiteHelper.COLUMN_SEM_GRADE + " = "
		// + semester_grade + " WHERE " + semId + " = "
		// + MySQLiteHelper.COLUMN_SEM_ID;
		//
		// cursor = database.rawQuery(updateCourseGrade, null);

		cursor.close();

		return semester_grade;
	}
	
	

	

	private boolean updateGrade(float semester_grade, int semId) {

		ContentValues cv = new ContentValues();
		cv.put(MySQLiteHelper.COLUMN_SEM_GRADE, semester_grade);

		int r = database.update(MySQLiteHelper.TABLE_SEMESTERS, cv,
				MySQLiteHelper.COLUMN_SEM_ID + " = " + semId, null);

		if (r < 1) {

			Log.d("update", "update failed");
			return false;
		}
		return true;

	}
	
	public boolean updateName(String sName, int semId) {

		ContentValues cv = new ContentValues();
		cv.put(MySQLiteHelper.COLUMN_SEM_NAME, sName);

		int r = database.update(MySQLiteHelper.TABLE_SEMESTERS, cv,
				MySQLiteHelper.COLUMN_SEM_ID + " = " + semId, null);

		if (r < 1) {

			Log.d("update", "update failed");
			return false;
		}
		return true;

	}
	
	
	
	public boolean resetGrade(int sID) {

		float reset = 0;
		
		ContentValues cv = new ContentValues();
		cv.put(MySQLiteHelper.COLUMN_SEM_GRADE, reset);

		int r = database.update(MySQLiteHelper.TABLE_SEMESTERS, cv,
				MySQLiteHelper.COLUMN_SEM_ID + " = " + sID, null);
		

		if (r < 1) {

			Log.d("update", "update failed");
			return false;
		}
		return true;

	}
	
	
	public float getOverallGPA() {

	
		float overallGPA = 0;
		
		//String countQuery = "SELECT " + MySQLiteHelper.COLUMN_SEM_GRADE + " FROM " + MySQLiteHelper.TABLE_SEMESTERS;
		
		String countQuery = "SELECT " + MySQLiteHelper.COLUMN_SEM_GRADE
				+ " FROM " + MySQLiteHelper.TABLE_COURSES + " , " + MySQLiteHelper.TABLE_TASKS + " , " + MySQLiteHelper.TABLE_SEMESTERS +
				" WHERE " + MySQLiteHelper.COLUMN_SEM_ID + " = " + MySQLiteHelper.COLUMN_SEM2COURSE_ID + " and " +
				MySQLiteHelper.COLUMN_COURSE_ID + " = " + MySQLiteHelper.COLUMN_COURSE2TASK_ID	
				;
		
		Cursor cursor = database.rawQuery(countQuery, null);

		int semCount = cursor.getCount();
		
		if(semCount == 0){
			return overallGPA;
		}
		
		
		cursor.moveToFirst();

		
		while (!cursor.isAfterLast()) {

			overallGPA += cursor.getFloat(0);
			
			cursor.moveToNext();
		}
		
		overallGPA = overallGPA/semCount;
		overallGPA = Calculator.round(overallGPA, 1);
	
		return overallGPA;
	}
	


}
