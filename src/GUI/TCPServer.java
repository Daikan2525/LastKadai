package GUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
<<<<<<< HEAD
=======

import kadai.MessagePack;
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d

public class TCPServer {

	static Map<Long, String> map = new HashMap<>();
	private static long clientIdCounter = 0;
	static List<String> lines;
	private Random random = new Random();

	public static void main(String[] args) {
		TCPServer application = TCPServer.getInstance();
		lines = new ArrayList<>();

<<<<<<< HEAD
		// NGワードリストの読み取り
=======
		//NGワードリストの読み取り
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
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

		application.start();
	}

	private static TCPServer instance;

<<<<<<< HEAD
	// インスタンスの取得
=======
	//インスタンスの取得
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	public static TCPServer getInstance() {
		if (instance == null) {
			instance = new TCPServer();
		}
		return instance;
	}

	private ServerSocket server;
	private ArrayList<ChatClientUser> userList; // ユーザー管理のリスト

	private TCPServer() {
		userList = new ArrayList<ChatClientUser>(); // ユーザーリストの初期化
	}

<<<<<<< HEAD
	// 処理開始
=======
	//処理開始
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	public void start() {
		try {
			server = new ServerSocket(2815);

			while (!server.isClosed()) {
				Socket client = server.accept();
				ChatClientUser user = new ChatClientUser(client, generateClientId());
				assignRandomNgWord(user.getClientId());
<<<<<<< HEAD
				user.setNGWord(map.get(user.getClientId()));
=======
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
				addUser(user); // ユーザーリストに追加
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

<<<<<<< HEAD
	// クライアントIDの生成
=======
	//クライアントIDの生成
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	private synchronized long generateClientId() {
		return clientIdCounter++;
	}

<<<<<<< HEAD
	// サーバー管理のクライアントリストにクライアントを追加
=======
	//サーバー管理のクライアントリストにクライアントを追加
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	public void addUser(ChatClientUser user) {
		if (userList.contains(user))
			return;

		userList.add(user); // ユーザーリストにユーザーを追加
<<<<<<< HEAD
		System.out.println("addUser=[" + user + "], clientID=[" + user.getClientId() + "]");
	}

	// ユーザー情報(個人)を取得
=======
		System.out.println("addUser=[" + user + "]");
	}

	//ユーザー情報(個人)を取得
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	public ChatClientUser getUser(String name) {
		for (int i = 0; i < userList.size(); i++) {
			ChatClientUser user = userList.get(i);
			if (user.getName().equals(name))
				return user; // 指定された名前のユーザーを取得
		}
		return null;
	}

<<<<<<< HEAD
	// ユーザー情報(全員)を取得
=======
	//ユーザー情報(全員)を取得
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	public ChatClientUser[] getUsers() {
		ChatClientUser[] users = new ChatClientUser[userList.size()];
		userList.toArray(users);
		return users;
	}

<<<<<<< HEAD
	// ユーザーの削除
=======
	//ユーザーの削除
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	public void removeUser(ChatClientUser user) {
		userList.remove(user);
		System.out.println("removeUser=[" + user + "]");
	}

<<<<<<< HEAD
	// 全ユーザーの削除
=======
	//全ユーザーの削除
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	public void clearUser() {
		userList.clear();
	}

	public void close() throws IOException {
		server.close();
	}

<<<<<<< HEAD
	// 全クライアントにパックを送信
=======
	//全クライアントにパックを送信
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	public void broadcast(MessagePack messagePack) {
		for (ChatClientUser user : userList) {
			user.sendMessage(messagePack);
		}
	}

<<<<<<< HEAD
	// IDからNGワードを取得
=======
	//IDからNGワードを取得
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	public String getNgWord(long clientId) {
		return map.get(clientId);
	}

<<<<<<< HEAD
	// NGワードを登録
=======
	//NGワードを登録
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	private void assignRandomNgWord(long clientId) {
		if (!lines.isEmpty()) {
			String ngWord = lines.get(random.nextInt(lines.size()));
			map.put(clientId, ngWord);
			System.out.println("Assigned NG word '" + ngWord + "' to client " + clientId);
		}
	}

}

<<<<<<< HEAD
// メッセージを受け取った時のイベントを管理する
=======
//メッセージを受け取った時のイベントを管理する
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
class MessageEvent extends EventObject {
	private ChatClientUser source;
	private String name;
	private String value;

	public MessageEvent(ChatClientUser source, String name, String value) {
		super(source);
		this.source = source;
		this.name = name;
		this.value = value;
	}

	public ChatClientUser getUser() {
		return source; // イベント発生元のユーザーを取得
	}

	public String getName() {
		return this.name; // イベントの名前を取得
	}

	public String getValue() {
		return this.value; // イベントの中身を取得
	}
}

interface MessageListener extends EventListener {
	void messageThrow(MessageEvent e); // メッセージイベントを処理するリスナインターフェース
}

<<<<<<< HEAD
// クライアント一人の相手をして処理をするクラス
=======
//クライアント一人の相手をして処理をするクラス
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
class ChatClientUser implements Runnable, MessageListener {
	private Socket socket; // クライアントとのソケット
	private String name; // ユーザー名
	private long clientId;
<<<<<<< HEAD
	private String ngWord;
=======
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	private TCPServer server = TCPServer.getInstance(); // ChatServerのインスタンス
	private ArrayList<MessageListener> messageListeners; // メッセージリスナーのリスト
	private ObjectOutputStream output;

	public ChatClientUser(Socket socket, long id) {
		try {
			messageListeners = new ArrayList<MessageListener>(); // メッセージリスナーリストの初期化
			this.socket = socket;
			this.clientId = id;
			output = new ObjectOutputStream(socket.getOutputStream());

			addMessageListener(this); // 自身をメッセージリスナーとして追加

			Thread thread = new Thread(this); // 新しいスレッドで自身を実行
			thread.start();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	public long getClientId() {
		return clientId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

<<<<<<< HEAD
	public void setNGWord(String word) {
		this.ngWord = word;
	}

	public String getNGWord() {
		return this.ngWord;
	}

=======
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	public void run() {
		try {
			InputStream input = socket.getInputStream(); // ソケットからの入力ストリームを取得
			ObjectInputStream objectInput = new ObjectInputStream(input);
			MessagePack messagePack = null;

			while (!socket.isClosed()) {
				messagePack = (MessagePack) objectInput.readObject(); // クライアントからのメッセージを読み込む
				System.out.println("INPUT=" + messagePack.getMessage());

				reachedMessage(messagePack.getName(), messagePack.getMessage()); // メッセージを処理
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

<<<<<<< HEAD
	// メッセージイベントの受け取り
=======
	//メッセージイベントの受け取り
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	public void messageThrow(MessageEvent e) {
		String msgType = e.getName(); // イベントの種類
		String msgValue = e.getValue(); // イベントの値

		if (msgType.equals("close")) {
			try {
				close(); // クライアントをクローズ
			} catch (IOException err) {
				err.printStackTrace();
			}
<<<<<<< HEAD
		} else if (msgType.equals("setName")) { // イベント「名前変更」
=======
		} else if (msgType.equals("setName")) { //イベント「名前変更」
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
			String name = msgValue;
			if (name.indexOf(" ") == -1) {
				String before = getName();
				setName(name);

<<<<<<< HEAD
				// 送信するパックの作成
=======
				//送信するパックの作成
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
				MessagePack pack = new MessagePack();
				pack.setName("setName");
				pack.setIsNG(false);
				pack.setMessage("successful setName");
				pack.setClientId(clientId);
				sendMessage(pack);

<<<<<<< HEAD
				// 全員に送信するパックの作成
=======
				//全員に送信するパックの作成
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
				MessagePack packBroadcast = new MessagePack();
				packBroadcast.setName("broadcast");
				packBroadcast.setIsNG(false);
				packBroadcast.setMessage(before + " さんが " + name + " に名前を変更しました");
				packBroadcast.setClientId(clientId);
				server.broadcast(packBroadcast);
				updateUsers();
			} else {
<<<<<<< HEAD
				// エラー時のメッセージ
				MessagePack error = new MessagePack();
				error.setName("error");
				error.setIsNG(false);
=======
				//エラー時のメッセージ
				MessagePack error = new MessagePack();
				error.setName("error");
				error.setIsNG(true);
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
				error.setMessage("名前にスペースを含めることはできません");
				error.setClientId(clientId);
				sendMessage(error);
			}
<<<<<<< HEAD
		} else if (msgType.equals("getUsers")) { // イベント「全ユーザーの取得」
			String result = "";
			ChatClientUser[] users = server.getUsers();
			for (int i = 0; i < users.length; i++) {
				result += users[i].getName() + " "; // スペース区切りでユーザー名を書き込む
			}

			// 送信用パック
=======
		} else if (msgType.equals("getUsers")) { //イベント「全ユーザーの取得」
			String result = "";
			ChatClientUser[] users = server.getUsers();
			for (int i = 0; i < users.length; i++) {
				result += users[i].getName() + " "; //スペース区切りでユーザー名を書き込む
			}

			//送信用パック
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
			MessagePack userList = new MessagePack();
			userList.setName("users");
			userList.setIsNG(false);
			userList.setMessage(result.toString());
			userList.setClientId(clientId);
			sendMessage(userList);
		} else if (msgType.equals("msg")) { // イベント「メッセージ」
			String ngWord = server.getNgWord(clientId);
			if (msgValue.contains(ngWord)) {
<<<<<<< HEAD
				// 送信用パックの作成
				MessagePack error = new MessagePack();
				error.setName("error");
				error.setIsNG(true);
				error.setMessage("<" + getName() + "> : " + msgValue + "NGワードが含まれています: " + ngWord);
				error.setClientId(clientId);
				server.broadcast(error);
=======
				//送信用パックの作成
				MessagePack error = new MessagePack();
				error.setName("error");
				error.setIsNG(true);
				error.setMessage("<"+ getName()+"> : "+ msgValue +"NGワードが含まれています: " + ngWord);
				error.setClientId(clientId);
				sendMessage(error);
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
			} else {
				String message = "<" + getName() + "> : " + msgValue;
				MessagePack msg = new MessagePack();
				msg.setName("msg");
				msg.setIsNG(false);
				msg.setMessage(message);
				msg.setClientId(clientId);
				server.broadcast(msg);
			}
		}
	}

	public String toString() {
		return "NAME=" + getName();
	}

	public void close() throws IOException {
		server.removeUser(this);
		messageListeners.clear();
		socket.close();
		updateUsers();
	}

<<<<<<< HEAD
	// 送信メソッド
=======
	//送信メソッド
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	public void sendMessage(MessagePack messagePack) {
		try {
			output.writeObject(messagePack);
			output.flush();
		} catch (Exception err) {
		}
	}

<<<<<<< HEAD
	// メッセージ受け取り処理
=======
	//メッセージ受け取り処理
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
	public void reachedMessage(String name, String value) {
		MessageEvent event = new MessageEvent(this, name, value);
		for (int i = 0; i < messageListeners.size(); i++) {
			messageListeners.get(i).messageThrow(event);
		}
	}

	public void addMessageListener(MessageListener l) {
		messageListeners.add(l);
	}

	public void removeMessageListener(MessageListener l) {
		messageListeners.remove(l);
	}

	public MessageListener[] getMessageListeners() {
		MessageListener[] listeners = new MessageListener[messageListeners.size()];
		messageListeners.toArray(listeners);
		return listeners;
	}

	private void updateUsers() { // ユーザー情報を更新する処理
		String result = "";
<<<<<<< HEAD
		String ngResult = "";
		ChatClientUser[] users = server.getUsers();
		for (ChatClientUser user : users) {
			result += user.getName() + " ";
			ngResult += user.getNGWord() + " ";
=======
		ChatClientUser[] users = server.getUsers();
		for (ChatClientUser user : users) {
			result += user.getName() + " ";
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
		}
		MessagePack forUsers = new MessagePack();
		forUsers.setName("users");
		forUsers.setIsNG(false);
		forUsers.setMessage(result.toString());
		forUsers.setClientId(clientId);
<<<<<<< HEAD
		forUsers.setNGWord(ngResult);
=======
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
		server.broadcast(forUsers);
	}
}