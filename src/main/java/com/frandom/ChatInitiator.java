package com.frandom;

import com.frandom.api.OnlineUser;
import com.frandom.api.User;
import com.frandom.api.UserManager;

import javax.inject.Named;
import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import java.io.StringReader;
import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: amrit
 * Date: 5/11/14
 * Time: 10:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Named
@ServerEndpoint("/chat")
public class ChatInitiator {

    public static final String FB_USER_ID_PARAM = "fbUserID";
    public static final String FB_USER_NAME_PARAM = "fbUserName";
    public static final String FB_FRIENDS_ID_LIST_PARAM = "fbFriends";
    public static final String DISPLAY_NAME = "FRandom user";

    private static SecureRandom random = new SecureRandom();

    @Inject
    private UserManager userManager;

    public String nextUserId() {
        return new BigInteger(130, random).toString(32);
    }

    @OnOpen
    public void open(Session session) {
        //sessionHandler.addSession(session);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonMessage = reader.readObject();
        String fbUserID = null;
        String fbUserName = null;
        try {
            fbUserID = jsonMessage.getString(FB_USER_ID_PARAM);
        } catch (NullPointerException e) {
            //ignore
        }
        try {
            if (fbUserID != null) {
                fbUserName = jsonMessage.getString(FB_USER_NAME_PARAM);
                JsonArray jsonArray = jsonMessage.getJsonArray(FB_FRIENDS_ID_LIST_PARAM);
                Set<String> fbFriendIDs = new HashSet<String>();
                Set<String> friendsIDs = new HashSet<String>();
                for (JsonValue jsonValue: jsonArray) {
                    String fbFriendID = jsonValue.toString();
                    fbFriendIDs.add(fbFriendID);
                    friendsIDs.add(userManager.getUserByFbID(fbFriendID).getID());
                }
                OnlineUser user = new OnlineUserImpl(DISPLAY_NAME, fbUserName, nextUserId(), fbUserID, "", friendsIDs,
                        fbFriendIDs, session);
                userManager.addOnlineUser(user);
                session.getBasicRemote().sendText("added : " + fbUserID);
            } else {
                session.getBasicRemote().sendText("received : " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /*public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException {

        String fbUserID = request.getParameter(FB_USER_ID_PARAM);
        String fbUserName = request.getParameter(FB_USER_NAME_PARAM);
        String [] fbFriends = request.getParameterValues(FB_FRIENDS_ID_LIST_PARAM);
        Set<String> friendsIDs = new HashSet<String>();
        Set<String> fbFriendIDs = new HashSet<String>();
        boolean error = true;
        if (fbUserID != null && fbUserName != null) {
            error = false;
            if (fbFriends != null) {
                for (String fbID : fbFriends) {
                    friendsIDs.add(userManager.getUserByFbID(fbID).getID());
                    fbFriendIDs.add(fbID);
                }
            }
            User user = new UserImpl(DISPLAY_NAME, fbUserName, nextUserId(), fbUserID, "", friendsIDs, fbFriendIDs);
            userManager.addOnlineUser(user);
        }
        response.setContentType("text/html");
        try {
            PrintWriter out = response.getWriter();

            if (!error) {
                out.println(fbUserID);
            } else {
                out.println("Error : Data Insufficient");
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    } */
}
