package GUI;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ChatGUI extends JFrame{
    JTextField tf;
  	JPanel pane;
    DefaultListModel model;
    JList list;
    JButton sendButton;

	public static void main(String[] args) {
        JFrame w = new ChatGUI( "Chat" );
        w.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        w.setSize( 600,800);
        w.setVisible( true );

        new GUIChatClient();
    }

    // コンストラクタ
	public ChatGUI(String title) {
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
	}

    class SendAction extends AbstractAction{
        SendAction(){
            putValue( Action.NAME, "送信" );
		    putValue( Action.SHORT_DESCRIPTION, "送信" );
        }
        public void actionPerformed(ActionEvent e){
            String message = tf.getText();
            tf.setText("");

            model.addElement(message);
        }
    }
}


