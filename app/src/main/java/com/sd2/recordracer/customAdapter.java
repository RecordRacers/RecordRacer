package com.sd2.recordracer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

/*Used to display data about previous runs dynamically
*/
public class customAdapter extends ArrayAdapter<Exercise> {

    private TextView distance_ran;
    private TextView time_elapsed;
    private TextView target_pace;
    private TextView caloriesBurned;
    private TextView date;
    private Exercise exercise;
    private Format formatter = new SimpleDateFormat("yyyy-MM-dd");

    customAdapter(Context context, List exercise_history) {
        super(context, R.layout.history_entry, exercise_history);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        exercise = getItem(position);
        LayoutInflater inflate = LayoutInflater.from(getContext());
        View row = inflate.inflate(R.layout.history_entry, parent, false);
        distance_ran = (TextView) row.findViewById(R.id.distance);
        distance_ran.setText(Float.toString(exercise.getDistance()));
        time_elapsed = (TextView) row.findViewById(R.id.time_elapsed);
        time_elapsed.setText(Integer.toString(exercise.getTimeElapsed()));
        target_pace = (TextView) row.findViewById(R.id.target);
        target_pace.setText(Integer.toString(exercise.getTargetPace()));
        caloriesBurned = (TextView) row.findViewById(R.id.calories);
        caloriesBurned.setText(Integer.toString(exercise.getCaloriesBurned()));
        date = (TextView) row.findViewById(R.id.date);
        date.setText(formatter.format(exercise.getDate()));
        return row;
    }
}
