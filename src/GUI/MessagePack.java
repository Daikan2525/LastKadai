package GUI;
import java.io.Serializable;

public class MessagePack implements Serializable{
    String name;
    String NGWord;
    String message;
    boolean isNG;

    public void setName(String name){
        this.name = name;
    }

    public void setNGWord(String NGWord){
        this.NGWord = NGWord;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setIsNG(boolean check){
        this.isNG = check;
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
        if(getMessage().contains(NGWord)){
            isNG = true;
        }
    }
}
