package com.sd2.recordracer;

public interface Dao {
    /**
     * @return true if the user was successfully created. False if there was an error.
     */
    public boolean createUser(String username, String encryptedPassword, String email, String sport, int height, int weight, boolean useCentimeters, boolean useKilograms);

    /**
     * @return user with specified username if one exists. Null otherwise
     */
    public User getUserByUsername(String username);

    /**
     * @return user with specified email address if one exists. Null otherwise
     */
    public User getUserByEmail(String email);

    /**
     * @param user to be updated
     * @throws IllegalArgumentException if the user is not in the database
     */
    public void updateUser(User user) throws IllegalArgumentException;




}
