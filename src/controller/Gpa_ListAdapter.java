package controller;


import java.util.List;

import javax.xml.datatype.Duration;








import com.calc.gpacalculator.R;
import com.calc.gpacalculator.Task;


import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class Gpa_ListAdapter extends ArrayAdapter<Task>{
	
	private List<Task> list;
	private int layoutResourceId;
	private Context context;
	
	public Gpa_ListAdapter(Context context, int layoutResourceId, List<Task> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.list = items;
	}
	
	
	public static class TaskListHolder {
		Task task;
		TextView name;
		TextView mark_value;
		TextView total_value;
		TextView weight_value;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);
		
		TaskListHolder holder = new TaskListHolder();
		holder.task = list.get(position);
		
		holder.name = (TextView)row.findViewById(R.id.task_nameedit);
		holder.mark_value = (TextView)row.findViewById(R.id.your_markedit);
		holder.total_value = (TextView)row.findViewById(R.id.total_edit);
		holder.weight_value = (TextView)row.findViewById(R.id.task_weightedit);
		row.setTag(holder);
		setupItem(holder);
		return row;
		
		
	}
	

	private void setupItem(TaskListHolder holder) {
		holder.name.setText(holder.task.getName());
		holder.mark_value.setText(String.valueOf(holder.task.getAverage()));
		holder.total_value.setText(String.valueOf(holder.task.getTotal_marks()));
		holder.weight_value.setText(String.valueOf(holder.task.getWeight()));
	}
	


}
