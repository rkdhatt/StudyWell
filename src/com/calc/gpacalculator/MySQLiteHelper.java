package com.calc.gpacalculator;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_TASKS = "tasks";
	   public static final String COLUMN_ID = "_id";
	   public static final String COLUMN_NAME = "taskname";
	   public static final String COLUMN_AVG = "average";
	   public static final String COLUMN_TOTAL = "total";
	   public static final String COLUMN_COURSE2TASK_ID = "course2taskID";
	   public static final String COLUMN_WEIGHT = "weight";
	   public static final String COLUMN_GRADE = "grade";
	   
	   public static final String TABLE_COURSES = "courses";   
	   public static final String COLUMN_COURSE_NAME = "coursename";
	   public static final String COLUMN_COURSE_ID = "courseID";
	   public static final String COLUMN_SEM2COURSE_ID = "sem2courseID";
	   public static final String COLUMN_COURSE_GRADE = "coursegrade";
	   
	   
	   
	   public static final String TABLE_SEMESTERS = "semesters";   
	   public static final String COLUMN_SEM_NAME = "semestername";
	   public static final String COLUMN_SEM_ID = "semesterID";
	   public static final String COLUMN_SEM_GRADE = "semestergrade";
	   

	   private static final String DATABASE_NAME = "tasks.db";
	   
	   private static final int DATABASE_VERSION = 1;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE_TASKS = 
			  "create table " + TABLE_TASKS + "(" 
			  + COLUMN_ID + " integer primary key autoincrement, " 
			  + COLUMN_NAME + " TEXT," 
			  + COLUMN_AVG + " FLOAT," 
			  + COLUMN_TOTAL + " FLOAT," 
			  + COLUMN_COURSE2TASK_ID + " INTEGER, "
			  + COLUMN_WEIGHT + " FLOAT, "
			  + COLUMN_GRADE + " FLOAT"
			  + ")";

	  private static final String DATABASE_CREATE_COURSES =
			  "create table " + TABLE_COURSES + "("
			  + COLUMN_COURSE_ID + " integer primary key autoincrement, "
			  + COLUMN_COURSE_NAME + " TEXT,"
			  + COLUMN_SEM2COURSE_ID + " INTEGER,"
			  + COLUMN_COURSE_GRADE + " FLOAT"
			  + ")";
	  
	  private static final String DATABASE_CREATE_SEMESTERS =
			  "create table " + TABLE_SEMESTERS + "("
			  + COLUMN_SEM_ID + " integer primary key autoincrement, "
			  + COLUMN_SEM_NAME + " TEXT,"
			  + COLUMN_SEM_GRADE + " FLOAT"
			  + ")";
	  
	  
	 
	  public MySQLiteHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_TASKS);
		database.execSQL(DATABASE_CREATE_COURSES);
		database.execSQL(DATABASE_CREATE_SEMESTERS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEMESTERS);
		    onCreate(db);	
	}

}
