package com.frandom;

import com.frandom.api.OnlineUser;
import com.frandom.api.User;
import com.frandom.api.UserManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amrit
 * Date: 5/14/14
 * Time: 12:56 AM
 * To change this template use File | Settings | File Templates.
 */
@Named
public class UserManagerImpl implements UserManager {

    private final Map<String, OnlineUser> userIDtoUserMap;
    private final Map<String, OnlineUser> fbUserIDtoUserMap;


    public UserManagerImpl() {
        userIDtoUserMap = new HashMap<String, OnlineUser>();
        fbUserIDtoUserMap = new HashMap<String, OnlineUser>();
    }

    @Override
    public boolean isOnline(String userID) {
        return userIDtoUserMap.containsKey(userID);
    }

    @Override
    public boolean isValidUser(String userID) {
        //TODO move this to check from persistent storage
        return userIDtoUserMap.containsKey(userID);
    }

    @Override
    public boolean isRegistered(String fbUserID) {
        return fbUserIDtoUserMap.containsKey(fbUserID);
    }

    @Override
    public Set<OnlineUser> getOnlineUsers() {
        return new HashSet<OnlineUser>(userIDtoUserMap.values());
    }

    @Override
    public void addOnlineUser(OnlineUser user) {
        userIDtoUserMap.put(user.getID(), user);
        fbUserIDtoUserMap.put(user.getFbID(), user);
    }

    @Override
    public Set<OnlineUser> getOnlineFriendsOf(User user) {
        Set<OnlineUser> friends = new HashSet<OnlineUser>();
        for (String id : user.getFriendsIDs()) {
            if (userIDtoUserMap.containsKey(id)) {
                friends.add(userIDtoUserMap.get(id));
            }
        }
        return friends;
    }

    @Override
    public User getUserByID(String id) {
        return userIDtoUserMap.get(id);
    }

    @Override
    public User getUserByFbID(String fbId) {
        return fbUserIDtoUserMap.get(fbId);
    }
}
