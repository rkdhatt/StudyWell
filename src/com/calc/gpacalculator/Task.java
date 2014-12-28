package com.calc.gpacalculator;

public class Task {

	private String task_name;
	private float task_average;
	private float total_marks;
	private int semester_ID;
	private String class_name;
	private int ID;
	private float weight;
	private float grade;
	
	public String getClass_name() {
		return class_name;
	}


	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}


	public Task(int id, String name_of_task, int class_average, int class_total, int sem, float task_weight, float task_grade){
		this.ID = id;
		this.task_name = name_of_task;
		this.task_average = class_average; 
		this.total_marks = class_total;
		this.semester_ID = sem;
		this.weight = task_weight;
		this.grade = task_grade;
	}


	public float getGrade() {
		return grade;
	}


	public void setGrade(float grade) {
		this.grade = grade;
	}


	public float getWeight() {
		return weight;
	}


	public void setWeight(float weight) {
		this.weight = weight;
	}


	public int getID() {
		return ID;
	}


	public void setID(int iD) {
		this.ID = iD;
	}


	public int getSemester_ID() {
		return semester_ID;
	}


	public void setSemester_ID(int semester) {
		this.semester_ID = semester;
	}


	public String getName() {
		return task_name;
	}


	public void setName(String name) {
		this.task_name = name;
	}


	public float getAverage() {
		return task_average;
	}


	public void setAverage(float average) {
		this.task_average = average;
	}


	public float getTotal_marks() {
		return total_marks;
	}


	public void setTotal_marks(float f) {
		this.total_marks = f;
	}

}
