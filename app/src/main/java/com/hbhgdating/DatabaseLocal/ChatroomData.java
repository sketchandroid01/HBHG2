package com.hbhgdating.DatabaseLocal;

/**
 * Created by Developer on 12/6/17.
 */

public class ChatroomData {

    private String CHATROOM_DB_ID;
    private String CHATROOM_DATA;
    private String CHATROOM_INCOMING_MSG_TIME;
    private String CHATROOM_CHAT_IS_NEW;

    public ChatroomData() {
    }



    public String getCHATROOM_DB_ID() {
        return CHATROOM_DB_ID;
    }

    public void setCHATROOM_DB_ID(String CHATROOM_DB_ID) {
        this.CHATROOM_DB_ID = CHATROOM_DB_ID;
    }

    public String getCHATROOM_DATA() {
        return CHATROOM_DATA;
    }

    public void setCHATROOM_DATA(String CHATROOM_DATA) {
        this.CHATROOM_DATA = CHATROOM_DATA;
    }

    public String getCHATROOM_INCOMING_MSG_TIME() {
        return CHATROOM_INCOMING_MSG_TIME;
    }

    public void setCHATROOM_INCOMING_MSG_TIME(String CHATROOM_INCOMING_MSG_TIME) {
        this.CHATROOM_INCOMING_MSG_TIME = CHATROOM_INCOMING_MSG_TIME;
    }

    public String getCHATROOM_CHAT_IS_NEW() {
        return CHATROOM_CHAT_IS_NEW;
    }

    public void setCHATROOM_CHAT_IS_NEW(String CHATROOM_CHAT_IS_NEW) {
        this.CHATROOM_CHAT_IS_NEW = CHATROOM_CHAT_IS_NEW;
    }
}
