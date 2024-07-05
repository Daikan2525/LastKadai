package GUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
/*
サーバーの起動
cd ./src
javac --enable-preview --source 20 TCPServer.java
java --enable-preview TCPServer
クライアントの起動
cd ./src
javac --enable-preview --source 20 TCPClient.java
java --enable-preview TCPClient
 */
public class TCPClientCp2 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final InetAddress localhost = InetAddress.getLocalHost();
        System.out.println(
                "クライアントを起動しました. これから " + localhost + " のポート番号 " + TCPServer.portNumber + "に接続します");
        final Socket socket = new Socket(localhost, TCPServer.portNumber);

        final ObjectInputStream serverToClientStream = new ObjectInputStream(socket.getInputStream());
        final ObjectOutputStream clientToServerStream = new ObjectOutputStream(socket.getOutputStream());

        new Thread(
                () -> {
                    MessagePack sendPack = null;

                    while (true) {
                        try {
                            sendPack = (MessagePack) serverToClientStream.readObject();
                        } catch (ClassNotFoundException | IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if(sendPack.getIsNG()){
                            System.out.println(sendPack.getMessage() + "-NGワードが含まれています-");
                        }else{
                            System.out.println(sendPack.getMessage());
                        }
                    }
                })
                .start();

        final Scanner consoleInputScanner = new Scanner(System.in);

        while (true) {
            // コンソールから入力を受け付ける
            final String message = consoleInputScanner.nextLine();
            // サーバーにメッセージを送る
            MessagePack sentPack = new MessagePack();
            sentPack.setName("Taro");
            sentPack.setIsNG(false);
            sentPack.setNGWord("o");
            sentPack.setMessage(message);

            clientToServerStream.writeObject(sentPack);
            clientToServerStream.flush();
        }
    }
}