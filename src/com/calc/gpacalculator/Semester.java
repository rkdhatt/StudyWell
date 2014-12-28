package com.calc.gpacalculator;

public class Semester {

	private String name;
	private float mark_value;
	private int sID;
	
	public Semester(String name, int sID, float marks) {
		this.name = name;
		this.sID = sID;
		this.mark_value = marks;
		
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
