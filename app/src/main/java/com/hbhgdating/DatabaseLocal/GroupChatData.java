package com.hbhgdating.DatabaseLocal;

/**
 * Created by Developer on 12/6/17.
 */

public class GroupChatData {


    private String CHAT_ROOM_ID;
    private String GROUP_CHAT_DATA;
    private String GROUP_CHAT_IS_NEW;

    public GroupChatData() {
    }

    public GroupChatData(String CHAT_ROOM_ID, String GROUP_CHAT_DATA, String GROUP_CHAT_IS_NEW) {
        this.CHAT_ROOM_ID = CHAT_ROOM_ID;
        this.GROUP_CHAT_DATA = GROUP_CHAT_DATA;
        this.GROUP_CHAT_IS_NEW = GROUP_CHAT_IS_NEW;
    }


    public String getCHAT_ROOM_ID() {
        return CHAT_ROOM_ID;
    }

    public void setCHAT_ROOM_ID(String CHAT_ROOM_ID) {
        this.CHAT_ROOM_ID = CHAT_ROOM_ID;
    }

    public String getGROUP_CHAT_DATA() {
        return GROUP_CHAT_DATA;
    }

    public void setGROUP_CHAT_DATA(String GROUP_CHAT_DATA) {
        this.GROUP_CHAT_DATA = GROUP_CHAT_DATA;
    }

    public String getGROUP_CHAT_IS_NEW() {
        return GROUP_CHAT_IS_NEW;
    }

    public void setGROUP_CHAT_IS_NEW(String GROUP_CHAT_IS_NEW) {
        this.GROUP_CHAT_IS_NEW = GROUP_CHAT_IS_NEW;
    }
}
