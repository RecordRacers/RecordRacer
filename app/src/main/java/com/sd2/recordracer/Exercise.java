package com.sd2.recordracer;

import android.util.Log;

import java.util.Date;

public class Exercise {

    /**
     * distances stored in miles
     * times stored in seconds
     */
    private ExerciseType exerciseType;
    private float distance;
    private int timeElapsed;
    private int targetPace;
    private int caloriesBurned;
    private Date date;

    /**
     *
     * @param type exercise type. Either "run" or "bike"
     * @param distance total distance in miles
     * @param timeElapsed total time elapsed in seconds
     * @param targetPace target pace set in minutes / mile
     * @param caloriesBurned total number of calories burnt
     * @param date date and time of exercise
     */
    public Exercise(String type, float distance, int timeElapsed, int targetPace, int caloriesBurned, Date date) {
        try {
            if (type.equals("run")) {
                exerciseType = ExerciseType.RUNNING;
            } else if (type.equals("bike")) {
                exerciseType = ExerciseType.BIKING;
            } else {
                throw new IllegalArgumentException("The exercise type must be either \"run\" or \"bike\"");
            }
        }catch (Exception e) {
            Log.d(TAG, e + "\n Exercise saved without a type");
        }
        this.distance=distance;
        this.timeElapsed=timeElapsed;
        this.caloriesBurned=caloriesBurned;
        this.date = date;
        this.targetPace=targetPace;

    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(int timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public int getTargetPace() {
        return targetPace;
    }

    public void setTargetPace(int targetPace) {
        this.targetPace = targetPace;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private enum ExerciseType {
        RUNNING, BIKING
    }

}
