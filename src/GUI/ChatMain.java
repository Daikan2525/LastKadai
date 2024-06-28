package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ChatMain extends JFrame {

	public static void main(String[] args) {// main 関数
		/* Frame関係調整：開始 */
		System.out.println("GUIChatMain started");
		ChatMain chat = new ChatMain();

		

		
		

		/***********************/
		// TCPChatサーバをスタート!!!!!
		/***********************/
		new GUIChatServer2(chat);

	}// main end

	ActionListener updateProBar;


	public void actionPerformed(ActionEvent e) {
		repaint();
	}

}// GUI Animation End