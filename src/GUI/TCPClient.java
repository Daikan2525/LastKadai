package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TCPClient extends JFrame implements Runnable, ActionListener {
    public static void main(String[] args) throws IOException {
        System.out.println("キーボードから名前を入力してください");

        Scanner scan = new Scanner(System.in);
        String str = scan.next();
        System.out.println("入力された文字は「" + str + "」です");
        scan.close();

        TCPClient window = new TCPClient(str);
        window.setSize(800, 600);
        window.setVisible(true);
    }

    private String name;
    private static final String APPNAME = "NGワードゲーム";
    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    private Socket socket;
    private Thread thread;
    private JList userList;
    private JTextArea msgTextArea;
    private JTextField msgJTextField;
    private JTextField nameAndNGWordField;
    private JButton submitButton;

    private ObjectOutputStream output;
    private DefaultListModel<String> userModel;

    public TCPClient(String name) {
        super(APPNAME);
        this.name = name;

        JPanel leftPanel = new JPanel();
        JPanel buttomPanel = new JPanel();
        JPanel userPanel = new JPanel();

        userModel = new DefaultListModel<>();
        userList = new JList(userModel);
        msgTextArea = new JTextArea();
        msgJTextField = new JTextField();
        nameAndNGWordField = new JTextField();
        submitButton = new JButton("送信");

        submitButton.addActionListener(this);
        submitButton.setActionCommand("submit");

        userPanel.setLayout(new BorderLayout());
        userPanel.add(new JLabel("参加者とNGワード"), BorderLayout.NORTH);
        userPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

        nameAndNGWordField.setPreferredSize(new Dimension(200, nameAndNGWordField.getPreferredSize().height));

        leftPanel.setLayout(new GridLayout(2, 1));
        leftPanel.add(userPanel);

        buttomPanel.setLayout(new BorderLayout());
        buttomPanel.add(msgJTextField, BorderLayout.CENTER);
        buttomPanel.add(submitButton, BorderLayout.EAST);

        msgTextArea.setEditable(false);

        this.getContentPane().add(new JScrollPane(msgTextArea), BorderLayout.CENTER);
        this.getContentPane().add(leftPanel, BorderLayout.WEST);
        this.getContentPane().add(buttomPanel, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    close();
                } catch (Exception err) {
                }
            }
        });
        connectServer();

        thread = new Thread(this);
        thread.start();
    }

    public void connectServer() {
        try {
            socket = new Socket(HOST, PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            msgTextArea.append("接続しました。\n");
        } catch (Exception err) {
            msgTextArea.append("ERROR>" + err + "\n");
        }
    }

    public void close() throws IOException {
        socket.close();
    }

    public void sendMessage(String msg) {
        try {
            msg = msgJTextField.getText();
            MessagePack sentPack = new MessagePack();
            sentPack.setName(name);
            sentPack.setIsNG(false);
            sentPack.setMessage(msg);

            output.writeObject(sentPack);
            output.flush();
        } catch (Exception err) {
            msgTextArea.append("ERROR>" + err + "\n");
        }
    }

    public void reachedMessage(String value, boolean isNG, long clientId) {
        msgTextArea.append(value + "\n");

        if (value.contains("さんが参加しました") || value.contains("さんが退出しました")) {
            String[] parts = value.split("さんが");
            if (parts.length > 0) {
                if (value.contains("参加しました")) {
                    String user = parts[0];
                    if (isNG) {
                        user += " (NGワード: " + parts[1].split("NGワード:")[1].trim() + ")";
                    }
                    if (!userModel.contains(user)) {
                        userModel.addElement(user);
                    }
                } else if (value.contains("退出しました")) {
                    userModel.removeElement(parts[0]);
                }
            }
        }
    }

    public void run() {
        try {
            MessagePack sendPack = null;
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            while (!socket.isClosed()) {
                sendPack = (MessagePack) input.readObject();
                if (sendPack.getIsNG()) {
                    reachedMessage(sendPack.getMessage() + "-NGワードが含まれています-", sendPack.getIsNG(), sendPack.getClientId());
                } else {
                    reachedMessage(sendPack.getMessage(), sendPack.getIsNG(), sendPack.getClientId());
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        sendMessage("msg " + msgJTextField.getText());
        msgJTextField.setText("");
        System.out.println("送信しました");
    }
}