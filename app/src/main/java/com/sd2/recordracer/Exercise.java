package com.sd2.recordracer;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Exercise implements Serializable {

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
     * @param type exercise type. Either "Running" or "Biking"
     * @param distance total distance in miles
     * @param timeElapsed total time elapsed in seconds
     * @param targetPace target pace set in minutes / mile
     * @param caloriesBurned total number of calories burnt
     * @param date date and time of exercise
     */
    public Exercise(String type, float distance, int timeElapsed, int targetPace, int caloriesBurned, Date date) {
        if (type.equals("Running")) {
            exerciseType = ExerciseType.RUNNING;
        } else if (type.equals("Biking")) {
            exerciseType = ExerciseType.BIKING;
        } else {
            throw new IllegalArgumentException("The exercise type must be either \"Running\" or \"Biking\"");
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

    /**
     * Takes all information in object and stores it in a Hashmap
     * @return map for use with database
     */
    public Map<String, Object> objectToMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (exerciseType.compareTo(ExerciseType.RUNNING)==0) {
            map.put("Type", "Running");
        } else {
            map.put("Type", "Biking");
        }
        map.put("Distance", Float.valueOf(distance));
        map.put("Time Elapsed", Integer.valueOf(timeElapsed));
        map.put("Target Pace", Integer.valueOf(targetPace));
        map.put("Calories Burned", Integer.valueOf(caloriesBurned));
        map.put("Date", date);
        return map;
    }

    public static Exercise mapToObject(Map<String, Object> map) {
        if (!mapIsValid(map)) {
            throw new IllegalArgumentException("Map does not have all attributes necessary to create an exercise");
        }
        Float distance = (Float)map.get("Distance");
        Integer timeElapsed = (Integer)map.get("Time Elapsed");
        Integer targetPace = (Integer)map.get("Target Pace");
        Integer caloriesBurned = (Integer)map.get("Calories Burned");
        return new Exercise((String) map.get("Type"), distance.floatValue(), timeElapsed.intValue(),
                targetPace.intValue(), caloriesBurned.intValue(), (Date) map.get("Date"));

    }

    private static boolean mapIsValid (Map<String, Object> map) {
        return map.containsKey("Distance") && map.containsKey("Time Elapsed") && map.containsKey("Target Pace")
                && map.containsKey("Calories Burned") && map.containsKey("Type");

    }

    public String toString() {
        return objectToMap().toString();
    }

}
