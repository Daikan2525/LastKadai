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

public class TCPServer {
    public static int portNumber = 1234;

    static Map<Long, String> map = new HashMap<>();
    static List<String> lines;

    public static void main(String[] args) throws IOException {
        lines = new ArrayList<>();

        //NGワードリストtxtファイルの読み込み
        try {
            File file = new File("NGWordList.txt");

            if (!file.exists()) {
                System.out.print("ファイルが存在しません");
                return;
            }

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String data;
            while ((data = bufferedReader.readLine()) != null) {
                System.out.println(data);
                lines.add(data);
            }

            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        final ArrayList<Server1ClientThread> serverThreadArrayList = new ArrayList<>(); //接続したクライアントのスレッドリスト
        final ServerSocket serverSocket = new ServerSocket(portNumber);

        try {
            while (true) {
                System.out.println("新たなクライアントとの接続を待機しています");
                final Socket socket = serverSocket.accept();
                System.out.println("新たにクライアントと接続しました!");

                final Server1ClientThread lastServerThread = new Server1ClientThread(socket,
                        (pack, clientId) -> {
                            try {
                                sendPackToAllClient(
                                        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + ": <"
                                                + pack.getName() + "> "
                                                + pack.getMessage(),
                                        pack.checkMessage(map.get(clientId)),
                                        null,
                                        serverThreadArrayList);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        },
                        (disconnected) -> {
                            try {
                                sendPackToAllClient(disconnected + "さんが退出しました", false, null, serverThreadArrayList);
                                System.out.println(disconnected + "さんが退出しました");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                serverThreadArrayList.add(lastServerThread);    //新しい参加者をスレッドリストに追加
                lastServerThread.start();
                try {
                    long clientId = lastServerThread.threadId();
                    map.put(clientId, chooseNGWord(lines));         //クライアントIDとワードとの紐づけ
                    System.out.println(clientId + map.get(clientId));

                    // 新しいクライアントに既存の参加者リストを送信
                    sendExistingUsersToNewClient(lastServerThread, serverThreadArrayList);

                    // 既存のクライアントに新しい参加者を通知
                    sendPackToAllClient(clientId + "さんが参加しました", false,
                            map.get(clientId), serverThreadArrayList);
                    System.out.println(clientId + "さんが参加しました");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //Stringリストからランダムにワードを選ぶ
    static public String chooseNGWord(List<String> lines) {
        int index = new Random().nextInt(lines.size());
        return lines.get(index);
    }


    //すべてのクライアントにパックを送る
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


    //参加ユーザー情報を新規クライアントに送る
    static public void sendExistingUsersToNewClient(Server1ClientThread newClient,
            ArrayList<Server1ClientThread> serverThreadArrayList) throws IOException {
        for (final Server1ClientThread serverThread : serverThreadArrayList) {
            if (serverThread != newClient && !serverThread.isDisconnected) {
                MessagePack pack = new MessagePack();
                pack.setName("System");
                pack.setIsNG(false);
                pack.setMessage(serverThread.threadId() + "さんが参加しました");
                newClient.sendDataToClient(pack);
            }
        }
    }
}


/*
 * ひとつのクライアントからメッセージを受け取り、送信するためのスレッド
 */
class Server1ClientThread extends Thread {
    final Socket socket;
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
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            isDisconnected = true;
            onDisconnect.accept(threadId());
        }
    }


    /*
     * データをクライアントに送る
     */
    public void sendDataToClient(final MessagePack pack) throws IOException {
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