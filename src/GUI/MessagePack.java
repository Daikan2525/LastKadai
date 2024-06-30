package GUI;

import java.io.Serializable;

public class MessagePack implements Serializable{
    private String name;
    private String NGWord;
    private String message;
    private boolean isNG;

    public MessagePack(String name,String NGWord){
        setName(name);
        setNGWord(NGWord);
        isNG = true;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setNGWord(String NGWord){
        this.NGWord = NGWord;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getName(){
        return name;
    }

    public String getNGWord(){
        return NGWord;
    }

    public String getMessage(){
        return message;
    }

    public boolean getIsNG(){
        return isNG;
    }

    public void checkMessage(){
        if(this.message.contains(NGWord)){
            isNG = true;
        }
    }
}
