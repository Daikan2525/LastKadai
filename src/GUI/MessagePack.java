package GUI;

import java.io.Serializable;

public class MessagePack implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private boolean isNG;
    private String message;
    private long clientId;
    private String ngWord;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsNG() {
        return isNG;
    }

    public void setIsNG(boolean isNG) {
        this.isNG = isNG;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getNGWord() {
        return ngWord;
    }

    public void setNGWord(String ngWord) {
        this.ngWord = ngWord;
    }

    public boolean checkMessage(String ngWord) {
        return message.contains(ngWord);
    }
}
