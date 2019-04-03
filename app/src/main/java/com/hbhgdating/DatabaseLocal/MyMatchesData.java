package com.hbhgdating.DatabaseLocal;

/**
 * Created by Developer on 12/6/17.
 */

public class MyMatchesData {

    private String MY_MATCHES_USER_ID;
    private String MY_MATCHES_DATA;
    private String MY_MATCHES_INCOMING_MSG_TIME;
    private String CHAT_IS_NEW;

    public MyMatchesData() {
    }


    public MyMatchesData(String MY_MATCHES_USER_ID, String MY_MATCHES_DATA, String MY_MATCHES_INCOMING_MSG_TIME, String CHAT_IS_NEW) {
        this.MY_MATCHES_USER_ID = MY_MATCHES_USER_ID;
        this.MY_MATCHES_DATA = MY_MATCHES_DATA;
        this.MY_MATCHES_INCOMING_MSG_TIME = MY_MATCHES_INCOMING_MSG_TIME;
        this.CHAT_IS_NEW = CHAT_IS_NEW;

    }


    public String getMY_MATCHES_USER_ID() {
        return MY_MATCHES_USER_ID;
    }

    public void setMY_MATCHES_USER_ID(String MY_MATCHES_USER_ID) {
        this.MY_MATCHES_USER_ID = MY_MATCHES_USER_ID;
    }

    public String getMY_MATCHES_DATA() {
        return MY_MATCHES_DATA;
    }

    public void setMY_MATCHES_DATA(String MY_MATCHES_DATA) {
        this.MY_MATCHES_DATA = MY_MATCHES_DATA;
    }

    public String getMY_MATCHES_INCOMING_MSG_TIME() {
        return MY_MATCHES_INCOMING_MSG_TIME;
    }

    public void setMY_MATCHES_INCOMING_MSG_TIME(String MY_MATCHES_INCOMING_MSG_TIME) {
        this.MY_MATCHES_INCOMING_MSG_TIME = MY_MATCHES_INCOMING_MSG_TIME;
    }

    public String getCHAT_IS_NEW() {
        return CHAT_IS_NEW;
    }

    public void setCHAT_IS_NEW(String CHAT_IS_NEW) {
        this.CHAT_IS_NEW = CHAT_IS_NEW;
    }
}
