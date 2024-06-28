package GUI;

public class MessagePack {
    private String name;
    private int ID;
    private String NGWord;
    private String message;
    private boolean isNG;

    MessagePack(String name, int ID, String NGWord){
        setName(name);
        setID(ID);
        setNGWord(NGWord);
        isNG = true;
    }

    void setName(String name){
        this.name = name;
    }

    void setID(int ID){
        this.ID = ID;
    }

    void setNGWord(String NGWord){
        this.NGWord = NGWord;
    }

    void setMessage(String message){
        this.message = message;
    }

    String getName(){
        return name;
    }

    int getID(){
        return ID;
    }

    String getNGWord(){
        return NGWord;
    }

    String getMessage(){
        return message;
    }

    boolean getIsNG(){
        return isNG;
    }

    void checkMessage(String message){
        if(message.contains(NGWord)){
            isNG = true;
        }
    }
}
