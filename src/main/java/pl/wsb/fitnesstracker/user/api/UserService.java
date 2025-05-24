package pl.wsb.fitnesstracker.user.api;

import pl.wsb.fitnesstracker.user.api.User;

/**
 * Interface (API) for modifying operations on {@link User} entities through the API.
 * Implementing classes execute changes within a transaction.
 */
public interface UserService {
    User createUser(User user);
}

