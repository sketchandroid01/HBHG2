package com.hbhgdating.chat;

/**
 * Created by Developer on 10/26/16.
 */

public class ChatMessage {

    public String body, sender, receiver,note;
    public String Date, img;
    public String msgid;
    public String type;
    public boolean isMine;// Did I send the message.

    public ChatMessage(String mtype, String Sender, String Receiver, boolean isMINE) {
        isMine = isMINE;
        sender = Sender;
        receiver = Receiver;
        type=mtype;
    }

}

