package com.frandom;

import com.frandom.api.OnlineUser;

import javax.websocket.Session;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amrit
 * Date: 5/22/14
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class OnlineUserImpl extends UserImpl implements OnlineUser{

    Session session;

    public OnlineUserImpl(String displayName, String name, String id, String fbId, String emailId,
                          Set<String> friendIDs, Set<String> fbFriendIDs, Session session) {
        super(displayName, name, id, fbId, emailId, friendIDs, fbFriendIDs);
        this.session = session;
    }

    @Override
    public Session getSession() {
        return session;
    }
}
