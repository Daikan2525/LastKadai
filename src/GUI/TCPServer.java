package GUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
/*
サーバーの起動
cd ./src
javac --enable-preview --source 20 TCPServer.java
java --enable-preview TCPServer
クライアントの起動
cd ./src
javac --enable-preview --source 20 TcpClient.java
java --enable-preview TCPClient
 */

/**
 * チャット コンソールアプリ の サーバー
 */

public class TCPServer {
    public static int portNumber = 1234;

    public static void main(String[] args) throws IOException {
        final ArrayList<Server1ClientThread> serverThreadArrayList = new ArrayList<>();
        final ServerSocket serverSocket = new ServerSocket(portNumber);

        try {
            while (true) {
                System.out.println("新たなクライアントとの接続を待機しています");
                final Socket socket = serverSocket.accept();
                System.out.println("新たにクライアントと接続しました!");
                // クライアントからメッセージを受け取るスレッド
                final Server1ClientThread lastServerThread = new Server1ClientThread(socket,
                        // 1つのクライアントからメッセージが来た
                        (pack, clientId) -> {
                            try {
                                sendPackToAllClient(
                                        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + ": <" + clientId + "> "
                                                + pack.getMessage(), pack.getIsNG(),
                                        serverThreadArrayList);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        },
                        // 1つのクライアントとの接続が切れた
                        (disconnected) -> {
                            try {
                                sendPackToAllClient(disconnected + "さんが退出しました", false,  serverThreadArrayList);
                                System.out.println(disconnected + "さんが退出しました");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                serverThreadArrayList.add(lastServerThread);
                lastServerThread.start();
                try {
                    sendPackToAllClient(lastServerThread.threadId() + "さんが参加しました", false, serverThreadArrayList);
                    System.out.println(lastServerThread.threadId() + "さんが参加しました");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * サーバーに接続しているすべてのクライアントにメッセージを送信する
     */


    static public void sendPackToAllClient(String message, boolean check, ArrayList<Server1ClientThread> serverThreadArrayList)throws IOException {
        MessagePack pack = new MessagePack();
        pack.setName("System");
        pack.setNGWord(null);
        pack.setIsNG(check);
        pack.setMessage(message);
        for (final Server1ClientThread serverThread : serverThreadArrayList) {
            if (!serverThread.isDisconnected) {
                serverThread.sendDataToClient(pack);
            }
        }
    }
}

/**
 * 1つのクライアントからメッセージを受け取り, 送信するためのスレッド
 */
class Server1ClientThread extends Thread {
    final Socket socket;

    final MessagePack pack;

    //ラムダ式で２つの入力を受け付けるクラス
    final BiConsumer<MessagePack, Long> handler;

    final Consumer<Long> onDisconnect;
    boolean isDisconnected = false;

    ObjectOutputStream serverToClientStream = null;

    Server1ClientThread(final Socket socket, final BiConsumer<MessagePack, Long> handler, final Consumer<Long> onDisconnect) {
        System.out.println("Server1ClientThreadを起動します");
        this.socket = socket;
        this.handler = handler;
        this.onDisconnect = onDisconnect;
        pack = null;
    }

    @Override
    public void run() {
        try {
            serverToClientStream = new ObjectOutputStream(socket.getOutputStream());

            final ObjectInputStream clientToServerStream = new ObjectInputStream(socket.getInputStream());
            MessagePack sendPack;
            
            while (true) {
                try {
                    sendPack = (MessagePack)clientToServerStream.readObject();
                    System.out.println(sendPack.getMessage());
                    logWithId("クライアントから " + sendPack.getMessage() + "を受け取りました");
                    sendPack.checkMessage();
                    handler.accept(sendPack, threadId());
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            isDisconnected = true;
            onDisconnect.accept(threadId());
        }
    }

    /**
     * クライアントにデータを送信する
     */

    public void sendDataToClient(final MessagePack pack) throws IOException {
        // まだ接続していないときは, 送信しない
        if (serverToClientStream == null) {
            return;
        }

        serverToClientStream.writeObject(pack);
        serverToClientStream.flush();
    }

    private void logWithId(final String message) {
        System.out.println("[Server1ClientThread id: " + threadId() + "] " + message);
    }
}