package com.ibm.openpages.ext.interfaces.cmp.bean;

public class MessageBean {
    public final static int ERROR_TYPE = 0;
    public final static int SUCCESS_TYPE = 1;
    private String message;
    private int type;

    /**
     *
     */
    public MessageBean() {
        super();
        this.message = "";
        this.type = 0;
    }

    /**
     *
     * @param message
     * @param type
     */
    public MessageBean(String message, int type) {
        super();
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "message='" + message + '\'' +
                ", type=" + type +
                '}';
    }
}
