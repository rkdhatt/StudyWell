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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Semester_ListAdapter extends ArrayAdapter<Semester> {

		private List<Semester> list;
		private int layoutResourceId;
		private Context context;
		
		public Semester_ListAdapter(Context context, int resource, List<Semester> items) {
			super(context, resource, items);
			this.layoutResourceId = resource;
			this.context = context;
			this.list = items;
		}
		
		
		public static class SemesterListHolder {
			Semester semester;
			TextView name;
			TextView mark_value;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			SemesterListHolder holder = new SemesterListHolder();
			holder.semester = list.get(position);
			
			holder.name = (TextView)row.findViewById(R.id.sem_nameedit);
			holder.mark_value = (TextView)row.findViewById(R.id.sem_gpaedit);
			row.setTag(holder);
			setupItem(holder);
			return row;
		
		}
		
		private void setupItem(SemesterListHolder holder) {
			holder.name.setText(holder.semester.getName());
			holder.mark_value.setText(String.valueOf(holder.semester.getMark_value()));
		}

	}

		