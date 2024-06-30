package GUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GUIChatServer {

	ServerSocket serverSoc;

	public static void main(String[] args) {
		new GUIChatServer();
	}// mainend

	public GUIChatServer() {
		System.out.println("server started");
		System.out.println("creating srv socket");

		try {
			// ポート番号は、5656
			// ソケットを作成
			serverSoc = new ServerSocket(5656);
			boolean flag = true;

			// クライアントからの接続を待機するaccept()メソッド。
			// accept()は、接続があるまで処理はブロックされる。
			// もし、複数のクライアントからの接続を受け付けるようにするには
			// スレッドを使う。
			// accept()は接続時に新たなsocketを返す。これを使って通信を行なう。
			System.out.println("Waiting for Connection. ");
			int thcounter = 0;
			while (flag) {
				Socket socket = null;
				socket = serverSoc.accept();
				// accept()は、クライアントからの接続要求があるまでブロックする。
				// 接続があれば次の命令に移る。
				// スレッドを起動し、クライアントと通信する。
				new SrvWorkerThread(socket, thcounter++).start();

			}// while end
		} catch (IOException e) {
			System.out.println("IOException!");
			e.printStackTrace();
		} finally {
			try {
				if (serverSoc != null) {
					serverSoc.close();
				}
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}
		}

	}

	class SrvWorkerThread extends Thread {
		private Socket soc;

		public SrvWorkerThread(Socket sct, int thcounter) {
			soc = sct;
			System.out.println("Thread " + thcounter + "is Generated.  Connect to " + soc.getInetAddress());
		}

		public void run() {
			try {
				// socketからのデータはInputStreamReaderに送り、さらに
				ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());

				// Clientへの出力用PrintWriter
				ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());

				// データ読み取りと表示
				MessagePack receivedPack = (MessagePack)ois.readObject();
				System.out.println("Message from client :" + receivedPack); // 確認用

				receivedPack.checkMessage();

				// Clientにメッセージ送信
				oos.writeObject(receivedPack);
				oos.flush();

				ois.close();
				oos.close();
				

			} catch (IOException ioex) {
				ioex.printStackTrace();
			} finally {
				try {
					if (soc != null) {
						soc.close();
					}
				} catch (IOException ioex) {
					ioex.printStackTrace();
				}
			}// finall end
		}// run end
	}// SrvWorkerThread end

}// class MultiServerSample end