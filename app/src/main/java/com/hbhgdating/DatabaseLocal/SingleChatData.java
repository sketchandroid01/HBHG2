package com.hbhgdating.DatabaseLocal;

/**
 * Created by Developer on 12/6/17.
 */

public class SingleChatData {


    private String CHAT_WITH_ID;
    private String CHAT_DATA;



    public SingleChatData() {
    }

    public SingleChatData(String CHAT_WITH_ID, String CHAT_DATA, String CHAT_IS_NEW) {
        this.CHAT_WITH_ID = CHAT_WITH_ID;
        this.CHAT_DATA = CHAT_DATA;

    }


    public String getCHAT_WITH_ID() {
        return CHAT_WITH_ID;
    }

    public void setCHAT_WITH_ID(String CHAT_WITH_ID) {
        this.CHAT_WITH_ID = CHAT_WITH_ID;
    }

    public String getCHAT_DATA() {
        return CHAT_DATA;
    }

    public void setCHAT_DATA(String CHAT_DATA) {
        this.CHAT_DATA = CHAT_DATA;
    }


}
