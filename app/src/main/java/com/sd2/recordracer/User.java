package com.sd2.recordracer;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class User {

    //user information
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

    private static final String usernameKey = "Username";
    private static final String encryptedPasswordKey = "Encrypted Password";
    private static final String useMetricSystemKey = "Using Metric System";
    private static final String weightKey = "Weight";
    private static final String preferredExerciseKey = "Preferred Exercise";
    private static final String emailKey = "Email";
    private static final String emailConfirmedKey = "Email Confirmed";
    private static final String automaticallyPauseMusicKey = "Automatically Pause Music";
    private static final String speedUpMusicKey = "Speed Up Music";
    private static final String slowDownMusicKey = "Slow Down Music";


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
    private int totalRuns;

    private static final String longestRunKey = "Longest Run";
    private static final String fastestMileRunKey = "Fastest Mile Run";
    private static final String fastestKilometerRunKey = "Fastest Kilometer Run";
    private static final String fastest5kRunKey = "Fastest 5k Run";
    private static final String totalMilesRunKey = "Total Miles Run";
    private static final String totalRunsKey = "Total Runs";

    //bike records
    private float longestRide;
    private int fastestMileBiked;
    private int fastestKilometerBiked;
    private int fastest5kBiked;
    private float totalMilesBiked;
    private int totalRides;

    private static final String longestRideKey = "Longest Ride";
    private static final String fastestMileBikedKey = "Fastest Mile Biked";
    private static final String fastestKilometerBikedKey = "Fastest Kilometer Biked";
    private static final String fastest5kBikedKey = "Fastest 5k Biked";
    private static final String totalMilesBikedKey = "Total Miles Biked";
    private static final String totalRidesKey = "Total Rides";

    //list of all exercises
    List<Exercise> exercises;

    private final String exercisesKey = "Exercises";

    public User(String username, String encryptedPassword, String email) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.email = email;
        weight = 0;
        preferredExercise = PreferredExercise.NONE;
        emailConfirmed=false;
        automaticallyPauseMusic=true;
        speedUpMusic=true;
        slowDownMusic=true;
        longestRun=0;
        fastestMileRun=3600; //60 minutes by default
        fastest5kRun=3600; //60 minutes by default
        fastestKilometerRun=3600; //60 minutes by default
        totalMilesRun = 0;
        totalRuns=0;
        longestRide=0;
        fastestMileBiked=3600; //60 minutes by default
        fastest5kBiked=3600; //60 minutes by default
        fastestKilometerBiked=3600; //60 minutes by default
        totalMilesBiked = 0;
        totalRides=0;


    }

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

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public float getLongestRide() {
        return longestRide;
    }

    public void setLongestRide(float longestRide) {
        this.longestRide = longestRide;
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

    /**
     * Takes all information in object and stores it in a Hashmap
     * @return map for use with database
     */
    public Map<String, Object> objectToMap() {
        Map<String, Object> map = new HashMap<String, Object>();

        //user information
        map.put(usernameKey, username);
        map.put(encryptedPasswordKey, encryptedPassword);
        map.put(useMetricSystemKey, Boolean.valueOf(useMetricSystem));
        map.put(weightKey, Integer.valueOf(weight));
        if (preferredExercise.compareTo(PreferredExercise.RUNNING)==0) {
            map.put(preferredExerciseKey,"run");
        } else if (preferredExercise.compareTo(PreferredExercise.BIKING)==0) {
            map.put(preferredExerciseKey,"bike");
        } else {
            map.put(preferredExerciseKey,"none");
        }
        map.put(emailKey, email);
        map.put(emailConfirmedKey,Boolean.valueOf(emailConfirmed));
        map.put(automaticallyPauseMusicKey, Boolean.valueOf(automaticallyPauseMusic));
        map.put(speedUpMusicKey, Boolean.valueOf(speedUpMusic));
        map.put(slowDownMusicKey, Boolean.valueOf(slowDownMusic));

        //run recrods
        map.put(longestRunKey, Float.valueOf(longestRun));
        map.put(fastestMileRunKey, Integer.valueOf(fastestMileRun));
        map.put(fastestKilometerRunKey, Integer.valueOf(fastestKilometerRun));
        map.put(fastest5kRunKey, Integer.valueOf(fastest5kRun));
        map.put(totalMilesRunKey, Float.valueOf(totalMilesRun));
        map.put(totalRunsKey,Integer.valueOf(totalRuns));

        //bike records
        map.put(longestRideKey, Float.valueOf(longestRide));
        map.put(fastestMileBikedKey, Integer.valueOf(fastestMileBiked));
        map.put(fastestKilometerBikedKey, Integer.valueOf(fastestKilometerBiked));
        map.put(fastest5kBikedKey, Integer.valueOf(fastest5kBiked));
        map.put(totalMilesBikedKey, Float.valueOf(totalMilesBiked));
        map.put(totalRidesKey,Integer.valueOf(totalRides));

        return map;
    }

    public static User mapToObject(Map<String, Object> map) {
        if (!mapIsValid(map)) {
            throw new IllegalArgumentException("Map does not have all attributes necessary to create a user");
        }
        User user = new User((String) map.get(usernameKey),(String) map.get(encryptedPasswordKey), (String) map.get(emailKey));

        Integer weight = (Integer) map.get(weightKey);
        user.setWeight(weight.intValue());

        String preferredExercise = (String) map.get(preferredExerciseKey);
        if (preferredExercise.compareTo("run")==0) {
            user.setPreferredExercise(PreferredExercise.RUNNING);
        } else if (preferredExercise.compareTo("bike")==0) {
            user.setPreferredExercise(PreferredExercise.BIKING);
        }
        //preferred exercise is none by default so no need to set it

        Boolean emailConfirmed = (Boolean) map.get(emailConfirmedKey);
        user.setEmailConfirmed(emailConfirmed.booleanValue());

        Boolean automaticallyPauseMusic = (Boolean) map.get(automaticallyPauseMusicKey);
        user.setAutomaticallyPauseMusic(automaticallyPauseMusic.booleanValue());

        Boolean speedUpMusic = (Boolean) map.get(speedUpMusicKey);
        user.setSpeedUpMusic(speedUpMusic.booleanValue());

        Boolean slowDownMusic = (Boolean) map.get(slowDownMusicKey);
        user.setSlowDownMusic(slowDownMusic.booleanValue());

        Float longestRun = (Float) map.get(longestRunKey);
        user.setLongestRun(longestRun.floatValue());

        Integer fastestMileRun = (Integer) map.get(fastestMileRunKey);
        user.setFastestMileRun(fastestMileRun.intValue());

        Integer fastest5kRun = (Integer) map.get(fastest5kRunKey);
        user.setFastest5kRun(fastest5kRun.intValue());

        Integer fastestKilometerRun = (Integer) map.get(fastestKilometerRunKey);
        user.setFastestKilometerRun(fastestKilometerRun.intValue());

        Float totalMilesRun = (Float) map.get(totalMilesRunKey);
        user.setTotalMilesRun(totalMilesRun.floatValue());

        Integer totalRuns = (Integer) map.get(totalRunsKey);
        user.setTotalRuns(totalRuns.intValue());

        Float longestRide = (Float) map.get(longestRideKey);
        user.setLongestRide(longestRide.floatValue());

        Integer fastestMileBiked = (Integer) map.get(fastestMileBikedKey);
        user.setFastestMileBiked(fastestMileBiked.intValue());

        Integer fastest5kBiked = (Integer) map.get(fastest5kBikedKey);
        user.setFastest5kBiked(fastest5kBiked.intValue());

        Integer fastestKilometerBiked = (Integer) map.get(fastestKilometerBikedKey);
        user.setFastestKilometerBiked(fastestKilometerBiked.intValue());

        Float totalMilesBiked = (Float) map.get(totalMilesBikedKey);
        user.setTotalMilesBiked(totalMilesBiked.floatValue());

        Integer totalRides = (Integer) map.get(totalRidesKey);
        user.setTotalRides(totalRides.intValue());

        return user;
    }

    public String toString() {
        return objectToMap().toString();
    }

    private static boolean mapIsValid(Map<String, Object> map) {
        return map.containsKey(usernameKey) && map.containsKey(encryptedPasswordKey) &&
                map.containsKey(useMetricSystemKey) && map.containsKey(weightKey) &&
                map.containsKey(preferredExerciseKey) && map.containsKey(emailKey) &&
                map.containsKey(emailConfirmedKey) && map.containsKey(automaticallyPauseMusicKey) &&
                map.containsKey(speedUpMusicKey) && map.containsKey(slowDownMusicKey) &&
                map.containsKey(longestRunKey) && map.containsKey(fastestMileRunKey) &&
                map.containsKey(fastestKilometerRunKey) && map.containsKey(fastest5kRunKey) &&
                map.containsKey(totalMilesRunKey) && map.containsKey(totalRunsKey) &&
                map.containsKey(longestRideKey) && map.containsKey(fastestMileBikedKey) &&
                map.containsKey(fastestKilometerBikedKey) && map.containsKey(fastest5kBikedKey) &&
                map.containsKey(totalMilesBikedKey) && map.containsKey(totalRidesKey);
    }


}
