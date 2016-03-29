package com.sd2.recordracer;

import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExerciseTest {
    @Test
    public void testUserInitialization() {

        User user = new User("Username", "Password", "Email");
        assert user.isEmailConfirmed()==false;
        assert user.getExercises().size()==0;
        assert user.getEncryptedPassword().compareTo("Password")==0;
    }

    @Test
    public void testUserToMap() {

        Date date = new Date();
        Exercise exercise1 = new Exercise("run", (float) 6.3, 3600, 600, 821, date);
        Exercise exercise2 = new Exercise("run", (float) 6.1, 3330, 600, 801, date);
        List<Exercise> exercises = new LinkedList<Exercise>();
        exercises.add(exercise1);
        exercises.add(exercise2);
        User user = new User("Username", "Password", "Email");
        user.setExercises(exercises);
        Map<String, Object> map = user.objectToMap();


    }

}
