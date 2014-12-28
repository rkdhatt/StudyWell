package com.calc.gpacalculator;

import java.util.ArrayList;

public class User {

	private String user_name;
	private ArrayList <Course> semester;
	private float gpa;
	
	public User(String user_name, ArrayList<Course> semester, int gpa){
		this.user_name = user_name;
		this.semester = semester;
		this.gpa = gpa;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public ArrayList<Course> getSemester() {
		return semester;
	}

	public void setSemester(ArrayList<Course> semester) {
		this.semester = semester;
	}

	public float getGpa() {
		return gpa;
	}

	public void setGpa(float gpa) {
		this.gpa = gpa;
	}
	
	
}



