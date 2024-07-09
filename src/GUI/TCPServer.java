package GUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

    // public static NGWordList NGlist = new NGWordList();

    static Map<Long, String> map = new HashMap<>();

    static List<String> lines;

    public static void main(String[] args) throws IOException {

        lines = new ArrayList<>();

        try {
            // ファイルのパスを指定する
            File file = new File("NGWordList.txt");

            // ファイルが存在しない場合に例外が発生するので確認する
            if (!file.exists()) {
                System.out.print("ファイルが存在しません");
                return;
            }

            // BufferedReaderクラスのreadLineメソッドを使って1行ずつ読み込み表示する
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String data;
            while ((data = bufferedReader.readLine()) != null) {
                System.out.println(data);
                lines.add(data);
            }

            // 最後にファイルを閉じてリソースを開放する
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

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
                                        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + ": <"
                                                + clientId + "> "
                                                + pack.getMessage(),
                                        pack.checkMessage(map.get(clientId)),
                                        null,
                                        serverThreadArrayList);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        },
                        // 1つのクライアントとの接続が切れた

                        (disconnected) -> {
                            try {
                                sendPackToAllClient(disconnected + "さんが退出しました", false, null, serverThreadArrayList);
                                System.out.println(disconnected + "さんが退出しました");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                serverThreadArrayList.add(lastServerThread);
                lastServerThread.start();
                try {
                    map.put(lastServerThread.threadId(), chooseNGWord(lines));// IDとNGWordをセットでマップに格納
                    System.out.println(lastServerThread.threadId() + map.get(lastServerThread.threadId()));// IDとNGWord確認用

                    sendPackToAllClient(lastServerThread.threadId() + "さんが参加しました", false,
                            map.get(lastServerThread.threadId()), serverThreadArrayList);
                    System.out.println(lastServerThread.threadId() + "さんが参加しました");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static public String chooseNGWord(List<String> lines) {
        int index = new Random().nextInt(lines.size());
        String NGWord = lines.get(index);
        return NGWord;
    }

    /**
     * サーバーに接続しているすべてのクライアントにメッセージを送信する
     */

    static public void sendPackToAllClient(String message, boolean check, String NGWord,
            ArrayList<Server1ClientThread> serverThreadArrayList) throws IOException {
        MessagePack pack = new MessagePack();
        pack.setName("System");
        pack.setIsNG(check);
        pack.setNGWord(NGWord);
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

    // ラムダ式で２つの入力を受け付けるクラス
    final BiConsumer<MessagePack, Long> handler;

    final Consumer<Long> onDisconnect;
    boolean isDisconnected = false;

    ObjectOutputStream serverToClientStream = null;

    Server1ClientThread(final Socket socket, final BiConsumer<MessagePack, Long> handler,
            final Consumer<Long> onDisconnect) {
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
                    sendPack = (MessagePack) clientToServerStream.readObject();
                    System.out.println(sendPack.getMessage());
                    logWithId("クライアントから " + sendPack.getMessage() + "を受け取りました");
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