package GUI;

import java.io.Serializable;

public class MessagePack implements Serializable {
    String name;
    String message;
    String NGWord;
    boolean isNG;

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNGWord(String NGWord) {
        this.NGWord = NGWord;
    }

    public void setIsNG(boolean check) {
        this.isNG = check;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getNGWord() {
        return NGWord;
    }

    public boolean getIsNG() {
        return isNG;
    }

    public boolean checkMessage(String NGWord) {
        if (getMessage().contains(NGWord)) {
            isNG = true;
        }
        return isNG;
    }
}
