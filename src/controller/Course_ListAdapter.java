package controller;

import java.util.List;



import com.calc.gpacalculator.R;
import com.calc.gpacalculator.Task;

import controller.Gpa_ListAdapter.TaskListHolder;
import com.calc.gpacalculator.*;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Course_ListAdapter extends ArrayAdapter<Course> {

	private List<Course> list;
	private int layoutResourceId;
	private Context context;
	
	public Course_ListAdapter(Context context, int resource, List<Course> items) {
		super(context, resource, items);
		this.layoutResourceId = resource;
		this.context = context;
		this.list = items;
	}
	
	
	public static class CourseListHolder {
		Course course;
		TextView name;
		TextView mark_value;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);
		
		CourseListHolder holder = new CourseListHolder();
		holder.course = list.get(position);
		
		holder.name = (TextView)row.findViewById(R.id.course_nameedit);
		holder.mark_value = (TextView)row.findViewById(R.id.course_markedit);
		row.setTag(holder);
		setupItem(holder);
		return row;
	
	}
	
	
	
	private void setupItem(CourseListHolder holder) {
		holder.name.setText(holder.course.getName());
		holder.mark_value.setText(String.valueOf(holder.course.getMark()));
	}

	




}

	
