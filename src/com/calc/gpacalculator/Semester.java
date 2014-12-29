package com.calc.gpacalculator;

public class Semester {

	private String name;
	private float mark_value;
	private int sID;
	private float semgoal_gpa;
	
	/*
	 * add goal gpa
	 * 
	 * 
	 * 
	 */
	
	public Semester(String name, int sID, float marks, float goal) {
		this.name = name;
		this.sID = sID;
		this.mark_value = marks;
		this.semgoal_gpa = goal;
		
	}

	public float getSemgoal_gpa() {
		return semgoal_gpa;
	}

	public void setSemgoal_gpa(float semgoal_gpa) {
		this.semgoal_gpa = semgoal_gpa;
	}

	public String getName() {
		return name;
	}

	public  int getID() {
		return sID;
	}

	public  void setID(int iD) {
		this.sID = iD;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getMark_value() {
		return mark_value;
	}

	public void setMark_value(float mark_value) {
		this.mark_value = mark_value;
	}
	
	
	
}
