package com.frandom.api;

import javax.websocket.Session;

/**
 * Created with IntelliJ IDEA.
 * User: amrit
 * Date: 5/22/14
 * Time: 11:26 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OnlineUser extends User {
    Session getSession();
}
