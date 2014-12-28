package com.calc.gpacalculator;

import java.util.ArrayList;

public class Course {

	private String name;
	private float mark_value;
	private int ID;
	private int sem2course;
	
	
	


	public Course(String name, float mark_value, int cID, int s2c){
		this.name = name;
		this.mark_value = mark_value;
		this.ID = cID;
		this.sem2course = s2c;
		
	}

	public int getSem2course() {
		return sem2course;
	}

	public void setSem2course(int sem2course) {
		this.sem2course = sem2course;
	}

	public int getID() {
		return ID;
	}

	public  void setID(int iD) {
		this.ID = iD;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public float getMark() {
		return mark_value;
	}


	public void setMark(float mark_value) {
		this.mark_value = mark_value;
	}
	
	public static float courseGrade2GPA(float mark) {
		
		int rounded_mark = Math.round(mark);
		
		if (90 <= rounded_mark) {
			return (float) 4.0;
		}
		else if ((85 <= rounded_mark) && (rounded_mark <= 89)) {	
			return (float) 3.9;
		}
		else if ((80 <= rounded_mark) && (rounded_mark <= 84)) {	
			return (float) 3.7;
		}
		else if ((75 <= rounded_mark) && (rounded_mark <= 79)) {	
			return (float) 3.3;
		}
		else if ((70 <= rounded_mark) && (rounded_mark <= 74)) {	
			return (float) 3.0;
		}
		else if ((65 <= rounded_mark) && (rounded_mark <= 69)) {	
			return (float) 2.7;
		}
		else if ((60 <= rounded_mark) && (rounded_mark <= 64)) {	
			return (float) 2.3;
		}
		else if ((55 <= rounded_mark) && (rounded_mark <= 59)) {	
			return (float) 2.0;
		}
		/*else if ((50 <= rounded_mark) && (rounded_mark <= 54)) {	
			return (float) 1.7;
		}
		else if ((50 <= rounded_mark) && (rounded_mark <= 54)) {	
			return (float) 1.3;
		}*/
		else if ((50 <= rounded_mark) && (rounded_mark <= 54)) {	
			return (float) 1.0;
		}
		else if (rounded_mark <= 49) {	
			return (float) 0.0;
		}
		
		else {
			rounded_mark = 0;
			return rounded_mark;
			
		}
		
	}


}
