package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

<<<<<<< HEAD
public class TCPClient extends JFrame implements Runnable, ActionListener {
    public static void main(String[] args) {
        TCPClient window = new TCPClient();
        window.setSize(800, 600);
        window.setVisible(true);
    }

    private static final String APPNAME = "チャットクライアント";
    private static final String HOST = "localhost";
    private static final int PORT = 2815;

=======
import kadai.MessagePack;

public class TCPClient extends JFrame implements Runnable, ActionListener {
    public static void main(String[] args) {
        TCPClient window = new TCPClient();
        window.setSize(800, 600);
        window.setVisible(true);
    }

    private static final String APPNAME = "チャットクライアント";
    private static final String HOST = "localhost";
    private static final int PORT = 2815;

>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
    private Socket socket;
    private Thread thread;
    private ObjectOutputStream output;

    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private JTextArea msgTextArea;
    private JTextField msgTextField;
    private JTextField nameTextField;
    private JButton submitButton;
    private JButton renameButton;

    public TCPClient() {
        super(APPNAME);

        JPanel userPanel = new JPanel();
        JPanel leftPanel = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        msgTextArea = new JTextArea();
        msgTextField = new JTextField();
        nameTextField = new JTextField();
        submitButton = new JButton("送信");
        renameButton = new JButton("名前変更");

        userPanel.setBorder(new TitledBorder("ユーザー一覧"));
        userPanel.setLayout(new BorderLayout());
        userPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(userPanel, BorderLayout.CENTER);

        topPanel.setLayout(new BorderLayout());
        topPanel.add(new JScrollPane(msgTextArea), BorderLayout.CENTER);
        topPanel.add(leftPanel, BorderLayout.EAST);

        bottomPanel.setBorder(new TitledBorder("メッセージ"));
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(msgTextField, BorderLayout.CENTER);
        bottomPanel.add(submitButton, BorderLayout.EAST);

        JPanel renamePanel = new JPanel();
        renamePanel.setBorder(new TitledBorder("ユーザー名変更"));
        renamePanel.setLayout(new BorderLayout());
        renamePanel.add(nameTextField, BorderLayout.CENTER);
        renamePanel.add(renameButton, BorderLayout.EAST);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(topPanel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        contentPane.add(renamePanel, BorderLayout.NORTH);

        addWindowListener(new WindowAdapter() {
<<<<<<< HEAD
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
=======
            public void windowClosing(WindowEvent e) { System.exit(0); }
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
        });

        submitButton.addActionListener(this);
        renameButton.addActionListener(this);
        msgTextField.addActionListener(this);
        nameTextField.addActionListener(this);

        msgTextArea.setEditable(false);

        connect();
    }

    public void connect() {
        try {
            socket = new Socket(HOST, PORT);
            output = new ObjectOutputStream(socket.getOutputStream());

            thread = new Thread(this);
            thread.start();
<<<<<<< HEAD
        } catch (Exception err) {
=======
        }
        catch(Exception err) {
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
            err.printStackTrace();
        }
    }

    public void run() {
        try {
            MessagePack sendPack = null;
            InputStream input = socket.getInputStream();
            ObjectInputStream objectInput = new ObjectInputStream(input);

<<<<<<< HEAD
            while (!socket.isClosed()) {
                sendPack = (MessagePack) objectInput.readObject();

                if (sendPack.getName().equals("users")) {
                    String[] users = sendPack.getMessage().split(" ");
                    String[] ngWords = sendPack.getNGWord().split(" ");
                    userListModel.clear();
                    /*
                     * for (String user : users) {
                     * userListModel.addElement(user + ",NGWord:" + sendPack.getNGWord());
                     * }
                     * for (String ngWord : ngWords) {
                     * userListModel.addElement(user + ",NGWord:" + sendPack.getNGWord());
                     * }
                     */

                    for (int i = 0; i < users.length; i++) {
                        if (!users[i].equals(getName())) {
                            userListModel.addElement(users[i] + ",NGWord:" + ngWords[i]);
                        }
                    }

                } else if (sendPack.getName().equals("msg")) {
                    msgTextArea.append(sendPack.getMessage() + "\n");
                } else if (sendPack.getName().equals("successful")) {
                    if (sendPack.getName().equals("setName"))
                        msgTextArea.append("名前変更に失敗しました。/n");
                } else if (sendPack.getName().equals("error")) {
                    msgTextArea.append("ERROR> " + sendPack.getMessage() + "\n");
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == msgTextField || source == submitButton) {
            String text = msgTextField.getText();
            msgTextField.setText("");
            MessagePack pack = new MessagePack();
            pack.setName("msg");
            pack.setIsNG(false);
            pack.setMessage(text);

            sendMessage(pack);
        } else if (source == nameTextField || source == renameButton) {
            String text = nameTextField.getText();
            // nameTextField.setText("");
            if (!text.isBlank()) {
                MessagePack pack = new MessagePack();
                pack.setName("setName");
                pack.setIsNG(false);
                pack.setMessage(text);

                sendMessage(pack);
            }
=======
            while(!socket.isClosed()) {
                sendPack = (MessagePack) objectInput.readObject();

                if(sendPack.getName().equals("users")) {
                    String[] users = sendPack.getMessage().split(" ");
                    userListModel.clear();
                    for (String user : users) {
                        userListModel.addElement(user);
                    }
                }
                else if(sendPack.getName().equals("msg")){
                    msgTextArea.append(sendPack.getMessage() + "\n");
                }
                else if(sendPack.getName().equals("successful")){
                    if(sendPack.getName().equals("setName")) msgTextArea.append("名前変更に失敗しました。/n");
                }
                else if(sendPack.getName().equals("error")){
                    msgTextArea.append("ERROR> " + sendPack.getMessage() + "\n");
                }
            }
        }
        catch(Exception err) {
            err.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == msgTextField || source == submitButton) {
            String text = msgTextField.getText();
            msgTextField.setText("");
            MessagePack pack = new MessagePack();
            pack.setName("msg");
            pack.setIsNG(false);
            pack.setMessage(text);
            
            sendMessage(pack);
        }
        else if (source == nameTextField || source == renameButton) {
            String text = nameTextField.getText();
            nameTextField.setText("");
            MessagePack pack = new MessagePack();
            pack.setName("setName");
            pack.setIsNG(false);
            pack.setMessage(text);

            sendMessage(pack);
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
        }
    }

    public void sendMessage(MessagePack messagePack) {
        try {
            output.writeObject(messagePack);
            output.flush();
<<<<<<< HEAD
        } catch (Exception err) {
=======
        }
        catch(Exception err) {
>>>>>>> 25c1d03882f9d5ee7b16ffb17845c47b89bb0d5d
            err.printStackTrace();
        }
    }
}