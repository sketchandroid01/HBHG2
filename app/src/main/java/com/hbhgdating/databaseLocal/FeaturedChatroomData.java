package com.hbhgdating.databaseLocal;

/**
 * Created by Developer on 12/12/17.
 */

public class FeaturedChatroomData {

    private String F_CHATROOM_DB_ID;
    private String F_CHATROOM_DATA;
    private String F_CHATROOM_INCOMING_MSG_TIME;
    private String F_CHATROOM_IS_NEW;


    public FeaturedChatroomData() {
    }


    public FeaturedChatroomData(String f_CHATROOM_DB_ID, String f_CHATROOM_DATA,
                                String f_CHATROOM_INCOMING_MSG_TIME, String f_CHATROOM_IS_NEW) {
        F_CHATROOM_DB_ID = f_CHATROOM_DB_ID;
        F_CHATROOM_DATA = f_CHATROOM_DATA;
        F_CHATROOM_INCOMING_MSG_TIME = f_CHATROOM_INCOMING_MSG_TIME;
        F_CHATROOM_IS_NEW = f_CHATROOM_IS_NEW;
    }


    public String getF_CHATROOM_DB_ID() {
        return F_CHATROOM_DB_ID;
    }

    public void setF_CHATROOM_DB_ID(String f_CHATROOM_DB_ID) {
        F_CHATROOM_DB_ID = f_CHATROOM_DB_ID;
    }

    public String getF_CHATROOM_DATA() {
        return F_CHATROOM_DATA;
    }

    public void setF_CHATROOM_DATA(String f_CHATROOM_DATA) {
        F_CHATROOM_DATA = f_CHATROOM_DATA;
    }

    public String getF_CHATROOM_INCOMING_MSG_TIME() {
        return F_CHATROOM_INCOMING_MSG_TIME;
    }

    public void setF_CHATROOM_INCOMING_MSG_TIME(String f_CHATROOM_INCOMING_MSG_TIME) {
        F_CHATROOM_INCOMING_MSG_TIME = f_CHATROOM_INCOMING_MSG_TIME;
    }

    public String getF_CHATROOM_IS_NEW() {
        return F_CHATROOM_IS_NEW;
    }

    public void setF_CHATROOM_IS_NEW(String f_CHATROOM_IS_NEW) {
        F_CHATROOM_IS_NEW = f_CHATROOM_IS_NEW;
    }
}
