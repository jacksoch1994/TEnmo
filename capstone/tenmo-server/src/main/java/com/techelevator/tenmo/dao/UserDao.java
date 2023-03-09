package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDao {

    /**
     * Obtains a List of all Users.
     *
     * @return a List containing all Users.
     */
    List<User> findAll();

    /**
     * Returns the User with the specified id. Returns null if no User by that id exists.
     *
     * @param id the id of the User.
     * @return the User with the specified id.
     */
    User getUserById(int id);

    /**
     * Returns the User with the specified id. Returns null if no user by that id exists.
     *
     * @param username the username of the User.
     * @return the User with the specified username.
     */
    User findByUsername(String username);

    /**
     * Returns the id of the User with the specified username.
     *
     * @param username the username of the User.
     * @return the id of the specified User.
     */
    int findIdByUsername(String username);

    /**
     * Creates a new User.
     *
     * @param username the username of the new User to be created.
     * @param password the password of the new User to be created.
     * @return a boolean indicating successful user creation.
     */
    boolean create(String username, String password);
}
