import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class LoginClient {

	private static JFrame frame;
	private JTextField textField;
	private static String ipAddr;
	private static int port;
	public static String username;
	public static Connect connect;
	public static WhiteBoard whiteBoard;
	private static Socket socket;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		if (args.length >= 3) {
			try {
				ipAddr = args[0];
				port = Integer.parseInt(args[1]);
				username = args[2];
			} catch (Exception e) {
				System.out.println("Please enter the correct IP address and port.");
				System.exit(1);
			}
		} else {
			System.out.println("Use default setting: localhost 10000 User2");
			ipAddr = "localhost";
			port = 10000;
			username = "User2";
//			System.exit(1);
		}

//		System.out.println(ipAddr);
//		System.out.println(port);
//		System.out.println(username);
		try {
			socket = new Socket(ipAddr, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("This port doesn't exist.");
			System.exit(2);
		}
		connect = new Connect(socket);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginClient window = new LoginClient();
					// window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		connect.begin(frame);
//		System.out.println(whiteBoard);
		// Connect.begin();

		// Login window = new Login();
		// LaunchManager.begin(ipAddr, port, username);

	}

	/**
	 * Create the application.
	 */
	public LoginClient() {
		initialize();
	}

//	public static void openboard() {
//
//		try {
//
//			whiteBoard = new WhiteBoard(connect);
//			whiteBoard.setFrame(whiteBoard);
//
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//
//	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		DrawListener drawListener = new DrawListener();

		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton login = new JButton("OK");
		login.setBounds(158, 126, 111, 34);

		JLabel Username = new JLabel("Join in as");
		Username.setBounds(185, 33, 75, 24);
		frame.getContentPane().add(Username);

		textField = new JTextField();
		textField.setBounds(79, 69, 274, 45);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		textField.setText(username);
//		System.out.println(textField.getText());

		login.addActionListener(drawListener);
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("OK")) {
					System.out.println(" Login pressed");
					try {
						// while (connect.getAllow().equals("wait")) {
						username = textField.getText();
						username = username.replace(" ", "");
						connect.out.writeUTF("request " + username);
						int time = 0;
						while (connect.getAllow().equals("wait") && time < 60000) {
//							System.out.println(connect.getAllow());
							TimeUnit.MILLISECONDS.sleep(100);
							time += 100;
						}

						String allow = connect.getAllow();
						// while (allow != nuconnect.resetAllow();ll && flag != "ready") {
						if (allow.equals("no")) {
//							System.out.println("fuk3");
							JOptionPane.showMessageDialog(frame, "Duplicate User Name!");
							connect.resetAllow();

						} else if (allow.equals("rejected")) {
							System.out.println("reject");
							JOptionPane.showMessageDialog(frame, "Manager rejected you!");
							frame.dispose();
							try {
								connect.out.writeUTF("OJBK");
								connect.out.flush();
								socket.close();
								System.exit(1);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

						else if (allow.equals("yes")) {
//							System.out.println("fuk4");
							frame.dispose();
							try {
								if (whiteBoard == null) {
									
									// whiteBoard = new WhiteBoard(Connect);
									whiteBoard = new WhiteBoard(connect, username);
									whiteBoard.setFrame(whiteBoard);
								
								}
							
							} catch (Exception e1) {
							
								e1.printStackTrace();
							
							}
						} else {
							
							JOptionPane.showMessageDialog(frame, "TimeOut!");
							
							try {
								socket.close();
								System.exit(1);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							frame.dispose();
						}

					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (InterruptedException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

				}
			}
		});

		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(login);
		frame.setVisible(true);

	}
}
