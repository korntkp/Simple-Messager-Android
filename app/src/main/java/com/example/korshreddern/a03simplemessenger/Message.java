package com.example.korshreddern.a03simplemessenger;

/**
 * Created by Korshreddern on 21-Apr-16.
 */
public class Message {
    public String seqno;
    public String fromuser;
    public String touser;
    public String message;
    public String msgdate;

    public Message() {
        this.seqno = "";
        this.fromuser = "";
        this.touser = "";
        this.message = "";
        this.msgdate = "";
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public String getFromuser() {
        return fromuser;
    }

    public void setFromuser(String fromuser) {
        this.fromuser = fromuser;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsgdate() {
        return msgdate;
    }

    public void setMsgdate(String msgdate) {
        this.msgdate = msgdate;
    }
}
