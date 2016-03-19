package com.sd2.recordracer;


import java.util.List;


public class User {

    private String username;
    private String encryptedPassword;
    private boolean useMetricSystem;
    private int weight; //in pounds
    private PreferredExercise preferredExercise;
    private String email;
    private boolean emailConfirmed;
    private boolean automaticallyPauseMusic;
    private boolean speedUpMusic;
    private boolean slowDownMusic;

    /**
     * distances stored in miles
     * times stored in seconds
     */

    //run records
    private float longestRun;
    private int fastestMileRun;
    private int fastestKilometerRun;
    private int fastest5kRun;
    private float totalMilesRun;
    private float totalKilometersRun;
    private int totalRuns;

    //bike records
    private float longestRides;
    private int fastestMileBiked;
    private int fastestKilometerBiked;
    private int fastest5kBiked;
    private float totalMilesBiked;
    private float totalKilometersBiked;
    private int totalRides;

    //list of all exercises
    List<Exercise> exercises;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public boolean isUseMetricSystem() {
        return useMetricSystem;
    }

    public void setUseMetricSystem(boolean useMetricSystem) {
        this.useMetricSystem = useMetricSystem;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public PreferredExercise getPreferredExercise() {
        return preferredExercise;
    }

    public void setPreferredExercise(PreferredExercise preferredExercise) {
        this.preferredExercise = preferredExercise;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public boolean isAutomaticallyPauseMusic() {
        return automaticallyPauseMusic;
    }

    public void setAutomaticallyPauseMusic(boolean automaticallyPauseMusic) {
        this.automaticallyPauseMusic = automaticallyPauseMusic;
    }

    public boolean isSpeedUpMusic() {
        return speedUpMusic;
    }

    public void setSpeedUpMusic(boolean speedUpMusic) {
        this.speedUpMusic = speedUpMusic;
    }

    public boolean isSlowDownMusic() {
        return slowDownMusic;
    }

    public void setSlowDownMusic(boolean slowDownMusic) {
        this.slowDownMusic = slowDownMusic;
    }

    public float getLongestRun() {
        return longestRun;
    }

    public void setLongestRun(float longestRun) {
        this.longestRun = longestRun;
    }

    public int getFastestMileRun() {
        return fastestMileRun;
    }

    public void setFastestMileRun(int fastestMileRun) {
        this.fastestMileRun = fastestMileRun;
    }

    public int getFastestKilometerRun() {
        return fastestKilometerRun;
    }

    public void setFastestKilometerRun(int fastestKilometerRun) {
        this.fastestKilometerRun = fastestKilometerRun;
    }

    public int getFastest5kRun() {
        return fastest5kRun;
    }

    public void setFastest5kRun(int fastest5kRun) {
        this.fastest5kRun = fastest5kRun;
    }

    public float getTotalMilesRun() {
        return totalMilesRun;
    }

    public void setTotalMilesRun(float totalMilesRun) {
        this.totalMilesRun = totalMilesRun;
    }

    public float getTotalKilometersRun() {
        return totalKilometersRun;
    }

    public void setTotalKilometersRun(float totalKilometersRun) {
        this.totalKilometersRun = totalKilometersRun;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public float getLongestRides() {
        return longestRides;
    }

    public void setLongestRides(float longestRides) {
        this.longestRides = longestRides;
    }

    public int getFastestMileBiked() {
        return fastestMileBiked;
    }

    public void setFastestMileBiked(int fastestMileBiked) {
        this.fastestMileBiked = fastestMileBiked;
    }

    public int getFastestKilometerBiked() {
        return fastestKilometerBiked;
    }

    public void setFastestKilometerBiked(int fastestKilometerBiked) {
        this.fastestKilometerBiked = fastestKilometerBiked;
    }

    public int getFastest5kBiked() {
        return fastest5kBiked;
    }

    public void setFastest5kBiked(int fastest5kBiked) {
        this.fastest5kBiked = fastest5kBiked;
    }

    public float getTotalMilesBiked() {
        return totalMilesBiked;
    }

    public void setTotalMilesBiked(float totalMilesBiked) {
        this.totalMilesBiked = totalMilesBiked;
    }

    public float getTotalKilometersBiked() {
        return totalKilometersBiked;
    }

    public void setTotalKilometersBiked(float totalKilometersBiked) {
        this.totalKilometersBiked = totalKilometersBiked;
    }

    public int getTotalRides() {
        return totalRides;
    }

    public void setTotalRides(int totalRides) {
        this.totalRides = totalRides;
    }

    public void setExercises(List exercises) {
        this.exercises = exercises;
    }

    public List getExercises() {
        return exercises;
    }


    private enum PreferredExercise {
        RUNNING, BIKING, NONE
    }


}
