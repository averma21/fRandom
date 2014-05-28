package com.frandom.api;

import java.util.Set;

/**
 * User manager.
 */
public interface UserManager {

    /**
     * @param userID userId
     * @return true if user is online.
     */
    boolean isOnline(String userID);

    /**
     * @param userID userID
     * @return true if such a user exists.
     */
    boolean isValidUser(String userID);

    /**
     * @param fbUserID facebook user id.
     * @return true if the fb user is registered with this app.
     */
    boolean isRegistered(String fbUserID);

    /**
     * @return online users.
     */
    Set<OnlineUser> getOnlineUsers();

    /**
     * @param id user id
     * @return {@link User}
     */
    User getUserByID(String id);

    /**
     * @param fbId fb user id
     * @return {@link User}
     */
    User getUserByFbID(String fbId);

    /**
     * @param user new online user to be added.
     */
    void addOnlineUser(OnlineUser user);

    /**
     * Get online friends of passed user.
     * @param user user
     * @return online friends ({@link User}s)
     */
    Set<OnlineUser> getOnlineFriendsOf(User user);

}
