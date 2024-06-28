package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

//************以下、クライアントソフト。

//クライアントサンプルプログラム
//サーバーに接続し、メッセージを送信する。
//ポートは5000に固定。先にMultiServerSampleを起動しておくこと。
//第2引数で、メッセージを指定する。一行送ってサーバーからの
//メッセージ受信，表示後にプログラム終了する。
//コマンドライン例：java MultiClientSample localhost abcdefg

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


class GUIChatClient extends JFrame {
	JTextField tf;
  	JPanel pane;
    DefaultListModel model;
    JList list;
    JButton sendButton;

	String hostname = "localhost";


	public static void main(String[] args) {

		JFrame w = new GUIChatClient( "Chat");

        w.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        w.setSize( 480,640);
        w.setVisible( true );
	}

	GUIChatClient(String title) {// コンストラクター
//GUI
		super( title );
		pane = (JPanel)getContentPane();
		
		JPanel messagePanel   = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
		pane.add( messagePanel, BorderLayout.SOUTH);
		
		tf = new JTextField();
		tf.setBorder( new TitledBorder( "テキストを入力" ) );
		messagePanel.add(tf,BorderLayout.WEST);

        model = new DefaultListModel();
		list = new JList( model );
		JScrollPane sc = new JScrollPane( list );
		sc.setBorder( new TitledBorder( "トーク履歴" ) );
		pane.add( sc, BorderLayout.CENTER );

        sendButton = new JButton( new SendAction() );
		messagePanel.add( sendButton );

//通信
		// String hostname="133.27....";//おとなりのipaddress

		// doClientJob(hostname,"message hello1岩井");
		// doClientJob(hostname,"message hello2");
		// doClientJob(hostname, "face,接続実験メッセージfromClient名前");

	}//multi tcp client
	

    class SendAction extends AbstractAction{
        SendAction(){
            putValue( Action.NAME, "送信" );
		    putValue( Action.SHORT_DESCRIPTION, "送信" );
        }
        public void actionPerformed(ActionEvent e){
            String sendMessage = tf.getText();
            tf.setText("");

			doClientAccess(hostname, sendMessage);

            
		}
    }

	void sleep5() {
		System.out.println("5s wait..");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	public void doClientAccess(String hostname, String msg) {
		try {

			// アドレス情報を保持するsocketAddressを作成。
			// ポート番号は5000
			InetSocketAddress socketAddress = new InetSocketAddress(hostname,5656);

			// socketAddressの値に基づいて通信に使用するソケットを作成する。
			Socket socket = new Socket();
			// タイムアウトは10秒(10000msec)
			socket.connect(socketAddress, 10000);

			// 接続先の情報を入れるInetAddress型のinadrを用意する。
			InetAddress inadr;

			// inadrにソケットの接続先アドレスを入れ、nullである場合には
			// 接続失敗と判断する。
			// nullでなければ、接続確立している。
			if ((inadr = socket.getInetAddress()) != null) {
				System.out.println("Connect to " + inadr);
			} else {
				System.out.println("Connection failed.");
				return;
			}

			// メッセージの送信処理
			// コマンドライン引数の2番目をメッセージとする。
			String message = msg;

			// PrintWriter型のwriterに、ソケットの出力ストリームを渡す。(Auto Flush)
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			// ソケットの入力ストリームをBufferedReaderに渡す。
			BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			//System.out.println("Send message: " + message);　//送信確認表示

			// ソケットから出力する。
			writer.println(message);

			// もしPrintWriterがAutoFlushでない場合は，以下が必要。
			// writer.flush();

			// サーバーからのメッセージ読み取り
			String getline = rd.readLine();
			System.out.println("recieved message: " + getline);//受信確認表示

			//トーク履歴に追加
			if(getline.contains("\u001b[00;31m")){
				list.setBackground(Color.RED);
				model.addElement("☆" + message + "☆");
			}else{
				model.addElement(message);
			}


			// 終了処理

			rd.close();
			writer.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}